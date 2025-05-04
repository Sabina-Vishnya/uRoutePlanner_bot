package com.example.urouteplanner_bot.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressNormalizerTest {

    private final AddressNormalizer addressNormalizer = new AddressNormalizer();

    @ParameterizedTest
    @MethodSource("provideValidAddresses")
    void shouldNormalizeValidAddresses(String input, String expected) {
        String actual = addressNormalizer.normalizeAddress(input);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> provideValidAddresses() {
        return Stream.of(
            Arguments.of("москва кутуза 32", "москва, кутуза, 32"),
            Arguments.of("Москва, кутуза 32", "Москва, кутуза, 32"),
            Arguments.of("Москва, кутуза, 32", "Москва, кутуза, 32"),
            Arguments.of("Санкт Петербург, Ленина, 1", "Санкт Петербург, Ленина, 1"),
            Arguments.of("Санкт Петербург Ленина 1", "Санкт Петербург, Ленина, 1"),
            Arguments.of("Санкт-Петербург Ленина, 1", "Санкт-Петербург, Ленина, 1")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
        " ",
        "Москва, Красная площадь",
        "3",
        "Москва"
    })
    void isNotValidAddressTest(String value) {
        assertThrows(IllegalArgumentException.class, () -> addressNormalizer.normalizeAddress(value));
    }
}