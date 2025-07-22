package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testSaveMp.testSaveMp.server.service.web.WebService;

import java.io.File;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final WebService telegramService;

    @GetMapping("/categories")
    public List<String> getAllCategory() {
        return telegramService.getCategories();
    }

    @GetMapping
    public List<File> getFiles(String msg, String category) {
        return telegramService.getItemFiles(msg,category);
    }
}
