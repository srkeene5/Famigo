package com.famigo.website.utilities;

import java.util.Random;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utilities {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public static String getUserID() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        return user.getID();
    }

    public static String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        return user.getUsername();
    }

    public static void setUsername(String username) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl user = (UserDetailsImpl) auth.getPrincipal();
        UserDetailsImpl user2 = new UserDetailsImpl(username, null, user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked(), user.getAuthorities(), user.getID());
        UsernamePasswordAuthenticationToken auth2 = new UsernamePasswordAuthenticationToken(user2, auth.getCredentials());
        SecurityContextHolder.getContext().setAuthentication(auth2);
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
