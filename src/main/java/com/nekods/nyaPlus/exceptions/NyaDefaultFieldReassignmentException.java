package com.nekods.nyaPlus.exceptions;

public class NyaDefaultFieldReassignmentException extends NyaPlusException{

    public NyaDefaultFieldReassignmentException(String var) {
        super("不能对默认变量（"+var+"）进行重新赋值");
    }
}
