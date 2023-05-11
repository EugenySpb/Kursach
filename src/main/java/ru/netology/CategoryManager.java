package ru.netology;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CategoryManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> categoryMap = new HashMap<>();
    private final Map<String, Double> expenseMap = new HashMap<>();
    private final Map<String, Double> yearExpenseMap = new HashMap<>();
    private final Map<String, Double> monthExpenseMap = new HashMap<>();
    private final Map<String, Double> dayExpenseMap = new HashMap<>();
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

    public void saveData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("data.bin"))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в файл");
            e.printStackTrace();
        }
    }

    public static CategoryManager loadData() {
        CategoryManager categoryManager = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("data.bin"))) {
            categoryManager = (CategoryManager) inputStream.readObject();
            System.out.println("Данные из файла загружены");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных из файла");
            e.printStackTrace();
        }
        return categoryManager;
    }

    public String getCategory(String title) {
        String category = categoryMap.get(title);
        if (category == null) {
            category = otherCategory;
        }
        return category;
    }

    public void addExpense(String category, double sum, String date) {
        addCurrentExpense(category, sum, expenseMap, yearExpenseMap);

        addCurrentExpense(category, sum, monthExpenseMap, dayExpenseMap);

        // Преобразование строки даты в объект LocalDate
        LocalDate expenseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        // Обновление значения расходов по году
        YearMonth yearMonth = YearMonth.of(expenseDate.getYear(), expenseDate.getMonthValue());
        String yearKey = category + "-" + expenseDate.getYear();
        double yearSum = yearExpenseMap.getOrDefault(yearKey, 0.0) + sum;
        yearExpenseMap.put(yearKey, yearSum);

        // Обновление значения расходов по месяцу
        String monthKey = category + "-" + yearMonth;
        double monthSum = monthExpenseMap.getOrDefault(monthKey, 0.0) + sum;
        monthExpenseMap.put(monthKey, monthSum);

        // Обновление значения расходов по дню
        String dayKey = category + "-" + expenseDate;
        double daySum = dayExpenseMap.getOrDefault(dayKey, 0.0) + sum;
        dayExpenseMap.put(dayKey, daySum);
    }

    private void addCurrentExpense(String category, double sum, Map<String, Double> expenseMap, Map<String, Double> yearExpenseMap) {
        Double currentExpense = expenseMap.get(category);
        if (currentExpense == null) {
            currentExpense = 0.0;
        }
        currentExpense += sum;
        expenseMap.put(category, currentExpense);

        Double currentYearExpense = yearExpenseMap.get(category);
        if (currentYearExpense == null) {
            currentYearExpense = 0.0;
        }
        currentYearExpense += sum;
        yearExpenseMap.put(category, currentYearExpense);
    }

    public String getMaxYearCategory(String date) {
        int year = Integer.parseInt(date.split("\\.")[0]);
        return getMaxCategoryByExpenseMap(yearExpenseMap, year);
    }

    public String getMaxMonthCategory(String date) {
        int month = Integer.parseInt(date.split("\\.")[1]);
        return getMaxCategoryByExpenseMap(monthExpenseMap, month);
    }

    public String getMaxDayCategory(String date) {
        int day = Integer.parseInt(date.split("\\.")[2]);
        return getMaxCategoryByExpenseMap(dayExpenseMap, day);
    }

    private String getMaxCategoryByExpenseMap(Map<String, Double> expenseMap, int filter) {
        String maxCategory = otherCategory;
        double maxSum = 0.0;

        for (Map.Entry<String, Double> entry : expenseMap.entrySet()) {
            String[] parts = entry.getKey().split("-");
            if (parts.length < 2) {
                continue;
            }
            String category = parts[0];
            int entryFilter = Integer.parseInt(parts[parts.length - 1]);

            if (entryFilter == filter) {
                double sum = entry.getValue();
                if (sum > maxSum) {
                    maxCategory = category;
                    maxSum = sum;
                }
            }
        }
        return maxSum + ": " + maxCategory;
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

