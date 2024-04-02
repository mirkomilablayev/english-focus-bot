package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.entity.User;
import com.ielts.englishfocusbot.util.ButtonConst;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class ButtonService {

    public ReplyKeyboardRemove createEmptyButton() {
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        keyboardRemove.setRemoveKeyboard(true);
        return keyboardRemove;
    }




    private static ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        return replyKeyboardMarkup;
    }


    public ReplyKeyboard userMainMenuButton(User user) {


        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        //first row
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboardRowList.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(ButtonConst.IELTS_SECTION);
        keyboardRow.add(ButtonConst.VOCABULARIES);
        keyboardRowList.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(ButtonConst.PODCASTS);
        keyboardRow.add(ButtonConst.MOVIES);
        keyboardRowList.add(keyboardRow);

        keyboardRow = new KeyboardRow();
        keyboardRow.add(ButtonConst.TRANSLATOR);
        keyboardRow.add(ButtonConst.CONTACT_ADMIN);
        keyboardRowList.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }



    public ReplyKeyboard adminMainMenu() {
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        //first row
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ButtonConst.ADD_CHALLENGE);
        keyboardRowList.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }
    public ReplyKeyboard backMainMenuButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = getReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(ButtonConst.MAIN_MENU);
        keyboardRowList.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }


}
