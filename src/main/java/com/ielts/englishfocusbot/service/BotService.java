package com.ielts.englishfocusbot.service;

import com.ielts.englishfocusbot.dto.TranslatorResponseDTO;
import com.ielts.englishfocusbot.entity.*;
import com.ielts.englishfocusbot.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BotService extends TelegramLongPollingBot {


    private final BotConfiguration botConfiguration;
    private final LogicService logicService;
    private final ButtonService buttonService;

    private final ChallengeDayRepository challengeDayRepository;
    private final ChallengeFileRepository challengeFileRepository;
    private final UserRepository userRepository;

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
        String message = getMessage(update);
        String inlineMsg = getInlineMsg(update);
        String step = user.getStep();
        if (!handleMessage(user, message)) {
            if (!handleUserStep(update, user, step)) {
                if (!handleInlineMessage(update, user, inlineMsg)) undefined(user);
            }
        }

    }


    public String getMessage(Update update) {
        String message = "";
        if (update.hasMessage()) {
            Message updateMessage = update.getMessage();
            if (updateMessage.hasText()) {
                message = updateMessage.getText();
            }
        }
        return message;
    }

    public String getInlineMsg(Update update) {
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


    public boolean handleMessage(User user, String message) {
        switch (message) {
            case "/start":
            case ButtonConst.MAIN_MENU:
                updateUserREGISTERED(user);
                start(user);
                return true;
            case ButtonConst.ADD_DAY:
                addChallengeDay(user);
                return true;
            case ButtonConst.ADD_PREMIUM:
                addPremium(user);
                return true;
            case ButtonConst.DONE:
                doneChallenge(user);
                return true;
            case ButtonConst.BUY_CHALLENGE:
                buyChallenge(user);
                return true;
            case ButtonConst.TODAY_CHALLENGE:
            case "/challenge":
                System.out.println("1");
                return true;
            case ButtonConst.CONTACT_ADMIN:
            case "/contact":
                SendMessage sendMessage = getSendMessage(user);
                sendMessage.setText("\uD83C\uDD94 Raqamingiz: " + user.getChatId() + "\n" +
                        "\uD83D\uDC64 Admin: @mirkomil_ablayev ga yozing!");
                sendMessageExecutor(sendMessage);
                return true;
            case ButtonConst.TRANSLATOR:
            case "/translator":
                translatorButtonClick(user);
                return true;
            case ButtonConst.CHANGE_LANGUAGE:
            case ButtonConst.UZBEK:
            case ButtonConst.ENGLISH:
                changeLanguages(user);
            default:
                return false;
        }
    }

    public boolean handleUserStep(Update update, User user, String userStep) {
        switch (userStep) {
            case Steps.ASK_CHALLENGE_FILE:
                saveChallengeFile(update, user);
                return true;
            case Steps.ASK_WORD_TO_TRANSLATE:
                translateWords(update, user);
                return true;
            case Steps.ASK_USER_CHAT_ID_TO_MAKE_PREMIUMM:
                makePremium(update, user);
                return true;
            default:
                return false;
        }
    }

    public boolean handleInlineMessage(Update update, User user, String inlineMsg) {
        String[] split = inlineMsg.split("-");
        switch (split[0]) {
            case "ads":
                System.out.println("1");
                return true;
            case "chooseLanguage":
                System.out.println("2");
                return true;
            default:
                return false;
        }


    }


    private void start(User user) {
        SendMessage sendMessage = getSendMessage(user);
        sendMessage.setText("");
        if (user.getAdmin()) {
            sendMessage.setReplyMarkup(buttonService.adminMainMenu());
            sendMessage.setText("ADMIN MAIN MENU");
            sendMessageExecutor(sendMessage);
        } else {
            sendMessage.setReplyMarkup(buttonService.userMainMenuButton(user));
            String text = user.getPremium() ?
                    "Assalomu alaykum Botga xush kelibsiz! \uD83D\uDC4B,\n" +
                            "Challengeda qatnashish uchun \uD83D\uDC49 /challenge ni bosing \uD83D\uDC51" :
                    "Assalomu alaykum, Bugungi challengeni olish uchun  \uD83D\uDC49 /challenge ni bosing \uD83D\uDC51";
            sendMessage.setText(text);
            sendMessageExecutor(sendMessage);
        }
    }

    // ADMIN

    private void addPremium(User user) {
        SendMessage sendMessage = getSendMessage(user);
        sendMessage.setText("Send id of user!!!");
        sendMessage.setReplyMarkup(buttonService.backMainMenuButton());
        sendMessageExecutor(sendMessage);

        user.setStep(Steps.ASK_USER_CHAT_ID_TO_MAKE_PREMIUMM);
        logicService.updateUser(user);
    }

    private void makePremium(Update update, User user) {
        SendMessage sendMessage = getSendMessage(user);
        String id;
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                id = message.getText();
                if (id.length() > 10) {
                    sendMessage.setText("Please send valid text");
                    sendMessageExecutor(sendMessage);
                    sendMessage.setReplyMarkup(buttonService.adminMainMenu());
                    updateUserREGISTERED(user);
                    return;
                }
            } else {
                sendMessage.setText("Please send valid text");
                sendMessageExecutor(sendMessage);
                sendMessage.setReplyMarkup(buttonService.adminMainMenu());
                updateUserREGISTERED(user);
                return;
            }
        } else {
            sendMessage.setText("Please send valid text");
            sendMessageExecutor(sendMessage);
            sendMessage.setReplyMarkup(buttonService.adminMainMenu());
            updateUserREGISTERED(user);
            return;
        }

        Optional<User> userOptional = userRepository.findByChatId(id);
        if (!userOptional.isPresent()) {
            sendMessage.setText("Please send valid text");
            sendMessageExecutor(sendMessage);
            sendMessage.setReplyMarkup(buttonService.adminMainMenu());
            updateUserREGISTERED(user);
            return;
        }

        User user1 = userOptional.get();
        user1.setPremium(true);
        user1.setChallengeStartDate(currentTashkentDate());
        userRepository.save(user1);

        sendMessage.setText("Siz uchun challengelar ochildi\uD83C\uDF89\uD83C\uDF89");
        sendMessage.setReplyMarkup(buttonService.userMainMenuButton(user1));
        sendMessageExecutor(sendMessage);

        sendMessage.setText("Success");
        sendMessage.setReplyMarkup(buttonService.adminMainMenu());
        sendMessageExecutor(sendMessage);

        updateUserREGISTERED(user);

    }

    private LocalDate currentTashkentDate() {
        return LocalDate.now(ZoneId.of("Asia/Tashkent"));
    }

    private void addChallengeDay(User user) {
        if (!user.getAdmin()) {
            undefined(user);
            return;
        }

        SendMessage sendMessage = getSendMessage(user);
        Long countByDone = challengeDayRepository.countByDone(true);
        if (countByDone == 14L) {
            sendMessage.setText("Siz 14 kunlik challengeni qo'shib bo'ldingiz!!");
            sendMessage.setReplyMarkup(buttonService.adminMainMenu());
            sendMessageExecutor(sendMessage);
            updateUserREGISTERED(user);
            return;
        }

        Optional<ChallengeDay> challengeDayOptional = challengeDayRepository.findByDone(false);
        if (challengeDayOptional.isPresent()) {
            Long fileCount = challengeFileRepository.countByChallengeDayId(challengeDayOptional.get().getId());
            sendMessage.setText("Sizda yopilmagan kun bor, day - " + challengeDayOptional.get().getOrderChallenge() + "! fiyllar soni - " + fileCount + "\nFayl qo'shing yoki yoping!");
        } else {
            List<ChallengeDay> sortedList = challengeDayRepository.findAll()
                    .stream()
                    .sorted(Comparator.comparingInt(ChallengeDay::getOrderChallenge).reversed())
                    .collect(Collectors.toList());

            int order;
            if (sortedList.isEmpty()) order = 1;
            else {
                order = sortedList.get(0).getOrderChallenge() + 1;
            }

            ChallengeDay challengeDay = new ChallengeDay();
            challengeDay.setOrderChallenge(order);
            challengeDay.setDone(false);
            ChallengeDay save = challengeDayRepository.save(challengeDay);

            sendMessage.setText("Day - " + save.getOrderChallenge() + "\n" +
                    "Successfully added\n" +
                    "Fayl yuborishingiz mumkin, yopish uchun DONE ni bosing");
        }
        sendMessage.setReplyMarkup(buttonService.doneChallengeDay());
        sendMessageExecutor(sendMessage);

        user.setStep(Steps.ASK_CHALLENGE_FILE);
        logicService.updateUser(user);

    }

    private void saveChallengeFile(Update update, User user) {
        if (!user.getAdmin()) {
            undefined(user);
            return;
        }
        SendMessage sendMessage = getSendMessage(user);
        Optional<ChallengeDay> challengeDayOptional = challengeDayRepository.findByDone(false);
        if (!challengeDayOptional.isPresent()) {
            sendMessage.setText("Undone Challenge topilmadi!!");
            sendMessage.setReplyMarkup(buttonService.adminMainMenu());
            sendMessageExecutor(sendMessage);
            updateUserREGISTERED(user);
            return;
        }
        FileIdDTO fileId = getFileId(update);
        if (ContentTypeConst.NONE.equals(fileId.getContentType())) {
            sendMessage.setText("Media type is not supported!!");
            sendMessageExecutor(sendMessage);
            return;
        }

        ChallengeFile challengeFile = new ChallengeFile();
        challengeFile.setChallengeDayId(challengeDayOptional.get().getId());
        challengeFile.setFileType(fileId.getContentType());
        challengeFile.setFileId(fileId.getFileId());

        challengeFileRepository.save(challengeFile);
        sendMessage.setText("Successfully added");
        sendMessage.setReplyMarkup(buttonService.doneChallengeDay());
        sendMessageExecutor(sendMessage);
    }

    private void doneChallenge(User user) {
        if (!user.getAdmin()) {
            undefined(user);
            return;
        }
        SendMessage sendMessage = getSendMessage(user);
        Optional<ChallengeDay> challengeDayOptional = challengeDayRepository.findByDone(false);
        if (!challengeDayOptional.isPresent()) {
            sendMessage.setText("Undone Challenge topilmadi!!");
            sendMessage.setReplyMarkup(buttonService.adminMainMenu());
            sendMessageExecutor(sendMessage);
            updateUserREGISTERED(user);
            return;
        }

        ChallengeDay challengeDay = challengeDayOptional.get();
        challengeDay.setDone(true);
        challengeDayRepository.save(challengeDay);
        sendMessage.setText("Challenge Saqlandi!!!");
        sendMessage.setReplyMarkup(buttonService.adminMainMenu());
        sendMessageExecutor(sendMessage);

        updateUserREGISTERED(user);
    }

    // USER

    private void buyChallenge(User user) {
        SendMessage sendMessage = getSendMessage(user);
        sendMessage.setText("\uD83C\uDD94 Raqamingiz: " + user.getChatId() + "\n" +
                "✅ Istalgan narx evaziga!\n" +
                "\uD83D\uDCB8 Minimal: 5 ming so'm\n" +
                "\uD83D\uDCB3 Karta: 9860 0401 0261 7475\n" +
                "\uD83D\uDC64 Admin: @mirkomil_ablayev ga yozing!");
        sendMessage.setReplyMarkup(buttonService.backMainMenuButton());
        sendMessageExecutor(sendMessage);
    }


    public synchronized void translatorButtonClick(User user) {
        SendMessage sendMessage = getSendMessage(user);
        sendMessage.setText(user.getFromLang().equals(Lang.UZBEK) ? "So'zni kiriting!" : "Send text");
        sendMessage.setReplyMarkup(buttonService.googleTranslateButton(user));
        sendMessageExecutor(sendMessage);

        user.setStep(Steps.ASK_WORD_TO_TRANSLATE);
        logicService.updateUser(user);
    }

    public void translateWords(Update update, User user) {
        SendMessage sendMessage = getSendMessage(user);
        String textToTranslate = logicService.getText(update);
        TranslatorResponseDTO translated = translatorService.translate(user.getFromLang().getLan(), user.getToLang().getLan(), textToTranslate);
        String sb = user.getFromLang().getName() + " ➡️ " + user.getToLang().getName() + "\n\n" + "Tarjima: " + translated.getTranslatedText();
        sendMessage.setText(sb);
        sendMessage.setReplyMarkup(buttonService.googleTranslateButton(user));
        sendMessageExecutor(sendMessage);
    }

    public void changeLanguages(User user) {
        SendMessage sendMessage = getSendMessage(user);
        Lang from = user.getFromLang();
        Lang userTo = user.getToLang();
        user.setFromLang(userTo);
        user.setToLang(from);
        user = logicService.updateAndGetUser(user);
        sendMessage.setText("Languages changed::");
        sendMessage.setReplyMarkup(buttonService.googleTranslateButton(user));
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
