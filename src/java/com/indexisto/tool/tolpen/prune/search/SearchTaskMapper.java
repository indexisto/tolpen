package com.indexisto.tool.tolpen.prune.search;

import static com.indexisto.tool.tolpen.prune.search.SearchType.HighPhrase;
import static com.indexisto.tool.tolpen.prune.search.SearchType.HighPhrasePrefix;
import static com.indexisto.tool.tolpen.prune.search.SearchType.LowPhrase;
import static com.indexisto.tool.tolpen.prune.search.SearchType.LowPhrasePrefix;
import static com.indexisto.tool.tolpen.prune.search.SearchType.MedPhrase;
import static com.indexisto.tool.tolpen.prune.search.SearchType.MedPhrasePrefix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

class SearchTaskMapper {

    private final static Map<SearchType, SearchType> map = new EnumMap<>(SearchType.class);
    static {
        addMapping(HighPhrase, HighPhrasePrefix);
        addMapping(MedPhrase,  MedPhrasePrefix);
        addMapping(LowPhrase,  LowPhrasePrefix);
    }

    public static Collection<SearchType> map(SearchType type) {
        final Collection<SearchType> result = new ArrayList<>();
        result.add(type);
        if (map.containsKey(type)) {
            result.add(map.get(type));
        }
        return result;
    }


    private static void addMapping(SearchType a, SearchType b) {
        map.put(a, b);
        map.put(b, a);
    }
}