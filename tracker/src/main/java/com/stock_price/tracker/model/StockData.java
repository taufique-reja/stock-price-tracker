package com.stock_price.tracker.model;

import java.util.List;

public class StockData {
    private String symbol;
    private String companyName;
    private double price;
    private double change;
    private String changePercent;
    private double high;
    private double low;
    private double[] chart;

    // constructor

    public StockData() {

    }

    public StockData(String symbol, String companyName, double price, double change, String changePercent, double high, double low, double[] chart) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
        this.change = change;
        this.changePercent = changePercent;
        this.high = high;
        this.low = low;
        this.chart = chart;
    }

    //setter and getter

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double[] getChart() {
        return chart;
    }

    public void setChart(double[] chart) {
        this.chart = chart;
    }



}
