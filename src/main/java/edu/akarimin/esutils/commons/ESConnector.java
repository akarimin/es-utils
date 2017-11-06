package edu.akarimin.esutils.commons;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * Created by amir amirhoshangi@gmail.com
 */
public class ESConnector {

    public static Client getClient() throws Exception {
        return (new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "akarimin-cluster")
                .build()))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress
                        .getByName(OperationBuilder.prepareNode().getEsServer()),
                        Integer.valueOf(OperationBuilder.prepareNode().getEsPort())));
    }
}
