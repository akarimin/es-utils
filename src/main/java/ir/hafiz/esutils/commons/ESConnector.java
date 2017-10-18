package ir.hafiz.esutils.commons;

import ir.hafiz.logger.ESLogger;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;

/**
 * Created by amir amirhoshangi@gmail.com
 */
@Component
public class ESConnector {

    public static Client getElasticClient() {
        return ESLogger.getElasticClient();
    }
}
