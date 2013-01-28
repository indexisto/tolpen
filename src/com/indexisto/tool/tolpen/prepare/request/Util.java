package com.indexisto.tool.tolpen.prepare.request;

import static com.indexisto.tool.tolpen.util.Util.HTTP_NEW_LINE;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.indexisto.tool.tolpen.storage.StorageInput;
import com.indexisto.tool.tolpen.storage.StorageOutput;

class Util {

    private final static String httpPattern = StringUtils.join(new String[] {
        "{METHOD} {PATH} HTTP/1.1", // /twitter/tweet?routing=kimchy
        //"User-Agent: curl/7.27.0",
        "Host: {HOST}:{PORT}", //localhost:9200
        "Accept: */*",
        "Content-Length: {CONTENT-LENGHT}",
        "Content-Type: application/x-www-form-urlencoded",
        HTTP_NEW_LINE
    }, HTTP_NEW_LINE);


    public static void writeRequest(
            String bulkOp,
            URL url,
            StorageOutput requestOutput,
            Iterable<StorageInput> contentsInput
    ) throws IOException {
        //final Collection<StorageInput> inputs = new ArrayList<>();
        //Iterables.addAll(inputs, contentsInput);
        final OutputStream requestStream = requestOutput.getStream();
        //writeHTTPHead(bulkOp, url, requestStream, inputs);
        for (final StorageInput input : contentsInput) {
            try (InputStream inputStream = input.getStream()) {
                IOUtils.write(prepareBulkOp(bulkOp, input), requestStream);
                IOUtils.write(HTTP_NEW_LINE.getBytes(), requestStream);
                IOUtils.copyLarge(inputStream, requestStream);
                IOUtils.write(HTTP_NEW_LINE.getBytes(), requestStream);
            }
        }
    }


    private static void writeHTTPHead(
            String bulkOp,
            URL url,
            OutputStream requestOutput,
            Iterable<StorageInput> contentsInput
    ) throws IOException {
        try (InputStream headInput = newHTTPHeadInputStream("POST", url, calcContentsLength(bulkOp, contentsInput))) {
            IOUtils.copyLarge(headInput, requestOutput);
        }
    }


    private static InputStream newHTTPHeadInputStream(String method, URL url, long contentsLength) {
        final String head = createHead(method, url, contentsLength);
        return new ByteArrayInputStream(head.getBytes());
    }


    private static String createHead(String method, URL url, long contentsLength) {
        String result = httpPattern;
        result = StringUtils.replace(result, "{METHOD}", method, 1);
        result = StringUtils.replace(result, "{PATH}", url.getFile(), 1);
        result = StringUtils.replace(result, "{HOST}", url.getHost(), 1);
        result = StringUtils.replace(result, "{PORT}", Integer.toString(url.getPort()), 1);
        result = StringUtils.replace(result, "{CONTENT-LENGHT}", Long.toString(contentsLength), 1);
        return result;
    }


    private static long calcContentsLength(String bulkOp, Iterable<StorageInput> contents) {
        long result = 0;
        for(final StorageInput input : contents) {
            result += prepareBulkOp(bulkOp, input).length()
                    + HTTP_NEW_LINE.getBytes().length
                    + input.getPath().toFile().length()
                    + HTTP_NEW_LINE.getBytes().length
                    ;
        }
        return result;
    }


    private static String prepareBulkOp(String bulkOp, StorageInput input) {
        return String.format("{\"%s\":{\"_id\":\"%s\"}}", bulkOp, input.getPath());
    }
}