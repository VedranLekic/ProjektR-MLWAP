package com.projektER.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
class JsonRoot {
    private List<Map<String, Object>> identities;

    // Getters/setters
    public List<Map<String, Object>> getIdentities() { return identities; }
    public void setIdentities(List<Map<String, Object>> identities) { this.identities = identities; }
}
public class JsonManipulator {
    private static final ObjectMapper mapper = new ObjectMapper();

    public File transfer(int sourceIndex, int targetIndex, String filePath) throws IOException {
        List<Map<String, Object>> jsonData = readJson(filePath);
        return unscramble(sourceIndex, targetIndex, jsonData);
    }

    private List<Map<String, Object>> readJson(String filePath) throws IOException {
        JsonRoot root = mapper.readValue(new File(filePath), JsonRoot.class);
        return root.getIdentities();
    }

    private File unscramble(int sourceIndex, int targetIndex, List<Map<String, Object>> jsonData) throws IOException {
        if (isValidIndex(sourceIndex, jsonData) && isValidIndex(targetIndex, jsonData) && sourceIndex != targetIndex) {
            Collections.swap(jsonData, sourceIndex, targetIndex);
        }
        return writeJson(jsonData);
    }

    private boolean isValidIndex(int index, List<?> list) {
        return index >= 0 && index < list.size();
    }

    private File writeJson(List<Map<String, Object>> jsonData) throws IOException {
        Path outputPath = Files.createTempFile("myapp-", ".json");
        JsonRoot outputRoot = new JsonRoot();
        outputRoot.setIdentities(jsonData);
        mapper.writeValue(outputPath.toFile(), outputRoot);
        return outputPath.toFile();
    }
}