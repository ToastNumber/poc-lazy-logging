package com.kelseymckenna.lazylogging;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Created by Kelsey McKenna on 07/12/16.
 */
public class Benchmarker {
    private static Random rand = new Random();
    private static final int randDataSize = 100;

    public static void prettyPrint(final List<Benchmark> benchmarks, final int width) {
        benchmarks.sort(Comparator.comparing(Benchmark::ops).reversed());

        System.out.println("| Title      | Time taken | Ops/s      |");
        System.out.println("|------------|------------|------------|");
        benchmarks.stream()
                  .map(benchmark -> String.format("| %10s | %10d | %10d |",
                                                  benchmark.getName(),
                                                  benchmark.getTotalTime(),
                                                  benchmark.opsPerSecond()))
                  .forEach(System.out::println);

        System.out.println();

        final double maxOps = benchmarks.get(0).opsPerSecond();
        final double factor = width / maxOps;

        benchmarks.stream()
                .map(benchmark -> String.format("%-10s: %s %-10d",
                                                benchmark.getName(),
                                                repeat("|", (int) Math.round(factor * benchmark.opsPerSecond())),
                                                benchmark.opsPerSecond()))
                .forEach(System.out::println);
    }

    private static String repeat(final String s, final int n) {
        return new String(new char[n]).replaceAll("\0", s);
    }

    private static String randomString(final int size) {
        String result = "";
        for (int i = 0; i < size; ++i) {
            result += (char) (48 + rand.nextInt(100));
        }
        return result;
    }

    public static Benchmark benchmark(final String name, final Consumer<String> r, final int n) {
        long totalTime = 0;

        for (int i = 0; i < n; ++i) {
            final String var = randomString(randDataSize);
            final long start = System.nanoTime();
            r.accept(var);
            totalTime += System.nanoTime() - start;
        }
        return new Benchmark(name, totalTime, n);
    }
}
