package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Path;

import com.indexisto.tool.tolpen.config.Config.RequestStorage;

public class RequestMeta {

    private final String name;
    private final Path path;
    private final String uri;


    public RequestMeta(String name, Path path, String uri) {
        this.name = checkNotNull(name);
        this.path = checkNotNull(path);
        this.uri = checkNotNull(uri);
    }


    public String getName() {
        return name;
    }


    public Path getPath() {
        return RequestStorage.storage.relativize(path);
    }


    public String getUri() {
        return uri;
    }
}
