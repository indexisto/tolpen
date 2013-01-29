package com.indexisto.tool.tolpen.util;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Range;

public class Util {

	private final static Logger LOG = LoggerFactory.getLogger(Util.class);

    private final static RandomDataGenerator random = new RandomDataGenerator();

    public final static String HTTP_NEW_LINE = "\r\n";


    public static int nextInt(int upper) {
        return random.nextInt(0, upper-1);
    }


    public static long nextLong(long upper) {
        return random.nextLong(0, upper-1);
    }


    public static Range<Integer> nextRange(int count, int upper) {
        checkArgument(upper > count);

        final int lower = Util.nextInt(upper - count + 1);
        return Range.closedOpen(lower, lower + count);
    }


    public static Range<Long> nextRange(long count, long upper) {
        checkArgument(upper > count);

        final long lower = Util.nextLong(upper - count + 1);
        return Range.closedOpen(lower, lower + count);
    }


    public static void writeLineTo(Path path, String... args) throws IOException {
        FileUtils.write(path.toFile(), StringUtils.join(args, ","));
    }


    public static void appendLineTo(Path path, String... args) throws IOException {
        FileUtils.write(path.toFile(), HTTP_NEW_LINE + StringUtils.join(args, ","), true);
    }
}