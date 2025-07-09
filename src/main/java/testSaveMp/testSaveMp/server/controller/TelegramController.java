package testSaveMp.testSaveMp.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaAudio;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {
    private boolean categoryCheck = false;
    private boolean searchCheck = false;
    private final BotConfig botConfig;
    private final TelegramService service;

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();
            String category = null;
            String msg = message.getText();
            if (!service.checkUserId(user.getId())) {
                log.info("Добавлен новый пользователь: {} / {}", user.getId(), user.getFirstName());
                service.saveUser(user);
            }
            if(message.hasText()) {
                String time = LocalTime.now().isAfter(LocalTime.of(18, 0)) ? "вечер" : "день";
                switch (msg) {
                    case  "/info" -> SendMessage(message.getChatId(),
                            "Я Напомика, помощник Аски, бот был создан при поддержке Александра и заточен на сохранение и загрузку медиаДанных, таких как mp3/mp4");
                    case "/start" -> SendMessage(message.getChatId(),
                            "Добрый " + time + " " + user.getFirstName()
                                    + "\nДля продолжения работы введите команды из перечня:\n/search\n/info");
                    case "/search" -> SendMessage(message.getChatId(),
                            "Хай вы вошли в поиск, чтобы выйти из него пропишите /unsearch");
                }

                if(msg.equalsIgnoreCase("/search")) {
                    searchCheck = true;
                } else if(msg.equalsIgnoreCase("/unsearch")) {
                    SendMessage(message.getChatId(), "Вы вышли из поиска");
                    searchCheck = false;
                    categoryCheck = false;
                }

                if(searchCheck) {
                    sendSearchButtons(message.getChatId());
                    category = msg.replaceFirst("@" + botConfig.getBotName() + "\\s*", "").trim();
                    categoryCheck = true;
                    if(categoryCheck) {
                        SendMessage(message.getChatId(), "Введите че нить для поиска:");
                        List<File> files = service.getItemFiles(message.getText(), category);
                        ConvertMedia(files,message.getChatId());
                    }
                }
            }
        }
    }

    @Override
    public void onRegister() {
        List<BotCommand> commands = List.of(
                new BotCommand("info", "Информация"),
                new BotCommand("start", "Start"),
                new BotCommand("search", "Начать поиск"),
                new BotCommand("unsearch", "Остановить поиск")
        );
        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void ConvertMedia(List<File> files, Long chatId) {
        List<InputMediaAudio> inputMediaAudios = new ArrayList<>(); //mp3
        List<InputMediaVideo> inputMediaVideos = new ArrayList<>(); //mp4
        for (File f : files) {
            String name = f.getName().toLowerCase();
            try {
                if (name.endsWith(".mp3") || name.endsWith(".ogg"))  {
                    InputMediaAudio i = new InputMediaAudio("attach://"+f.getName());
                    i.setMedia(f, f.getName());
                    inputMediaAudios.add(i);
                } else if (name.endsWith(".mp4") || name.endsWith(".mov")) {
                    InputMediaVideo i = new InputMediaVideo("attach://"+f.getName());
                    i.setMedia(f, f.getName());
                    inputMediaVideos.add(i);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        SendDocument(chatId, inputMediaAudios, inputMediaVideos);
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

    private void SendDocument(Long chatId, List<InputMediaAudio> inputMediaAudios,
                                            List<InputMediaVideo> inputMediaVideos) {
        List<InputMedia> inputMedia = new ArrayList<>();
        SendMediaGroup group = new SendMediaGroup();
        inputMedia.addAll(inputMediaVideos);
        inputMedia.addAll(inputMediaAudios);

        if(inputMedia.isEmpty()) return;

        group.setChatId(chatId);
        group.setMedias(inputMedia);
        try {
            execute(group);
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