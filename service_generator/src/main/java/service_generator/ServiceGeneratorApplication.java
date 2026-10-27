package service_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceGeneratorApplication.class, args);
    }

}
