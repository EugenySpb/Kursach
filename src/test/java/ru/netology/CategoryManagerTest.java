package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryManagerTest {
    private static final String CATEGORIES_FILE = "categories.tsv";
    private static final String OTHER_CATEGORY = "другое";
    private static final CategoryManager categoryManager = new CategoryManager(CATEGORIES_FILE, OTHER_CATEGORY);

    @BeforeEach
    void setUp() {
        categoryManager.addExpense("еда", 100);
        categoryManager.addExpense("еда", 200);
        categoryManager.addExpense("одежда", 400);
    }
    @AfterEach
    void tearDown() {
        categoryManager.clearExpenses();
    }

    @Test
    void getCategory() {
        assertEquals("еда", categoryManager.getCategory("булка"));
        assertEquals("финансы", categoryManager.getCategory("акции"));
        assertEquals("другое", categoryManager.getCategory("книга"));
    }

    @org.junit.jupiter.api.Test
    void addExpense() {
        categoryManager.addExpense("еда", 500);
        assertEquals(800, categoryManager.getMaxSum());
        assertEquals("еда", categoryManager.getMaxCategory());
    }

    @org.junit.jupiter.api.Test
    void getMaxCategory() {
        assertEquals("одежда", categoryManager.getMaxCategory());
    }

    @org.junit.jupiter.api.Test
    void getMaxSum() {
        assertEquals(400, categoryManager.getMaxSum());
    }
}