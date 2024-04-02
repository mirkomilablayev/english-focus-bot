package com.ielts.englishfocusbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class InlineButtonService {
/*
    public InlineKeyboardMarkup generateInlineButtons(List<Task1BuildPageDto> list, long pageCount, int currentPage, String routing) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        // Add index lines
        int elementsPerPage = 5; // Assuming 5 elements per line
        for (int i = 0; i < list.size(); i += elementsPerPage) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            int endIndex = Math.min(i + elementsPerPage, list.size());
            for (int j = i; j < endIndex; j++) {
                String closed = "\uD83D\uDEAB";
                InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                inlineKeyboardButton.setText(list.get(j).getOrder() + (list.get(j).getIsOpen() ? "" : closed));
                inlineKeyboardButton.setCallbackData(routing + "-" + list.get(j).getId() + "-premiumStatus-" + (list.get(j).getIsOpen() ? "true" : "false"));
                rowInline.add(inlineKeyboardButton);
            }
            rowsInline.add(rowInline);
        }

        List<InlineKeyboardButton> navigationRow = new ArrayList<>();
        if (currentPage > 1) {
            InlineKeyboardButton previousButton = new InlineKeyboardButton();
            previousButton.setText(ButtonConst.PREVIOUS);
            previousButton.setCallbackData(routing + "-previous-" + (currentPage - 1));
            navigationRow.add(previousButton);
        }

        InlineKeyboardButton rejectButton = new InlineKeyboardButton();
        rejectButton.setText(ButtonConst.REJECT);
        rejectButton.setCallbackData(routing + "-reject-" + currentPage);
        navigationRow.add(rejectButton);

        if (currentPage < pageCount) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText(ButtonConst.NEXT);
            nextButton.setCallbackData(routing + "-next-" + (currentPage + 1));
            navigationRow.add(nextButton);
        }

        rowsInline.add(navigationRow);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }


    public ReplyKeyboard downloadButton(String key, Long id) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> navigationRow = new ArrayList<>();

        InlineKeyboardButton downloadButton = new InlineKeyboardButton();
        downloadButton.setText(ButtonConst.DOWNLOAD_CONTENT);
        downloadButton.setCallbackData(key + "-" + id);
        navigationRow.add(downloadButton);

        InlineKeyboardButton rejectButton = new InlineKeyboardButton();
        rejectButton.setText(ButtonConst.REJECT);
        rejectButton.setCallbackData(key + "-reject");
        navigationRow.add(rejectButton);

        rowsInline.add(navigationRow);
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
*/
}
