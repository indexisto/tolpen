package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.prepare.index.IndexParams;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;

public class CreateMultiIndexRequest extends SelectableBase implements Request {

    private final String multiIndexName;
    private final IndexParams childIndexParams;

    public CreateMultiIndexRequest(String multiIndexName, IndexParams childIndexParams) {
        super(1, 0);

        this.multiIndexName = multiIndexName;
        this.childIndexParams = childIndexParams;
    }


    @Override
    public String write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            Util.writeRequest(
                output,
                "{\"settings\":{\"number_of_shards\":%d,\"number_of_replicas\":%d}}",
                childIndexParams.getParentIndex().getShardNum(),
                childIndexParams.getParentIndex().getReplicaNum()
            );
            return StringUtils.join(new Object[] {
                "createMultiIndex",
                output.getPath().toAbsolutePath(),
                "/" + multiIndexName
            }, ",");
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .toString();
    }
}