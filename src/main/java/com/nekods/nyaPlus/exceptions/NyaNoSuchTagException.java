package com.nekods.nyaPlus.exceptions;

public class NyaNoSuchTagException extends NyaPlusException{

    public NyaNoSuchTagException(String tag) {
        super("没有 %s 标签".formatted(tag));
    }
}
