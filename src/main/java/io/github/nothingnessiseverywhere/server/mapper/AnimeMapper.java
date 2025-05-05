package io.github.nothingnessiseverywhere.server.mapper;



import io.github.nothingnessiseverywhere.server.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnimeMapper extends JpaRepository<Anime, Long> {
// 根据用户名查找用户
    List<Anime> findByTitle(String title);
// 查找所有用户
    List<Anime> findAll();

// 根据用户ID删除用户
    void deleteById(Long animeId);

// 根据用户ID查找用户
    Optional<Anime> findById(Long animeId);


}