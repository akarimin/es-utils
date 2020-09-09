package edu.akarimin.esutils.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Created by akarimin on 11/1/17.
 */
public final class ScannerUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScannerUtil.class);
    private static final Scanner SCANNER = new Scanner(System.in);

    public static Object fetchConsoleInput(String title, String preInputMessage) {
        LOGGER.debug("#######################################--- {} " +
                "---#####################################\n {}",
            title, preInputMessage);
        return SCANNER.nextLine();
    }
}
