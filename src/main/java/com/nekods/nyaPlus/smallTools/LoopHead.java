package com.nekods.nyaPlus.smallTools;

import com.nekods.nyaPlus.core.BoolExprAnalyser;
import com.nekods.nyaPlus.core.LineAnalyser;

public class LoopHead {
    private final String init;
    private final String condition;
    private final String update;
    private final int index;
    private boolean hasInited = false;
    private LineAnalyser la = null;

    public int getIndex() {
        return this.index;
    }

    public void init() {
        if (!this.hasInited) {
            this.la.analyze(this.init);
            this.hasInited = true;
        }

    }

    public void update() {
        this.la.analyze(this.update);
    }

    public LoopHead(String init, String condition, String update, int index) {
        this.init = init;
        this.condition = condition;
        this.update = update;
        this.index = index;
    }

    public LoopHead(String[] group, int index, LineAnalyser la) {
        this.init = group[0];
        this.condition = group[1];
        this.update = group[2];
        this.index = index;
        this.la = la;
    }

    public boolean canRun() {
        return BoolExprAnalyser.analyze(this.la.analyze(this.condition));
    }
}