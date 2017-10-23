package ir.hafiz.esutils.tools;

import ir.hafiz.esutils.commons.*;
import ir.hafiz.esutils.model.TransactionMonitoringModel;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.ReindexAction;
import org.elasticsearch.index.reindex.ReindexRequestBuilder;

import java.util.Scanner;

import static ir.hafiz.esutils.commons.ESConnector.getClient;


/**
 * Created by akarimin on 10/17/17.
 */
public final class ESOperations {

    private static ESOperations INSTANCE = new ESOperations();
    private static String NEW_INDEX_NAME;

    static ESOperations prepareOperation() {
        return INSTANCE;
    }

    ESOperations reindex() throws Exception {
        ReindexRequestBuilder builder = ReindexAction.INSTANCE
                .newRequestBuilder(getClient())
                .source(OperationBuilder.initialize().getEsIndex())
                .destination(NEW_INDEX_NAME);
        builder.destination().setOpType(IndexRequest.OpType.CREATE);
        builder.abortOnVersionConflict(false);
        builder.refresh(true);
        builder.get();
        System.out.println("---------------------------------REINDEX DONE--------------------------------------------");
        return this;
    }

    ESOperations setMapping() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("########################################################################################\n" +
                "Please enter your new INDEX NAME:\t");
        NEW_INDEX_NAME = scanner.nextLine();
        String content = FileManager.readMappingFile(FileManager.getMappingFilePath());
        XContentBuilder builder = XContentFactory.jsonBuilder().prettyPrint();
        try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                .createParser(NamedXContentRegistry.EMPTY, content.getBytes())) {
            builder.copyCurrentStructure(parser);
        }
        getClient().admin().indices()
                .prepareCreate(NEW_INDEX_NAME)
                .setSource(builder.string())
                .get();

        System.out.println("---------------------------------MAPPING CREATED-----------------------------------------");
        return this;
    }


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


    public ESOperations dumpOnFile() {
        //TODO: Develop
        return this;
    }

    ESOperations then() {
        return this;
    }

    private ESOperations() {

    }
}
