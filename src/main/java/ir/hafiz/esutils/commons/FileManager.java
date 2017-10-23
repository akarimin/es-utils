package ir.hafiz.esutils.commons;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * Created by akarimin on 10/17/17.
 */
public class FileManager {

    private static String FILE_PATH = System.getProperty("json.path");

    public static String getMappingFilePath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("##############################---MAPPING FILE PATH---###################################\n" +
                "Please Enter the JSON-formatted mapping file's absolute path:\t");
        FILE_PATH = scanner.nextLine();
        System.out.println("---------------------------------MAPPING FILE FETCHED-----------------------------------");
        return FILE_PATH;
    }

    public static String readMappingFile(String filePath) throws IOException {
        System.out.println("---------------------------------MAPPING FILE READ--------------------------------------");
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        return content;
    }
}
