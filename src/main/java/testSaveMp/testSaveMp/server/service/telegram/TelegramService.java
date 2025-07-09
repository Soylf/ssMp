package testSaveMp.testSaveMp.server.service.telegram;

import org.telegram.telegrambots.meta.api.objects.User;

import java.io.File;
import java.util.List;

public interface TelegramService {
    boolean checkUserId(Long userId);
    void saveUser(User user);
    List<String> getCategories();
    List<File> getItemFiles(String message, String category);
}
