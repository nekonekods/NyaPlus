package com.nekods.nyaPlus.exceptions;

public class NyaFinalFieldReassignmentException extends NyaPlusException{

    public NyaFinalFieldReassignmentException(String var) {
        super("不能对常量（"+var+"）进行重新赋值");
    }
}
