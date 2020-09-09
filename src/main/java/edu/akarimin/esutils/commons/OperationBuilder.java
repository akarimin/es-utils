package edu.akarimin.esutils.commons;

import edu.akarimin.esutils.model.OperationBuilderResponse;
import edu.akarimin.esutils.model.Operations;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Created by akarimin on 10/23/17.
 */
public final class OperationBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperationBuilder.class);
    private static final OperationBuilder INSTANCE = new OperationBuilder();

    private static String ES_SERVER = System.getProperty("elastic.host");

    private static String ES_PORT = System.getProperty("elastic.port");

    private static String ES_CLUSTER = System.getProperty("elastic.cluster");

    private static String ES_INDEX = System.getProperty("elastic.index");

    public static OperationBuilder prepareNode() {
        return INSTANCE;
    }

    public String[] fetchIndexNames() throws Exception {
        return ESConnector.getClient().admin().cluster().prepareState().execute()
            .actionGet().getState().getMetaData().getConcreteAllIndices();
    }

    public String getEsServer() {
        return ES_SERVER;
    }

    public OperationBuilder setEsServer() {
        ES_SERVER = (String) ScannerUtil.fetchConsoleInput("HOST",
            "Please enter your Elasticsearch host:\t");
        return this;
    }

    public String getEsPort() {
        if (Objects.isNull(ES_PORT))
            setEsPort();
        return ES_PORT;
    }

    public OperationBuilder setEsPort() {
        ES_PORT = (String) ScannerUtil.fetchConsoleInput("PORT",
            "Please enter your Elasticsearch port:\t");
        return this;
    }

    public String getEsIndex() {
        return ES_INDEX;
    }

    public OperationBuilder setEsIndex() throws Exception {
        String[] indices = fetchIndexNames();
        LOGGER.debug(
            "##################################---INDICES---#######################################");
        IntStream.range(0, indices.length)
            .forEach(i -> System.out.println("[" + i + "] : [" + indices[i] + "]"));
        int indexNum;

        indexNum = Integer.parseInt((String) ScannerUtil.fetchConsoleInput("NUM",
            "Please enter your Elasticsearch index NUMBER:\t"));

        if (indexNum < 0 || indexNum > indices.length) {
            indexNum = Integer.parseInt((String) ScannerUtil.fetchConsoleInput("NUM",
                "Number out of range. Please enter your Elasticsearch index NUMBER:\t"));
            if (indexNum < 0 || indexNum > indices.length) {
                System.exit(1);
            }
        }
        ES_INDEX = indices[indexNum];
        return this;
    }

    public OperationBuilder checkNodeStatus() {
        try {
            ClusterHealthResponse healths = ESConnector.getClient().admin().cluster()
                .prepareHealth().get();
            ES_CLUSTER = healths.getClusterName();
            ClusterHealthStatus status = healths.getStatus();
            int numberOfNodes = healths.getNumberOfNodes();
            LOGGER.debug("Pinging ... ");
            Thread.sleep(3000L);
            LOGGER.debug(
                "--------------------------------------------------------------------------------------\n"
                    + "CLUSTER-NAME: [{}] \t STATUS: [{}]\t NUMBER OF DATA NODES: [{}]\n"
                    + "--------------------------------------------------------------------------------------\n",
                ES_CLUSTER, status, numberOfNodes);
        } catch (Exception e) {
            if (e instanceof NoNodeAvailableException)
                LOGGER.debug("Node not found !");
            System.exit(1);
        }
        return this;
    }

    public String getClusterName() {
        return ES_CLUSTER;
    }

    public OperationBuilder displayConfig() {
        LOGGER.debug(
            "###################################---CONFIG---######################################\n"
                + "----------------------------------------------------------------------------------------\n"
                + "ELASTICSEARCH NODE WITH HOST: [{}] PORT: [{}] CLUSTER NAME: [{}] INDEX: [{}] SELECTED.\n"
                + "----------------------------------------------------------------------------------------\n",
            ES_SERVER, ES_PORT, ES_CLUSTER, ES_INDEX);
        return this;
    }

    public OperationBuilderResponse decide() {
        System.out.println("WHAT DO YOU WANT TO DO: \n");
        IntStream.range(0, Operations.values().length).forEach(
            i -> LOGGER.debug("[{}] : [{}]\n", i, Operations.values()[i]));
        OperationBuilderResponse response = new OperationBuilderResponse();
        Map<Integer, String> decision = new HashMap<>(1);
        int operationId = Integer.parseInt((String) ScannerUtil.fetchConsoleInput("", ""));
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
