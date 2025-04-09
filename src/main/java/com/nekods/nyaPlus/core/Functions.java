package com.nekods.nyaPlus.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nekods.nyaPlus.annotation.Outside;
import com.nekods.nyaPlus.exceptions.NyaIllegalArgumentException;
import com.nekods.nyaPlus.smallTools.Task;
import com.nekods.nyaPlus.smallTools.Toolbox;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Functions {

    //todo 得要处理这个函数类的继承问题，所有内部调用过的函数都要final（要不就乱套了）

    //这是旧版方案（挠头
//    public static HashMap<String, String> nameTOchin = new HashMap<String, String>() {
//        {
//            this.put("test", "测试");
//            this.put("setVar", "变量");
//            this.put("setGlobalVar", "全局变量");
//            this.put("getVar", "取变量");
//        }
//    };
    static HashMap<String, Method> methods = new HashMap();

    public Functions() {
    }

    public static HashMap<String, Method> getFuncs() {
        Class<? extends Functions> funcClass = Controller.getFuncSrc();
        Method[] tempMethods = funcClass.getDeclaredMethods();
        for (Method m : tempMethods) {
            if (m.isAnnotationPresent(Outside.class)) {
                methods.put(m.getAnnotation(Outside.class).name(), m);
            }
        }
        return methods;

    }

    @Outside(name = "测试")
    public static String test(Task t, String input) {
        Controller.getOUTPUTTER().send(input, t);
        return "完成，task的info为：" + t.getInfo();

    }

    /*reminder 将task设置为备选项，用户可以自行选择
     *  那么同时还得修改调用函数那里的逻辑，可以直接用之前应对VM的代码
     *
     * done 就是,将task作为第一个参数时，自动传入，
     *      否则，就当一般函数操作  */

    /*reminder 将整个变量系统做成该是什么类型就是什么类型的，然后在调用时，自己转换。*/

    @Outside(name = "变量")
    public static final void setVar(Task t, String name, String value) {
        t.getVarManager().setVar(name, value);
    }

    @Outside(name = "取变量")
    public static void getVar(Task t, String name) {
        t.getVarManager().getVar(name);
    }

    @Outside(name = "全局变量")
    public static void setGlobalVar(Task t, String name, String value) {
        t.getVarManager().setGlobalVar(name, value);
    }

    @Outside(name = "随机数")
    public static String getRandomNum(String start, String end) {
        int s = Integer.parseInt(start);
        int e = Integer.parseInt(end);
        return String.valueOf((int)(Math.random() * (e - s) + s));
    }

    @Outside(name = "JSON")
    public static String JSON(Task task,String op,String args) {
        String[] argsArr = args.split(" ",2);
        String jsonName = argsArr[0];
        if(argsArr.length > 1){
            argsArr = argsArr[1].split(" ",2);
        }else{
            argsArr = new String[0];
        }

        try {
            VarManager vm = task.getVarManager();
            if(op.equals("添加")){
                switch (argsArr.length){
                    case 1 :
                        if (!vm.hasVar(jsonName))
                            vm.setVar(jsonName, new JSONArray());
                        vm.getJSONArr(jsonName).add(argsArr[0]);
                        break;
                    case 2:
                        if (!vm.hasVar(jsonName))
                            vm.setVar(jsonName, new JSONObject());
                        vm.getJSONObj(jsonName).put(argsArr[0],argsArr[1]);
                        break;
                }
                return null;
            }else if(op.equals("包含")){
                switch (argsArr.length){
                    case 1:
                        return vm.getJSONArr(jsonName).contains(argsArr[0])?"t":"f";
                    case 2:
                        switch (argsArr[0]){
                            case "Key":
                                return vm.getJSONObj(jsonName).containsKey(argsArr[1])?"t":"f";
                            case "Value":
                                return vm.getJSONObj(jsonName).containsValue(argsArr[1])?"t":"f";
                            default:
                                throw new NyaIllegalArgumentException("JSON操作错误 $JSON %s %s %s$".formatted(op,jsonName,args));
                        }
                }
            }else if(op.equals("删除")){

                JSON li = vm.getJSON(jsonName);
                if(li instanceof JSONArray l){
                    l.remove(Integer.parseInt(argsArr[0]));
                }else if(li instanceof JSONObject o){
                    o.remove(argsArr[0]);
                }
                return null;

            } else if (op.equals("获取")) {
                return Toolbox.getObjectByIndex(vm.getJSON(jsonName), argsArr[0]).toString();
            } else if (op.equals("长度")) {
                JSON j = vm.getJSON(jsonName);
                if(j instanceof JSONArray){
                    return String.valueOf(((JSONArray) j).size());
                }else if(j instanceof JSONObject){
                    return String.valueOf(((JSONObject) j).size());
                }
            }
            throw new NyaIllegalArgumentException("JSON操作错误 $JSON %s %s %s$".formatted(op,jsonName,args));
        } catch (NumberFormatException e) {
            throw new NyaIllegalArgumentException("JSON操作错误 $JSON %s %s %s$".formatted(op,jsonName,args));
        }

    }

    @Outside(name = "跳")
    public static void jump(Task task,String tag){
        task.getLineAnalyser().jump(tag);
    }

    @Outside(name = "Jump")
    public static void jump_(Task task,String tag){
        jump(task,tag);
    }
}
