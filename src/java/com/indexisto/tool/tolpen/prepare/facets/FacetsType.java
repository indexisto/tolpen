package com.indexisto.tool.tolpen.prepare.facets;

import static com.indexisto.tool.tolpen.util.Util.nextInt;

import java.util.UUID;

import com.google.common.base.Preconditions;

public enum FacetsType {
    highCountTag(1000, 10, 50),
    medCountTag(100, 10, 10),
    lowCountTag(10, 10, 5);


    private final String[] tags;
    private final int maxPerDocument;

    private FacetsType(int distinctCount, int tagLength, int maxPerDocument) {
        Preconditions.checkArgument(distinctCount >= maxPerDocument);

        this.maxPerDocument = maxPerDocument;
        tags = prepareTags(distinctCount, tagLength);
    }


    public String[] getTags() {
        final String[] result = new String[nextInt(maxPerDocument)];
        for(int i = 0; i < result.length; ++i) {
            result[i] = selectTag();
        }
        return result;
    }


    private String selectTag() {
        return tags[nextInt(tags.length)];
    }


    private static String[] prepareTags(int count, int length) {
        final String[] result = new String[count];
        for(int i = 0; i < result.length; ++i) {
            result[i] = generateTag(length);
        }
        return result;
    }


    private static String generateTag(int length) {
        return UUID.randomUUID().toString().substring(0, length);
    }
}
