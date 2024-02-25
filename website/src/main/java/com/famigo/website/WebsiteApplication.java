package com.famigo.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// @ComponentScan("com.famigo.website")
public class WebsiteApplication {
    
    static Profile profile;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsiteApplication.class, args);
    }

}
