package com.indexisto.tool.tolpen.util.selectable;

import static com.google.common.base.Preconditions.checkArgument;


public abstract class SelectableBase implements Selectable {

    private final long probability;
    private final long maxCount;


    public SelectableBase(long probability) {
        checkArgument(probability > 0);

        this.probability = probability;
        this.maxCount    = Long.MAX_VALUE;
    }


    public SelectableBase(long probability, long maxCount) {
        checkArgument(probability >= 0);

        this.probability = probability;
        this.maxCount    = maxCount;
    }


    @Override
    public long getProbaility() {
        return probability;
    }


    @Override
    public long getMaxCount() {
        return maxCount;
    }
}
