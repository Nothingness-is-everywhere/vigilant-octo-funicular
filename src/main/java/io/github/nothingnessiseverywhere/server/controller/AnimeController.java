package io.github.nothingnessiseverywhere.server.controller;

import io.github.nothingnessiseverywhere.server.entity.Anime;
import io.github.nothingnessiseverywhere.server.handler.AnimeResourceHttpRequestHandler;
import io.github.nothingnessiseverywhere.server.service.AnimeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Controller
public class AnimeController {

    // 注入AnimeService
    private final AnimeService animeService;
    private final AnimeResourceHttpRequestHandler handler;

    public AnimeController(AnimeService animeService, AnimeResourceHttpRequestHandler handler) {
        this.animeService = animeService;
        this.handler = handler;
    }

    // 获取所有动漫
    @GetMapping("/getAllAnimes")
    public ResponseEntity<List<Anime>> getAllAnimes() {
        List<Anime> animes = animeService.getAllAnimes();
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/getAnimeById/{id}")
    public void streamAnime(@PathVariable Long id,
                            HttpServletRequest request,
                            HttpServletResponse response) {
        Optional<Anime> animeOpt = animeService.getAnimeById(id);
        if (animeOpt.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value()); // 文件不存在
            return;
        }

        File file = new File(animeOpt.get().getStoragePath());
        if (!file.exists()) {
            response.setStatus(HttpStatus.NOT_FOUND.value()); // 文件不存在
            return;
        }

        // 将文件路径存入请求属性，供自定义Handler获取
        request.setAttribute(AnimeResourceHttpRequestHandler.REQUEST_ATTR_FILE_PATH, file.getAbsolutePath());

        try {
            handler.handleRequest(request, response); // 委托处理器处理请求
        } catch (IOException | ServletException e) {
            if ("断开的管道".equals(e.getMessage())) {
                System.out.println("客户端断开连接，停止传输");
            } else {
                System.out.println("视频流错误: " + e);
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
        }
    }

    // 根据id显示动漫页面
    @GetMapping("/home/Anime/{id}")
    public String  showAnimeByIdPage(@PathVariable Long id, Model model) {
        Optional<Anime> optionalAnime = animeService.getAnimeById(id);
        if (optionalAnime.isPresent()) {
            Anime anime = optionalAnime.get();
            model.addAttribute("anime", anime);
        }
        return "anime";
    }
}
