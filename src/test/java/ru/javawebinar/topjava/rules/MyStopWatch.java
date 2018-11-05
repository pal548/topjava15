package ru.javawebinar.topjava.rules;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyStopWatch extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger("result");

    private final Map<String, Long> map;

    public MyStopWatch(Map<String, Long> testsLogMap) {
        this.map = testsLogMap;
    }

    @Override
    protected void finished(long nanos, Description description) {
        map.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
        log.info(result);
    }
    
}
