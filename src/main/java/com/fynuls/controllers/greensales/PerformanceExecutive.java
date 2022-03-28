package com.fynuls.controllers.greensales;

import com.fynuls.dal.BarChartData;
import com.fynuls.dal.Dataset;
import com.fynuls.dal.Graph;
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
import java.util.Arrays;
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
        BarChartData barChartData = null;
        if(type.equals("monthlyBarChart")){
            query = "SELECT * FROM base_monthly_team_target tt\n" +
                    "INNER JOIN base_monthly_team_ach ta on ta.TEAM = tt.TEAM\n" +
                    "INNER JOIN base_team_dept td on td.NAME = tt.TEAM\n" +
                    "WHERE tt.MONTH = '2022-03-01' and ta.REPORTINGMONTH = 'March,2022'";
            barChartData = getBarChart(query);
        }else if(type.equals("yearlyBarChart")){
            query = "SELECT * FROM base_monthly_team_target tt\n" +
                    "INNER JOIN base_monthly_team_ach ta on ta.TEAM = tt.TEAM\n" +
                    "INNER JOIN base_team_dept td on td.NAME = tt.TEAM\n" +
                    "WHERE tt.MONTH = '2022-03-01' and ta.REPORTINGMONTH = 'March,2022'";
            barChartData = getBarChart(query);
        }else if(type.equals("GSM")){
            barChartData = getGSMBarChart();
        }


        return new Gson().toJson(barChartData);
    }

    private BarChartData getGSMBarChart() {
        String targetQuery = "SELECT ROUND(SUM(TGT_NET_VALUE),0) TARGET FROM BASE_TARGET where month between '2021-07-01' and '2022-03-01'";
        String achQuery = "SELECT ROUND(SUM(NET_SALE_VALUE),0) AS ACH from SALE_DETAIL_TEMP WHERE TRANSACTION_DATE BETWEEN   '2021-07-01' and '2022-06-30'";
        long target = new BigDecimal(HibernateUtil.getSingleString(targetQuery)).longValue();
        long achievement = new BigDecimal(HibernateUtil.getSingleString(achQuery)).longValue();
        List<Long> dataTarget = new ArrayList<>();
        dataTarget.add(target);

        List<Long> dataAchievement = new ArrayList<>();
        dataAchievement.add(achievement);

        List<Dataset> datasetList = new ArrayList<>();
        Dataset datasetTarget = new Dataset();
        datasetTarget.setBackgroundColor("#cccccc");
        datasetTarget.setLabel("Target");
        datasetTarget.setData(dataTarget);

        Dataset datasetAchievement = new Dataset();
        datasetAchievement.setBackgroundColor("#ff4444");
        datasetAchievement.setLabel("Achievement");
        datasetAchievement.setData(dataAchievement);

        datasetList.add(datasetTarget);
        datasetList.add(datasetAchievement);

        BarChartData barChartData = new BarChartData();
        barChartData.setLabels(new ArrayList<>(Arrays.asList("GSM")));
        barChartData.setDatasets(datasetList);
        return  barChartData;
    }

    private BarChartData getBarChart(String query) {
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
        barChartData.setDatasets(datasets);
        barChartData.setLabels(labels);

        return barChartData;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getExecutiveLineChart", method = RequestMethod.GET, params = {"token","type"})
    @ResponseBody
    public String getExecutiveLineChart(String token, String type) {
        Graph graph = null;
        List<Graph> graphs = new ArrayList<>();
        BarChartData barChartData = null;
                String query = "SELECT DATE_FORMAT(TRANSACTION_DATE, '%M-%y'),td.ALIAS as 'TEAM', reportingmonth, ROUND(SUM(E_QTY),0) AS 'E_QTY', ROUND(SUM(TP_SALE_VALUE),0) AS 'TP_SALE_VALUE', ROUND(SUM(NET_SALE_VALUE),0) AS 'NET_SALE_VALUE' FROM `sale_detail_temp` sdt INNER JOIN base_team_dept td on td.NAME = sdt.TEAM WHERE TRANSACTION_DATE between '2021-07-01' and '2022-06-30' group by reportingmonth, TEAM, td.ALIAS order by TRANSACTION_DATE desc";
        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
                // E_QTY is at the 4 column so we are giving columnNumber 3.
        barChartData = Common.getBarChartData(query,3, objs);
        graph=new Graph();
        graph.setUnitType(Codes.E_QTY);
        graph.setBarChartData(barChartData);
        graphs.add(graph);

        barChartData = Common.getBarChartData(query,4, objs);
        graph=new Graph();
        graph.setUnitType(Codes.TP_SALE_VALUE);
        graph.setBarChartData(barChartData);
        graphs.add(graph);

        barChartData = Common.getBarChartData(query,5, objs);
        graph=new Graph();
        graph.setUnitType(Codes.NET_SALE_VALUE);
        graph.setBarChartData(barChartData);
        graphs.add(graph);

        return new Gson().toJson(graphs);
    }

}
