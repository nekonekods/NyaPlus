package com.nekods.nyaPlus.exceptions;

import com.nekods.nyaPlus.core.Controller;

public abstract class NyaPlusException extends RuntimeException{
    /**
     * 使用的时候直接catch这个类，setLine，show就可以了
     *
     * */

    int line = -1 ;
    String message ;
    String head;

    public NyaPlusException(String m){
        this.message = m;
    }

    public NyaPlusException setLine(int line) {
        this.line = line;
        return this;
    }

    public NyaPlusException setHead(String name) {
        this.head = name;
        return this;
    }

    public void show(){
        Controller.getOUTPUTTER().err(message + "    所属：" + head   + "    行数：" + line);
    }
}
