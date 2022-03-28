package com.fynuls.dal;

/**
 * @author Syed Muhammad Hassan
 * 9th May, 2019
 */
public class Graph {
    public String unitType;
    public BarChartData barChartData;

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public BarChartData getBarChartData() {
        return barChartData;
    }

    public void setBarChartData(BarChartData barChartData) {
        this.barChartData = barChartData;
    }
}
