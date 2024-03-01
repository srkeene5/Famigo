package com.famigo.website.utilities;

import java.util.Random;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class Util {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public static String getUserID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        return user.getUsername();
    }
    
    public static String generateID(int size) {
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        int maxChar = 123;
        int minChar = 65;
        for (int i = 0; i < size; i++) {
            int x = random.nextInt(maxChar - minChar) + minChar;
            if (x >= 91 && x <= 96) i = i - 1;
            else s.append(Character.toString((char) x));
        }
        return s.toString();
    }

}