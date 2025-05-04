package com.example.urouteplanner_bot.controller;

import com.example.urouteplanner_bot.service.TelegramBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotifyController {
    private final TelegramBotService telegramBotservice;

    @PostMapping("/notify")
    public ResponseEntity<Void> notify(@RequestParam Long chatId, @RequestParam String text) {
        telegramBotservice.sendMessage(chatId, text);
        return ResponseEntity.ok().build();
    }
}
