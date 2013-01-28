package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;
import java.net.URL;

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
    public String getName() {
        return "index";
    }


    @Override
    public String write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            final String urlPath =
                    "/" + context.getIndexName() + "/" + Prepare.type + "/_bulk";
            final URL url =
                new URL("http", Prepare.host, Prepare.port, urlPath)
                ;
            Util.writeRequest("index", url, output, FSStorage.newDocumentInput(docCount));
            return StringUtils.join(new Object[] {getName(), output.getPath(), urlPath}, ",");
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .add("requestName", getName())
            .toString();
    }
}