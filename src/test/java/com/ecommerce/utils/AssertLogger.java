package com.ecommerce.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Custom assertion utility that logs assertions before executing them.
 * Helps make logs and Allure reports more descriptive.
 */
public class AssertLogger {

    private static final Logger log = LoggerFactory.getLogger(AssertLogger.class);

    /**
     * Assert that two objects are equal with logging.
     */
    public static <T> void assertEqualsWithLog(T expected, T actual, String message) {
        log.info("ASSERT: {} | Expected='{}' | Actual='{}'", message, expected, actual);
        assertEquals(expected, actual, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that a condition is true with logging.
     */
    public static void assertTrueWithLog(boolean condition, String message) {
        log.info("ASSERT: {}", message);
        assertTrue(condition, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that a condition is false with logging.
     */
    public static void assertFalseWithLog(boolean condition, String message) {
        log.info("ASSERT: {}", message);
        assertFalse(condition, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that an object is null with logging.
     */
    public static void assertNullWithLog(Object object, String message) {
        log.info("ASSERT: {} | Value='{}'", message, object);
        assertNull(object, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that an object is not null with logging.
     */
    public static void assertNotNullWithLog(Object object, String message) {
        log.info("ASSERT: {} | Value='{}'", message, object);
        assertNotNull(object, message);
        log.info("✔ PASSED: {}", message);
    }

    /**
     * Assert that two objects are not equal with logging.
     */
    public static <T> void assertNotEqualsWithLog(T unexpected, T actual, String message) {
        log.info("ASSERT: {} | Unexpected='{}' | Actual='{}'", message, unexpected, actual);
        assertNotEquals(unexpected, actual, message);
        log.info("✔ PASSED: {}", message);
    }
}