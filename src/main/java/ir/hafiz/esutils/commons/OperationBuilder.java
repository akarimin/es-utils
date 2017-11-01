package ir.hafiz.esutils.commons;

import ir.hafiz.esutils.model.OperationBuilderResponse;
import ir.hafiz.esutils.model.Operations;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.cluster.health.ClusterHealthStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.IntStream;

import static ir.hafiz.esutils.commons.ESConnector.getClient;

/**
 * Created by akarimin on 10/23/17.
 */
public final class OperationBuilder {

    private static OperationBuilder INSTANCE = new OperationBuilder();
    private static String ES_SERVER = System.getProperty("hafiz.ibank.elastic.host");
    private static String ES_PORT = System.getProperty("hafiz.ibank.elastic.port");
    private static String ES_CLUSTER = System.getProperty("hafiz.ibank.elastic.cluster");
    private static String ES_INDEX = System.getProperty("hafiz.ibank.elastic.index");

    public static OperationBuilder prepareNode() {
        return INSTANCE;
    }

    public String[] fetchIndexNames() throws Exception {
        return getClient()
                .admin()
                .cluster()
                .prepareState()
                .execute()
                .actionGet()
                .getState()
                .getMetaData()
                .getConcreteAllIndices();
    }

    public String getEsServer() throws Exception {
        return ES_SERVER;
    }

    public OperationBuilder setEsServer() throws Exception {
        ES_SERVER = (String) ScannerUtil.fetchconsoleInput("HOST", "Please enter your Elasticsearch host:\t");
        return this;
    }

    public String getEsPort() {
        if (Objects.isNull(ES_PORT))
            setEsPort();
        return ES_PORT;
    }

    public OperationBuilder setEsPort() {
        ES_PORT = (String) ScannerUtil.fetchconsoleInput("PORT", "Please enter your Elasticsearch port:\t");
        return this;
    }

    public String getEsIndex() throws Exception {
        return ES_INDEX;
    }

    public OperationBuilder setEsIndex() throws Exception {
        String[] indices = fetchIndexNames();
        System.out.println("##################################---INDICES---#######################################");
        IntStream.range(0, indices.length)
                .forEach(i -> System.out.println("[" + i + "] : [" + indices[i] + "]"));
        int indexNum = 0;

        indexNum = Integer.valueOf((String) ScannerUtil.fetchconsoleInput("",
                "Please enter your Elasticsearch index NUMBER:\t"));

        if (indexNum < 0 || indexNum > indices.length) {
            indexNum = Integer.valueOf((String) ScannerUtil.fetchconsoleInput("",
                    "Number out of range. Please enter your Elasticsearch index NUMBER:\t"));
            if (indexNum < 0 || indexNum > indices.length) {
                System.exit(1);
            }
        }
        ES_INDEX = indices[indexNum];
        return this;
    }

    public OperationBuilder checkNodeStatus() throws Exception {
        try {
            ClusterHealthResponse healths = getClient()
                    .admin()
                    .cluster()
                    .prepareHealth()
                    .get();
            ES_CLUSTER = healths.getClusterName();
            ClusterHealthStatus status = healths.getStatus();
            int numberOfNodes = healths.getNumberOfNodes();
            System.out.println("Pinging ... ");
            Thread.sleep(3000L);
            System.out.printf("--------------------------------------------------------------------------------------\n" +
                            "CLUSTER-NAME: [%s] \t STATUS: [%s]\t NUMBER OF DATA NODES: [%s]\n" +
                            "--------------------------------------------------------------------------------------\n",
                    ES_CLUSTER, status, numberOfNodes);
        } catch (Exception e) {
            if (e instanceof NoNodeAvailableException)
                System.out.println("Node not found !");
            System.exit(1);
        }
        return this;
    }

    public String getClusterName() throws Exception {
        return ES_CLUSTER;
    }

    public OperationBuilder displayConfig() {
        System.out.printf("###################################---CONFIG---#########################################\n" +
                        "----------------------------------------------------------------------------------------\n" +
                        "ELASTICSEARCH NODE WITH HOST: [%s] PORT: [%s] CLUSTER NAME: [%s] INDEX: [%s] SELECTED.\n" +
                        "----------------------------------------------------------------------------------------\n",
                ES_SERVER, ES_PORT, ES_CLUSTER, ES_INDEX);
        return this;
    }

    public OperationBuilderResponse decide() {
        System.out.println("WHAT DO YOU WANT TO DO: \n");
        IntStream.range(0, Operations.values().length)
                .forEach(i -> System.out.printf("[%d] : [%s]\n", i, Operations.values()[i]));
        OperationBuilderResponse response = new OperationBuilderResponse();
        Map<Integer, String> decision = new HashMap<>(1);
        Scanner scanner = new Scanner(System.in);
        int operationId = scanner.nextInt();
        decision.put(operationId, Operations.values()[operationId].toString());
        response.setOperation(decision);
        return response;
    }


    private OperationBuilder() {
    }

    public OperationBuilder then() {
        return this;
    }
}
