package com.nekods.nyaPlus.test;

import com.nekods.nyaPlus.core.AbstractOutPutter;
import com.nekods.nyaPlus.smallTools.Task;

public class Outputter implements AbstractOutPutter {
    @Override
    public void send(String message, Task t) {
        t.getInfo();
        System.out.println("发送："+message);
    }

    @Override
    public void log(String message) {
        System.out.println("------日志："+message);
    }

    @Override
    public void err(String message) {
        System.err.println("------错误："+message);
    }
}
