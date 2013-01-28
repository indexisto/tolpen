package com.indexisto.tool.tolpen.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class StorageInput implements Closeable {

    private final Path path;
    private final InputStream stream;


    public StorageInput(Path path, InputStream stream) {
        this.path = checkNotNull(path);
        this.stream = checkNotNull(stream);
    }


    public Path getPath() {
        return path;
    }


    public InputStream getStream() {
        return stream;
    }


    @Override
    public void close() throws IOException {
        stream.close();
    }
}