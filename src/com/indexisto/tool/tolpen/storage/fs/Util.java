package com.indexisto.tool.tolpen.storage.fs;

import java.io.File;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

class Util {

    private final static Joiner joiner = Joiner.on(File.separator);
    private final static Splitter splitter = Splitter.fixedLength(1);


    public static String toFilePath(long index, String extension) {
        final Iterable<String> parts = splitter.split(Long.toString(index, 36));
        return joiner.join(parts) + extension;
    }
}