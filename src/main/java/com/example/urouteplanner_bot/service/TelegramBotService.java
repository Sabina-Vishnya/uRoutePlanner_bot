package com.example.urouteplanner_bot.service;

import com.example.urouteplanner_bot.client.URoutePlannerClient;
import com.example.urouteplanner_bot.config.BotConfig;
import com.example.urouteplanner_bot.model.ChatState;
import com.example.urouteplanner_bot.model.UserSetting;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

//todo  wtf
@Component
@AllArgsConstructor
public class TelegramBotService extends TelegramLongPollingBot {

    public static final String INCORRECT_ADDRESS_FORMAT_MESSAGE = "Некорректный формат адреса!" +
                                                                  "\nПожалуйста, введи адрес в формате (город, улица, дом):";
    private final BotConfig botConfig;
    private final URoutePlannerClient uRoutePlannerClient;
    private final ChatValidator chatValidator;
    private final AddressNormalizer addressNormalizer;
    private final Map<Long, ChatState> userChatStates = new HashMap<>();
    private final Map<Long, UserSetting> userSettings = new HashMap<>();

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
        var message = update.getMessage();
        if (update.hasMessage() && message.hasText()) {
            String text = message.getText();
            long chatId = message.getChatId();
            var user = message.getFrom();

            var dto = userSettings.getOrDefault(chatId, new UserSetting());
            dto.setChatId(chatId);
            dto.setUserId(user.getId());
            dto.setUsername(user.getUserName());

            var currentChatState = userChatStates.getOrDefault(chatId, ChatState.START);

            if ("/start".equals(text)) {
                if (userSettings.containsKey(chatId)) {
                    sendMessage(chatId, "У тебя уже есть настройки! Я пока не умею их обновлять.");
                    return;
                }

                sendMessage(chatId, "Привет! Я буду помогать тебе вовремя добираться на работу."
                                    + "\nВведи домашшний адрес (например: Москва, Кутузовский проспект, 32):");
                userChatStates.put(chatId, ChatState.AWAIT_ADDRESS_A);
                return;
            }

            switch (currentChatState) {
                case AWAIT_ADDRESS_A -> processAddressA(chatId, dto, text);
                case AWAIT_ADDRESS_B -> processAddressB(chatId, dto, text);
                case AWAIT_TIME -> processTime(chatId, dto, text);
            }
        }
    }

    private void processAddressA(long chatId, UserSetting dto, String text) {
        if (chatValidator.isValidAddress(text)) {
            String normalizeAddress = addressNormalizer.normalizeAddress(text);
            dto.setPointA(normalizeAddress);
            sendMessage(chatId, "Теперь введи адрес работы (например: Москва, Ленинский проспект, 15):");
            userChatStates.put(chatId, ChatState.AWAIT_ADDRESS_B);
            userSettings.put(chatId, dto);
        } else {
            sendMessage(chatId, INCORRECT_ADDRESS_FORMAT_MESSAGE);
        }
    }

    private void processAddressB(long chatId, UserSetting dto, String text) {
        if (chatValidator.isValidAddress(text)) {
            String normalizeAddress = addressNormalizer.normalizeAddress(text);
            dto.setPointB(normalizeAddress);
            sendMessage(chatId, "Укажи время прибытия на работу (например: 09:00):");
            userChatStates.put(chatId, ChatState.AWAIT_TIME);
            userSettings.put(chatId, dto);
        } else {
            sendMessage(chatId, INCORRECT_ADDRESS_FORMAT_MESSAGE);
        }
    }

    private void processTime(long chatId, UserSetting dto, String text) {
        if (chatValidator.isValidTime(text)) {
            trySaveUserSetting(chatId, dto, text);
        } else {
            sendMessage(chatId, "Некорректный формат времени!" +
                                "\n Пожалуйста, введи время в формате (HH:MM):");
        }
    }

    private void trySaveUserSetting(long chatId, UserSetting dto, String text) {
        try {
            dto.setPointTime(text);
            userSettings.put(chatId, dto);
            userChatStates.put(chatId, ChatState.END);
            uRoutePlannerClient.processUserSetting(dto);
            sendMessage(chatId, "Спасибо, твои настройки сохранены! Буду уведомлять тебя по будням.");
        } catch (Exception e) {
            sendMessage(chatId, "Ошибка сохранения =/ Сервис временно недоступен, попробуй позже");
            userSettings.remove(chatId);
            userChatStates.remove(chatId);
        }
    }

    public void sendMessage(long chatId, String text) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}