package com.indexisto.tool.tolpen.prepare.index;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class IndexParams {

    private final int shardNum;
    private final int replicaNum;
    private final IndexParams parentIndex;


    public static IndexParams newIndex(int shardNum, int replicaNum) {
        checkArgument(shardNum > 0);
        checkArgument(replicaNum > 0);

        return new IndexParams(shardNum, replicaNum, null);
    }


    public static IndexParams newChildIndex(IndexParams parentIndex) {
        checkNotNull(parentIndex);

        return new IndexParams(-1, -1, parentIndex);
    }


    private IndexParams(int shardNum, int replicaNum, IndexParams parentIndex) {
        this.parentIndex = parentIndex;
        this.shardNum = shardNum;
        this.replicaNum = replicaNum;
    }


    public int getShardNum() {
        return shardNum;
    }


    public int getReplicaNum() {
        return replicaNum;
    }


    public boolean isChildIndex() {
        return getParentIndex() != null;
    }


    public IndexParams getParentIndex() {
        return parentIndex;
    }
}