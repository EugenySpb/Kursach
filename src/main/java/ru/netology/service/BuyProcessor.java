package ru.netology.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import ru.netology.model.Buy;

import java.io.PrintWriter;
import java.io.Serializable;

public class BuyProcessor implements Serializable {
    private static final long serialVersionUID = 1L;
    static final String SUM = "sum";
    static final String CATEGORY = "category";
    private final ObjectMapper mapper = new ObjectMapper();
    protected CategoryManager categoryManager = new CategoryManager();

    public String process(StringBuilder requestBody) throws JsonProcessingException {
        System.out.println("Request received: " + requestBody);
        Buy buy = mapper.readValue(requestBody.toString(), Buy.class);

        String title = buy.getTitle().toLowerCase();
        String date = buy.getDate();
        double sum = buy.getSum();

        String category = categoryManager.getCategory(title);
        categoryManager.addExpense(category, sum, date);
        String maxCategory = categoryManager.getMaxCategory();
        double maxSum = categoryManager.getMaxSum();
        String maxYearCategory = categoryManager.getMaxYearCategory(date);
        String maxMonthCategory = categoryManager.getMaxMonthCategory(date);
        String maxDayCategory = categoryManager.getMaxDayCategory(date);


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

        categoryManager.saveData();

        System.out.println("Response sent: " + result);
        System.out.println();

        return result.toString();
    }
}
