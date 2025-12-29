package com.learning.platform;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ContextLoadTest {
    
    @Test
    void contextLoads() {
        // Просто проверяем, что контекст загружается
        assertTrue(true);
    }
}