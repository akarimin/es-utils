package edu.akarimin.esutils.tools;

import edu.akarimin.esutils.commons.OperationBuilder;
import edu.akarimin.esutils.model.OperationBuilderResponse;

/**
 * Created by akarimin on 10/23/17.
 */
public class FlowDecider {


    public static void flowOperations() throws Exception {

        OperationBuilderResponse decider = OperationBuilder.prepareNode()
                .setEsServer()
                .then()
                .setEsPort()
                .then()
                .checkNodeStatus()
                .then()
                .setEsIndex()
                .then()
                .displayConfig()
                .decide();

        switch (decider.getOperation().keySet().iterator().next()) {
            case 0:
                ESOperations.prepareOperation()
                        .then()
                        .getMapping()
                        .then()
                        .reindex()
                        .done();
                //TODO: Ask for Deleting Index
                break;
            case 1:
                ESOperations.prepareOperation()
                        .then()
                        .selfReindex()
                        .done();
            default:
                break;

        }
    }
}
