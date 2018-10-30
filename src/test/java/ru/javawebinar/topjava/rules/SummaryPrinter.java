package ru.javawebinar.topjava.rules;


import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SummaryPrinter implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(SummaryPrinter.class);

    private final Map<String, Long> map = new HashMap<>();

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
        stringBuilder.append("\n");
        map.forEach((key, value) -> stringBuilder.append(String.format("%s() - %d ms\n", key, value)));
        log.debug(stringBuilder.toString());
    }
}
