package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaDocument;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaVideo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import testSaveMp.testSaveMp.config.BotConfig;
import testSaveMp.testSaveMp.server.service.telegram.TelegramService;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {
    private boolean categoryCheck = false;
    private boolean searchCheck = false;
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
            if(message.hasText()) {
                String category = "Веселье";
                String msg = message.getText();
                String time = LocalTime.now().isAfter(LocalTime.of(18, 0)) ? "вечер" : "день";
                switch (msg) {
                    case  "/info" -> SendMessage(message.getChatId(),
                            "Я Напомика, помощник Аски, бот был создан при поддержке Александра и заточен на сохранение и загрузку медиаДанных, таких как mp3/mp4");
                    case "/start" -> SendMessage(message.getChatId(),
                            "Добрый " + time + " " + user.getFirstName());
                }

                if(msg.equalsIgnoreCase("/search")) {
                    sendSearchButtons(message.getChatId());
                    category = msg;
                    categoryCheck = true;
                    searchCheck = true;
                } else if(msg.equalsIgnoreCase("/unSearch")) {
                    searchCheck = false;
                    categoryCheck = false;
                }

                if(searchCheck && categoryCheck) {
                    List<File> files = service.getItemFiles(message.getText(), category);
                    List<InputMedia> inputMedia = List.of();
                    SendDocument(message.getChatId(), inputMedia);
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
                        "search"),
                new BotCommand("/unSearch",
                        "unSearch")
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

    private void SendDocument(Long chatId, List<InputMedia> documents) {
        SendMediaGroup sendDocument = new SendMediaGroup();
        sendDocument.setChatId(chatId);
        sendDocument.setMedias(documents);
        try {
            execute(sendDocument);
        }catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendSearchButtons(Long chatId) {
        List<String> categories = service.getCategories();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        for (String cat : categories) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(cat);
            button.setSwitchInlineQueryCurrentChat(cat);
            rows.add(List.of(button));
        }
        markup.setKeyboard(rows);

        SendMessage msg = new SendMessage();
        msg.setChatId(chatId.toString());
        msg.setText("Выберите категорию:");
        msg.setReplyMarkup(markup);
        try {
            execute(msg);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}