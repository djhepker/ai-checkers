package main.java.hepker.game.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnvLoader {
    private final Map<String, String> envVars = new HashMap<>();

    public EnvLoader(String filePath) throws IOException {
        FileLoader fileLoader = new FileLoader(filePath);
        loadEnv(fileLoader.readLines());
    }

    private void loadEnv(List<String> lines) {
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                envVars.put(parts[0].trim(), parts[1].trim());
            }
        }
    }

    public String get(String key) {
        return envVars.get(key);
    }
}
