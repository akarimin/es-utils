package edu.akarimin.esutils.tools;

import edu.akarimin.esutils.model.OperationBuilderResponse;

/**
 * @author akarimin on 10/23/17.
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
                .then()
                .decide();

        switch (decider.getOperation().keySet().iterator().next()) {
            case 0:
                ESOperations.prepareOperation()
                        .setMapping()
                        .then()
                        .reindex()
                        .then()
                        .indexTransaction();
                //TODO: Ask for Deleting Index
                break;
            default:
                break;

        }
    }
}
