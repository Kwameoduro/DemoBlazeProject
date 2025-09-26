package com.ecommerce.utils;




import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonDataReader {

    private static final String TESTDATA_PATH = "src/test/resources/testdata/";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static void validateFileExtension(String fileName) {
        if (!fileName.toLowerCase().endsWith(".json")) {
            throw new IllegalArgumentException("Only .json files are supported: " + fileName);
        }
    }

    /**
     * Reads a JSON file and returns it as a JsonNode (tree structure).
     */
    public static JsonNode readJson(String fileName) {
        validateFileExtension(fileName);
        try {
            File file = new File(TESTDATA_PATH + fileName);
            return mapper.readTree(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + fileName, e);
        }
    }

    /**
     * Reads a JSON file and maps it into a given POJO class.
     */
    public static <T> T readJsonAsObject(String fileName, Class<T> clazz) {
        validateFileExtension(fileName);
        try {
            File file = new File(TESTDATA_PATH + fileName);
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to map JSON to object: " + fileName, e);
        }
    }

    /**
     * Reads a JSON test data file and returns it as a nested Map.
     *
     * Example:
     * {
     *   "validSignup": { "username": "test", "password": "pass" }
     * }
     *
     * Will return Map<String, Map<String, String>>
     */
    public static Map<String, Map<String, String>> getTestData(String fileName) {
        validateFileExtension(fileName);
        try {
            File file = new File(TESTDATA_PATH + fileName);
            return mapper.readValue(file, new TypeReference<Map<String, Map<String, String>>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse JSON test data: " + fileName, e);
        }
    }

    /**
     * Reads a JSON file and retrieves a field value as a string.
     */
    public static String getValue(String fileName, String field) {
        validateFileExtension(fileName);
        JsonNode node = readJson(fileName);
        return node.path(field).asText();
    }
}
