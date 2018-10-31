package ru.javawebinar.topjava.rules;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MyStopWatch extends Stopwatch {
    private static final Logger log = LoggerFactory.getLogger(MyStopWatch.class);

    private final Map<String, Long> map;

    private static void logInfo(Description description, String status, long nanos) {
        String testName = description.getMethodName();
        log.debug(String.format("Test %s() %s, spent %d ms",
                testName, status, TimeUnit.NANOSECONDS.toMillis(nanos)));
    }

    public MyStopWatch(Map<String, Long> testsLogMap) {
        this.map = testsLogMap;
        log.debug("creating");
    }

    @Override
    protected void finished(long nanos, Description description) {
        logInfo(description, "finished", nanos);
        map.put(description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
    }
    
}
