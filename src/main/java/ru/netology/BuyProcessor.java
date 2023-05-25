package ru.netology;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import java.io.PrintWriter;
import java.io.Serializable;

public class BuyProcessor implements Serializable {
    private static final long serialVersionUID = 1L;
    static final String SUM = "sum";
    static final String CATEGORY = "category";
    private static final ObjectMapper mapper = new ObjectMapper();
    protected ListOfCategories categories;
    protected CategoryManager categoryManager;

    public void process(StringBuilder requestBody, PrintWriter out) throws JsonProcessingException {
        if (categoryManager == null) {
            categoryManager = new CategoryManager();
            categories = categoryManager.loadData();
            if (categories == null) {
                categories = new ListOfCategories();
            }
            categoryManager.saveData(categories);
        }

        System.out.println("Получен запрос: " + requestBody);
        Buy buy = mapper.readValue(requestBody.toString(), Buy.class);

        String title = buy.getTitle().toLowerCase();
        String date = buy.getDate();
        double sum = buy.getSum();

        String category = categoryManager.getCategory(title, categories);
        categoryManager.addExpense(category, sum, date, categories);
        String maxCategory = categoryManager.getMaxCategory(categories);
        double maxSum = categoryManager.getMaxSum(categories);
        String maxYearCategory = categoryManager.getMaxYearCategory(date, categories);
        String maxMonthCategory = categoryManager.getMaxMonthCategory(date, categories);
        String maxDayCategory = categoryManager.getMaxDayCategory(date, categories);


        JsonObject result = new JsonObject();
        JsonObject maxCategoryObject = new JsonObject();
        JsonObject maxYearCategoryObject = new JsonObject();
        JsonObject maxMonthCategoryObject = new JsonObject();
        JsonObject maxDayCategoryObject = new JsonObject();

        maxCategoryObject.addProperty(CATEGORY, maxCategory);
        maxCategoryObject.addProperty(SUM, maxSum);
        result.add("maxCategory", maxCategoryObject);

        maxYearCategoryObject.addProperty(CATEGORY, maxYearCategory.split(": ")[1]);
        maxYearCategoryObject.addProperty(SUM, maxYearCategory.split(": ")[0]);
        result.add("maxYearCategory", maxYearCategoryObject);

        maxMonthCategoryObject.addProperty(CATEGORY, maxMonthCategory.split(": ")[1]);
        maxMonthCategoryObject.addProperty(SUM, maxMonthCategory.split(": ")[0]);
        result.add("maxMonthCategory", maxMonthCategoryObject);

        maxDayCategoryObject.addProperty(CATEGORY, maxDayCategory.split(":")[1]);
        maxDayCategoryObject.addProperty(SUM, maxDayCategory.split(": ")[0]);
        result.add("maxDayCategory", maxDayCategoryObject);

        categoryManager.saveData(categories);

        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: application/json");
        out.println();
        out.println(result);

        System.out.println("Отправлен ответ: " + result);
        System.out.println();
    }
}
