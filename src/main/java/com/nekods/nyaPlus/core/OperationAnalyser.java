//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nekods.nyaPlus.core;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//其实这一段都是从网上copy来的，BoolExprAnalyser也是基于这个改造的，用的就是最传统的转化成后缀表达式的方法，比较好理解的。

abstract class OperationAnalyser {


    //计算的函数
    private static double doubleCal(double a1, double a2, char operator) {
        switch (operator) {
            case '+':
                return a1 + a2;
            case '-':
                return a1 - a2;
            case '*':
                return a1 * a2;
            case '/':
                return a1 / a2;
            case '%':
                return a1 % a2;
            case '^':
                return Math.pow(a1,a2);
            default:
                break;
        }
        throw new RuntimeException("illegal operator!");
    }


    //获得符号权限
    private static int getPriority(String s) throws Exception {
        if(s==null) return 0;
        switch(s) {
            case "(":return 1;
            case "+":
            case "-":return 2;
            case "*":
            case "%":
            case "/":return 3;
            case "^":return 4;

            default:break;
        }
        throw new Exception("illegal operator!");
    }



    private static String toSufExpr(String expr) throws Exception {
//        System.out.println("将"+expr+"解析为后缀表达式...");
        /*返回结果字符串*/
        StringBuffer sufExpr = new StringBuffer();
        /*盛放运算符的栈*/
        Stack<String> operator = new Stack<String>();
        operator.push(null);//在栈顶压人一个null，配合它的优先级，目的是减少下面程序的判断
        /* 将expr打散分散成运算数和运算符 */
        Pattern p = Pattern.compile("(?<!\\d)-?\\d+(\\.\\d+)?|[+\\-*/%()^]");//这个正则为匹配表达式中的数字或运算符
        /**
         * 这是一个正则表达式，用于匹配特定的字符模式。下面逐段进行解释：
         *
         * 1. `(?<!\d)`：这是一个负向零宽断言，表示当前位置的前面不能是数字。
         * 2. `-?\d+(\.\d+)?`：
         *    - `-?`表示可选的负号。
         *    - `\d+`表示一个或多个数字。
         *    - `(\.\d+)?`表示可选的小数部分，其中`\.`表示小数点，`\d+`表示一个或多个数字。这部分整体用于匹配整数或浮点数。
         * 3. `|`：表示“或”的关系。
         * 4. `[+\-/*%()]`：这是一个字符组，用于匹配加、减、乘、除、取余运算符以及括号。
         *
         *总体来说，这个正则表达式可以匹配整数、浮点数以及常见的算术运算符和括号。例如：
         *
         *- “123”可以被匹配，因为它是整数。
         *- “3.14”可以被匹配，因为它是浮点数。
         *- “+”、“-”、“*”、“/”、“%”、“(”、“)”也可以被分别匹配。
         * */


        Matcher m = p.matcher(expr);
        while (m.find()) {
            String temp = m.group();
            if (temp.matches("[+\\-*/%()^]")) { //是运算符
                if (temp.equals("(")) { //遇到左括号，直接压栈
                    operator.push(temp);
//                    System.out.println("'('压栈");
                } else if (temp.equals(")")) { //遇到右括号，弹栈输出直到弹出左括号（左括号不输出）
                    String topItem = null;
                    while (!(topItem = operator.pop()).equals("(")) {
//                        System.out.println(topItem+"弹栈");
                        sufExpr.append(topItem+" ");
//                        System.out.println("输出:"+sufExpr);
                    }
                } else {//遇到运算符，比较栈顶符号，若该运算符优先级大于栈顶，直接压栈；若小于栈顶，弹栈输出直到大于栈顶，然后将改运算符压栈。
                    while(getPriority(temp) <= getPriority(operator.peek())) {
                        sufExpr.append(operator.pop()+" ");
//                        System.out.println("输出sufExpr:"+sufExpr);
                    }
                    operator.push(temp);
//                    System.out.println("\""+temp+"\""+"压栈");
                }
            }else {//遇到数字直接输出
                sufExpr.append(temp+" ");
//                System.out.println("输出sufExpr:"+sufExpr);
            }

        }

        String topItem = null;//最后将符号栈弹栈并输出
        while(null != (topItem = operator.pop())) {
            sufExpr.append(topItem+" ");
        }
        return sufExpr.toString();
    }



    public static String getResult(String expr) throws Exception {
        if(expr.matches("\\d")|!expr.matches("((-?\\d+(\\.\\d+)?)[+\\-*%/^])+(-?\\d+(\\.\\d+)?)"))
            return null;
        String sufExpr = toSufExpr(expr);// 转为后缀表达式
//        System.out.println("开始计算后缀表达式...");
        /* 盛放数字栈 */
        Stack<Double> number = new Stack<Double>();
        /* 这个正则匹配每个数字和符号 */

        Pattern p = Pattern.compile("-?\\d+(\\.\\d+)?|[+\\-*%/^]");
        Matcher m = p.matcher(sufExpr);
        while (m.find()) {
            String temp = m.group();
//            .
            if (temp.matches("[+\\-*%/^]")) {// 遇到运算符，将最后两个数字取出，进行该运算，将结果再放入容器
//                System.out.println("符号" + temp);
                double a1 = number.pop();
                double a2 = number.pop();
                double res = doubleCal(a2, a1, temp.charAt(0));
                number.push(res);
//                System.out.println(a2 + "和" + a1 + "弹栈，并计算" + a2 + temp + a1);
//                System.out.println("数字栈：" + number);
            } else {// 遇到数字直接放入容器
                number.push(Double.valueOf(temp));
//                System.out.println("数字栈：" + number);
            }
        }

        Double pop = number.pop();
        return Math.abs(pop % 1) < 0.01 || Math.abs(pop % 1) > 0.99 /*是整数吗？*/? (int)Math.round(pop) + "" : pop + "";
    }


}
