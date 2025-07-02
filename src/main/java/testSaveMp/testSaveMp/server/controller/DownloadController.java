package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import testSaveMp.testSaveMp.server.service.download.DownloadService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/download")
public class DownloadController {
    private final DownloadService service;

    @PostMapping
    public ResponseEntity<String> createItem(
            @RequestParam(name = "file") MultipartFile multipartFile,
            @RequestParam(name = "description") String description,
            @RequestParam(name = "category") String category) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.endsWith(".mp3") && !originalFilename.endsWith(".mp4")) {
            return ResponseEntity.badRequest().body("Поддерживаются только .mp3 и .mp4 файлы");
        }

        service.createItem(multipartFile, description, category);
        return ResponseEntity.ok("Успешно сохранено");
    }

    @GetMapping
    public List<String> searchCategories(@RequestParam String query) {
        return service.searchCategories(query.toLowerCase());
    }
}
