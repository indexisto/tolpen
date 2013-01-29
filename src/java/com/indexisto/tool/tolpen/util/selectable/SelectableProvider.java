package com.indexisto.tool.tolpen.util.selectable;

import org.elasticsearch.common.inject.Provider;

public class SelectableProvider<T> extends SelectableBase implements Provider<T>, Selectable {

    private final T item;


    public SelectableProvider(long probability, T item) {
        super(probability);

        this.item = item;
    }


    public SelectableProvider(long probability, long maxCount, T item) {
        super(probability, maxCount);

        this.item = item;
    }


    @Override
    public T get() {
        return item;
    }
}
