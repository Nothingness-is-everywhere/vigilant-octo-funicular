package io.github.nothingnessiseverywhere.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 漫画实体类
 */
@Setter
@Getter
@Entity
@Table(name = "comic")
public class comic implements Serializable {

    // Setters
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false, unique = true)
    private String storagePath;

}
