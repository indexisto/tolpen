package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;

import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.prepare.index.IndexParams;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;

public class CreateSimpleIndexRequest extends SelectableBase implements Request {

    private final IndexParams params;

    public CreateSimpleIndexRequest(IndexParams params) {
        super(1, 0);

        this.params = params;
    }


    @Override
    public RequestMeta write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            Util.writeRequest(
                output,
                "{\"settings\":{\"number_of_shards\":%d,\"number_of_replicas\":%d}}",
                params.getShardNum(),
                params.getReplicaNum()
            );
            return new RequestMeta("createSimpleIndex", output.getPath(), "/" + context.getIndexName());
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .toString();
    }
}
