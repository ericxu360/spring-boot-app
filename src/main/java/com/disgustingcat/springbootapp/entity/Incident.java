package com.disgustingcat.springbootapp.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "incident")
@Data
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "cat_id", nullable = false)
    private long catId;

    @Column(name = "cat_name")
    private String catName;

    @Column(name = "user_id", nullable = false)
    private String catUser;

    @Column(name = "date_created")
    private Date date;

    @Column(name = "audio_url")
    private String audioUrl;

}
