package com.fynuls.dal;

import java.util.List;

public class LineChartData {
    private List<String> labels;
    private List<LineDataset> datasets;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<LineDataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<LineDataset> datasets) {
        this.datasets = datasets;
    }
}
