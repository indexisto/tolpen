package com.indexisto.tool.tolpen.storage.fs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Range;
import com.indexisto.tool.tolpen.config.Config.TaskStorage;
import com.indexisto.tool.tolpen.prune.search.SearchTask;
import com.indexisto.tool.tolpen.prune.search.SearchType;
import com.indexisto.tool.tolpen.util.Util;

enum TaskSource {

    instance;

    private final Multimap<SearchType, SearchTask> map = HashMultimap.create();


    private TaskSource() {
        try {
            loadTasks();
        }
        catch(final IOException e) {
            throw new RuntimeException("Cant read task source", e);
        }
    }


    public Iterable<InputStream> getTasks(SearchType type, int count) {
        final Collection<SearchTask> tasks = map.get(type);
        final Range<Integer> range = Util.nextRange(count, tasks.size());
        return FluentIterable
            .from(tasks)
            .skip(range.lowerEndpoint())
            .limit(count)
            .transform(new Function<SearchTask, InputStream>() {
                @Override
                public InputStream apply(SearchTask task) {
                    return task.newInputStream();
                }
            })
            ;
    }


    private void loadTasks() throws IOException {
        for(final String line : Files.readAllLines(TaskStorage.source, Charset.defaultCharset())) {
            try {
                for(final SearchTask task : SearchTask.parse(line)) {
                    map.put(task.getType(), task);
                }
            }
            catch (final Exception e) {
                // do nothing
            }
        }
    }
}