package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.exceptions.NyaIlegalBoolExprException;
import com.nekods.nyaPlus.smallTools.Toolbox;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BoolExprAnalyser {

    public static boolean analyze(String expression)  {
        try {
            Matcher m = Pattern.compile("(((?<!\\d)-?\\d+(\\.\\d+)?)(>|<|<=|>=|!=)((?<!\\d)-?\\d+(\\.\\d+)?))|(([^&|]+)?(==)([^&|]+)?)").matcher(expression);
            while (m.find()) {
                if (m.group(1) != null) {  //第一种情况
                    expression = expression.replace(m.group(1), calculate(m.group(2),m.group(4),m.group(5)));   //这些参数依赖于正则表达式，需注意
                }else if (m.group(7) != null) {   //第二种情况
                    expression = expression.replace(m.group(7), calculate(m.group(8),m.group(9),m.group(10)));
                }else {
                    throw new RuntimeException();
                }
            }
            return gateCal(expression) == 't';
        } catch (RuntimeException e) {
            throw new NyaIlegalBoolExprException(expression);
        }
    }

    private static String calculate(String a1, String operator, String a2){
        return switch (operator) {
            case "==" -> a1.equals(a2) ? "t" : "f";
            case "!=" -> !a1.equals(a2) ? "t" : "f";
            case ">=" -> Double.parseDouble(a1) >= Double.parseDouble(a2) ? "t" : "f";
            case "<=" -> Double.parseDouble(a1) <= Double.parseDouble(a2) ? "t" : "f";
            case ">" -> Double.parseDouble(a1) > Double.parseDouble(a2) ? "t" : "f";
            case "<" -> Double.parseDouble(a1) < Double.parseDouble(a2) ? "t" : "f";
            default -> null;
        };
    }


    private static char boolCal(char a1, char a2, char operator) {
        return switch (operator) {
            case '&' -> a1 == 't' && a2 == 't' ? 't' : 'f';
            case '|' -> a1 == 't' || a2 == 't' ? 't' : 'f';
            default -> throw new RuntimeException("illegal operator!");
        };
    }


    //获得符号权限
    private static int getPriority(Character s){
        if(s == null) return 0;
        switch(s) {
            case '(':return 1;
            case '&':
            case '|':return 2;
            default:break;
        }
        return -1;
    }



    private static String toSufExpr(String expr){
//        System.out.println("将"+expr+"解析为后缀表达式...");
        /*返回结果字符串*/
        StringBuffer sufExpr = new StringBuffer();
        /*盛放运算符的栈*/
        Stack<Character> operator = new Stack<>();
        operator.push(null);//在栈顶压人一个null，配合它的优先级，目的是减少下面程序的判断
        /* 将expr打散分散成运算数和运算符 */
        char[] expCharSet = expr.toCharArray();
        final Character[] ops = {'&','|','(',')'};
        for(Character temp : expCharSet){
            if (Toolbox.setHas(ops,temp)) { //是运算符
                if (temp.equals('(')) { //遇到左括号，直接压栈
                    operator.push(temp);
//                    System.out.println("'('压栈");
                } else if (temp.equals(')')) { //遇到右括号，弹栈输出直到弹出左括号（左括号不输出）
                    Character topItem = null;
                    while (!(topItem = operator.pop()).equals('(')) {
//                        System.out.println(topItem+"弹栈");
                        sufExpr.append(topItem+" ");
//                        System.out.println("输出:"+sufExpr);
                    }
                } else {//遇到运算符，比较栈顶符号，若该运算符优先级大于栈顶，直接压栈；若小于栈顶，弹栈输出直到大于栈顶，然后将改运算符压栈。
                    while(getPriority(temp) <= getPriority(operator.peek())) {
                        sufExpr.append(operator.pop());
//                        System.out.println("输出sufExpr:"+sufExpr);
                    }
                    operator.push(temp);
//                    System.out.println("\""+temp+"\""+"压栈");
                }
            }else {//遇到数字直接输出
                sufExpr.append(temp);
//                System.out.println("输出sufExpr:"+sufExpr);
            }

        }

        Character topItem;//最后将符合栈弹栈并输出
        while(null != (topItem = operator.pop())) {
            sufExpr.append(topItem);
        }
        return sufExpr.toString();
    }



    private static char gateCal(String expr)  {
        String sufExpr = toSufExpr(expr);// 转为后缀表达式
//        System.out.println("开始计算后缀表达式...");
        /* 盛放数字栈 */
        Stack<Character> number = new Stack<>();
        /* 这个正则匹配每个数字和符号 */
        char[] expCharSet = sufExpr.toCharArray();
        Character[] ops = {'&','|'};
        for(Character temp : expCharSet) {
            if (Toolbox.setHas(ops,temp)) {// 遇到运算符，将最后两个数字取出，进行该运算，将结果再放入容器
//                System.out.println("符号" + temp);
                char a1 = number.pop();
                char a2 = number.pop();
                char res = boolCal(a2, a1, temp);
                number.push(res);
//                System.out.println(a2 + "和" + a1 + "弹栈，并计算" + a2 + temp + a1);
//                System.out.println("数字栈：" + number);
            } else {// 遇到数字直接放入容器
                number.push(temp);
//                System.out.println("数字栈：" + number);
            }
        }
        return number.pop();
    }


}