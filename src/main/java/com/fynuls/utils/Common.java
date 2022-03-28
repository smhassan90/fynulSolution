package com.fynuls.utils;

import com.fynuls.dal.BarChartData;
import com.fynuls.dal.Dataset;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 24th March, 2022
 */

public class Common {
    /*
    ColumnNumber will be the column of number you want to show in the graph. By default it was 3.
    It will start from 0.
     */
    public static BarChartData getBarChartData(String query, int columnNumber, ArrayList<Object> objs){
        List<String> backgroundColors = new ArrayList<>();
        backgroundColors.add("#666EE8");
        backgroundColors.add("#28D094");
        backgroundColors.add("#FF4961");
        backgroundColors.add("#1E9FF2");
        backgroundColors.add("#FF9149");
        backgroundColors.add("#F44336");
        backgroundColors.add("#E91E63");
        backgroundColors.add("#9C27B0");
        backgroundColors.add("#673AB7");
        backgroundColors.add("#3F51B5");
        backgroundColors.add("#2196F3");
        backgroundColors.add("#03A9F4");
        backgroundColors.add("#00BCD4");
        backgroundColors.add("#009688");
        backgroundColors.add("#4CAF50");
        backgroundColors.add("#8BC34A");
        backgroundColors.add("#CDDC39");

        List<String> labels = new ArrayList<>();
        List<String> months = new ArrayList<>();
        BarChartData barChartData = new BarChartData();
        Dataset dataset = new Dataset();
        List<Dataset> datasets =new ArrayList<>();
        List<Long> data = new ArrayList<>();
        int colorLoop = 0;

        if(objs!=null && objs.size()>0) {
            for (int i = 0; i < objs.size(); i++) {
                Object[] temp = (Object[]) objs.get(i);
                if (!labels.contains(temp[2].toString())) {
                    labels.add(temp[2].toString());
                }

                if (!months.contains(temp[1].toString())) {
                    months.add(temp[1].toString());
                }
            }
        }


        if(objs!=null && objs.size()>0){
            for (int i=0; i<months.size() ; i++) {


                dataset = new Dataset();
                Object[] temp = (Object[]) objs.get(i);

                if(colorLoop>=backgroundColors.size())
                    colorLoop=0;
                dataset.setBackgroundColor(backgroundColors.get(colorLoop));
                colorLoop++;
                dataset.setLabel(months.get(i).toString());
                data = new ArrayList<>();
                for(String label : labels){
                    long dataNum = 0;
                    for(int j=0; j<objs.size(); j++){
                        Object[] innerTemp = (Object[]) objs.get(j);

                        if(innerTemp[2].toString().equals(label) && innerTemp[1].toString().equals(months.get(i).toString())){
                            dataNum = (long) Double.parseDouble(innerTemp[columnNumber].toString().trim());
                        }
                    }
                    data.add(dataNum);
                }

                dataset.setData(data);
                datasets.add(dataset);
            }
        }
        barChartData.setDatasets(datasets);
        barChartData.setLabels(labels);
        return barChartData;
    }
}
