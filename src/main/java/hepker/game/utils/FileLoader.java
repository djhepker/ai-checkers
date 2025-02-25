package main.java.hepker.game.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileLoader {
    private final String filePath;

    public FileLoader(String filePath) {
        this.filePath = filePath;
    }

    public void updateLineByKey(String key, String updatedValue) {
        try {
            List<String> lines = readLines();
            lines.remove(getLine(key));
            lines.addFirst(key + updatedValue);
            writeLines(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLine(String key) throws IOException {
        return readLines().stream()
                .filter(line -> line.contains(key))
                .findFirst()
                .orElse("");
    }

    public List<String> readLines() throws IOException {
        return Files.readAllLines(Path.of(filePath));
    }

    public void writeLines(List<String> lines) throws IOException {
        Files.write(Path.of(filePath), lines);
    }
}
