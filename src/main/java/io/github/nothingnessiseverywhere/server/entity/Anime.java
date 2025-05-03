package io.github.nothingnessiseverywhere.server.entity;


import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 动漫实体类
 */
@Setter
@Getter
@Entity
@Table(name = "anime")
public class Anime implements Serializable {

    // Setters
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // 动漫ID（主键）

    @Column(nullable = false, unique = true)
    private String title;           // 动漫名字（必填）

    @Column(nullable = false)
    private String author;          // 作者

    @Column(nullable = false, unique = true)
    private String storagePath;     // 存储路径

}
