package com.nekods.nyaPlus.exceptions;

public class NyaRenamingVarException extends NyaPlusException{

    public NyaRenamingVarException(String varName) {
        super("已经在其他变量领域中定义" + varName +"了，不能跨范围定义变量" );
    }
}
