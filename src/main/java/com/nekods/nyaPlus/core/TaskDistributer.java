//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.smallTools.Task;
import java.io.BufferedReader;

public class TaskDistributer {

    //done 这是给线程池提供任务的类，从BatchExe中间分离出来的，这则就是按照文件路径分发任务

    private BufferedReader fr;
    private int poolSize;
    private String[] sourcePath;

    public void init() {
        ThreadPool.getINSTANCE(this.sourcePath.length);
        Controller.getOUTPUTTER().log("初始化完成..................");
    }

    public TaskDistributer(String... filepath) {
        new TaskDistributer(3, filepath);
    }

    public TaskDistributer(int poolSize, String... filepath) {
        this.sourcePath = filepath;
        this.poolSize = poolSize;
    }

    public void execute(String s, long info) {
        for(String p : sourcePath)
            ThreadPool.getINSTANCE().add(new Task(p,s,info));
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setSourcePath(String... sourcePath) {
        this.sourcePath = sourcePath;
    }
}
