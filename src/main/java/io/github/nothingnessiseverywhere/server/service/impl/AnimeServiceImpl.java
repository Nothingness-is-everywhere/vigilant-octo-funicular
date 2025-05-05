package io.github.nothingnessiseverywhere.server.service.impl;

import io.github.nothingnessiseverywhere.server.entity.Anime;
import io.github.nothingnessiseverywhere.server.mapper.AnimeMapper;
import io.github.nothingnessiseverywhere.server.service.AnimeService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class AnimeServiceImpl implements AnimeService{
    private final AnimeMapper animeMapper;

    public AnimeServiceImpl(AnimeMapper animeMapper) {
        this.animeMapper = animeMapper;
    }

    @Override
    public boolean AddAnime(Anime anime) {
        // 获取Anime对象的文件路径
        String filePath = anime.getStoragePath();

        // 检查文件是否存在
        File file = new File(filePath);
        if (file.exists()) {
            // 文件不存在，返回false
            return false;
        }

        // 尝试保存Anime对象
        try {
            animeMapper.save(anime);
            return true;
        } catch (Exception e) {
            // 保存失败，处理异常
            System.err.println("添加动漫名时出错: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Anime> getAllAnimes() {
        return animeMapper.findAll();
    }

    @Override
    public boolean deleteById(Long animeId) {
        try {
            animeMapper.deleteById(animeId);
            return true;
        } catch (Exception e) {
            System.err.println("删除动漫时出错: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Anime> findByAnimeTitle(String title) {
        return animeMapper.findByTitle(title);
    }

    @Override
    public void UpdateAnime(Anime anime) {
        animeMapper.save(anime);
    }

    @Override
    public Optional<Anime> getAnimeById(Long animeId) {
        return animeMapper.findById(animeId);
    }
}