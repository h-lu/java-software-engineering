package com.week01;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarterSmokeTest {

    @Test
    void formatCardPlaceholderCompiles() {
        String card = BusinessCard.formatCard("张三", "软件工程师", "zhangsan@example.com", 20, 1.5);

        assertTrue(card.contains("TODO"));
    }

    @Test
    void entryPointClassesAreLoadable() {
        assertDoesNotThrow(() -> Class.forName("com.week01.HelloWorld"));
        assertDoesNotThrow(() -> Class.forName("com.week01.VariableDemo"));
        assertDoesNotThrow(() -> Class.forName("com.week01.ScannerDemo"));
    }
}
