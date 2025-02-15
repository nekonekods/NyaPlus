package com.nekods.nyaPlus.exceptions;

public class NyaUndefinedJSONIndex extends NyaPlusException{

    public NyaUndefinedJSONIndex(String list,String index) {
        super("找不到该的JSON键值：" + index + "，列表：" + list);
    }
}
