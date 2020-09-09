package edu.akarimin.esutils.commons;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * Created by akarimin@buffalo.edu
 */

public class ESConnector {

    public static Client getClient() throws Exception {
        return (new PreBuiltTransportClient(Settings.builder()
            .put("cluster.name", System.getProperty("elastic-cluster")).build()))
            .addTransportAddress(new InetSocketTransportAddress(InetAddress
                .getByName(OperationBuilder.prepareNode().getEsServer()),
                Integer.parseInt(OperationBuilder.prepareNode().getEsPort())));
    }

    private ESConnector() {
        throw new AssertionError("Non-instantiable.");
    }
}
