package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.indexisto.tool.tolpen.config.Config.Prepare;
import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.storage.fs.FSStorage;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;

public class IndexRequest extends SelectableBase implements Request {

    private final static Logger LOG = LoggerFactory.getLogger(IndexRequest.class);

    private long docCount;


    public IndexRequest(long docCount) {
        super(0, Long.MAX_VALUE);

        this.docCount = docCount;
    }


    public IndexRequest(long probability, long maxCount) {
        super(probability, maxCount);
    }


    @Override
    public String write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            Util.writeBulkRequest("index", output, FSStorage.newDocumentInput(docCount));
            return StringUtils.join(new Object[] {
                "index",
                output.getPath().toAbsolutePath(),
                "/" + context.getIndexName() + "/" + Prepare.type + "/_bulk"
            }, ",");
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .toString();
    }
}