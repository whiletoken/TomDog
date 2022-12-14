package com.tomdog.base;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * CollectionsTest
 * jmh基准测试
 *
 * @author william
 **/
@Warmup(iterations = 3)
@Measurement(iterations = 10, time = 5)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.Throughput)
@Threads(8)
@Fork(2)
public class CollectionsTest {

    private final static Logger log = LoggerFactory.getLogger(CollectionsTest.class);

    private static final int TEN_MILLION = 10000;

    @Benchmark
    public void arrayList() {
        List<String> array = new ArrayList<>();
        for (int i = 0; i < TEN_MILLION; i++) {
            array.add("123" + i);
        }
    }

    @Benchmark
    public void arrayListSize() {
        List<String> array = new ArrayList<>(TEN_MILLION);
        for (int i = 0; i < TEN_MILLION; i++) {
            array.add("123" + i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(CollectionsTest.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}
