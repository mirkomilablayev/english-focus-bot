package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class BotServiceV2 {

    private final BotService botService;

    public void test(Update update, User user){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("salom from service v2");
        sendMessage.setChatId(user.getChatId());
        botService.sendMessageExecutor(sendMessage);
    }

}
