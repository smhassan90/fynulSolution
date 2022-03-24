package com.fynuls.controllers.greensales;

import com.fynuls.dal.BarChartData;
import com.fynuls.dal.Dataset;
import com.fynuls.utils.Common;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 24th March, 2022
 */

@Controller
public class PerformanceExecutive {
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getExecutiveBarChart", method = RequestMethod.GET, params = {"token","type"})
    @ResponseBody
    public String getExecutiveBarChart(String token, String type) {
        String query = "";

        if(type.equals("monthlyBarChart")){
            query = "SELECT * FROM base_monthly_team_target tt\n" +
                    "INNER JOIN base_monthly_team_ach ta on ta.TEAM = tt.TEAM\n" +
                    "INNER JOIN base_team_dept td on td.NAME = tt.TEAM\n" +
                    "WHERE tt.MONTH = '2022-03-01' and ta.REPORTINGMONTH = 'March,2022'";
        }
        query = "SELECT * FROM base_monthly_team_target tt\n" +
                "INNER JOIN base_monthly_team_ach ta on ta.TEAM = tt.TEAM\n" +
                "INNER JOIN base_team_dept td on td.NAME = tt.TEAM\n" +
                "WHERE tt.MONTH = '2022-03-01' and ta.REPORTINGMONTH = 'March,2022'";
        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
        List<String> labels = new ArrayList<>();
        List<Long> ach = new ArrayList<>();
        List<Long> target = new ArrayList<>();
        Dataset datasetTarget = new Dataset();
        Dataset datasetAch = new Dataset();

        if(objs!=null){
            for(Object obj : objs){
                if(obj!=null){
                    Object[] objArray = (Object[]) obj;
                    labels.add(objArray[14].toString());
                }
            }
        }

        if(objs!=null){
            for(Object obj : objs){
                datasetAch = new Dataset();
                if(obj!=null){
                    Object[] arr = (Object[]) obj;
                    target.add(new BigDecimal(arr[4].toString()).longValue());
                    ach.add(new BigDecimal(arr[10].toString()).longValue());
                }

            }
        }
        datasetTarget.setData(target);
        datasetTarget.setBackgroundColor("#FF4961");
        datasetTarget.setLabel("Target");

        datasetAch.setLabel("Achievement");
        datasetAch.setBackgroundColor("#00BCD4");
        datasetAch.setData(ach);

        List<Dataset> datasets = new ArrayList<>();
        datasets.add(datasetTarget);
        datasets.add(datasetAch);

        BarChartData barChartData = new BarChartData();
        barChartData.setLabels(labels);
        barChartData.setDatasets(datasets);

        return new Gson().toJson(barChartData);
    }
}
