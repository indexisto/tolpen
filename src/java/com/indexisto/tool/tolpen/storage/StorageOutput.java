package com.indexisto.tool.tolpen.storage;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;


public class StorageOutput implements Closeable {

    private final Path path;
    private final OutputStream stream;


    public StorageOutput(Path path, OutputStream stream) {
        this.path = checkNotNull(path);
        this.stream = checkNotNull(stream);
    }


    public Path getPath() {
        return path;
    }


    public OutputStream getStream() {
        return stream;
    }


    @Override
    public void close() throws IOException {
        stream.close();
    }
}