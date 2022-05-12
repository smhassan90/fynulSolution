package com.fynuls.dal;

import java.util.List;

public class Dataset {
    private String label;
    private String backgroundColor;
    private String borderColor;
    List<Long> data;

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<Long> getData() {
        return data;
    }


    public void setData(List<Long> data) {
        this.data = data;
    }
}
