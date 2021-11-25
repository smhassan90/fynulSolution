package com.fynuls.dal;

public class ProductPerformance {
    String positionCode;
    String groupOn;
    int TP_SALE_VALUE;
    int target;
    double percentage;

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getGroupOn() {
        return groupOn;
    }

    public void setGroupOn(String groupOn) {
        this.groupOn = groupOn;
    }

    public int getTP_SALE_VALUE() {
        return TP_SALE_VALUE;
    }

    public void setTP_SALE_VALUE(int TP_SALE_VALUE) {
        this.TP_SALE_VALUE = TP_SALE_VALUE;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
