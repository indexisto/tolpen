package com.indexisto.tool.tolpen.prepare.index;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.indexisto.tool.tolpen.config.Config.Prepare;
import com.indexisto.tool.tolpen.prepare.request.CreateChildIndexRequest;
import com.indexisto.tool.tolpen.prepare.request.CreateMultiIndexRequest;
import com.indexisto.tool.tolpen.prepare.request.CreateSimpleIndexRequest;
import com.indexisto.tool.tolpen.prepare.request.IndexRequest;
import com.indexisto.tool.tolpen.prepare.request.Request;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;
import com.indexisto.tool.tolpen.util.selectable.Selectables;

public class IndexType extends SelectableBase {

    private final int ttl;
    private final Collection<Request> queries;
    private final long docCount;
    private final long througput;

    public IndexType(
            long probability,
            long maxCount,
            int ttl,
            Collection<Request> queries,
            long docCount,
            long througput
    ) {
        super(probability, maxCount);

        checkArgument(ttl > 0);
        checkArgument(docCount > 0);
        checkArgument(througput > 0);

        this.ttl = ttl;
        this.queries = queries;
        this.docCount = docCount;
        this.througput = througput;
    }


    public Iterable<Request> newRequestGenerator() throws IOException {
        return Iterables
            .limit(
                Iterables.concat(
                    new IndexCreator(),
                    new IndexChunker(),
                    Selectables.newSelector(queries)
            ), ttl);
    }


    public long getThrougput() {
        return througput;
    }


    public long getDocCount() {
        return docCount;
    }


    private class IndexCreator implements Iterable<Request> {

        @Override
        public Iterator<Request> iterator() {
            final IndexParams params = Prepare.calcIndexParams(IndexType.this);
            final Collection<Request> result = new ArrayList<>();
            if (params.isChildIndex()) {
                while (!MultiIndexRepo.instance.tryAddChild(IndexType.this)) {
                    MultiIndexRepo.instance.newMultiIndex();
                    result.add(new CreateMultiIndexRequest(
                        MultiIndexRepo.instance.getMultiIndexName(), params)
                    );
                }
                result.add(new CreateChildIndexRequest(
                    MultiIndexRepo.instance.getMultiIndexName())
                );
            }
            else {
                result.add(new CreateSimpleIndexRequest(params));
            }
            return result.iterator();
        }
    }


    private class IndexChunker implements Iterable<Request> {

        private final long docChunk;
        private long docCount;

        public IndexChunker() {
            docCount = IndexType.this.docCount;
            docChunk = Prepare.getDocChunkLenght(IndexType.this);
        }


        @Override
        public Iterator<Request> iterator() {
            return new AbstractIterator<Request>() {

                @Override
                protected Request computeNext() {
                    if (docCount == 0) {
                        return endOfData();
                    }
                    return newRequest();
                }

                private Request newRequest() {
                    final long currentChunk = Math.min(docCount, docChunk);
                    docCount -= currentChunk;
                    return new IndexRequest(currentChunk);
                }
            };
        }
    }
}