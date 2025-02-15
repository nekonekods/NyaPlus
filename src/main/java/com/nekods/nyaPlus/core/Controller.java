//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.nekods.nyaPlus.core;

public abstract class Controller {
    private static AbstractVarManagerFactory VM_FACTORY;
    private static AbstractOutPutter OUTPUTTER;
    private static Class<? extends Functions> funcSrc = Functions.class;

    public static Class<? extends Functions> getFuncSrc() {
        return funcSrc;
    }

    public static AbstractOutPutter getOUTPUTTER() {
        if (OUTPUTTER == null) {
            throw new RuntimeException("没有设置Outputter");
        } else {
            return OUTPUTTER;
        }
    }

    public static AbstractVarManagerFactory getVMFactory() {
        if (VM_FACTORY == null) {
            throw new RuntimeException("没有设置VMFactory");
        } else {
            return VM_FACTORY;
        }
    }

    public void setFuncSrc(Class<? extends Functions> funcSrc) {
        Controller.funcSrc = funcSrc;
    }

    public static void setOUTPUTTER(AbstractOutPutter Outputter) {
        OUTPUTTER = Outputter;
    }

    public static void setVMFactory(AbstractVarManagerFactory vmFactory) {
        VM_FACTORY = vmFactory;
    }
}
