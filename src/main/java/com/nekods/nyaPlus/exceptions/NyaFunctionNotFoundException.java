package com.nekods.nyaPlus.exceptions;

public class NyaFunctionNotFoundException extends NyaPlusException{

    public NyaFunctionNotFoundException(String fuc){
        super("未定义的函数：" + fuc);

    }
}
