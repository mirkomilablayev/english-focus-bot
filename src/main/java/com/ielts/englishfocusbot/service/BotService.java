package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import com.ielts.englishfocusbot.configuration.BotConfiguration;
import com.ielts.englishfocusbot.dto.FileIdDTO;
import com.ielts.englishfocusbot.util.*;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BotService extends TelegramLongPollingBot {


    private final BotConfiguration botConfiguration;
    private final LogicService logicService;
    private final ButtonService buttonService;
    private final ItemPageService itemPageService;
    private final InlineButtonService inlineButtonService;
    private final TranslatorService translatorService;


    @Override
    public String getBotUsername() {
        return this.botConfiguration.getUsername();
    }

    @Override
    public String getBotToken() {
        return this.botConfiguration.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        User user = logicService.createUser(update);


        if (user.getIsAdmin()){
        new AdminService(this, buttonService, logicService).handle(update, user);
        }else {
            new UserService(this).handle(update, user);
        }
    }


    public  String getMessage(Update update) {
        String message = "";
        if (update.hasMessage()) {
            Message updateMessage = update.getMessage();
            if (updateMessage.hasText()) {
                message = updateMessage.getText();
            }
        }
        return message;
    }

    public  String getInlineMsg(Update update) {
        String inlineMsg = "";
        if (update.hasCallbackQuery()) {
            CallbackQuery updateCallbackQuery = update.getCallbackQuery();
            inlineMsg = updateCallbackQuery.getData().split("-")[0];
        }
        return inlineMsg;
    }


    public void undefined(User user) {
        System.out.println("undefined");
        SendMessage sendMessage = getSendMessage(user);
        sendMessage.setText(MessageConst.WRONG_COMMAND);
        sendMessageExecutor(sendMessage);
    }

    public void updateUserREGISTERED(User user) {
        user.setStep(Steps.REGISTERED);
        logicService.updateUser(user);
    }


    public SendMessage getSendMessage(User user) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getChatId());
        return sendMessage;
    }


    public void editMessageCaptionExecutor(EditMessageCaption editMessageCaption) {
        try {
            execute(editMessageCaption);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendDocumentExecutor(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            String chatId = sendDocument.getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Fayl yuborilmadi");
            sendMessageExecutor(sendMessage);
        }
    }

    public void editMessageExecutor(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void sendMessageExecutor(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            System.out.println("Yuborilmadi");
        }
    }

    public void sendVideoExecutor(SendVideo sendVideo) {
        try {
            execute(sendVideo);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            System.out.println("Send Video ishlamadi");
            System.out.println(e.getMessage());
        }
    }


    public void sendPhotoExecutor(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            System.out.println("Yuborilmadi");
        }
    }

    public void sendAudioExecutor(SendAudio sendAudio) {
        try {
            execute(sendAudio);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            System.out.println("yuborilmadi");
        }
    }

    public void sendAnimationExecutor(SendAnimation sendAnimation) {
        try {
            execute(sendAnimation);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void deleteMessageExecutor(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public FileIdDTO getFileId(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasPhoto()) {
                List<PhotoSize> photos = message.getPhoto();
                PhotoSize largestPhoto = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
                String id = largestPhoto != null ? largestPhoto.getFileId() : "";
                return new FileIdDTO(id, ContentTypeConst.PHOTO);
            } else if (message.hasDocument()) {
                message.getDocument().getFileId();
                return new FileIdDTO(message.getDocument().getFileId(), ContentTypeConst.FILE);
            } else if (message.hasVideo()) {
                message.getVideo().getFileId();
                return new FileIdDTO(message.getVideo().getFileId(), ContentTypeConst.VIDEO);
            } else if (message.hasAudio()) {
                return new FileIdDTO(message.getAudio().getFileId(), ContentTypeConst.AUDIO);
            }
            return new FileIdDTO("", ContentTypeConst.NONE);
        }
        return new FileIdDTO("", ContentTypeConst.NONE);
    }


}
