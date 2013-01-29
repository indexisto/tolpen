package com.indexisto.tool.tolpen.prune.search;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class SearchTask {

    private final SearchType type;
    private final String query;
    private final int slop;
    private final boolean isHighlight;


    public static Iterable<SearchTask> parse(String line) {
        checkNotNull(line);

        final Iterable<SearchType> types = SearchTaskMapper.map(
            SearchType.valueOf(StringUtils.substringBefore(line, ":"))
        );
        return Iterables.transform(types, new Parser(line));
    }


    SearchTask(SearchType type, String query) {
        this.type = checkNotNull(type);
        this.query = checkNotNull(query);
        slop = 0;
        isHighlight = true;
    }


    SearchTask(SearchType type, String query, int span) {
        this.type = checkNotNull(type);
        this.query = checkNotNull(query);
        slop = span;
        isHighlight = true;
    }


    SearchTask(SearchType type, String query, int slop, boolean isHighlight) {
        this.type = checkNotNull(type);
        this.query = checkNotNull(query);
        this.slop = slop;
        this.isHighlight = isHighlight;
    }


    public SearchType getType() {
        return type;
    }


    public String getQuery() {
        return query;
    }


    public int getSlop() {
        return slop;
    }


    public boolean isHighlight() {
        return isHighlight;
    }


    public InputStream newInputStream() {
        return type.synt(this);
    }


    private static final class Parser implements Function<SearchType, SearchTask> {
        private final String line;

        public Parser(String line) {
            this.line = line;
        }

        @Override
        public SearchTask apply(SearchType input) {
            return input.parse(line);
        }
    }
}
