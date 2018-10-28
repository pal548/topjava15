package ru.javawebinar.topjava.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleRule implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(SimpleRule.class);

    @Override
    public Statement apply(final Statement statement, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = System.currentTimeMillis();
                log.debug("before " + description.getMethodName());
                statement.evaluate();
                long end = System.currentTimeMillis();
                log.debug("after " +  description.getMethodName() + ", took " + (end - start) + " ms");
            }
        };
    }
}
