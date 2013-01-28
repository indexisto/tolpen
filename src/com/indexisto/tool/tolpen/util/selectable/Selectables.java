package com.indexisto.tool.tolpen.util.selectable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Preconditions;
import com.google.common.collect.AbstractIterator;
import com.indexisto.tool.tolpen.util.Util;

public class Selectables {

    public static <T extends Selectable> Iterable<T> newSelector(Collection<T> selections) {
        return new Selector<T>(selections);
    }


    private static final class Selector<T extends Selectable> implements Iterable<T> {
        private SortedMap<Long, T> selections;
        private final Map<T, Long> counters;


        public Selector(Collection<T> selections) {
            Preconditions.checkNotNull(selections);

            this.selections = prepareSelections(selections);
            this.counters   = prepareCounters(selections);
        }


        @Override
        public Iterator<T> iterator() {
            return new AbstractIterator<T>() {

                @Override
                protected T computeNext() {
                    if (selections.isEmpty()) {
                        return endOfData();
                    }
                    return doSelect();
                }
            };
        }


        private T doSelect() {
            final long rnd = Util.nextLong(selections.lastKey());
            for (final Long key : selections.keySet()) {
                if (rnd < key) {
                    final T result = selections.get(key);
                    counters.put(result, counters.get(result) - 1);
                    if (counters.get(result) == 0) {
                        counters.remove(result);
                        selections.remove(key);
                        selections = prepareSelections(selections.values());
                    }
                    return result;
                }
            }
            throw new IllegalStateException();
        }


        private static <T extends Selectable> SortedMap<Long, T> prepareSelections(Collection<T> selections) {
            final SortedMap<Long, T> result = new TreeMap<>();
            long sum = 0;
            for (final T item : selections) {
                if (   (item.getProbaility() > 0)
                    && (item.getMaxCount() > 0)
                ) {
                    sum += item.getProbaility();
                    result.put(sum, item);
                }
            }
            return result;
        }


        private static <T extends Selectable> Map<T, Long> prepareCounters(Collection<T> selections) {
            final Map<T, Long> result = new HashMap<>();
            for (final T item : selections) {
                result.put(item, item.getMaxCount());
            }
            return result;
        }
    }
}