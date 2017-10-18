package ir.hafiz.esutils.tools;

import ir.hafiz.esutils.commons.ESConnector;
import ir.hafiz.esutils.commons.ESDetector;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;


/**
 * Created by akarimin on 10/17/17.
 */
public class ESOperations {

    private static String ES_CLUSTER = System.getProperty("hafiz.ibank.elastic.cluster");

    private static Settings settingsBuilder() {
        return Settings.builder()
                .put("index.number_of_shards", 15)
                .put("index.number_of_replicas", 1)
                .build();
    }

    public static String[] fetchIndexNames() {
        return ESConnector.getElasticClient()
                .admin()
                .cluster()
                .prepareState()
                .execute()
                .actionGet()
                .getState()
                .getMetaData()
                .getConcreteAllIndices();
    }


    public String dumpOnFile() {
        return null;
    }


    public String reindex(String oldIndexName, String newIndexName) throws Exception {
        TransportClient client = new PreBuiltTransportClient(settingsBuilder())
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ESDetector.getEsServer()), 9300))
                .addPlugin(ReindexPlugin.class).build();
        ReindexRequestBuilder builder = ReindexAction.INSTANCE
                .newRequestBuilder(client)
                .source(oldIndex)
                .destination(newIndex);
        builder.destination().setOpType(opType);
        builder.abortOnVersionConflict(false);
        builder.get();


        return null;
    }

    public static void checkNodeStatus() throws InterruptedException {
        ClusterHealthResponse healths = ESConnector.getElasticClient()
                .admin()
                .cluster()
                .prepareHealth()
                .get();
        ES_CLUSTER = healths.getClusterName();
        ClusterHealthStatus status = healths.getStatus();
        int numberOfNodes = healths.getNumberOfNodes();
        System.out.println("PINGING ... ");
        Thread.sleep(5000L);
        System.out.printf("----------------------------------------------------------------------------------------\n" +
                        "CLUSTER-NAME: [%s] \t STATUS: [%s]\t NUMBER OF DATA NODES: [%s]\n" +
                        "----------------------------------------------------------------------------------------\n",
                ES_CLUSTER, status, numberOfNodes);

    }

    public static String getClusterName(){
        return ES_CLUSTER;
    }
}
