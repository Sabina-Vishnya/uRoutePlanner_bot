package com.example.urouteplanner_bot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
class AddressNormalizer {
    String normalizeAddress(String address) {
        if (StringUtils.isBlank(address)) {
            throw new IllegalArgumentException("Адрес не должен быть пустым");
        }

        String cleaned = address.trim().replaceAll("[\\s,]+", " ");
        String[] parts = cleaned.split(" ");

        if (parts.length < 3) {
            throw new IllegalArgumentException("Ошибка! Адрес неполный");
        }

        String house = parts[parts.length - 1];
        if (!house.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Ошибка! Номер дома отсутствует");
        }

        String city = String.join(" ", Arrays.copyOfRange(parts, 0, parts.length - 2));
        String street = parts[parts.length - 2];

        return String.format("%s, %s, %s", city, street, house);
    }
}
