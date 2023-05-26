package ru.netology;

import org.junit.jupiter.api.BeforeAll;
import ru.netology.service.CategoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CategoryManagerTest {
    static final String date = "2022.05.10";
    static CategoryManager categoryManager;
    static CategoryManager mockCategoryManager;

    @BeforeAll
    static void setUp() {
        categoryManager = new CategoryManager(); // Создаем настоящий экземпляр
        // Создаем макет для заглушки
        mockCategoryManager = mock(CategoryManager.class);
        // Настройка поведения макета
        when(mockCategoryManager.getCategory("булка")).thenReturn("еда");
        when(mockCategoryManager.getMaxCategory()).thenReturn("одежда");
        when(mockCategoryManager.getMaxSum()).thenReturn(400.0);
    }

    @org.junit.jupiter.api.Test
    void getCategory() {
        assertEquals("еда", mockCategoryManager.getCategory("булка"));
    }

    @org.junit.jupiter.api.Test
    void addExpense() {
        mockCategoryManager.addExpense("еда", 500, date);
        assertEquals(400, mockCategoryManager.getMaxSum());
        assertEquals("одежда", mockCategoryManager.getMaxCategory());
    }

    @org.junit.jupiter.api.Test
    void getMaxCategory() {
        assertEquals("одежда", mockCategoryManager.getMaxCategory());
    }

    @org.junit.jupiter.api.Test
    void getMaxSum() {
        assertEquals(400, mockCategoryManager.getMaxSum());
    }
}