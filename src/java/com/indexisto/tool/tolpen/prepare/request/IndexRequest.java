package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.indexisto.tool.tolpen.config.Config.Prepare;
import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.prepare.facets.FacetsType;
import com.indexisto.tool.tolpen.storage.StorageInput;
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
    public RequestMeta write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            final Iterable<StorageInput> contentsInput = Iterables.transform(
                FSStorage.newDocumentInput(docCount), new DocumentEnricher(context)
            );
            Util.writeBulkRequest("index", output, contentsInput);
            final String uri =
                "/" + context.getIndexName()
              + "/" + Prepare.type
              + "/_bulk"
              ;
            return new RequestMeta("index", output.getPath(), uri);
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .toString();
    }


    private static class DocumentEnricher implements Function<StorageInput, StorageInput> {

        private final PrepareContext context;

        public DocumentEnricher(PrepareContext context) {
            this.context = context;
        }


        @Override
        public StorageInput apply(StorageInput input) {
            return new StorageInput(input.getPath(), transform(input.getStream()));
        }


        private InputStream transform(InputStream input) {
            final JsonElement root = readElement(input);
            addMultiIndexFilter(root);
            addTags(root);
            return IOUtils.toInputStream(root.toString());
        }


        private void addMultiIndexFilter(final JsonElement root) {
            root.getAsJsonObject().addProperty("user", context.getIndexName());
        }


        private static void addTags(final JsonElement root) {
            for(final FacetsType facet : FacetsType.values()) {
                root.getAsJsonObject().add(facet.name(), prepareTags(facet));
            }
        }


        private static JsonElement readElement(InputStream input) {
            return new JsonParser().parse(new InputStreamReader(input));
        }


        private static JsonArray prepareTags(final FacetsType facet) {
            final JsonArray tags = new JsonArray();
            for(final String tag : facet.getTags()) {
                tags.add(new JsonPrimitive(tag));
            }
            return tags;
        }
    }
}