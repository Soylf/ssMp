package testSaveMp.testSaveMp.server.service;

import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramService {
    boolean checkUserId(Long userId);

    void saveUser(User user);

    void saveAudio(String fileName, String absolutePath);
}
