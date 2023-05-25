package ru.netology;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CategoryManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String otherCategory;
    public static File FILE = new File("data.bin");
    protected ListOfCategories categories = new ListOfCategories();

    public CategoryManager(String categoriesFile, String otherCategory) {
        this.otherCategory = otherCategory;
        try (BufferedReader br = new BufferedReader(new FileReader(categoriesFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    categories.getCategoryMap().put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла категорий");
            e.printStackTrace();
        }
    }

    public void saveData(ListOfCategories categories) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE))) {
            outputStream.writeObject(categories);
            System.out.println("Файл сохранен");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в файл");
            e.printStackTrace();
        }
    }

    public static ListOfCategories loadData() {
        if (!FILE.exists()) {
            // Файл не существует, создаем новый файл
            try {
                FILE.createNewFile();
                return new ListOfCategories();
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла");
                e.printStackTrace();
                return null;
            }
        }
        ListOfCategories categories;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE))) {
            categories = (ListOfCategories) inputStream.readObject();
            System.out.println("Данные из файла загружены");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных из файла");
            e.printStackTrace();
            return null;
        }
        return categories;
    }

    public String getCategory(String title) {
        String category = categories.getCategoryMap().get(title);
        if (category == null) {
            category = otherCategory;
        }
        return category;
    }

    public void addExpense(String category, double sum, String date, ListOfCategories categories) {
        addCurrentExpense(category, sum, this.categories.getExpenseMap(), this.categories.getYearExpenseMap());

        addCurrentExpense(category, sum, this.categories.getMonthExpenseMap(), this.categories.getDayExpenseMap());

        // Преобразование строки даты в объект LocalDate
        LocalDate expenseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        // Обновление значения расходов по году
        YearMonth yearMonth = YearMonth.of(expenseDate.getYear(), expenseDate.getMonthValue());
        String yearKey = category + "-" + expenseDate.getYear();
        double yearSum = this.categories.getYearExpenseMap().getOrDefault(yearKey, 0.0) + sum;
        this.categories.getYearExpenseMap().put(yearKey, yearSum);

        // Обновление значения расходов по месяцу
        String monthKey = category + "-" + yearMonth;
        double monthSum = this.categories.getMonthExpenseMap().getOrDefault(monthKey, 0.0) + sum;
        this.categories.getMonthExpenseMap().put(monthKey, monthSum);

        // Обновление значения расходов по дню
        String dayKey = category + "-" + expenseDate;
        double daySum = this.categories.getDayExpenseMap().getOrDefault(dayKey, 0.0) + sum;
        this.categories.getDayExpenseMap().put(dayKey, daySum);
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
        return getMaxCategoryByExpenseMap(categories.getYearExpenseMap(), year);
    }

    public String getMaxMonthCategory(String date) {
        int month = Integer.parseInt(date.split("\\.")[1]);
        return getMaxCategoryByExpenseMap(categories.getMonthExpenseMap(), month);
    }

    public String getMaxDayCategory(String date) {
        int day = Integer.parseInt(date.split("\\.")[2]);
        return getMaxCategoryByExpenseMap(categories.getDayExpenseMap(), day);
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
        for (Map.Entry<String, Double> entry : categories.getExpenseMap().entrySet()) {
            if (entry.getValue() > maxSum) {
                maxCategory = entry.getKey();
                maxSum = entry.getValue();
            }
        }
        return maxCategory;
    }

    public double getMaxSum() {
        double maxSum = 0.0;
        for (Double value : categories.getExpenseMap().values()) {
            if (value > maxSum) {
                maxSum = value;
            }
        }
        return maxSum;
    }

    public void clearExpenses() {
        categories.getExpenseMap().clear();
    }
}

