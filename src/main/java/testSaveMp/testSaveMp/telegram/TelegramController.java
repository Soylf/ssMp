package testSaveMp.testSaveMp.telegram;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import testSaveMp.testSaveMp.config.BotConfig;
import testSaveMp.testSaveMp.server.service.TelegramService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class TelegramController extends TelegramLongPollingBot {
    private final String absolutePath = "C:/Users/Soyl/Documents/AudionModelP/";
    private final BotConfig botConfig;
    private final TelegramService service;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage())  {
        User user = update.getMessage().getFrom();
            if(!service.checkUserId(user.getId())){
                service.saveUser(user);
            }
            if(update.getMessage().hasDocument()) {
                Document document = update.getMessage().getDocument();
                if (document.getMimeType().startsWith("audio/") || document.getFileName().endsWith(".mp3")) {
                    try {
                        createAudioDocument(document);
                    } catch (TelegramApiException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

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

    private void createAudioDocument(@NotNull Document document) throws TelegramApiException, IOException {
        String fileName = System.currentTimeMillis() + "-" + document.getFileName();
        String fileId = document.getFileId();
        GetFile getFile = new GetFile(fileId);
        File file = execute(getFile);

        String fileUrl = "https://api.telegram.org/file/bot" + botConfig.getToken() + "/" + file.getFilePath();
        java.io.File outputFile = new java.io.File(absolutePath+fileName);
        java.io.File p = outputFile.getParentFile();
        if (!p.exists()) {
            boolean c = p.mkdirs();
            if (!c) {
                throw new IOException("Не удалось создать директорию: " + outputFile.getParentFile().getAbsolutePath());
            }
        }

        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
        service.saveAudio(document.getFileName(), outputFile.getAbsolutePath());
    }
}