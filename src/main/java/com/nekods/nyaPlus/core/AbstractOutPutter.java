package com.nekods.nyaPlus.core;

import com.nekods.nyaPlus.smallTools.Task;

public interface AbstractOutPutter {
    void send(String var1, Task var2);

    void log(String var1);

    void err(String var1);
}
