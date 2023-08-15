package com.disgustingcat.springbootapp.entity;

import lombok.Getter;
import lombok.Setter;


import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name="cat")
@Getter
@Setter
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cat")
    private Set<Incident> incidents;
}
