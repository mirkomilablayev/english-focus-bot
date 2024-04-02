package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.entity.User;
import com.ielts.englishfocusbot.util.ButtonConst;
import com.ielts.englishfocusbot.util.Steps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@AllArgsConstructor
public class AdminService {

    private final BotService botService;
    private final ButtonService buttonService;
    private final LogicService logicService;

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
                adminStartButtonClick(user);
                return true;
            case ButtonConst.ADD_CHALLENGE:
                addChallengeButtonClic(user);
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


    private void adminStartButtonClick(User user) {
        SendMessage sendMessage = botService.getSendMessage(user);
        sendMessage.setText("Admin menu!!");
        sendMessage.setReplyMarkup(buttonService.adminMainMenu());
        botService.sendMessageExecutor(sendMessage);
    }

    private void addChallengeButtonClic(User user){
        SendMessage sendMessage = botService.getSendMessage(user);
        sendMessage.setText("Send the name of challenge");
        sendMessage.setReplyMarkup(buttonService.backMainMenuButton());
        botService.sendMessageExecutor(sendMessage);

        user.setStep(Steps.ASK_CHALLENGE_NAME);
        logicService.updateUser(user);
    }

    private void askChallengeName(Update  update){

    }


}
