package com.indexisto.tool.tolpen.prepare.request;

import static com.google.common.base.Objects.toStringHelper;

import java.io.IOException;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.util.selectable.SelectableBase;

public class UpdateRequest extends SelectableBase implements Request {

    private final static Logger LOG = LoggerFactory.getLogger(UpdateRequest.class);

    public UpdateRequest(long probability, long maxCount) {
        super(probability, maxCount);
    }


    @Override
    public String getName() {
        return "update";
    }


    @Override
    public String write(PrepareContext context) throws IOException {
        throw new NotImplementedException();
//      try (StorageOutput output = context.getNextRequestOutput()) {
//          final URL url =
//              new URL("http", Prepare.host, context.getIndexName() + "/" + Prepare.type)
//              ;
//          Util.writeRequest(url, output, FSStorage.newRequestInput(1));
//          return output.getPath();
//      }
    }


    @Override
    public String toString() {
        return toStringHelper(this)
            .add("requestName", getName())
            .toString();
    }
}