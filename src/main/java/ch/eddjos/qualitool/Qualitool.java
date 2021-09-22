package ch.eddjos.qualitool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Qualitool extends SpringBootServletInitializer {
    public static void main(String[] args){
        SpringApplication.run(Qualitool.class, args);
    }
}
