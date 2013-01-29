package com.indexisto.tool.tolpen.storage.fs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Range;
import com.indexisto.tool.tolpen.storage.StorageInput;

class InputGenerator implements Iterable<StorageInput> {

    private final Path storage;
    private final Range<Long> range;
    private final String extension;
    private long count;


    public InputGenerator(Path storage, Range<Long> range, String extension) {
        this.storage = checkNotNull(storage);
        this.range = checkNotNull(range);
        this.count = range.lowerEndpoint();
        this.extension = checkNotNull(extension);
    }


    @Override
    public Iterator<StorageInput> iterator() {
        return new AbstractIterator<StorageInput>() {

            @Override
            protected StorageInput computeNext() {
                if (!range.contains(count)) {
                    return endOfData();
                }
                final Path path = storage.resolve(Util.toFilePath(count++, extension));
                try {
                    return new StorageInput(path, new FileInputStream(path.toFile()));
                }
                catch (final FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}