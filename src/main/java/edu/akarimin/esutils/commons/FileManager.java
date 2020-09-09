package edu.akarimin.esutils.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by akarimin@buffalo.edu
 */
public class FileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileManager.class);
    private static String CONTENT;

    public static String getMappingFilePath() {
        Scanner scanner = new Scanner(System.in);
        LOGGER.debug(
            "##############################---MAPPING FILE PATH---###################################\n"
                + "Please Enter the JSON-formatted mapping file's absolute path:\t");
        String FILE_PATH = scanner.nextLine();
        LOGGER.debug(
            "---------------------------------MAPPING FILE FETCHED-----------------------------------");
        return FILE_PATH;
    }

    public static String readMappingFile(String filePath) throws IOException {
        LOGGER.debug(
            "---------------------------------MAPPING FILE READ--------------------------------------");
        CONTENT = new String(Files.readAllBytes(Paths.get(filePath)));
        return CONTENT;
    }

    public static String getContent() {
        return CONTENT;
    }

}
