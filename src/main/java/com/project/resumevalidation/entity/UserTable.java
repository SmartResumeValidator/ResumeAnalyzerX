package com.project.resumevalidation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "users", schema = "Resume_Validation_DB")
public class UserTable {

    /**
     * userId (corresponding to the 'id' column in the DB)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Adjust this for your DB (Oracle may use IDENTITY or SEQUENCE)
    private Long id;

    /**
     * userName (mapped to 'username' column in the DB)
     */
    @Column(name = "username", nullable = false)
    private String userName;

    /**
     * userGmail (mapped to 'gmail' column in the DB)
     */
    @Column(name = "gmail", nullable = false, unique = true)
    private String gmail;

    /**
     * userPassword (mapped to 'password' column in the DB)
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * userPhoneNumber (mapped to 'phonenumber' column in the DB)
     */
    @Column(name = "phonenumber", nullable = false)
    private String phoneNumber;
}
