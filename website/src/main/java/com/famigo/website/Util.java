package com.famigo.website;

import java.util.Random;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

public class Util {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
    
    public static String generateID(int size) {
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        int maxChar = 123;
        int minChar = 48;
        for (int i = 0; i < size; i++) {
            int x = random.nextInt(maxChar - minChar) + minChar;
            //if (x >= 91 && x <= 96) continue;
            s.append((char) x);
        }
        return s.toString();
    }

}
