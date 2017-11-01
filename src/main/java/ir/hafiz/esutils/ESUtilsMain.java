package ir.hafiz.esutils;

import ir.hafiz.esutils.tools.FlowDecider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
/**
 * Created by akarimin on 10/17/17.
 */
@SpringBootApplication
@ComponentScan("ir")
@EnableAutoConfiguration
public class ESUtilsMain {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(new Object[]{ESUtilsMain.class}, args);
        FlowDecider.flowOperations();
        System.out.println("---------------------------------------END OF MAIN---------------------------------------");
    }

}
