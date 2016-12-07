package com.kelseymckenna.lazylogging;

import java.util.Arrays;

import static com.kelseymckenna.lazylogging.Benchmarker.benchmark;

/**
 * Created by Kelsey McKenna on 07/12/16.
 */
public class Example {
    private static Logger logger = new Logger();

    private static void debugNaiveWithConcat(final String message) {
        logger.debug("some-string" + message);
    }

    private static void debugLazyWithConcat(final String message) {
        logger.debug(() -> "some-string" + message);
    }

    private static void debugNaiveNoMessage(final String message) {
        logger.debug("some-string");
    }

    private static void debugLazyNoMessage(final String message) {
        logger.debug(() -> "some-string");
    }

    private static void debugIfStatementWithConcat(final String message) {
        if (Logger.isDebugEnabled()) {
            logger.debug("some-string" + message);
        }
    }

    public static void main(String[] args) {
        final int n = 1_000_000;
        Logger.debug = false;

        benchmark("Warm up", Example::debugLazyWithConcat, n);
        benchmark("Warm up", Example::debugNaiveWithConcat, n);
        benchmark("Warm up", Example::debugIfStatementWithConcat, n);

        final Benchmark lazyBenchmarkWithConcat = benchmark("Lazy cat", Example::debugLazyWithConcat, n);
        final Benchmark naiveBenchmarkWithConcat = benchmark("Naive cat", Example::debugNaiveWithConcat, n);
        final Benchmark ifStmtWithConcat = benchmark("If stmt", Example::debugIfStatementWithConcat, n);

        Benchmarker.prettyPrint(Arrays.asList(naiveBenchmarkWithConcat,
                                              lazyBenchmarkWithConcat,
                                              ifStmtWithConcat),
                                100);
    }
}
