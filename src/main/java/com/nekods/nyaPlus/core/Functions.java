package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.annotation.Outside;
import com.nekods.nyaPlus.smallTools.Task;
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
}
