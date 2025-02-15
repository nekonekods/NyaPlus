package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.exceptions.NyaDefaultFieldReassignmentException;
import com.nekods.nyaPlus.exceptions.NyaFinalFieldReassignmentException;
import com.nekods.nyaPlus.exceptions.NyaRenamingVarException;
import com.nekods.nyaPlus.exceptions.NyaVariableNotFoundException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class VarManager {
    private static HashMap<String, Object> globalVars = new HashMap();
    private HashMap<String, Object> Vars = new HashMap();
    private HashMap<String, Object> finalVars = new HashMap();
    private HashMap<String, Object> defaultVars = new HashMap();
    static HashMap<String, Method> Methods = Functions.getFuncs();

    public VarManager() {
    }

    public void setGlobalVar(String name, Object value) {
        if (this.Vars.containsKey(name)) {
            throw new NyaRenamingVarException(name);
        } else if (this.finalVars.containsKey(name)) {
            throw new NyaFinalFieldReassignmentException(name);
        } else {
            globalVars.put(name, value);
        }
    }

    public void setFinalVars(String name, Object value) {
        if (!globalVars.containsKey(name) && !this.Vars.containsKey(name)) {
            if (this.finalVars.containsKey(name)) {
                throw new NyaFinalFieldReassignmentException(name);
            } else {
                this.finalVars.put(name, value);
            }
        } else {
            throw new NyaRenamingVarException(name);
        }
    }

    public void setDefaultVars(String name, Object value) {
        this.defaultVars.put(name, value);
    }

    public void setVar(String name, Object value) {
        if (globalVars.containsKey(name)) {
            throw new NyaRenamingVarException(name);
        } else if (this.finalVars.containsKey(name)) {
            throw new NyaFinalFieldReassignmentException(name);
        } else if (this.defaultVars.containsKey(name)) {
            throw new NyaDefaultFieldReassignmentException(name);
        } else {
            this.Vars.put(name, value);
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
