package hepker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class CSVHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVHelper.class);

    private CSVHelper() {

    }

    /**
     * Loads all lines from the specified CSV file into a String array.
     *
     * @param csvName the name of the CSV file to load
     * @return a String array containing each line of the CSV file, or an empty array if the file does not exist
     */
    public static String[] loadData(String csvName) {
        List<String> lines = new ArrayList<>();
        File file = new File(csvName);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    LOGGER.info("File '{}' did not exist and has been created.", csvName);
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create file '{}': {}", csvName, e.getMessage());
                return new String[0];
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading file '{}': {}", csvName, e.getMessage());
            return new String[0];
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Writes a new line to the specified CSV file with the given average and count.
     *
     * @param csvName the name of the CSV file to write to
     * @param average the average value to write
     * @param count   the count value to write
     */
    public static void writeData(String csvName, int count, double average) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvName, true))) {
            bw.write(String.format("%d,%.2f\n", count, average));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}