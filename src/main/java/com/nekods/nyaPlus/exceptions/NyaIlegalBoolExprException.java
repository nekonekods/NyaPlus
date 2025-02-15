package com.nekods.nyaPlus.exceptions;

public class NyaIlegalBoolExprException extends NyaPlusException{
    public NyaIlegalBoolExprException(String expr) {
        super("不合法的布尔表达式："+expr);

    }
}
