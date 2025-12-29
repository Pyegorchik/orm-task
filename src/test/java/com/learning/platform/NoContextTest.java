package com.learning.platform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class NoContextTest {
    
    @Test
    void testSimple() {
        assertTrue(true);
        System.out.println("Test passed without Spring context!");
    }
    
    @Test
    void testLazyLoadingConcept() {
        // Демонстрация концепции без Hibernate
        String message = "Lazy Loading: данные загружаются только при первом обращении";
        assertTrue(message.contains("Lazy Loading"));
    }
}