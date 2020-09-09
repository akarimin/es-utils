package edu.akarimin.esutils;

import edu.akarimin.esutils.tools.FlowDecider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by akarimin on 10/17/17.
 */
@SpringBootApplication
@ComponentScan("edu")
public class ESUtilsMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESUtilsMain.class);

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{ESUtilsMain.class}, args);
        FlowDecider.flowOperations();
        LOGGER.debug("---------------------------------------END OF MAIN--------------------------------------");
    }

}
