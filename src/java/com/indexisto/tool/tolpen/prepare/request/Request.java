package com.indexisto.tool.tolpen.prepare.request;

import java.io.IOException;

import com.indexisto.tool.tolpen.prepare.PrepareContext;
import com.indexisto.tool.tolpen.util.selectable.Selectable;

public interface Request extends Selectable {
    String write(PrepareContext context) throws IOException;
}
