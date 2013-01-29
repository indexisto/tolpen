package com.indexisto.tool.tolpen.prune.search;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.substringBetween;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public enum SearchType {
  //Respell,
    LowSpanNear {
        @Override
        public SearchTask parse(String line) {
            return parseSpanNear(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newSpanNearStream(task);
        }
    },
    MedSpanNear {
        @Override
        public SearchTask parse(String line) {
            return parseSpanNear(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newSpanNearStream(task);
        }
    },
    HighSpanNear {
        @Override
        public SearchTask parse(String line) {
            return parseSpanNear(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newSpanNearStream(task);
        }
    },
    LowPhrase {
        @Override
        public SearchTask parse(String line) {
            return parsePhrase(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newPhraseStream(task);
        }
    },
    MedPhrase {
        @Override
        public SearchTask parse(String line) {
            return parsePhrase(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newPhraseStream(task);
        }
    },
    HighPhrase {
        @Override
        public SearchTask parse(String line) {
            return parsePhrase(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newPhraseStream(task);
        }
    },
    LowSloppyPhrase {
        @Override
        public SearchTask parse(String line) {
            return parseSloppyPhrase(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newSloppyPhraseStream(task);
        }
    },
    MedSloppyPhrase {
        @Override
        public SearchTask parse(String line) {
            return parseSloppyPhrase(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newSloppyPhraseStream(task);
        }
    },
    HighSloppyPhrase {
        @Override
        public SearchTask parse(String line) {
            return parseSloppyPhrase(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newSloppyPhraseStream(task);
        }
    },
    Prefix3 {
        @Override
        public SearchTask parse(String line) {
            return parsePrefix3(this, line);
        }

        @Override
        public InputStream newInputStream(SearchTask task) {
            return newPrefix3Stream(task);
        }
    };


    public abstract SearchTask parse(String line);
    public abstract InputStream newInputStream(SearchTask task);


    private static SearchTask parseSpanNear(SearchType type, String line) {
        return new SearchTask(type, substringBetween(line, "//", " #"));
    }


    private static InputStream newSpanNearStream(SearchTask task) {
        return IOUtils.toInputStream("");
    }


    private static SearchTask parsePhrase(SearchType type, String line) {
        return new SearchTask(type, substringBetween(line, "\"", "\""));
    }


    private static InputStream newPhraseStream(SearchTask task) {
        // TODO Auto-generated method stub
        return null;
    }


    private static SearchTask parseSloppyPhrase(SearchType type, String line) {
        return new SearchTask(type, substringBetween(line, "\"", "\""), intBetween(line, "~", " #"));
    }


    private static InputStream newSloppyPhraseStream(SearchTask task) {
        // TODO Auto-generated method stub
        return null;
    }


    private static SearchTask parsePrefix3(SearchType type, String line) {
        return new SearchTask(type, substringBetween(line, ": ", "*"));
    }


    private static InputStream newPrefix3Stream(SearchTask task) {
        return IOUtils.toInputStream("{\"query\":{\"prefix\":{\"_all\":\"" + task.getQuery() + "\"}}}");
    }


    private static int intBetween(String line, String open, String close) {
        try {
            return parseInt(substringBetween(line, open, close));
        }
        catch(final NumberFormatException e) {
            return 0;
        }
    }
}