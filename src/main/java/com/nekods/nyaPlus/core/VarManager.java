package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.exceptions.NyaDefaultFieldReassignmentException;
import com.nekods.nyaPlus.exceptions.NyaRenamingVarException;
import com.nekods.nyaPlus.exceptions.NyaVariableNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;

//TODO 想要实现变量储存值为object的功能，但是如果这么写的话，直接把变量嵌入字符串里会出问题。
//解决方法，一般替换的时候调用对象的toString方法，再替换。
//如果出现那种必须要存成引用的变量（比如以后可能会出现的图片），就写一个函数进行存储和使用。
//也就是说%A%这种方式默认为值传递，而且是字符串传递，

public class VarManager {
    private static HashMap<String, Object> globalVars = new HashMap();
    private HashMap<String, Object> Vars = new HashMap();
    //private HashMap<String, Object> defaultVars = new HashMap();
    static HashMap<String, Method> Methods = Functions.getFuncs();

    public VarManager() {
    }
    //todo 为避免歧义，命名变量时，不能使用`%`符号，应当添加检查！！！
    //算了，不单独检查了，如果出问题让用户自己处理吧，这不是啥大问题。
    //tm写文档写一半想起来的

    public void setGlobalVar(String name, Object value) {
        if (this.Vars.containsKey(name)) {
            throw new NyaRenamingVarException(name);
        } else {
            globalVars.put(name, value);
        }
    }

    public void setVar(String name, Object value) {
        if (globalVars.containsKey(name)) {
            throw new NyaRenamingVarException(name);
        } else {
            Vars.put(name, value);
        }
    }

    public Object getVar(String var) {
        Object svar;
        if (globalVars.containsKey(var)) {
            svar = globalVars.get(var);     //这是变量值
        } else if (Vars.containsKey(var)) {
            svar = Vars.get(var);    //这是变量值
        } else {
            throw new NyaVariableNotFoundException(var);
        }
        return svar;
    }

    public Object getVar(char var) {
        return this.getVar(String.valueOf(var));
    }

    public boolean hasVar(String var) {
        return globalVars.containsKey(var) || this.Vars.containsKey(var);
    }

    public boolean hasVar(char var) {
        return this.hasVar(String.valueOf(var));
    }

    public String getStringVar(String var) {
        Object val = this.getVar(var);
        if (val.getClass() == String.class) {
            return (String)val;
        } else {
            throw new RuntimeException(var + "不是字符串类型");
        }
    }
}
