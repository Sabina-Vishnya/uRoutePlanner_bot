package com.example.urouteplanner_bot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component
class ChatValidator {

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^[\\p{L}\\d\\s,.-]+$");
    private static final String TIME_REGEX = "^(2[0-3]|[01]?[0-9]):([0-5][0-9])$";


    boolean isValidAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return false;
        }
        String[] parts = address.trim().split("[,\\s]+");

        if (parts.length < 3 || !ADDRESS_PATTERN.matcher(address).matches()) {
            return false;
        }
        return Arrays.stream(parts).anyMatch(part -> part.matches(".*\\d+.*"));
    }

    boolean isValidTime(String time) {
        return StringUtils.isNoneBlank(time) && time.matches(TIME_REGEX);
    }
}
