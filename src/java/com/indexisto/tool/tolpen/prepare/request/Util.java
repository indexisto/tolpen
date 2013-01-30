package com.indexisto.tool.tolpen.prepare.request;

import static com.indexisto.tool.tolpen.util.Util.HTTP_NEW_LINE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.indexisto.tool.tolpen.storage.StorageInput;
import com.indexisto.tool.tolpen.storage.StorageOutput;

class Util {

    public static void writeBulkRequest(
            String bulkOp,
            StorageOutput requestOutput,
            Iterable<StorageInput> contentsInput
    ) throws IOException {
        final OutputStream requestStream = requestOutput.getStream();
        for (final StorageInput input : contentsInput) {
            try (InputStream inputStream = input.getStream()) {
                IOUtils.write(prepareBulkOp(bulkOp, input), requestStream);
                IOUtils.write(HTTP_NEW_LINE.getBytes(), requestStream);
                IOUtils.copy(inputStream, requestStream);
                IOUtils.write(HTTP_NEW_LINE.getBytes(), requestStream);
            }
        }
    }


    public static void writeMSearchRequest(
            StorageOutput requestOutput,
            Iterable<InputStream> contentsInput
    ) throws IOException {
        final OutputStream requestStream = requestOutput.getStream();
        for (final InputStream inputStream : contentsInput) {
            try {
                IOUtils.write("{}", requestStream);
                IOUtils.write(HTTP_NEW_LINE.getBytes(), requestStream);
                IOUtils.copy(inputStream, requestStream);
                IOUtils.write(HTTP_NEW_LINE.getBytes(), requestStream);
            }
            finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }



    public static void writeRequest(
        StorageOutput requestOutput,
        String pattern,
        Object... args
    ) throws IOException {
        IOUtils.write(String.format(pattern, args), requestOutput.getStream());
    }


    private static String prepareBulkOp(String bulkOp, StorageInput input) {
        return String.format("{\"%s\":{\"_id\":\"%s\"}}", bulkOp, input.getPath());
    }
}