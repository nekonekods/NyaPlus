package com.nekods.nyaPlus.exceptions;

public class NyaVariableNotFoundException extends NyaPlusException {

    public NyaVariableNotFoundException(String val) {
        super("未定义的变量：" + val);
    }

}
