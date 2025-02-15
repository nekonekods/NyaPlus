package com.nekods.nyaPlus.exceptions;

public class NyaIndexOutOfBoundsException extends NyaPlusException {

    public NyaIndexOutOfBoundsException(int index,int size,String list) {
        super("数组超限，数组长度："+size+"，你的索引："+index+"，问题数组："+list);
    }
}
