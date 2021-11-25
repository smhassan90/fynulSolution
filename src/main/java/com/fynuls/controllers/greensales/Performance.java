package com.fynuls.controllers.greensales;

import com.fynuls.dal.*;
import com.fynuls.entity.base.Employee;
import com.fynuls.entity.base.EmployeeIDPositionIDMapping;
import com.fynuls.entity.base.EmployeeReportToMapping;
import com.fynuls.entity.login.LoginStatus;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 10th November, 2021
 */

@Controller
public class Performance {
    private LoginStatus getLoginStatus(String token){
        LoginStatus loginStatus = null;
        String query = "from LoginStatus where token='" + token + "'";
        List<LoginStatus> loginStatusList = (List<LoginStatus>) HibernateUtil.getDBObjects(query);

        if (loginStatusList != null && loginStatusList.size() > 0) {
            loginStatus = loginStatusList.get(0);
        }
        return loginStatus;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getPartners", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public String getPartners(String token){
        List<KeyValue> keyValueList = new ArrayList<>();
        DropDownResponse dropDownResponse = new DropDownResponse();
        try {

            String username = "";
            LoginStatus loginStatus = getLoginStatus(token);
            if(loginStatus!=null){
                username = loginStatus.getUsername();
                List<EmployeeIDPositionIDMapping> employeeIDPositionIDMappings = (List<EmployeeIDPositionIDMapping>) HibernateUtil.getDBObjects("from EmployeeIDPositionIDMapping where EMPLOYEE_ID ='" + username + "'");
                if (employeeIDPositionIDMappings != null && employeeIDPositionIDMappings.size() > 0) {
                    String positionCode = "";
                    positionCode = employeeIDPositionIDMappings.get(0).getPOSITION_ID();
                    keyValueList = getTaggedPositionCodes(positionCode);
                }
            }


            dropDownResponse.setStatus(Codes.ALL_OK);
        }catch(Exception e){
            dropDownResponse.setStatus(Codes.SOMETHING_WENT_WRONG);
        }

        dropDownResponse.setKeyValueList(keyValueList);

        return new Gson().toJson(dropDownResponse);
    }

    private List<KeyValue> getTaggedPositionCodes(String positionCode) {
        List<KeyValue> keyValueList = new ArrayList<>();
        List<EmployeeReportToMapping> employeeReportToMappings = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where POSITION_ID ='" + positionCode + "'");
        //Create Dal which has employee details with positionCode
        if (employeeReportToMappings != null && employeeReportToMappings.size() > 0) {
            KeyValue keyValueTemp = new KeyValue();
            keyValueTemp.setKey("");
            keyValueTemp.setValue("All");
            keyValueList.add(keyValueTemp);
            for (EmployeeReportToMapping employeeReportToMapping : employeeReportToMappings) {
                String partnerPositionCode = employeeReportToMapping.getREPORTTO_ID();
                KeyValue keyValue = new KeyValue();
                List<EmployeeIDPositionIDMapping> partnersIds = (List<EmployeeIDPositionIDMapping>) HibernateUtil.getDBObjects("from EmployeeIDPositionIDMapping where POSITION_ID ='" + partnerPositionCode + "'");
                if (partnersIds != null && partnersIds.size() > 0) {

                    for (EmployeeIDPositionIDMapping partnerId : partnersIds) {
                        String empID = partnerId.getEMPLOYEE_ID();
                        List<Employee> employees = (List<Employee>) HibernateUtil.getDBObjects("from Employee where ID='" + empID + "'");
                        if (employees != null && employees.size() > 0) {
                            Employee emp = employees.get(0);
                            keyValue = new KeyValue();
                            keyValue.setKey(partnerPositionCode);
                            keyValue.setValue(emp.getNAME());
                            keyValueList.add(keyValue);
                        }
                    }
                }
            }
        }
        return keyValueList;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getProductPerformance", method = RequestMethod.GET,params={"token","position_code", "month", "products"})
    @ResponseBody
    public String getProductPerformance(String token, String position_code, String month, String products){
        List<ProductPerformance> productPerformances = new ArrayList<>();
        ProductPerformance productPerformance = new ProductPerformance();
        String tempMonth="AUG-21";
        String whereClause="";
        if(!month.equals("")){
            whereClause += " sdt.reportingmonth = '"+month+"'\n";
        }
        if(!position_code.equals("")){
            if(!whereClause.equals("")){
                whereClause +=" and ";
            }
            whereClause += " sdt.position_code = '"+position_code+"' ";
        }
        if(!products.equals("")){
            if(!whereClause.equals("")){
                whereClause +=" and ";
            }
            whereClause += " sdt.groupon = '"+products+"' ";
        }
        if(whereClause.equals("")){
            whereClause = " 1=1 ";
        }

        String query = "SELECT sdt.groupon, SUM(sdt.TP_SALE_VALUE), SUM(t.target), ROUND((SUM(sdt.TP_SALE_VALUE)/SUM( t.target))*100,2)  FROM SALE_DETAIL_TEMP  SDT\n" +
                "INNER JOIN targets T ON t.position_id = sdt.position_code AND t.group_on_id = sdt.groupon\n" +
                "WHERE 1=1 and " + whereClause+
                "GROUP BY sdt.groupon";

        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQueryOracle(query);
        if(objs!=null){
            for(Object obj : objs){
                Object[] temp = (Object[]) obj;
                productPerformance = new ProductPerformance();
                productPerformance.setGroupOn(temp[0].toString());
                productPerformance.setTP_SALE_VALUE(Integer.valueOf(temp[1].toString()));
                productPerformance.setTarget(Integer.valueOf(temp[2].toString()));
                productPerformance.setPercentage(Double.valueOf(temp[3].toString()));

                productPerformances.add(productPerformance);
            }
        }

        return new Gson().toJson(productPerformances);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getReportingMonths", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public String getReportingMonths(String token){
    
        DropDownResponse dropDownResponse = new DropDownResponse();

        KeyValue keyValue = new KeyValue();
        List<KeyValue> keyValueList = new ArrayList<>();

        String query = "SELECT distinct reportingmonth FROM SALE_DETAIL_TEMP WHERE reportingmonth is not null";
        dropDownResponse = getStringDropdownData(query);

        return new Gson().toJson(dropDownResponse   );
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getProducts", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public String getProducts(String token){

        List<KeyValue> taggedList = new ArrayList<>();
        LoginStatus loginStatus = getLoginStatus(token);
        DropDownResponse dropDownResponse = new DropDownResponse();
        if(loginStatus!=null){
            taggedList = getTaggedPositionCodes(loginStatus.getPOSITION_CODE());
            String innerQuery="";
            if(taggedList!=null && taggedList.size()>0){
                boolean isFirst = true;
                innerQuery = " WHERE POSITION_CODE IN ( ";
                for(KeyValue keyValueTemp : taggedList){
                    if(!isFirst){
                        innerQuery += ",";
                    }
                    innerQuery += "'"+ keyValueTemp.getKey()+"'";
                    isFirst = false;
                }
                innerQuery += ")";
            }
            String query = "SELECT distinct groupOn FROM SALE_DETAIL_TEMP  "+innerQuery + " ORDER BY groupOn ASC";
            dropDownResponse = getStringDropdownData(query);

        }

        return new Gson().toJson(dropDownResponse,DropDownResponse.class);
    }

    private DropDownResponse getStringDropdownData(String query){
        DropDownResponse dropDownResponse = new DropDownResponse();
        KeyValue keyValue = new KeyValue();
        List<KeyValue> keyValueList = new ArrayList<>();
        try {
            ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQueryOracle(query);
            if(objs!=null && objs.size()>0){
                KeyValue keyValueTemp = new KeyValue();
                keyValueTemp.setKey("");
                keyValueTemp.setValue("All");
                keyValueList.add(keyValueTemp);
                for (Object obj : objs) {
                    keyValue = new KeyValue();
                    keyValue.setKey(obj.toString());
                    keyValue.setValue("");
                    keyValueList.add(keyValue);
                }
            }

            dropDownResponse.setKeyValueList(keyValueList);
            dropDownResponse.setStatus(Codes.ALL_OK);
        }catch (Exception e){
            dropDownResponse.setStatus(Codes.SOMETHING_WENT_WRONG);
        }
        return dropDownResponse;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getCardData", method = RequestMethod.GET,params={"token", "type"})
    @ResponseBody
    private String getCardData(String token, String type){

        String whereClause = getWhereClause(token);
        String query = "";


        if(type.equals("YTD_SALE")){
            query = "select to_char(sum(tp_sale_value), '999,999,999,999') as \"YTD TP Sale Value\" from sale_detail_temp where "+whereClause+" reportingmonth in\n" +
                    "('July,2021','August,2021','September,2021','October,2021','November,2021','December,2021','January,2022','February,2022','March,2022','April,2022','May,2022','June,2022')";
        }else if(type.equals("MTD_PERC")){
            query = "select CONCAT(round(sum(sdt.tp_sale_value)/sum(tgt.target)*100,2),'%') as \"MTD Achievement\" from sale_detail_temp sdt\n" +
                    "inner join targets tgt \n" +
                    "on sdt.position_code = tgt.position_id\n" +
                    "where "+whereClause+" sdt.reportingmonth='August,2021'";
        }else if(type.equals("YTD_CYP")){
            query = "select to_char(sum(CYP), '999,999,999,999') as \"YTD CYP\" from sale_detail_temp where reportingmonth in\n" +
                    "('July,2021','August,2021','September,2021','October,2021','November,2021','December,2021','January,2022','February,2022','March,2022','April,2022','May,2022','June,2022')";
        }

        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQueryOracle(query);
        CardData cardData = new CardData();


        if(objs!=null && objs.size()>0){
            for (Object obj : objs) {
                cardData.setStatus(Codes.ALL_OK);
                if(obj==null){
                    cardData.setNumber("0");
                }else{
                    cardData.setNumber(obj.toString());
                }

            }
        }

        return new Gson().toJson(cardData);
    }

    List<String> getPartnersArray(String token){
        List<String> partners = new ArrayList<>();
        LoginStatus loginStatus = getLoginStatus(token);
        String username = "";
        if(loginStatus!=null){
            username = loginStatus.getUsername();
            List<EmployeeIDPositionIDMapping> employeeIDPositionIDMappings = (List<EmployeeIDPositionIDMapping>) HibernateUtil.getDBObjects("from EmployeeIDPositionIDMapping where EMPLOYEE_ID ='" + username + "'");
            if (employeeIDPositionIDMappings != null && employeeIDPositionIDMappings.size() > 0) {
                String positionCode = "";

                positionCode = employeeIDPositionIDMappings.get(0).getPOSITION_ID();
                partners.add(positionCode);
                List<EmployeeReportToMapping> employeeReportToMappings = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where POSITION_ID ='" + positionCode + "'");
                //Create Dal which has employee details with positionCode
                if (employeeReportToMappings != null && employeeReportToMappings.size() > 0) {
                    for(EmployeeReportToMapping employeeReportToMapping : employeeReportToMappings){
                        partners.add(employeeReportToMapping.getREPORTTO_ID());
                    }
                }

            }
        }
        return partners;
    }

    /*
    This will return the where clause with position code of partners
     */
    private String getWhereClause(String token){
        String whereClause = "";
        List<String> partners = new ArrayList<>();
        partners = getPartnersArray(token);
        if(partners!=null && partners.size()>0){
            boolean isFirst = true;
            whereClause = "  POSITION_CODE IN ( ";
            for(String str : partners){
                if(!isFirst){
                    whereClause += ",";
                }
                whereClause += "'"+ str+"'";
                isFirst = false;
            }
            whereClause += ") and ";
        }
        return whereClause;
    }
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getCYPBarChartData", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public String getCYPBarChartData(String token){
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




        String whereClause = "";
        whereClause = getWhereClause(token);

        String query = "select to_char(transaction_date, 'MON-YY'), reportingmonth as \"Month\", groupon as \"Group\", sum(tp_sale_value) as \"Net Value\" from sale_detail_temp\n" +
                "where "+whereClause+"  reportingmonth IN ('July,2021','August,2021','September,2021','October,2021','November,2021','December,2021','January,2022','February,2022','March,2022','April,2022','May,2022','June,2022')\n" +
                "group by groupon, reportingmonth,to_char(transaction_date, 'MON-YY') ORDER BY to_char(transaction_date, 'MON-YY') DESC";

        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQueryOracle(query);
        List<String> labels = new ArrayList<>();
        List<String> months = new ArrayList<>();
        BarChartData barChartData = new BarChartData();
        Dataset dataset = new Dataset();
        List<Dataset> datasets =new ArrayList<>();
        List<Double> data = new ArrayList<>();
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
                    double dataNum = 0;
                    for(int j=0; j<objs.size(); j++){
                        Object[] innerTemp = (Object[]) objs.get(j);

                        if(innerTemp[2].toString().equals(label) && innerTemp[1].toString().equals(months.get(i).toString())){
                            dataNum = Double.valueOf(innerTemp[3].toString().trim());
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

        return new Gson().toJson(barChartData);
    }

}
