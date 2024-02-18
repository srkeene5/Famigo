package com.famigo.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class WebsiteApplication {
    
    static Profile profile;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(WebsiteApplication.class, args);
        Profile profile = context.getBean(Profile.class);
        profile.createProfile("user", "private", "email", "cain", "abel", "something");
        profile.printProfile();
        /*Place place = context.getBean(Place.class);
		place.createPlace("purdue", "WL", "0");*/
    }

}
