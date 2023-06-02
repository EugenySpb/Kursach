package ru.netology;

import org.junit.Test;
import ru.netology.service.CategoryManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CategoryManagerTest {
    static final String date = "2022.06.10";
    private static final CategoryManager categoryManager = new CategoryManager();

    @Test
    public void getCategory() {
        assertEquals("еда", categoryManager.getCategory("булка"));
        assertEquals("финансы", categoryManager.getCategory("акции"));
        assertEquals("другое", categoryManager.getCategory("книга"));
    }

    @Test
    public void addExpense() {
        categoryManager.clearExpenses();
        categoryManager.addExpense("еда", 500.0, date);
        categoryManager.addExpense("еда", 200.0, date);
        assertEquals(700, categoryManager.getMaxSum());
        assertEquals("еда", categoryManager.getMaxCategory());
    }

    @Test
    public void getMaxCategory() {
        categoryManager.clearExpenses();
        categoryManager.addExpense("еда", 500.0, date);
        categoryManager.addExpense("еда", 200.0, date);
        assertEquals("еда", categoryManager.getMaxCategory());
    }

    @Test
    public void getMaxSum() {
        categoryManager.clearExpenses();
        categoryManager.addExpense("еда", 500.0, date);
        categoryManager.addExpense("еда", 200.0, date);
        assertEquals(700, categoryManager.getMaxSum());
    }
}