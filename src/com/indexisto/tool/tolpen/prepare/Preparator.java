package com.indexisto.tool.tolpen.prepare;

import java.io.IOException;
import java.nio.file.Path;

import com.indexisto.tool.tolpen.config.Config.Prepare;
import com.indexisto.tool.tolpen.prepare.index.IndexType;
import com.indexisto.tool.tolpen.prepare.request.Request;
import com.indexisto.tool.tolpen.util.Util;
import com.indexisto.tool.tolpen.util.selectable.Selectables;

public class Preparator {

    private static PrepareContext context = new PrepareContext();


    public void execute() throws Exception {
        for(final IndexType indexType : Selectables.newSelector(Prepare.getIndexTypes())) {
        	context.moveToNext(indexType);
            createIndex();
        }
    }


    private void createIndex() throws IOException {
		writeProperties();
        writeRequestsMetaHead();
        for(final Request request : context.getIndexType().newRequestGenerator()) {
            writeRequest(request);
        }
    }


    private void writeRequest(Request request) throws IOException {
        Util.appendLineTo(
            context.getRequestsMetaPath(),
            request.write(context)
        );
    }


    private static void writeRequestsMetaHead() throws IOException {
        Util.writeLineTo(
            context.getRequestsMetaPath(),
            "requestName",
            "requestData",
            "requestPath"
        );
    }


    private void writeProperties() throws IOException {
        final Path propertyPath = context.getPropertiesPath();
        Util.writeLineTo (propertyPath, "througput");
        Util.appendLineTo(propertyPath, Long.toString(context.getIndexType().getThrougput()));
    }
}