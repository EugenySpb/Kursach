package ru.netology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CategoryManager {
    private final Map<String, String> categoryMap = new HashMap<>();
    private final Map<String, Double> expenseMap = new HashMap<>();
    private final String otherCategory;

    public CategoryManager(String categoriesFile, String otherCategory) {
        this.otherCategory = otherCategory;
        try (BufferedReader br = new BufferedReader(new FileReader(categoriesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    categoryMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла категорий");
            e.printStackTrace();
        }
    }

    public String getCategory(String title) {
        String category = categoryMap.get(title);
        if (category == null) {
            category = otherCategory;
        }
        return category;
    }

    public void addExpense(String category, double sum) {
        Double currentSum = expenseMap.get(category);
        if (currentSum == null) {
            currentSum = 0.0;
        }
        expenseMap.put(category, currentSum + sum);
    }

    public String getMaxCategory() {
        String maxCategory = otherCategory;
        double maxSum = 0.0;
        for (Map.Entry<String, Double> entry : expenseMap.entrySet()) {
            if (entry.getValue() > maxSum) {
                maxCategory = entry.getKey();
                maxSum = entry.getValue();
            }
        }
        return maxCategory;
    }

    public double getMaxSum() {
        double maxSum = 0.0;
        for (Double value : expenseMap.values()) {
            if (value > maxSum) {
                maxSum = value;
            }
        }
        return maxSum;
    }

    public void clearExpenses() {
        expenseMap.clear();
    }
}
