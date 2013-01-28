package com.indexisto.tool.tolpen.prepare;

import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.file.Path;
import java.util.Iterator;

import com.indexisto.tool.tolpen.config.Config.Prepare;
import com.indexisto.tool.tolpen.prepare.index.IndexType;
import com.indexisto.tool.tolpen.storage.StorageOutput;
import com.indexisto.tool.tolpen.storage.fs.FSStorage;


public class PrepareContext {

    private final Iterator<StorageOutput> requestOutputGenerator = FSStorage.newRequestOutput().iterator();

	private long count = 0;
	private IndexType type;


	public void moveToNext(IndexType type) {
		++count;
		this.type = checkNotNull(type);
	}


	public IndexType getIndexType() {
		return type;
	}


	public String getIndexName() {
        return Long.toString(count);
    }


    public Path getIndexPath() {
        return Prepare.storage.resolve(getIndexName());
    }


    public Path getRequestsMetaPath() {
        return getIndexPath().resolve("requests");
    }


    public Path getPropertiesPath() {
        return getIndexPath().resolve("properties");
    }


    public StorageOutput getNextRequestOutput() {
        if (!requestOutputGenerator.hasNext()) {
            throw new RuntimeException("Request Storage limit");
        }
        return requestOutputGenerator.next();
    }
}