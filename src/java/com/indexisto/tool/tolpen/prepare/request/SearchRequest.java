package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.indexisto.tool.tolpen.config.Config.Prepare;
import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.prune.search.SearchType;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.storage.fs.FSStorage;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;
import com.indexisto.tool.tolpen.util.selectable.SelectableProvider;
import com.indexisto.tool.tolpen.util.selectable.Selectables;

public class SearchRequest extends SelectableBase implements Request {

    private final static Logger LOG = LoggerFactory.getLogger(SearchRequest.class);
    private final Iterator<SelectableProvider<SearchType>> searchTypes;

    public SearchRequest(long probability, long maxCount, Collection<SelectableProvider<SearchType>> searchTypes) {
        super(probability, maxCount);

        this.searchTypes = Selectables.newSelector(checkNotNull(searchTypes)).iterator();
    }


    @Override
    public String write(PrepareContext context) throws IOException {
        try (StorageOutput output = context.getNextRequestOutput()) {
            final SearchType searchType = searchTypes.next().get();
            Util.writeMSearchRequest(output, FSStorage.newTaskInput(searchType, 1));
            return StringUtils.join(new Object[] {
                searchType,
                output.getPath().toAbsolutePath(),
                "/" + context.getIndexName() + "/" + Prepare.type + "/_msearch"
            }, ",");
        }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .toString();
    }
}