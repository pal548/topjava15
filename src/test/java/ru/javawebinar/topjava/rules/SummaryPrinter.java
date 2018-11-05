package ru.javawebinar.topjava.rules;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SummaryPrinter implements TestRule {
    private static final Logger log = LoggerFactory.getLogger("result");

    private final Map<String, Long> map = new LinkedHashMap<>();

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    base.evaluate();
                } finally {
                    printSummary();
                }

            }
        };
    }

    public Map<String, Long> getMap() {
        return map;
    }

    private void printSummary() {
        final StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> stringBuilder.append(String.format("\n%-25s %7d", key, value)));
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                stringBuilder.toString() +
                "\n---------------------------------");
    }
}
