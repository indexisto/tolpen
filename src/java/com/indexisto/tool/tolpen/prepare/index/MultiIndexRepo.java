package com.indexisto.tool.tolpen.prepare.index;

import static com.google.common.base.Preconditions.checkArgument;
import static com.indexisto.tool.tolpen.config.Config.Prepare.multiIndexDocumentThreshold;

public enum MultiIndexRepo {

    instance;


    private int count = 0;
    private long docCount = 0;


    public boolean tryAddChild(IndexType indexType) {
        checkArgument(indexType.getDocCount() < multiIndexDocumentThreshold);

        if (count == 0) {
            return false;
        }
        if (docCount + indexType.getDocCount() > multiIndexDocumentThreshold) {
            return false;
        }
        docCount += indexType.getDocCount();
        return true;
    }


    public void newMultiIndex() {
        docCount = 0;
        ++count;
    }


    public String getMultiIndexName() {
        return "m" + Integer.toString(count);
    }
}