package com.nekods.nyaPlus.exceptions;

public class NyaArrayIndexParamException extends NyaPlusException {

    public NyaArrayIndexParamException(String index) {
        super("需要一个整数作为获取数组中元素的索引，你提供的是：" + index);
    }
}
