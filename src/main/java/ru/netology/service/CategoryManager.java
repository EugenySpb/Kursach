package ru.netology.service;

import ru.netology.model.ListOfCategories;

import java.io.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CategoryManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String OTHER_CATEGORY = "другое";
    public static File FILE = new File("data.bin");
    static final String CATEGORIES_FILE = "categories.tsv";

//    protected ListOfCategories categories = new ListOfCategories();


    public void saveData(ListOfCategories categories) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FILE))) {
            outputStream.writeObject(categories);
            System.out.println("Файл сохранен");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в файл");
            e.printStackTrace();
        }
    }

    public ListOfCategories loadData() {
        if (!FILE.exists()) {
            // Файл не существует, создаем новый файл
            try {
                FILE.createNewFile();
                ListOfCategories categories = new ListOfCategories();
                loadCategory(categories);
                return categories;
            } catch (IOException e) {
                System.out.println("Ошибка при создании файла");
                e.printStackTrace();
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
        loadCategory(categories);
        return categories;
    }

    private static void loadCategory(ListOfCategories categories) {
        try (BufferedReader br = new BufferedReader(new FileReader(CATEGORIES_FILE))) {
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

    public String getCategory(String title, ListOfCategories categories) {
        String category = categories.getCategoryMap().get(title);
        if (category == null) {
            category = OTHER_CATEGORY;
        }
        return category;
    }

    public void addExpense(String category, double sum, String date, ListOfCategories categories) {
        addCurrentExpense(category, sum, categories.getExpenseMap(), categories.getYearExpenseMap());

        addCurrentExpense(category, sum, categories.getMonthExpenseMap(), categories.getDayExpenseMap());

        // Преобразование строки даты в объект LocalDate
        LocalDate expenseDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        // Обновление значения расходов по году
        YearMonth yearMonth = YearMonth.of(expenseDate.getYear(), expenseDate.getMonthValue());
        String yearKey = category + "-" + expenseDate.getYear();
        double yearSum = categories.getYearExpenseMap().getOrDefault(yearKey, 0.0) + sum;
        categories.getYearExpenseMap().put(yearKey, yearSum);

        // Обновление значения расходов по месяцу
        String monthKey = category + "-" + yearMonth;
        double monthSum = categories.getMonthExpenseMap().getOrDefault(monthKey, 0.0) + sum;
        categories.getMonthExpenseMap().put(monthKey, monthSum);

        // Обновление значения расходов по дню
        String dayKey = category + "-" + expenseDate;
        double daySum = categories.getDayExpenseMap().getOrDefault(dayKey, 0.0) + sum;
        categories.getDayExpenseMap().put(dayKey, daySum);
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

    public String getMaxYearCategory(String date, ListOfCategories categories) {
        int year = Integer.parseInt(date.split("\\.")[0]);
        return getMaxCategoryByExpenseMap(categories.getYearExpenseMap(), year);
    }

    public String getMaxMonthCategory(String date, ListOfCategories categories) {
        int month = Integer.parseInt(date.split("\\.")[1]);
        return getMaxCategoryByExpenseMap(categories.getMonthExpenseMap(), month);
    }

    public String getMaxDayCategory(String date, ListOfCategories categories) {
        int day = Integer.parseInt(date.split("\\.")[2]);
        return getMaxCategoryByExpenseMap(categories.getDayExpenseMap(), day);
    }

    private String getMaxCategoryByExpenseMap(Map<String, Double> expenseMap, int filter) {
        String maxCategory = OTHER_CATEGORY;
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

    public String getMaxCategory(ListOfCategories categories) {
        String maxCategory = OTHER_CATEGORY;
        double maxSum = 0.0;
        for (Map.Entry<String, Double> entry : categories.getExpenseMap().entrySet()) {
            if (entry.getValue() > maxSum) {
                maxCategory = entry.getKey();
                maxSum = entry.getValue();
            }
        }
        return maxCategory;
    }

    public double getMaxSum(ListOfCategories categories) {
        double maxSum = 0.0;
        for (Double value : categories.getExpenseMap().values()) {
            if (value > maxSum) {
                maxSum = value;
            }
        }
        return maxSum;
    }

    public void clearExpenses(ListOfCategories categories) {
        categories.getExpenseMap().clear();
    }
}

