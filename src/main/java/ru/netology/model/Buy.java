package ru.netology.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Buy {
    private final String title;
    private final String date;
    private final double sum;

    public Buy(
            @JsonProperty("title") String title,
            @JsonProperty("date") String date,
            @JsonProperty("sum") double sum
    ) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return "Buy{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", sum=" + sum +
                '}';
    }
}