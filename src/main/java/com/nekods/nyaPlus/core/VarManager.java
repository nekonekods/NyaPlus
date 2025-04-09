package com.nekods.nyaPlus.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.nekods.nyaPlus.exceptions.NyaIllegalArgumentException;
import com.nekods.nyaPlus.exceptions.NyaRenamingVarException;
import com.nekods.nyaPlus.exceptions.NyaVariableNotFoundException;
import com.nekods.nyaPlus.smallTools.Toolbox;

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

    //说着不写，还是写了（捂脸
    private boolean nameCheck(String name) {
        return !(name.contains("%")|name.contains(" "));
    }

    public void setGlobalVar(String name, Object value) {
        if (!nameCheck(name)) {
            throw new NyaIllegalArgumentException("变量名称中不能出现\"%\"");
        }
        if (this.Vars.containsKey(name)) {
            throw new NyaRenamingVarException(name);
        } else {
            globalVars.put(name, value);
        }
    }

    public void setGlobalVar(String name, String value) {
        setGlobalVar(name, Toolbox.checkJson(value)[1]);
    }


    public void setVar(String name, Object value) {
        if (!nameCheck(name)) {
            throw new NyaIllegalArgumentException("变量名称中不能出现\"%\"");
        }
        if (globalVars.containsKey(name)) {
            throw new NyaRenamingVarException(name);
        } else {
            Vars.put(name, value);
        }
    }
    public void setVar(String name, String value) {
        setVar(name, Toolbox.checkJson(value)[1]);
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
        return globalVars.containsKey(var) || Vars.containsKey(var);
    }

    public boolean hasVar(char var) {
        return this.hasVar(String.valueOf(var));
    }

    public String getStringVar(String var) {
        Object val = this.getVar(var);
        if (val.getClass() == String.class) {
            return (String)val;
        } else if (val instanceof JSON)  {
            return ((JSON) val).toJSONString();
        }else {
            return val.toString();
        }
    }
    public JSONObject getJSONObj(String var) {
        Object val = this.getVar(var);
        if (val.getClass() == JSONObject.class) {
            return (JSONObject)val;
        } else {
            throw new NyaIllegalArgumentException("变量" + var + "不是JSON对象");
        }
    }

    public JSONArray getJSONArr(String var) {
        Object val = this.getVar(var);
        if (val.getClass() == JSONArray.class) {
            return (JSONArray) val;
        } else {
            throw new NyaIllegalArgumentException("变量" + var + "不是JSON数组");
        }
    }

    public JSON getJSON(String var) {
        Object val = this.getVar(var);
        if (val instanceof JSON) {
            return (JSON) val;
        } else {
            throw new NyaIllegalArgumentException("变量" + var + "不是JSON");
        }
    }
}
