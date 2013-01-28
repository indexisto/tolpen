package com.indexisto.tool.tolpen.prune;

import org.elasticsearch.river.wikipedia.support.WikiXMLSAXParser;

import com.indexisto.tool.tolpen.config.Config.DocumentStorage;
import com.indexisto.tool.tolpen.prune.wiki.WikiPageCallbackHandler;
import com.indexisto.tool.tolpen.storage.fs.FSStorage;

public class Pruner {

    public void execute() throws Exception {
        final WikiPageCallbackHandler handler =
            new WikiPageCallbackHandler(FSStorage.newDocumentOutput())
            ;
        WikiXMLSAXParser.parseWikipediaDump(DocumentStorage.source.toFile().toURL(), handler);
    }
}
