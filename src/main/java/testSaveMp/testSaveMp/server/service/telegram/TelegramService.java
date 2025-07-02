package testSaveMp.testSaveMp.server.service.telegram;

import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramService {
    boolean checkUserId(Long userId);
    void saveUser(User user);
}
