package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;

import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;

public class CreateChildIndexRequest extends SelectableBase implements Request {

    private final String multiIndexName;

    public CreateChildIndexRequest(String multiIndexName) {
        super(1, 0);

        this.multiIndexName = multiIndexName;
    }


    @Override
    public RequestMeta write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            Util.writeRequest(
                output,
                "{\"actions\":[{\"add\":{"
                  + "\"index\":\"%1$s\","
                  + "\"alias\":\"%2$s\","
                  + "\"filter\":{\"term\":{\"user\":\"%2$s\"}},"
                  + "\"routing\":\"%2$s\""
                + "}}]}",
                multiIndexName,
                context.getIndexName()
            );
            return new RequestMeta("createChildIndex", output.getPath(), "/_aliases");
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .toString();
    }
}