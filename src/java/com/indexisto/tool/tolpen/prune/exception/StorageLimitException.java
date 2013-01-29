package com.indexisto.tool.tolpen.prune.exception;

public class StorageLimitException extends RuntimeException {

    private static final long serialVersionUID = 6847706498386652212L;

    public StorageLimitException(String message) {
        super(message);
    }
}
