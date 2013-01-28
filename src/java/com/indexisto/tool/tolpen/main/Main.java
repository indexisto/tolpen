package com.indexisto.tool.tolpen.main;

import static com.google.common.base.Preconditions.checkArgument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.indexisto.tool.tolpen.prepare.Preparator;
import com.indexisto.tool.tolpen.prune.Pruner;
import com.indexisto.tool.tolpen.prune.wiki.exception.StorageLimitException;

public class Main {

    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        checkArgument(args.length == 1, "Wrong params count");
        try {
            switch(args[0]) {
            case "prune":
                new Pruner().execute();
                break;

            case "prepare":
                new Preparator().execute();
                break;

            default:
                showHelp();
                break;
            }
        }
        catch (final StorageLimitException e) {
            LOG.info(e.getMessage());
        }
        catch (final Exception e) {
            LOG.error("Error", e);
        }
    }


    private static void showHelp() {
        // TODO Auto-generated method stub
    }
}
