package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.exceptions.*;
import com.nekods.nyaPlus.smallTools.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;

import static com.nekods.nyaPlus.core.VarManager.Methods;
import static com.nekods.nyaPlus.smallTools.Toolbox.getStringByIndexes;

public class LineAnalyser {
    private VarManager varManager;
    private Task task;

    public LineAnalyser() {
    }

    private String replaceWithVars(String input) {//输入转成的char列表
        char[] charsOfInput = input.toCharArray();

        //是否在%%之间
        boolean isInCheckingBox = false;

        //用于存储变量两端位置的东西
        int[] pair = {0, 0};

        //最终多次写入的时候会产生偏移
        int offset = 0;

        //写入的时候用的Builder
        StringBuilder inputBuilder = new StringBuilder(input);


        for (int i = 0; i < charsOfInput.length; i++) {
            if (charsOfInput[i] == '%') {
                if (!isInCheckingBox) {
                    //第一个%
                    pair[0] = i + 1;  //内部原因，得加1
                    isInCheckingBox = true;
                } else {
                    pair[1] = i;
                    isInCheckingBox = false;
                    //第二个%
                    char[] temp = new char[pair[1] - pair[0]];  //一个和变量长度相等的char数组，先创建出来，然后下一个函数里更新
                    input.getChars(pair[0], pair[1], temp, 0);  //获取变量名
                    String var = new String(temp);  //转换，这里var就是变量名
                    if (var.isBlank()) {  //变量名是空的-->顺延检查
                        pair[0] = i + 1;
                        isInCheckingBox = true;
                        continue;
                    }

                    //各得其所
                    //引入偏移，因为修改过的字符串会变长或者变短

                    //svar全程StringOfVar，就是变量实际值
                    String svar = varManager.getStringVar(var);
                    //下面的什么加一减一都是测试出来的，轻易别动
                    inputBuilder.replace(pair[0] - 1 + offset, pair[1] + 1 + offset, svar);
                    //更新i，charsOfInput
                    offset += svar.length() - var.length() - 2;
                }
            }
        }

        return inputBuilder.toString();
    }


    /**
     * 关于这一堆史:
     * 前一部分是从字符串里找@[][][]这种结构的过程，并从里面取出各个各个数组名称，
     * 后一部分是将数组取出来的值放进字符串里面
     */

    private String replaceWithJSON(String input) {
        //输入转成的char列表
        char[] charsOfInput = input.toCharArray();

        //是否在[]之间
        boolean isInCheckingBox = false;

        //是否在检查@[][][]结构（和计算区分开）
        boolean isCheckingJSON = false;

        //用于存储[]两端位置的东西
        int[] pair = {0, 0};

        //列表名（只能是单个字符）
        char listname = '0';

        //整个数组表达式的长度，用于最终的
        int back_length = 0;

        ArrayList<String> listGettingStream = new ArrayList<>();

        for (int i = 0; i < charsOfInput.length; i++) {     //正着向后查找
            if (charsOfInput[i] == '@' && charsOfInput[i + 2] == '[') {
                isCheckingJSON = true;
                listname = charsOfInput[i + 1];
                if (!varManager.hasVar(listname)) {  //没这个列表的话
                    throw new NyaJSONNotFoundException(listname);
                }
                pair[0] = i + 3;   //+1是本身，+3是下下一个
                i += 2;  //本来应该是+3，但是for结束后会+1，所以这里加2，用于跳到括号后面一个字符
                back_length += 2;  //@和数组名，两个
                isInCheckingBox = true;
            } else if (charsOfInput[i] == ']' && isInCheckingBox && isCheckingJSON) {
                pair[1] = i;
                isInCheckingBox = false;
                back_length += (2 + pair[1] - pair[0]);
                char[] temp = new char[pair[1] - pair[0]];  //一个和变量长度相等的char数组
                input.getChars(pair[0], pair[1], temp, 0);  //获取变量名
                listGettingStream.add(new String(temp));
                if (i == charsOfInput.length - 1/*表示这是最后一个字符，无需多言*/ || charsOfInput[i + 1] != '[') {  //这一段数组流的末尾了,进行一步替换
                    String svar = varManager.getStringVar(String.valueOf(listname));  //先获取列表   todo 这一行有一滩狗屎，要改。
                    String end = getStringByIndexes(svar, listGettingStream);  //再获取内容
                    input = new StringBuilder(input).replace(pair[1] - back_length + 1, pair[1] + 1, end).toString();   //替换

                    //以下都是交接
                    charsOfInput = input.toCharArray();
                    i = i + end.length() - back_length;
                    back_length = 0;
                    isCheckingJSON = false;
                    listGettingStream.clear();
                }
            } else if (charsOfInput[i] == '[' && !isInCheckingBox && isCheckingJSON) {  //第二次遇到
                pair[0] = i + 1;
                isInCheckingBox = true;
            }
        }  //这一段用于获取连续获取一段数组的流
        return input;

    }

    private String replaceCalculation(String input) {
        //输入转成的char列表
        char[] charsOfInput = input.toCharArray();

        //是否在[]之间
        boolean isInCheckingBox = false;

        //用于存储变量两端位置的东西
        int[] pair = {0, 0};

        //最终多次写入的时候会产生偏移
        int offset = 0;

        //写入的时候用的Builder
        StringBuilder inputBuilder = new StringBuilder(input);

        for (int i = 0; i < charsOfInput.length; i++) {

            if (charsOfInput[i] == '[' && !isInCheckingBox) {

                pair[0] = i + 1;  //内部原因，得加1
                isInCheckingBox = true;
            } else if (charsOfInput[i] == ']' && isInCheckingBox) {
                pair[1] = i;
                isInCheckingBox = false;

                char[] temp = new char[pair[1] - pair[0]];  //一个和变量长度相等的char数组，先创建出来，然后下一个函数里更新
                input.getChars(pair[0], pair[1], temp, 0);  //获取计算式子
                String expr = new String(temp);  //转换，这里var就是变量名
                if (expr.isBlank()) {  //变量名是空的-->顺延检查
                    pair[0] = i + 1;
                    continue;
                }

                //各得其所
                //引入偏移，因为修改过的字符串会变长或者变短

                //svar全程StringOfVar，就是变量实际值
                String svar = null;
                try {
                    svar = OperationAnalyser.getResult(expr);
                } catch (Exception e) {/*这里的错误不会被继续抛出*/}
                if (svar == null) {  //算数表达式不合法的分支
                    continue;
                } else {
                    //下面的什么加一减一都是测试出来的，轻易别动
                    inputBuilder.replace(pair[0] - 1 - offset, pair[1] + 1 - offset, svar);
                    //更新i，charsOfInput
                    offset += svar.length() - expr.length() - 2;
                }
            }

        }

        return inputBuilder.toString();
    }

    private String replaceAndExecuteFunction(String input) {
        //输入转成的char列表
        char[] charsOfInput = input.toCharArray();

        //是否在$$之间
        boolean isInCheckingBox = false;

        //用于存储变量两端位置的东西
        int[] pair = {0, 0};

        //最终多次写入的时候会产生偏移
        int offset = 0;

        //写入的时候用的Builder
        StringBuilder inputBuilder = new StringBuilder(input);

        for (int i = 0; i < charsOfInput.length; i++) {
            if (charsOfInput[i] == '$' && (i == 0 || charsOfInput[i - 1] != '\\')) {
                if (!isInCheckingBox) {
                    //第一个$
                    pair[0] = i + 1;  //内部原因，得加1
                    isInCheckingBox = true;
                } else {
                    pair[1] = i;
                    isInCheckingBox = false;
                    //第二个$
                    char[] temp = new char[pair[1] - pair[0]];  //一个和变量长度相等的char数组，先创建出来，然后下一个函数里更新
                    input.getChars(pair[0], pair[1], temp, 0);  //获取变量名
                    String funcWhole = new String(temp);  //转换，这里var就是变量名
                    if (funcWhole.isBlank()) {  //变量名是空的-->顺延检查
                        pair[0] = i + 1;
                        isInCheckingBox = true;
                        continue;
                    }

                    //下面执行函数
                    String[] funcSplited = Toolbox.splitStringByIndex(funcWhole, ' ', 2);
                    Method method = Methods.get(funcSplited[0]);
                    if (method == null) {
                        throw new NyaFunctionNotFoundException(funcSplited[0]);
                    }
                    int c = method.getParameterCount();


                    try {
                        String result = null;
                        if (c == 0) {
                            result = (String) method.invoke(null);
                        } else {

                            if (method.getParameterTypes()[0] == Task.class) {
                                Object[] params = Toolbox.addToFirst(task, Toolbox.splitStringByIndex(funcSplited[1], ' ', c));
                                result = (String) method.invoke(null, params);
                            } else {
                                result = (String) method.invoke(null, Toolbox.splitStringByIndex(funcSplited[1], ' ', c));
                            }
                        }
                        if (result == null) {
                            inputBuilder.delete(pair[0] - 1, pair[1] + 1);
                            break;
                        } else {
                            inputBuilder.replace(pair[0] - 1, pair[1] + 1, result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return inputBuilder.toString();
    }

    public String analyze(String line) {
        line = line.replaceFirst("##.*", "");
        if (line == null) {//去掉注释之后如果是空的，就跳过
            return "";
        } else {
            line = this.replaceWithVars(line);
            line = this.replaceWithJSON(line);
            line = this.replaceCalculation(line);
            line = this.replaceAndExecuteFunction(line);


            //赋值
            if (line.matches(".:([\\s\\S]*)")) {
                String[] temp = line.split(":", 2);
                this.varManager.setVar(temp[0], temp[1]);
                return "";  //赋值语句无需发送
            }
            //赋值结束

            return line;
        }
    }

    public void analyze(String[] lines, Task task) {

        this.varManager = task.getVarManager();
        this.task = task;


        int i = 0;
        try {
            int ifLevel = 0;
            int loopLevel = 0;     //用于循环条件不符合的快速掠过
            Stack<LoopHead> loop_marks = new Stack<>();
            String line;
            StringBuffer sb = new StringBuffer();


            for (; i < lines.length; i++) {
                line = lines[i];


                if (line.startsWith("如果:")) {
                    if (ifLevel != 0) {  //不是第一次来这里了（在一个正在掠过的如果结构中）
                        ifLevel++;
                    } else if (!BoolExprAnalyser.analyze(analyze(line.replaceFirst("如果:", "")))) {
                        //是第一次来，但是不符合（先剥皮，再解析替换，再判断t&f，不符合。）
                        ifLevel = 1;
                    }  //else  第一次来，还符合：直接跳过
                } else if (line.equals("如果尾")) {
                    if (ifLevel != 0)  //正在判断
                        ifLevel--;  //下降一级
                    //else  没在判断：退了

                } else if (line.startsWith("循环:")) {
                    if (ifLevel != 0) {   //正在掠过
                        ifLevel++;
                    } else {
                        line = line.replaceFirst("循环:", "");
                        LoopHead loopH = new LoopHead(line.split(";"), i, this);
                        loopH.init();
                        if (loopH.canRun()) {   //可以运行
                            loop_marks.push(loopH);
                        } else {
                            loopLevel = 1;
                        }
                    }
                    //warn 该循环结构中的变量没有生命周期！需要手动清除（或者不清除）
                } else if (line.equals("循环尾")) {
                    if (ifLevel != 0) {   //正在掠过
                        ifLevel--;
                    } else {   //不是在掠过  -->  向上跳（peek，更新i）
                        LoopHead loopH = loop_marks.peek();
                        loopH.update();
                        if (loopH.canRun()) {
                            i = loopH.getIndex();  //跳到最上面，循环头的下一行
                        } else {
                            loop_marks.pop();  //条件达成了，跑完了，弹出栈。
                        }
                    }
                } else {

                    if (ifLevel != 0 || loopLevel != 0) {  //还在掠过,直接跳了
                    } else {
                        if (line.equals("返回")) {   //单独处理返回语句
                            finalSend(sb.toString());
                            return;
                        }
                        sb.append(analyze(line));
                    }

                }
            }
            finalSend(sb.toString());
        } catch (NyaPlusException e) {
            throw e.setLine(i + 1);
        }
    }

    /**
     * 处理结束之后可以统一通过这里输出
     * @param s 要输出的内容
     * */
    private void finalSend(String s){
        Controller.getOUTPUTTER().send(s
                .replace("\\n", "\n")
                .replace("\\r", "\r"), task);  //先这么写着
    }
}
