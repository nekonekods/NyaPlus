package com.nekods.nyaPlus.exceptions;

public class NyaJSONNotFoundException extends NyaPlusException{
    public NyaJSONNotFoundException(String val) {
        super("未定义的数组：" + val);
    }

    public NyaJSONNotFoundException(char val) {
        super("未定义的数组：" + val);
    }
}
