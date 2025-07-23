package testSaveMp.testSaveMp.server.service.web;

import org.telegram.telegrambots.meta.api.objects.User;
import testSaveMp.testSaveMp.model.dto.ItemDto;

import java.io.File;
import java.util.List;

public interface WebService {
    boolean checkUserId(Long userId);
    void saveUser(User user);
    List<String> getCategories();
    List<File> getItemFiles(String message, String category);
    List<ItemDto> getItemFilesWeb(String msg, String category);
}
