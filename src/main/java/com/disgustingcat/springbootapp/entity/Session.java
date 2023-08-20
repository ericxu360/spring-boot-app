package com.disgustingcat.springbootapp.entity;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor()
@Table(name ="session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name="userid")
    private String userId;

    @Column(name="created_on")
    @CreationTimestamp
    private Date createdOn;

    @Column(name="last_used")
    @UpdateTimestamp
    private Date lastUsed;

    @Column(name="expires_on")
    private Date expiresOn;
}
