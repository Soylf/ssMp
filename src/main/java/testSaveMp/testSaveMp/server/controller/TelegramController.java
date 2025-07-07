package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import testSaveMp.testSaveMp.config.BotConfig;
import testSaveMp.testSaveMp.server.service.telegram.TelegramService;

import java.time.LocalTime;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final TelegramService service;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();
            if (!service.checkUserId(user.getId())) {
                log.info("Добавлен новый пользователь: {} / {}", user.getId(), user.getFirstName());
                service.saveUser(user);
            }
            if(message.getText().equalsIgnoreCase("Выведи все категории")) {
                String st = service.getCategories();
                SendMessage(message.getChatId(),st);
            }
            if(message.hasText()) {
                String msg = message.getText();
                String time = LocalTime.now().isAfter(LocalTime.of(18, 0)) ? "вечер" : "день";
                switch (msg) {
                    case  "/info" -> SendMessage(message.getChatId(),
                            "Я Напомика, помощник Аски, бот был создан при поддержке Александра и заточен на сохранение и загрузку медиаДанных, таких как mp3/mp4");
                    case "/start" -> SendMessage(message.getChatId(),
                            "Добрый " + time + " " + user.getFirstName());
                }
                if(msg.equalsIgnoreCase("/search")) {
                    SendMessage(message.getChatId(), "Введите что желаете найти:");

                }
            }
        }
    }

    @Override
    public void onRegister() {
        List<BotCommand> commands = List.of(
                new BotCommand("/info",
                        "info"),
                new BotCommand("/start",
                        "start"),
                new BotCommand("/search",
                        "search")
        );
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private void SendMessage(Long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void SendDocument(Long chatId, InputFile document) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(document);
        try {
            execute(sendDocument);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}