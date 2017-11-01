package ir.hafiz.esutils.tools;

import ir.hafiz.esutils.commons.OperationBuilder;
import ir.hafiz.esutils.model.OperationBuilderResponse;

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
                        .getMappingOrSelfReindex()
                        .then()
                        .unequalNameReindex()
                        .then()
                        .indexTransaction()
                        .done();
                //TODO: Ask for Deleting Index
                break;
            default:
                break;

        }
    }
}
