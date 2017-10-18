package ir.hafiz.esutils.commons;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import static ir.hafiz.esutils.commons.ESConnector.getElasticClient;


/**
 * Created by akarimin on 10/17/17.
 */
public abstract class AbstractESRepository {

    protected IndexRequestBuilder dumpOnFile(String indexName, String type, String id) {
        return getElasticClient().prepareIndex().setIndex(indexName).setType(type).setId(id);
    }

    protected GetRequestBuilder reindex(String indexName, String type, String id) {
        return getElasticClient().prepareGet(indexName, type, id);
    }

    protected IndexRequestBuilder prepareIndexWithoutID(String indexName, String type) {
        return getElasticClient().prepareIndex().setIndex(indexName).setType(type);
    }

    protected Client getClient() {
        return getElasticClient();
    }
}
