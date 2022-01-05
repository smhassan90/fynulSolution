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

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 10th November, 2021
 */

@Controller
public class Performance {
    private LoginStatus getLoginStatus(String token){
        LoginStatus loginStatus = null;
       // String query = "from LoginStatus where token='" + token + "'";
        String query = "SELECT * FROM LOGINSTATUS WHERE TOKEN='" + token + "'";
        ArrayList<Object> loginStatusList =  HibernateUtil.getDBObjectsFromSQLQueryClass(query, LoginStatus.class);



        if (loginStatusList != null && loginStatusList.size() > 0) {
            loginStatus = (LoginStatus) loginStatusList.get(0);
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
            whereClause += " reportingMonth = '"+month+"'\n";
        }

        if(!position_code.equals("")){
            if(!whereClause.equals("")){
                whereClause +=" and ";
            }
            whereClause += " position_code = '"+position_code+"' ";
        }else{
            if(!whereClause.equals("")){
                whereClause +=" and ";
            }
            whereClause += getWhereClause(token);
        }
        if(!products.equals("")){
            if(!whereClause.equals("")){
                whereClause +=" and ";
            }
            whereClause += " groupon = '"+products+"' ";
        }
        if(whereClause.equals("")){
            whereClause = " 1=1 ";
        }

        String query = "SELECT *  FROM PERFORMANCE " +
                "WHERE "+ whereClause+" and 1=1";

        ArrayList<Object> objs = null;//HibernateUtil.getDBObjectsFromSQLQuery(query);
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

       // return new Gson().toJson(productPerformances);
        return null;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getReportingMonths", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public String getReportingMonths(String token){
    
;        DropDownResponse dropDownResponse = new DropDownResponse();

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
            ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
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
    @RequestMapping(value = "/getAverageAndRequiredMonthly", method = RequestMethod.GET,params={"token", "type"})
    @ResponseBody
    private String getAverageAndRequiredMonthly(String token, String type){
        String status = "";
        String whereClause = getWhereClause(token);
        String query = "select SUM(Current_month_average), SUM(required_month_average) FROM performance WHERE " + whereClause;

        ArrayList<Object> objs = null;//HibernateUtil.getDBObjectsFromSQLQuery(query);
        AverageMonthly averageMonthly = new AverageMonthly();
        String averageMonthlyAchieved = "0";
        String averageMonthlyRequired = "0";
        if(objs!=null && objs.size()>0){
            for (Object obj : objs) {
                Object[] objects = (Object[]) obj;
                if(objects!= null && objects.length>0) {
                    averageMonthlyAchieved = currencyFormat(new BigDecimal(objects[0].toString()));
                    averageMonthlyRequired = currencyFormat(new BigDecimal(objects[1].toString()));

                    status=Codes.ALL_OK;
                }
            }
        }
        averageMonthly.setAchieved(averageMonthlyAchieved);
        averageMonthly.setRequired(averageMonthlyRequired);
        averageMonthly.setStatus(status);

        return new Gson().toJson(averageMonthly);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getUCC", method = RequestMethod.GET,params={"token", "type"})
    @ResponseBody
    private String getUCC(String token, String type){
        String status = "";
        String whereClause = getWhereClause(token);
        String whereMonth = getWhereMonthClause(type);
        String query = "select sum(IS_UCC) AS 'UCC' from ( SELECT CUST_NUMBER, CASE WHEN SUM(E_QTY) > 0 THEN 1 ELSE 0 END AS 'IS_UCC' FROM sale_detail_temp WHERE " +
                whereClause + " and transaction_date " + whereMonth +
                " GROUP BY CUST_NUMBER ) src";

        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
        SingleCard singleCard = new SingleCard();
        String number = "";
        if(objs!=null && objs.size()>0){
            for (Object obj : objs) {
                    number = currencyFormat(new BigDecimal(obj==null?"0":obj.toString()));
                    status=Codes.ALL_OK;


            }
        }
        singleCard.setNumber(number);
        singleCard.setStatus(status);
        singleCard.setText(type);

        return new Gson().toJson(singleCard);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getCardData", method = RequestMethod.GET,params={"token", "type"})
    @ResponseBody
    private String getCardData(String token, String type){
/*
YTD_SALE_VALUE
YTD_SALE_TP
YTD_SALE_UNIT
YTD_SALE_UCC
MTD_SALE_VALUE
MTD_SALE_TP
MTD_SALE_UNIT
MTD_SALE_UCC
 */
/*
Type : YTD
TYPE : MTD
 */
        String condition = "";

        if(type.equals("YTD")){
            condition = " between '2021-07-01' and '2022-06-30' ";
        }else if(type.equals("MTD")){
            condition = " like '2021-12-%' ";
        }

        String whereClause = getWhereClause(token);
        String whereMonth = getWhereMonthClause(type);
        String query = "SELECT SUM(E_QTY) AS E_QTY, "+
                "SUM(`TP_SALE_VALUE`) as TP_SALE_VALUE FROM `SALE_DETAIL_TEMP` " +
                "WHERE "+whereClause + " and transaction_date "+condition ;

        String targetQuery = "SELECT ROUND(SUM(TARGET),2) AS 'TARGET_E_QTY', ROUND(SUM(`TGT_TP_VALUE`),0) AS 'TARGET_TP_VALUE', ROUND(SUM(`TGT_NET_VALUE`),0) AS 'TARGET_NET_VALUE', ROUND(SUM(`TGT_DIST_COMM`),0) AS 'MNP_COMMISSION' FROM `BASE_TARGET`  WHERE month "+condition+" and "+whereClause;
        ArrayList<Object> objsTarget = HibernateUtil.getDBObjectsFromSQLQuery(targetQuery);
        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
        CardDataYear cardDataYear = new CardDataYear();

        double E_QTY = 0;
        double TARGET_E_QTY = 0;
        double TP_SALE_VALUE = 0;
        double TARGET_TP_VALUE = 0;
        String status = Codes.SOMETHING_WENT_WRONG;

        if(objsTarget!=null && objsTarget.size()>0){
            for (Object target : objsTarget) {

                Object[] objects = (Object[]) target;
                if(objects!= null && objects.length>0){
                    TARGET_E_QTY = Double.valueOf(objects[0]==null?"0":objects[0].toString());
                    TARGET_TP_VALUE = Double.valueOf(objects[1]==null?"0":objects[1].toString());
                    status=Codes.ALL_OK;

                }
            }
        }

        if(objs!=null && objs.size()>0){
            for (Object obj : objs) {

                Object[] objects = (Object[]) obj;
                if(objects!= null && objects.length>0){
                    E_QTY = Double.valueOf(objects[0]==null?"0":objects[0].toString());
                    TP_SALE_VALUE = Double.valueOf(objects[1]==null?"0":objects[1].toString());
                    status=Codes.ALL_OK;

                }
            }
        }
        cardDataYear.setAchTPValue(Codes.df.format(TP_SALE_VALUE));
        cardDataYear.setTargetTPValue(Codes.df.format(TARGET_TP_VALUE));
        cardDataYear.setTPValuePerc(Codes.df.format((TP_SALE_VALUE/TARGET_TP_VALUE)*100));

        cardDataYear.setAchUnit(Codes.df.format(E_QTY));
        cardDataYear.setTargetUnit(Codes.df.format(TARGET_E_QTY));
        cardDataYear.setUnitPerc(Codes.df.format((E_QTY/TARGET_E_QTY)*100));
        cardDataYear.setStatus(status);

        return new Gson().toJson(cardDataYear);
    }

    private String getWhereMonthClause(String type) {
        int month = Calendar.getInstance().get(Calendar.MONTH)+1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String query = " ";
        if(type.equals("MTD")){
            query = "  like '"+year+"-"+month+"%'";
        }else if(type.equals("YTD")){
            query = "  between '2021-07-01' and '2022-06-30'";
        }
        return query;
    }

    public static String currencyFormat(BigDecimal n) {

        return Codes.nf.format(n);
    }

    ArrayList<String> getPartnersArray(String token){
        ArrayList<String> partners = new ArrayList<>();
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
            whereClause += ") ";
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

        String query = "select DATE_FORMAT(TRANSACTION_DATE, '%M-%y'), reportingmonth as \"Month\", groupon as \"Group\", sum(tp_sale_value) as \"Net Value\" from sale_detail_temp\n" +
                "where "+whereClause+" and transaction_date between '2021-07-01' and '2022-06-30' \n" +
                "group by groupon, reportingmonth,DATE_FORMAT(TRANSACTION_DATE, '%M-%y') ORDER BY TRANSACTION_DATE DESC";

        ArrayList<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
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

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getBIO", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    private String getBIO(String token){
        LoginStatus loginStatus = getLoginStatus(token);
        String username = "";
        Employee employee = new Employee();
        String bio = "";
        if(loginStatus!=null){
            username = loginStatus.getUsername();
            employee = getEmployee(username);
            if(employee!=null){
                bio = employee.getID() + " | " + employee.getNAME() + " | " +employee.getBASE();
            }
        }
        return bio;
    }

    private Employee getEmployee(String username) {
        Employee emp = new Employee();
        List<Employee> employees = (List<Employee>) HibernateUtil.getDBObjects("from Employee where id='"+username+"'");
        if(employees!=null && employees.size()>0){
            emp = employees.get(0);
        }

        return emp;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getSPOProgress", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    private String getSPOProgress(String token){
        String where = getWhereClause(token);

        String mtdQuery = "";
        String ytdQuery = "";

        String targetMTD = "SELECT POSITION_CODE, ROUND(SUM(TGT_NET_VALUE), 0) as 'TARGET_NET_VALUE', e.name FROM `base_target` t INNER JOIN base_empid_positionid_mapping ep ON ep.POSITION_ID = t.POSITION_CODE INNER JOIN base_employee e on e.ID = ep.EMPLOYEE_ID  WHERE "+where+" and MONTH='2021-12-01' group by POSITION_CODE";
        String achMTD = "SELECT POSITION_CODE, ROUND(SUM(NET_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE "+where+" and transaction_date like '2021-12%' group by POSITION_CODE";
        ArrayList<Object> objTargetMTD= HibernateUtil.getDBObjectsFromSQLQuery(targetMTD);
        ArrayList<Object> objAchMTD= HibernateUtil.getDBObjectsFromSQLQuery(achMTD);

        String targetYTD = "SELECT POSITION_CODE, ROUND(SUM(TGT_NET_VALUE), 0) as 'TARGET_NET_VALUE' FROM `base_target` WHERE "+where+" and MONTH between '2021-07-01' and '2021-12-01' group by POSITION_CODE";
        String achYTD = "SELECT POSITION_CODE, ROUND(SUM(NET_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE "+where+" and transaction_date between '2021-07-01' and '2021-12-31' group by POSITION_CODE";
        ArrayList<Object> objTargetYTD= HibernateUtil.getDBObjectsFromSQLQuery(targetYTD);
        ArrayList<Object> objAchYTD= HibernateUtil.getDBObjectsFromSQLQuery(achYTD);

        String FYTarget = "SELECT POSITION_CODE, ROUND(SUM(TGT_NET_VALUE), 0) as 'TARGET_NET_VALUE' FROM `base_target` WHERE "+where+" and MONTH between '2021-07-01' and '2022-06-30' group by POSITION_CODE";
        ArrayList<Object> objFYTarget= HibernateUtil.getDBObjectsFromSQLQuery(FYTarget);


        List<String> partners = getPartnersArray(token);
        SPOProgress spoProgress = new SPOProgress();
        List<SPOProgress> spoProgressList = new ArrayList<>();


        if(partners!=null){
            for (String partner : partners){
                double achM = 0;
                double targetM = 0;
                double targetY = 0;
                double achY = 0;
                double targetFY = 0;
                String name = "";
                spoProgress = new SPOProgress();
                spoProgress.setName(getName(partner, objTargetMTD));
                spoProgress.setPosition_code(partner);
                targetM = getNumberAgainstPartner(partner, objTargetMTD);
                achM = getNumberAgainstPartner(partner, objAchMTD);

                targetY = getNumberAgainstPartner(partner, objTargetYTD);
                achY = getNumberAgainstPartner(partner, objAchYTD);

                targetFY = getNumberAgainstPartner(partner, objFYTarget);

                spoProgress.setMtdTarget(Codes.df.format(targetM));
                spoProgress.setMtdAch(Codes.df.format(achM));
                spoProgress.setYtdTarget(Codes.df.format(targetY));
                spoProgress.setYtdAch(Codes.df.format(achY));
                spoProgress.setMtdPerc(String.valueOf(Codes.df.format((achM/(targetM==0?1:targetM))*100)));
                spoProgress.setYtdPerc(String.valueOf(Codes.df.format((achY/(targetY==0?1:targetY)*100))));
                spoProgress.setFYTarget(String.valueOf(Codes.df.format(targetFY)));
                spoProgress.setBalance(String.valueOf(Codes.df.format(targetFY-achY)));


                spoProgressList.add(spoProgress);
            }

        }



        return new Gson().toJson(spoProgressList);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getSPOProgressProductWise", method = RequestMethod.GET,params={"token", "position_code"})
    @ResponseBody
    private String getSPOProgressProductWise(String token, String position_code){
        String where = getWhereClause(token);

        String mtdQuery = "";
        String ytdQuery = "";

        String targetMTD = "SELECT GROUP_ON_ID, ROUND(SUM(TGT_NET_VALUE), 0) as 'TARGET_NET_VALUE', e.name FROM `base_target` t INNER JOIN base_empid_positionid_mapping ep ON ep.POSITION_ID = t.POSITION_CODE INNER JOIN base_employee e on e.ID = ep.EMPLOYEE_ID  WHERE position_code='"+position_code+"' and MONTH='2021-12-01' group by GROUP_ON_ID";
        String achMTD = "SELECT GROUPON, ROUND(SUM(NET_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE position_code='"+position_code+"' and transaction_date like '2021-12%' group by GROUPON";
        ArrayList<Object> objTargetMTD= HibernateUtil.getDBObjectsFromSQLQuery(targetMTD);
        ArrayList<Object> objAchMTD= HibernateUtil.getDBObjectsFromSQLQuery(achMTD);

        String targetYTD = "SELECT GROUP_ON_ID, ROUND(SUM(TGT_NET_VALUE), 0) as 'TARGET_NET_VALUE' FROM `base_target` WHERE position_code='"+position_code+"' and MONTH between '2021-07-01' and '2021-12-01' group by GROUP_ON_ID";
        String achYTD = "SELECT GROUPON, ROUND(SUM(NET_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE position_code='"+position_code+"' and transaction_date between '2021-07-01' and '2021-12-31' group by GROUPON";
        ArrayList<Object> objTargetYTD= HibernateUtil.getDBObjectsFromSQLQuery(targetYTD);
        ArrayList<Object> objAchYTD= HibernateUtil.getDBObjectsFromSQLQuery(achYTD);

        String FYTarget = "SELECT GROUP_ON_ID, ROUND(SUM(TGT_NET_VALUE), 0) as 'TARGET_NET_VALUE' FROM `base_target` WHERE position_code='"+position_code+"' and MONTH between '2021-07-01' and '2022-06-30' group by GROUP_ON_ID";
        ArrayList<Object> objFYTarget= HibernateUtil.getDBObjectsFromSQLQuery(FYTarget);


      //  List<String> partners = getPartnersArray(token);
        List<String> groupOns = getGroupOns(objTargetYTD);

        SPOProgress spoProgress = new SPOProgress();
        List<SPOProgress> spoProgressList = new ArrayList<>();


        if(groupOns!=null){
            for (String groupon : groupOns){
                double achM = 0;
                double targetM = 0;
                double targetY = 0;
                double achY = 0;
                double targetFY = 0;
                String name = "";
                spoProgress = new SPOProgress();
                spoProgress.setName(groupon);
                spoProgress.setPosition_code(groupon);
                targetM = getNumberAgainstPartner(groupon, objTargetMTD);
                achM = getNumberAgainstPartner(groupon, objAchMTD);

                targetY = getNumberAgainstPartner(groupon, objTargetYTD);
                achY = getNumberAgainstPartner(groupon, objAchYTD);

                targetFY = getNumberAgainstPartner(groupon, objFYTarget);

                spoProgress.setMtdTarget(Codes.df.format(targetM));
                spoProgress.setMtdAch(Codes.df.format(achM));
                spoProgress.setYtdTarget(Codes.df.format(targetY));
                spoProgress.setYtdAch(Codes.df.format(achY));
                spoProgress.setMtdPerc(String.valueOf(Codes.df.format((achM/(targetM==0?1:targetM))*100)));
                spoProgress.setYtdPerc(String.valueOf(Codes.df.format((achY/(targetY==0?1:targetY)*100))));
                spoProgress.setFYTarget(String.valueOf(Codes.df.format(targetFY)));
                spoProgress.setBalance(String.valueOf(Codes.df.format(targetFY-achY)));


                spoProgressList.add(spoProgress);
            }

        }



        return new Gson().toJson(spoProgressList);
    }

    private List<String> getGroupOns(ArrayList<Object> objTargetYTD) {
        List<String> groupOns = new ArrayList<>();
        if(objTargetYTD!=null){
            for(Object obj : objTargetYTD){
                Object[] objs = (Object[]) obj;
                if(objs!=null){
                    groupOns.add(objs[0]==null?"":objs[0].toString());
                }
            }
        }
        return groupOns;
    }

    private String getName(String position_code, ArrayList<Object> objTargetMTD) {
        String name = "";
        if(objTargetMTD!=null){
            for (Object mtd : objTargetMTD){
                Object[] mtdArray = (Object[]) mtd;
                if(mtdArray!=null){
                    if(!position_code.equals(mtdArray[0]==null?"":mtdArray[0].toString())){
                        continue;
                    }else{
                        name = mtdArray[2].toString();
                        break;
                    }
                }

            }
        }
        return name;
    }

    private double getNumberAgainstPartner(String partner, ArrayList<Object> objTargetMTD) {
        double number = 0;
        if(objTargetMTD!=null){
            for (Object mtd : objTargetMTD){
                Object[] mtdArray = (Object[]) mtd;
                if(mtdArray!=null){
                    if(!partner.equals(mtdArray[0].toString())){
                        continue;
                    }else{
                        number = Double.valueOf(mtdArray[1]==null?"0":mtdArray[1].toString());

                    }
                }

            }
        }
        return number;
    }

}
