package com.indexisto.tool.tolpen.prune.wiki;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.river.wikipedia.support.PageCallbackHandler;
import org.elasticsearch.river.wikipedia.support.WikiPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.CharMatcher;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.util.Util;

public class WikiPageCallbackHandler implements PageCallbackHandler {

    private final static Logger LOG = LoggerFactory.getLogger(WikiPageCallbackHandler.class);

    private final Iterator<StorageOutput> outputIterator;


    public WikiPageCallbackHandler(Iterable<StorageOutput> outputIterable) {
        outputIterator = outputIterable.iterator();
    }


    @Override
    public void process(WikiPage page) {
        LOG.debug("page {} : {}", page.getID(), page.getTitle());

        try {
            if (!page.isRedirect()) {
                if (outputIterator.hasNext()) {
                    write(page, outputIterator.next().getStream());
                }
                else {
                    throw new RuntimeException("storage limit");
                }
            }
        }
        catch (final IOException e) {
            throw new RuntimeException("failed to construct index request", e);
        }
    }


    private static void write(WikiPage page, OutputStream output) throws IOException {
        final XContentBuilder builder = XContentFactory.jsonBuilder(output).startObject();
        builder.field("title", treatString(trimString(page.getTitle())));
        builder.field("text", treatString(page.getText()));
        builder.field("redirect", page.isRedirect());
        builder.field("special", page.isSpecialPage());
        builder.field("stub", page.isStub());
        builder.field("disambiguation", page.isDisambiguationPage());
        {
            builder.startArray("category");
            for (final String s : page.getCategories()) {
                builder.value(treatString(s));
            }
            builder.endArray();
        }
        {
            builder.startArray("link");
            for (final String s : page.getLinks()) {
                builder.value(treatString(s));
            }
            builder.endArray();
        }
        builder.endObject();
        builder.close();
    }


	private static String trimString(String input) {
		return CharMatcher.BREAKING_WHITESPACE.trimTrailingFrom(input);
	}



    private static String treatString(String input) {
    	return CharMatcher.anyOf(Util.HTTP_NEW_LINE).removeFrom(input);
    }
}