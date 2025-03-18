package hepker.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class CSVHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVHelper.class);
    private static final String DATA_FILEPATH = "src/main/resources/data/";

    private static EpochData headData = null;
    private static EpochData tailData = null;
    @Getter
    private static int dataSize = 0;
    @Getter
    private static double avgTurnCount = 0.0;

    private CSVHelper() {

    }

    /**
     * Loads all lines from the specified CSV file into a String array.
     *
     * @param fileName the name of the CSV file to load
     * @return a String array containing each line of the CSV file, or an empty array if the file does not exist
     */
    public static EpochData getPreviousEpochData(String fileName) {
        if (headData != null) {
            return headData;
        }
        String dataPathAndFileName = DATA_FILEPATH + fileName;
        File file = getFile(dataPathAndFileName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            String[] tmpSArr;
            if (line != null) {
                tmpSArr = line.split(",");
                queueEpochData(Double.parseDouble(tmpSArr[0]),
                        Integer.parseInt(tmpSArr[1]),
                        Integer.parseInt(tmpSArr[2]),
                        true);
            } else {
                return new EpochData();
            }
        } catch (IOException e) {
            LOGGER.error("Error reading file '{}': {}", dataPathAndFileName, e.getMessage());
            return new EpochData();
        }
        return headData;
    }

    /**
     * Writes a new line to the specified CSV file with the given turnAverage and numEpisodes.
     *
     * @param fileName the name of the CSV file to write to
     */
    public static void writeData(String fileName) {
        String dataPathAndFileName = DATA_FILEPATH + fileName;
        File file = getFile(dataPathAndFileName);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String[] tmpSArr;
            while ((line = br.readLine()) != null && dataSize < 300) {
                tmpSArr = line.split(",");
                queueEpochData(Double.parseDouble(tmpSArr[0]),
                        Integer.parseInt(tmpSArr[1]),
                        Integer.parseInt(tmpSArr[2]),
                        false);
            }
        } catch (IOException e) {
            LOGGER.error("Error reading file '{}'", dataPathAndFileName, e);
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            EpochData cursor = headData;
            while (cursor != null) {
                bw.write(
                        String.format(
                                "%.3f,%d,%d\n",
                                cursor.turnAverage,
                                cursor.numEpisodes,
                                cursor.sumTurnsPlayed));
                cursor = cursor.previous;
            }
            avgTurnCount = headData.turnAverage;
            headData = null;
            tailData = null;
            dataSize = 0;
        } catch (Exception e) {
            LOGGER.error("Error while writing EpochData back to CSV file", e);
        }
    }

    /**
     * Adds epoch data to a queue linkedlist to be added when a threshold is met
     *
     * @param turnAverage The average number of turns per epoch
     * @param numEpisodes The number of turns in this epoch
     * @param sumTurnsPlayed Total number of turns ever played
     * @param toHead True when we append Data to the front of the list. False when we append
     *               Data to the tail of the list
     */
    public static void queueEpochData(double turnAverage, int numEpisodes, int sumTurnsPlayed, boolean toHead) {
        if (headData == null) {
            headData = new EpochData(turnAverage, numEpisodes, sumTurnsPlayed, null, null);
            tailData = headData;
        }
        if (toHead) {
            headData.setNext(
                    new EpochData(turnAverage, numEpisodes, sumTurnsPlayed, null, headData));
            headData = headData.next;
        } else {
            tailData.setPrevious(
                    new EpochData(turnAverage, numEpisodes, sumTurnsPlayed, tailData, null));
            tailData = tailData.previous;
        }
        ++dataSize;
    }

    /**
     * Helper which checks file for its existence. If not, will create it, and either way will return
     * the file in question
     *
     * @param fileName Name of the file we are verifying and returning
     * @return The File matching fileName argument
     */
    private static File getFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    LOGGER.info("File '{}' did not exist and has been created.", fileName);
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create file '{}'", fileName, e);
            }
        }
        return file;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EpochData {
        private double turnAverage;
        private int numEpisodes;
        private int sumTurnsPlayed;
        @Setter
        private EpochData next;
        @Setter
        private EpochData previous;
    }
}