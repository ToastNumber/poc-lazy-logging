package com.kelseymckenna.lazylogging;

import lombok.Data;

/**
 * Created by Kelsey McKenna on 07/12/16.
 */
@Data
public class Benchmark {
    private final String name;
    private final long totalTime;
    private final int numRuns;

    public double ops() {
        return (double) numRuns / totalTime;
    }

    public long opsPerSecond() {
        return (long) (ops() * 1000000000);
    }
}
