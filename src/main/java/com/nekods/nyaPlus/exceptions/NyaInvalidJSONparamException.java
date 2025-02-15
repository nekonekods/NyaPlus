package com.nekods.nyaPlus.exceptions;

public class NyaInvalidJSONparamException extends NyaPlusException{

    public NyaInvalidJSONparamException(String list) {
        super("不合法的JSON数组:" + list);
    }
}
