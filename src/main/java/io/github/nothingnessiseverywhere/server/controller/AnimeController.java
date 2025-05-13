package io.github.nothingnessiseverywhere.server.controller;

import io.github.nothingnessiseverywhere.server.entity.Anime;
import io.github.nothingnessiseverywhere.server.service.AnimeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Controller
public class AnimeController {

    // 注入AnimeService
    private final AnimeService animeService;

    public AnimeController(AnimeService animeService) {
        this.animeService = animeService;
    }

    // 获取所有动漫
    @GetMapping("/getAllAnimes")
    public ResponseEntity<List<Anime>> getAllAnimes() {
        List<Anime> animes = animeService.getAllAnimes();
        return ResponseEntity.ok(animes);
    }

    @GetMapping("/getAnimeById/{id}")
    public ResponseEntity<Resource> streamAnime(@PathVariable Long id, HttpServletRequest request) {
        // 查询动漫信息
        Optional<Anime> animeOpt = animeService.getAnimeById(id);
        if (animeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 获取文件路径
        File file = new File(animeOpt.get().getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 创建文件资源
        Resource resource = new FileSystemResource(file);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers
                .setContentType(MediaType.valueOf("video/mp2t"));  // TS流的MIME类型
        headers
                .set(HttpHeaders.ACCEPT_RANGES, "bytes");          // 支持字节范围请求
        headers
                .setContentLength(file.length());

        // 处理范围请求（用于视频拖放进度）
        try {
            List<HttpRange> ranges = HttpRange.parseRanges(request.getHeader(HttpHeaders.RANGE));
            if (!ranges.isEmpty()) {
                HttpRange range = ranges.get(0);
                long start = range.getRangeStart(file.length());
                long end = range.getRangeEnd(file.length());
                long rangeLength = end - start + 1;

                headers
                        .set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + file.length());
                headers
                        .setContentLength(rangeLength);

                return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                        .headers(headers)
                        .body(resource);
            }
        } catch (IllegalArgumentException e) {
            // 处理无效的Range请求
        }

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
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
