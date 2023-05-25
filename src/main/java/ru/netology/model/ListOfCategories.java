package ru.netology.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ListOfCategories implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> categoryMap = new HashMap<>();
    private final Map<String, Double> expenseMap = new HashMap<>();
    private final Map<String, Double> yearExpenseMap = new HashMap<>();
    private final Map<String, Double> monthExpenseMap = new HashMap<>();
    private final Map<String, Double> dayExpenseMap = new HashMap<>();

    public Map<String, String> getCategoryMap() {
        return categoryMap;
    }

    public Map<String, Double> getExpenseMap() {
        return expenseMap;
    }

    public Map<String, Double> getYearExpenseMap() {
        return yearExpenseMap;
    }

    public Map<String, Double> getMonthExpenseMap() {
        return monthExpenseMap;
    }

    public Map<String, Double> getDayExpenseMap() {
        return dayExpenseMap;
    }
}
