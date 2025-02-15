package com.nekods.nyaPlus.exceptions;

public class NyaDicNotFoundException extends NyaPlusException{

    public NyaDicNotFoundException(String m) {
        super("没找到词库：" + m);
    }
}
