package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import testSaveMp.testSaveMp.server.service.download.DownloadService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/download")
public class DownloadController {
    private final DownloadService service;

    @PostMapping
    public ResponseEntity<String> createItem(@RequestParam(name = "file") MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        if (!originalFilename.endsWith(".mp3") && !originalFilename.endsWith(".mp4")) {
            return ResponseEntity.badRequest().body("Поддерживаются только .mp3 и .mp4 файлы");
        }

        service.createItem(multipartFile);
        return ResponseEntity.ok("Успешно сохранено");
    }
}
