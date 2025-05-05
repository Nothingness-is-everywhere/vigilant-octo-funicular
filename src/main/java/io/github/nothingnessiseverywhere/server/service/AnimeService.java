package io.github.nothingnessiseverywhere.server.service;

import io.github.nothingnessiseverywhere.server.entity.Anime;

import java.util.List;
import java.util.Optional;

public interface AnimeService {
    boolean AddAnime(Anime anime);
    List<Anime> getAllAnimes();

    boolean deleteById(Long animeId);

    List<Anime> findByAnimeTitle(String title);

    void UpdateAnime(Anime anime);

    Optional<Anime> getAnimeById(Long animeId);
}