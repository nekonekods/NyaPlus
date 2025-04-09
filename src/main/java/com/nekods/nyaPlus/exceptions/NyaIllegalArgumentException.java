package com.nekods.nyaPlus.exceptions;

public class NyaIllegalArgumentException extends NyaPlusException {
    public NyaIllegalArgumentException(String message) {
        super("参数错误："+message);
    }
}
