package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testSaveMp.testSaveMp.model.dto.ItemDto;
import testSaveMp.testSaveMp.server.service.web.WebService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    private final WebService webService;

    @GetMapping("/categories")
    public List<String> getAllCategory() {
        return webService.getCategories();
    }

    @GetMapping
    public List<ItemDto> getFiles(String msg, String category) {
        return webService.getItemFilesWeb(msg,category);
    }
}
