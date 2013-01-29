package com.indexisto.tool.tolpen.storage.fs;

import java.io.InputStream;

import com.google.common.collect.Range;
import com.indexisto.tool.tolpen.config.Config.DocumentStorage;
import com.indexisto.tool.tolpen.config.Config.RequestStorage;
import com.indexisto.tool.tolpen.prune.search.SearchType;
import com.indexisto.tool.tolpen.storage.StorageInput;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.util.Util;

public class FSStorage {

    public static Iterable<StorageInput> newDocumentInput(long docCount) {
        return newDocumentInput(Util.nextRange(docCount, DocumentStorage.limit));
    }


    public static Iterable<StorageInput> newDocumentInput(Range<Long> range) {
        return new InputGenerator(DocumentStorage.storage, range, DocumentStorage.extension);
    }


    public static Iterable<StorageOutput> newDocumentOutput() {
        return new OutputGenerator(DocumentStorage.storage, DocumentStorage.limit, DocumentStorage.extension);
    }


    public static Iterable<StorageInput> newRequestInput(long reqCount) {
        return newRequestInput(Util.nextRange(reqCount, RequestStorage.limit));
    }


    public static Iterable<StorageInput> newRequestInput(Range<Long> range) {
        return new InputGenerator(RequestStorage.storage, range, RequestStorage.extension);
    }


    public static Iterable<StorageOutput> newRequestOutput() {
        return new OutputGenerator(RequestStorage.storage, RequestStorage.limit, RequestStorage.extension);
    }


    public static Iterable<InputStream> newTaskInput(SearchType type, int count) {
        return TaskSource.instance.getTasks(type, count);
    }
}