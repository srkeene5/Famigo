package com.famigo.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class WebsiteApplication {
    
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsiteApplication.class, args);
        UserRepository ur = context.getBean(UserRepository.class);
        ur.createUser(new User(Util.generateID(20), "bob", "bob@bob.bob", "password", "bob", "bob", Visibility.ALL, Role.USER));
    }

}
