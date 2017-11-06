package edu.akarimin.esutils.tools;

import edu.akarimin.esutils.commons.*;
import edu.akarimin.esutils.model.TransactionMonitoringModel;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;

import java.io.IOException;


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

    ESOperations reindex() throws Exception {
        ReindexRequestBuilder builder = ReindexAction.INSTANCE
                .newRequestBuilder(ESConnector.getClient())
                .source(OperationBuilder.prepareNode().getEsIndex())
                .destination(NEW_INDEX_NAME);
        builder.destination().setOpType(IndexRequest.OpType.CREATE);
        builder.abortOnVersionConflict(false);
        builder.refresh(true);
        builder.get();
        System.out.println("---------------------------------REINDEX DONE-------------------------------------------");
        return this;
    }

    ESOperations getMapping() throws Exception {
        NEW_INDEX_NAME = (String) ScannerUtil.fetchConsoleInput("INDEX", "Please enter your new INDEX NAME:\t");
        this.buildMapping();
        try {
            this.createMapping(NEW_INDEX_NAME);
        } catch (ResourceAlreadyExistsException e) {
            System.exit(1);
        }
        return this;
    }

    /**
     * @test
     */
//    ESOperations indexTransaction() {
//        try {
//            SearchResponse response = ESConnector.getClient().prepareSearch(NEW_INDEX_NAME)
//                    .setTypes("log")
//                    .setQuery(QueryBuilders.matchPhraseQuery("cif", "1439"))
//                    .execute()
//                    .get();
//            if (response.getHits().getTotalHits() != 0) {
//                TransactionMonitoringModel fetched = JacksonMapperConfig.getObjectMapper().readValue(JacksonMapperConfig
//                        .getObjectMapper().writeValueAsBytes(response.getHits().getHits()[0]), TransactionMonitoringModel.class);
//                System.out.println("---->>>>>" + JacksonMapperConfig.getObjectMapper().writeValueAsString(fetched));
//            }
//            return this;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    ESOperations selfReindex() throws Exception {
        NEW_INDEX_NAME = OperationBuilder.prepareNode().getEsIndex();
        TMP_INDEX_NAME = NEW_INDEX_NAME + "_temp";
        this.buildMapping();
        System.out.println("New index name is equal to the old index name, but no worries we use temporary index...");

        ESOperations.prepareOperation()
                .createMapping(TMP_INDEX_NAME)
                .then()
                .reindex(OperationBuilder.prepareNode().getEsIndex(), TMP_INDEX_NAME)
                .then()
                .removeIndex(OperationBuilder.prepareNode().getEsIndex())
                .then()
                .createMapping(OperationBuilder.prepareNode().getEsIndex())
                .then()
                .reindex(TMP_INDEX_NAME, NEW_INDEX_NAME)
                .then()
                //TODO: It must be asked
                .removeIndex(TMP_INDEX_NAME)
                .done();

        System.out.println("---------------------------------SELF-REINDEX DONE---------------------------------------");
        return this;
    }

    private ESOperations removeIndex(String index) throws Exception {
        //TODO: Ask for backup
        ESConnector.getClient().admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
        System.out.println("---------------------------------TEMP INDEX DELETED--------------------------------------");
        return this;
    }

    private ESOperations createMapping(String index) throws Exception {
        ESConnector.getClient().admin().indices()
                .prepareCreate(index)
                .setSource(FileManager.getContent())
                .get();
        System.out.println("---------------------------------MAPPING CREATED----------------------------------------");
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
                .newRequestBuilder(ESConnector.getClient())
                .source(oldIndex)
                .destination(newIndex);
        builder.destination().setOpType(IndexRequest.OpType.CREATE);
        builder.abortOnVersionConflict(false);
        builder.refresh(true);
        builder.get();
        return this;
    }

    private String buildMapping() throws IOException {
        String filePath = FileManager.getMappingFilePath();
        String content = FileManager.readMappingFile(filePath);
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
