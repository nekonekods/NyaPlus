package com.nekods.nyaPlus.exceptions;

public class NyaIllegalArgumentNumException extends NyaPlusException{
    public NyaIllegalArgumentNumException(String funcName, int expected, int actual) {
        super(funcName + " 参数数量错误，应为" + expected + "个，实际为" + actual + "个");
    }
    public NyaIllegalArgumentNumException(String funcName) {
        super(funcName + " 参数数量错误，应无参，实际有参");
    }
}
