package ir.hafiz.esutils.tools;

import ir.hafiz.esutils.commons.*;
import ir.hafiz.esutils.model.TransactionMonitoringModel;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;

import java.io.IOException;

import static ir.hafiz.esutils.commons.ESConnector.getClient;


/**
 * Created by akarimin on 10/17/17.
 */
public final class ESOperations {

    private static ESOperations INSTANCE = new ESOperations();
    private static String NEW_INDEX_NAME;
    private static String TMP_INDEX_NAME;


    static ESOperations prepareOperation() {
        return INSTANCE;
    }

    ESOperations unequalNameReindex() throws Exception {
        ReindexRequestBuilder builder = ReindexAction.INSTANCE
                .newRequestBuilder(getClient())
                .source(OperationBuilder.prepareNode().getEsIndex())
                .destination(NEW_INDEX_NAME);
        builder.destination().setOpType(IndexRequest.OpType.CREATE);
        builder.abortOnVersionConflict(false);
        builder.refresh(true);
        builder.get();
        System.out.println("---------------------------------REINDEX DONE--------------------------------------------");
        return this;
    }

    ESOperations getMappingOrSelfReindex() throws Exception {
        NEW_INDEX_NAME = (String) ScannerUtil.fetchconsoleInput("" ,"Please enter your new INDEX NAME:\t");
        this.buildMapping();
        try {
            this.createMapping(NEW_INDEX_NAME);
        } catch (ResourceAlreadyExistsException e) {
            System.out.println("New index name is equal to the old index name, but no worries we use temporary index...");
            TMP_INDEX_NAME = NEW_INDEX_NAME + "_temp";
            ESOperations.prepareOperation()
                    .createMapping(TMP_INDEX_NAME)
                    .then()
                    .selfReindex()
                    .then()
                    //TODO: It must be asked
                    .removeIndex(TMP_INDEX_NAME)
                    .done();
            System.exit(1);
        }
        return this;
    }


    /** @test */
    ESOperations indexTransaction() {
        try {
            SearchResponse response = getClient().prepareSearch(NEW_INDEX_NAME)
                    .setTypes("log")
                    .setQuery(QueryBuilders.matchPhraseQuery("cif", "1439"))
                    .execute()
                    .get();
            if (response.getHits().getTotalHits() != 0) {
                TransactionMonitoringModel fetched = JacksonMapperConfig.getObjectMapper().readValue(JacksonMapperConfig
                        .getObjectMapper().writeValueAsBytes(response.getHits().getHits()[0]), TransactionMonitoringModel.class);
                System.out.println("---->>>>>" + JacksonMapperConfig.getObjectMapper().writeValueAsString(fetched));
            }
            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ESOperations selfReindex() throws Exception {

        ESOperations.prepareOperation()
                .reindex(OperationBuilder.prepareNode().getEsIndex(), TMP_INDEX_NAME)
                .then()
                .removeIndex(OperationBuilder.prepareNode().getEsIndex())
                .then()
                .createMapping(OperationBuilder.prepareNode().getEsIndex())
                .then()
                .reindex(TMP_INDEX_NAME, NEW_INDEX_NAME)
                .done();

        System.out.println("---------------------------------SELF-REINDEX DONE---------------------------------------");
        return this;
    }

    private ESOperations removeIndex(String index) throws Exception {
        //TODO: Ask for backup
        getClient().admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
        System.out.println("---------------------------------TEMP INDEX DELETED--------------------------------------");
        return this;
    }

    private ESOperations createMapping(String index) throws Exception {
        getClient().admin().indices()
                .prepareCreate(index)
                .setSource(FileManager.getContent())
                .get();
        System.out.println("---------------------------------MAPPING CREATED-----------------------------------------");
        return this;
    }

    ESOperations then() {
        return this;
    }

    void done() throws InterruptedException {
        System.out.println("Finishing ...");
        Thread.sleep(5000);
        System.out.println("Done.");
    }

    ESOperations dumpOnFile() {
        //TODO: Develop
        return this;
    }

    private ESOperations reindex(String oldIndex, String newIndex) throws Exception {
        ReindexRequestBuilder builder = ReindexAction.INSTANCE
                .newRequestBuilder(getClient())
                .source(oldIndex)
                .destination(newIndex);
        builder.destination().setOpType(IndexRequest.OpType.CREATE);
        builder.abortOnVersionConflict(false);
        builder.refresh(true);
        builder.get();
        return this;
    }

    private String buildMapping() throws IOException {
        String content = FileManager.readMappingFile(FileManager.getMappingFilePath());
        XContentBuilder builder = XContentFactory.jsonBuilder().prettyPrint();
        try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                .createParser(NamedXContentRegistry.EMPTY, content.getBytes())) {
            builder.copyCurrentStructure(parser);
        }
        return builder.toString();
    }

    private ESOperations() {

    }
}
