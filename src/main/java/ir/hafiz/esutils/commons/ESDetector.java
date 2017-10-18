package ir.hafiz.esutils.commons;

import ir.hafiz.esutils.tools.ESOperations;

import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

import static ir.hafiz.esutils.tools.ESOperations.checkNodeStatus;

/**
 * Created by akarimin on 10/17/17.
 */
public final class ESDetector {

    private static String ES_SERVER = System.getProperty("hafiz.ibank.elastic.host");
    private static String ES_PORT = System.getProperty("hafiz.ibank.elastic.port");
    private static String ES_INDEX = System.getProperty("hafiz.ibank.elastic.index");

    public static void detectESNode() throws Exception {
        getEsServer();
        getEsPort();
        checkNodeStatus();
        getEsIndex();
        System.out.printf("###################################---CONFIG---#########################################\n" +
                          "----------------------------------------------------------------------------------------\n" +
                        "ELASTICSEARCH NODE WITH HOST: [%s] PORT: [%s] CLUSTER NAME: [%s] INDEX: [%s] SELECTED.\n" +
                          "----------------------------------------------------------------------------------------\n",
                ES_SERVER, ES_PORT, ESOperations.getClusterName(), ES_INDEX);
        decider();


    }

    public static void decider(){
        System.out.println("WHAT DO YOU WANT TO DO: \n [0]: REINDEX \n [1]: DUMP ON FILE \n [2]: PARTIAL MAPPING UPDATE");
        Scanner scanner = new Scanner(System.in);
        int operation = scanner.nextInt();
    }

    public static String getEsServer() throws Exception {
        if (Objects.nonNull(ES_SERVER))
            return ES_SERVER;
        else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("#######################################---HOST---#####################################\n" +
                    "Please enter your Elasticsearch host:\t");
            ES_SERVER = scanner.nextLine();
            return ES_SERVER;
        }
    }

    public static String getEsPort() {
        if (Objects.nonNull(ES_PORT))
            return ES_PORT;
        else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("#######################################---PORT---#####################################\n" +
                    "Please enter your Elasticsearch port:\t");
            ES_PORT = scanner.nextLine();
            return ES_PORT;
        }
    }

    public static String getEsIndex() {
        if (Objects.nonNull(ES_INDEX))
            return ES_INDEX;
        else {
            String[] indices = ESOperations.fetchIndexNames();
            System.out.println("##################################---INDICES---#######################################");
            IntStream.range(0, indices.length)
                    .forEach(i -> System.out.println("[" + i + "] : [" + indices[i] + "]"));
            Scanner scanner = new Scanner(System.in);
            System.out.print("########################################################################################\n" +
                    "Please enter your Elasticsearch index NUMBER:\t");
            int indexNum = scanner.nextInt();
            if(indexNum < 0 || indexNum > indices.length) {
                System.out.print("########################################################################################\n" +
                        "Number out of range. Please enter your Elasticsearch index NUMBER:\t");
                indexNum = scanner.nextInt();
                if(indexNum < 0 || indexNum > indices.length) {
                    System.out.print("########################################################################################\n" +
                    "<--BYE-->");
                    System.exit(1);
                }
            }
            ES_INDEX = indices[indexNum];
            return ES_INDEX;
        }
    }
}
