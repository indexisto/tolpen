package com.indexisto.tool.tolpen.storage.fs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.indexisto.tool.tolpen.storage.StorageOutput;

class OutputGenerator implements Iterable<StorageOutput> {

    private final Path storage;
    private final long limit;
    private final String extension;
    private long count = 0;


    public OutputGenerator(Path storage, long limit, String extension) {
        this.storage = checkNotNull(storage);
        this.limit = limit;
        this.extension = checkNotNull(extension);
    }


    @Override
    public Iterator<StorageOutput> iterator() {
        return new AbstractIterator<StorageOutput>() {

            @Override
            protected StorageOutput computeNext() {
                if (limit < count) {
                    return endOfData();
                }
                final Path path = storage.resolve(Util.toFilePath(count++, extension));
                path.getParent().toFile().mkdirs();
                try {
                    path.toFile().createNewFile();
                    return new StorageOutput(path, new FileOutputStream(path.toFile()));
                }
                catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}