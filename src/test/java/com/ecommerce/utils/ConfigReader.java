package com.ecommerce.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ConfigReader {

    private static JsonNode configRoot;
    private static JsonNode activeEnv;

    static {
        loadConfig();
    }

    private static void loadConfig() {
        try {
            // Load config.json
            File file = new File("src/test/resources/config/config.json");
            ObjectMapper mapper = new ObjectMapper();
            configRoot = mapper.readTree(file);

            // Determine active environment (default: dev)
            String env = Optional.ofNullable(System.getProperty("env"))
                    .orElse("dev");

            activeEnv = configRoot.path("environments").path(env);

            if (activeEnv.isMissingNode()) {
                throw new RuntimeException("Environment '" + env + "' not found in config.json");
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.json", e);
        }
    }

    // ============ Base Settings ============
    public static String getBaseUrl() {
        return System.getProperty("baseUrl",
                activeEnv.path("baseUrl").asText(configRoot.path("baseUrl").asText()));
    }

    public static String getBrowser() {
        return System.getProperty("browser",
                activeEnv.path("browser").asText(configRoot.path("browser").asText("chrome")));
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(
                System.getProperty("headless",
                        String.valueOf(configRoot.path("headless").asBoolean(false)))
        );
    }

    public static int getTimeout() {
        return Integer.parseInt(
                System.getProperty("timeout",
                        String.valueOf(configRoot.path("timeout").asInt(5000)))
        );
    }

    // ============ Window Sizing ============
    public static boolean isFullscreen() {
        return Boolean.parseBoolean(
                System.getProperty("fullscreen",
                        String.valueOf(configRoot.path("fullscreen").asBoolean(false)))
        );
    }

    public static boolean isMaximize() {
        return Boolean.parseBoolean(
                System.getProperty("maximize",
                        String.valueOf(configRoot.path("maximize").asBoolean(false)))
        );
    }

    public static String getBrowserSize() {
        return System.getProperty("browserSize",
                activeEnv.path("browserSize").asText(configRoot.path("browserSize").asText("1920x1080")));
    }

    // ============ Reporting ============
    public static boolean isScreenshotOnFailure() {
        return configRoot.path("screenshotOnFailure").asBoolean(true);
    }

    public static boolean isSavePageSourceOnFailure() {
        return configRoot.path("savePageSourceOnFailure").asBoolean(true);
    }

    public static String getAllureResultsDir() {
        return configRoot.path("allure").path("resultsDirectory").asText("allure-results");
    }

    public static String getAllureReportDir() {
        return configRoot.path("allure").path("reportDirectory").asText("allure-report");
    }

    public static boolean isReportingScreenshots() {
        return configRoot.path("reporting").path("screenshots").asBoolean(true);
    }

    public static boolean isReportingLogs() {
        return configRoot.path("reporting").path("logs").asBoolean(true);
    }

    public static boolean isVideoRecordingEnabled() {
        return configRoot.path("reporting").path("videoRecording").asBoolean(false);
    }
}
