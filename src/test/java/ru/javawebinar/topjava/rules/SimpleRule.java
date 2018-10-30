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
                log.debug("\n\n" +
                        "*******************************************************************\n" +
                        "* starting - " + "\u001B[31m" + description.getMethodName() + "()" + "\u001B[0m\n" +
                        "*******************************************************************");
                statement.evaluate();
                long end = System.currentTimeMillis();
                log.debug("\n" +
                        "*******************************************************************\n" +
                        description.getMethodName() + "() took " + "\u001B[31m" + (end - start) + "\u001B[0m" + " ms" + "\n" +
                        "*******************************************************************");
            }
        };
    }
}
