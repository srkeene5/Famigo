package com.famigo.website.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class userEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenID;
    private String confirmationToken;
    private Date createdDate;
    

}
