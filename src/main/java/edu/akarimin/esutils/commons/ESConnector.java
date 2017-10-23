package edu.akarimin.esutils.commons;

import edu.akarimin.esutils.tools.OperationBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

/**
 * @author  akarimin
 */
public class ESConnector {

    public static Client getClient() throws Exception {
        return (new PreBuiltTransportClient(Settings.builder()
                .put("cluster.name", "akarimin-cluster")
                .build()))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress
                        .getByName(OperationBuilder.initialize().getEsServer()),
                        Integer.valueOf(OperationBuilder.initialize().getEsPort())));
    }
}
