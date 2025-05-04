package com.example.urouteplanner_bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ChatValidatorTest {

    private ChatValidator chatValidator;

    @BeforeEach
    void setUp() {
        chatValidator = new ChatValidator();
    }

    @Nested
    class IsValidAddressTest {

        @ParameterizedTest
        @ValueSource(strings = {
            "Москва, Красная площадь 5",
            "Москва Красная площадь, 522",
            "москва красная площадь, 512",
        })
        void isValidAddressTest(String value) {
            assertTrue(chatValidator.isValidAddress(value));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {
            " ",
            "Москва, Красная площадь",
            "3",
            "Москва"
        })
        void isNotValidAddressTest(String value) {
            assertFalse(chatValidator.isValidAddress(value));
        }
    }

    @Nested
    class IsValidTimeTest {

        @ParameterizedTest
        @ValueSource(strings = {"09:30", "9:30"})
        void isValidTimeTest(String value) {
            assertTrue(chatValidator.isValidTime(value));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "24:01", "09:66", "900"})
        void isNotValidTimeTest(String value) {
            assertFalse(chatValidator.isValidTime(value));
        }
    }
}