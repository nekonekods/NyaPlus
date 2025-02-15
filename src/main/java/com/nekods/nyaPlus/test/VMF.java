package com.nekods.nyaPlus.test;

import com.nekods.nyaPlus.core.AbstractVarManagerFactory;
import com.nekods.nyaPlus.core.VarManager;
import com.nekods.nyaPlus.smallTools.Task;

public class VMF extends AbstractVarManagerFactory {
    @Override
    public VarManager getVarManager(Task t) {
        return new VarManager(){{
            setVar("固定","yes");
            setVar("QQ",t.getInfo());
        }};
    }
    public VMF(){
        super();
    }
}
