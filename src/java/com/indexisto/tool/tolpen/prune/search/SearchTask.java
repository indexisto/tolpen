package com.indexisto.tool.tolpen.prune.search;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

public class SearchTask {

    private final SearchType type;
    private final String query;
    private final int span;
    private final boolean isHighlight;


    public static SearchTask parse(String line) {
        checkNotNull(line);
        return SearchType.valueOf(StringUtils.substringBefore(line, ":")).parse(line);
    }


    SearchTask(SearchType type, String query) {
        this.type = checkNotNull(type);
        this.query = checkNotNull(query);
        span = 0;
        isHighlight = true;
    }


    SearchTask(SearchType type, String query, int span) {
        this.type = checkNotNull(type);
        this.query = checkNotNull(query);
        this.span = span;
        isHighlight = true;
    }


    SearchTask(SearchType type, String query, int span, boolean isHighlight) {
        this.type = checkNotNull(type);
        this.query = checkNotNull(query);
        this.span = span;
        this.isHighlight = isHighlight;
    }


    public SearchType getType() {
        return type;
    }


    public String getQuery() {
        return query;
    }


    public int getSpan() {
        return span;
    }


    public boolean isHighlight() {
        return isHighlight;
    }


    public InputStream newInputStream() {
        return type.newInputStream(this);
    }
}
