package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import ru.netology.service.CategoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryManagerTest {
    private static final String CATEGORIES_FILE = "categories.tsv";
    private static final String OTHER_CATEGORY = "другое";
    private static final CategoryManager categoryManager = new CategoryManager(CATEGORIES_FILE, OTHER_CATEGORY);
    private static final String date = "2022.02.08";

    @BeforeEach
    void setUp() {
        categoryManager.addExpense("еда", 100, date);
        categoryManager.addExpense("еда", 200, date);
        categoryManager.addExpense("одежда", 400, date);
    }
    @AfterEach
    void tearDown() {
        categoryManager.clearExpenses();
    }

    @org.junit.jupiter.api.Test
    void getCategory() {
        assertEquals("другое", categoryManager.getCategory("книга"));
    }

    @org.junit.jupiter.api.Test
    void addExpense() {
        categoryManager.addExpense("еда", 500, date);
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