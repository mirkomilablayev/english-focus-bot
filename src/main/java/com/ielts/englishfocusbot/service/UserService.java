package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.entity.User;
import com.ielts.englishfocusbot.util.ButtonConst;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class UserService {

    private final BotService botService;

    public void handle(Update update, User user) {

        String inlineMsg = botService.getInlineMsg(update);
        String step = user.getStep();
        String message = botService.getMessage(update);

        if (!handleMessage(message, user)) {
            if (!handleUserStep(update, step, user)) {
                if (!handleInlineMessage(update, inlineMsg, user)) botService.undefined(user);
            }
        }
    }

    private boolean handleMessage(String message, User user) {
        if (message.isEmpty()) return false;

        switch (message) {
            case "/start":
                System.out.println();
                return true;
            case "/admin":
                System.out.println("here so");
                return true;
            default:
                return false;
        }
    }

    private boolean handleUserStep(Update update, String userStep, User user) {
        if (userStep.isEmpty()) return false;

        switch (userStep) {
            case "1":
                System.out.println("here sqwqw");
                return true;
            default:
                return false;
        }
    }

    private boolean handleInlineMessage(Update update, String inlineMsg, User user) {
        if (inlineMsg.isEmpty()) return false;
        switch (inlineMsg) {
            case "task1":
                System.out.println("shu");
                return true;
            default:
                return false;
        }
    }

}
