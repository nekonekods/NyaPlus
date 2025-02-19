package com.nekods.nyaPlus.exceptions;

import com.nekods.nyaPlus.core.Controller;

/**
 *   这是一个自定义异常，用于表示在NyaPlus中发生的异常情况。
 *   该类继承自 {@link RuntimeException}，因此可以在需要抛出异常的地方直接抛出该类的实例。
 *   所有的NyaPlus脚本可能出现的异常都应该继承自该类。
 * */
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
