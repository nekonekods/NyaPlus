
package com.nekods.nyaPlus.smallTools;

import com.nekods.nyaPlus.core.LineAnalyser;
import com.nekods.nyaPlus.core.VarManager;

public class Task<T> {
    //要寻找的那个路径
    private String path;

    //要执行的指令，也就是头
    private String commend;

    //等待注入的VM
    private VarManager varManager;

    //延迟注入的LineAnalyzer
    private LineAnalyser lineAnalyser;

    //附带的信息
    private T info;

    public Task(String path, String commend, T info) {
        this.path = path;
        this.commend = commend;
        this.info = info;
    }

    public String getPath() {
        return this.path;
    }

    public String getCommend() {
        return this.commend;
    }

    public VarManager getVarManager() {
        return this.varManager;
    }

    public void setVarManager(VarManager varManager) {
        this.varManager = varManager;
    }

    public T getInfo() {
        return this.info;
    }

    public LineAnalyser getLineAnalyser() {
        return lineAnalyser;
    }

    public void setLineAnalyser(LineAnalyser lineAnalyser) {
        this.lineAnalyser = lineAnalyser;
    }

}