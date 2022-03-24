package com.fynuls.controllers.greensales;

import com.fynuls.dal.*;
import com.fynuls.entity.base.Employee;
import com.fynuls.entity.base.EmployeeIDPositionIDMapping;
import com.fynuls.entity.base.EmployeeReportToMapping;
import com.fynuls.entity.base.TeamDepartment;
import com.fynuls.entity.login.LoginStatus;
import com.fynuls.utils.Common;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 10th November, 2021
 */

@Controller
public class Performance {

    private LoginStatus getLoginStatus(String token){
        LoginStatus loginStatus = new LoginStatus();
       // String query = "from LoginStatus where token='" + token + "'";
        String query = "SELECT * FROM LOGINSTATUS WHERE TOKEN='" + token + "'";
        ArrayList<Object> loginStatusList =  HibernateUtil.getDBObjectsFromSQLQuery(query);

        if (loginStatusList != null && loginStatusList.size() > 0) {
            for(Object obj : loginStatusList){
                Object[] objArray = (Object[]) obj;
                if(objArray!=null){
                    loginStatus.setUsername(objArray[0].toString());
                    loginStatus.setToken(objArray[1].toString());
                    loginStatus.setPOSITION_CODE(objArray[5].toString());
                }
            }
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
        List<EmployeeReportToMapping> employeeReportToMappings = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where REPORTTO_ID ='" + positionCode + "'");
        //Create Dal which has employee details with positionCode
        if (employeeReportToMappings != null && employeeReportToMappings.size() > 0) {
//            KeyValue keyValueTemp = new KeyValue();
//            keyValueTemp.setKey("");
//            keyValueTemp.setValue("All");
//            keyValueList.add(keyValueTemp);
            for (EmployeeReportToMapping employeeReportToMapping : employeeReportToMappings) {
                String partnerPositionCode = employeeReportToMapping.getPOSITION_ID();
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
            String query = "SELECT distinct productGroup FROM SALE_DETAIL_TEMP  "+innerQuery + " ORDER BY groupOn ASC";
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
//                KeyValue keyValueTemp = new KeyValue();
//                keyValueTemp.setKey("");
//                keyValueList.add(keyValueTemp);
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
        String query = "";
        if(type.equals("YTD")){
            query = "select sum(IS_UCC) AS 'UCC' from ( SELECT CUST_NUMBER, CASE WHEN SUM(E_QTY) > 0 THEN 1 ELSE 0 END AS 'IS_UCC' FROM sale_detail_temp WHERE " +
                    whereClause + " and transaction_date " + whereMonth +
                    " GROUP BY CUST_NUMBER ) src";
        }else{
            query = "select sum(IS_UCC) AS 'UCC' from ( SELECT CUST_NUMBER, CASE WHEN SUM(E_QTY) > 0 THEN 1 ELSE 0 END AS 'IS_UCC' FROM sale_detail_temp WHERE " +
                    whereClause + " and reportingMonth='"+Codes.monthNames[Codes.CALENDER_CURRENT_MONTH_NUMBER-1]+","+Codes.CURRENT_YEAR_NUMBER+"' "+
                    " GROUP BY CUST_NUMBER ) src";
        }


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
            condition = " between '"+HibernateUtil.getFiscalYearStart()+"-06-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31' ";
        }else if(type.equals("MTD")){
            condition = " like '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-%' ";
        }

        String whereClause = getWhereClause(token);
      //  String whereMonth = getWhereMonthClause(type);
        String query = "SELECT SUM(E_QTY) AS E_QTY, "+
                "SUM(`TP_SALE_VALUE`) as TP_SALE_VALUE FROM `SALE_DETAIL_TEMP` " +
                "WHERE "+whereClause + " and transaction_date "+condition ;

        String targetQuery = "SELECT ROUND(SUM(TARGET),2) AS 'TARGET_E_QTY', ROUND(SUM(`TGT_TP_VALUE`),0) AS 'TARGET_TP_VALUE', ROUND(SUM(`TGT_TP_VALUE`),0) AS 'TARGET_NET_VALUE', ROUND(SUM(`TGT_DIST_COMM`),0) AS 'MNP_COMMISSION' FROM `BASE_TARGET`  WHERE month "+condition+" and "+whereClause;
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
        cardDataYear.setAchTPValue(Codes.nonDecimal.format(TP_SALE_VALUE));
        cardDataYear.setTargetTPValue(Codes.nonDecimal.format(TARGET_TP_VALUE));
        cardDataYear.setTPValuePerc(Codes.df.format((TP_SALE_VALUE/TARGET_TP_VALUE)*100));

        cardDataYear.setAchUnit(Codes.df.format(E_QTY));
        cardDataYear.setTargetUnit(Codes.df.format(TARGET_E_QTY));
        cardDataYear.setUnitPerc(Codes.df.format((E_QTY/TARGET_E_QTY)*100));
        cardDataYear.setStatus(status);

        return new Gson().toJson(cardDataYear);
    }

    private String getWhereMonthClause(String type) {
        String query = " ";
        if(type.equals("MTD")){
            query = "  like '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"%'";
        }else if(type.equals("YTD")){
            query = "  between '"+HibernateUtil.getFiscalYearStart()+"-06-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31'";
        }
        return query;
    }

    public static String currencyFormat(BigDecimal n) {

        return Codes.nf.format(n);
    }

    public ArrayList<String> getPartnersArray(String token){
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
                List<EmployeeReportToMapping> employeeReportToMappings = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where REPORTTO_ID ='" + positionCode + "'");
                //Create Dal which has employee details with positionCode
                if (employeeReportToMappings != null && employeeReportToMappings.size() > 0) {
                    for(EmployeeReportToMapping employeeReportToMapping : employeeReportToMappings){
                        partners.add(employeeReportToMapping.getPOSITION_ID());
                        List<EmployeeReportToMapping> employeeReportToMappingsNested = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where REPORTTO_ID ='" + employeeReportToMapping.getPOSITION_ID() + "'");
                        if (employeeReportToMappingsNested != null && employeeReportToMappingsNested.size() > 0) {
                            for(EmployeeReportToMapping employeeReportToMappingNested : employeeReportToMappingsNested){
                                partners.add(employeeReportToMappingNested.getPOSITION_ID());
                                List<EmployeeReportToMapping> employeeReportToMappingsNestedNested = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where REPORTTO_ID ='" + employeeReportToMappingNested.getPOSITION_ID() + "'");
                                if (employeeReportToMappingsNestedNested != null && employeeReportToMappingsNestedNested.size() > 0) {
                                    for(EmployeeReportToMapping employeeReportToMappingNestedNested : employeeReportToMappingsNestedNested){
                                        partners.add(employeeReportToMappingNestedNested.getPOSITION_ID());
                                        List<EmployeeReportToMapping> employeeReportToMappingsNestedNestedNested = (List<EmployeeReportToMapping>) HibernateUtil.getDBObjects("from EmployeeReportToMapping where REPORTTO_ID ='" + employeeReportToMappingNestedNested.getPOSITION_ID() + "'");
                                        if (employeeReportToMappingsNestedNestedNested != null && employeeReportToMappingsNestedNestedNested.size() > 0) {
                                            for(EmployeeReportToMapping employeeReportToMappingNestedNestedNested : employeeReportToMappingsNestedNestedNested){
                                                partners.add(employeeReportToMappingNestedNestedNested.getPOSITION_ID());

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return partners;
    }



    /*
    This will return the where clause with position code of partners
     */
    public String getWhereClause(String token){
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
        }else{
            whereClause += " 1=1 ";
        }

        return whereClause;
    }
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getCYPBarChartData", method = RequestMethod.GET,params={"token", "type"})
    @ResponseBody
    public String getCYPBarChartData(String token, String type){
        String query = "";
        String whereClause = "";
        whereClause = getWhereClause(token);

        query = getBarChartQuery(whereClause, type);

        BarChartData barChartData = new BarChartData();
        barChartData = Common.getBarChartData(query);
        return new Gson().toJson(barChartData);
    }

    private String getBarChartQuery(String whereClause, String type) {
        String query = "";
        if(type.equals("groupon")){
            query = "select DATE_FORMAT(TRANSACTION_DATE, '%M-%y'), reportingmonth as \"Month\", groupon as \"Group\", sum(tp_sale_value) as \"Net Value\" from sale_detail_temp\n" +
                    "where nature is null and  "+whereClause+" and transaction_date between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Integer.valueOf(HibernateUtil.getFiscalYearStart()+1)+"-06-30' \n" +
                    "group by groupon, reportingmonth,DATE_FORMAT(TRANSACTION_DATE, '%M-%y') ORDER BY TRANSACTION_DATE";
        }else if(type.equals("ucc")){
            query = "select monthYear, reportingMonth, name, sum(IS_UCC) AS 'UCC' from UCC \n" +
                    "WHERE   \n" + whereClause +
                    " group by POSITION_CODE, reportingmonth, monthYear order by monthYear";
        }else if(type.equals("productivity")){
            query = "select DATE_FORMAT(TRANSACTION_DATE, '%M-%y'),  reportingmonth, e.name, sum(TP_SALE_VALUE) AS 'TP_SALE_VALUE' from sale_detail_temp sdt INNER JOIN base_employee e on e.ID = sdt.EMPLOYEEID " +
                    "WHERE nature is null and  " + whereClause +
                    " group by DATE_FORMAT(TRANSACTION_DATE, '%M-%y'), reportingmonth, POSITION_CODE order by TRANSACTION_DATE";
        }

        return query;
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

        String targetMTD = "SELECT POSITION_CODE, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE', e.name FROM `base_target` t INNER JOIN base_empid_positionid_mapping ep ON ep.POSITION_ID = t.POSITION_CODE INNER JOIN base_employee e on e.ID = ep.EMPLOYEE_ID  WHERE "+where+" and MONTH='"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-01' group by POSITION_CODE";
        String achMTD = "SELECT POSITION_CODE, ROUND(SUM(TP_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE "+where+" and transaction_date like '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"%' group by POSITION_CODE";
        ArrayList<Object> objTargetMTD= HibernateUtil.getDBObjectsFromSQLQuery(targetMTD);
        ArrayList<Object> objAchMTD= HibernateUtil.getDBObjectsFromSQLQuery(achMTD);

        String targetYTD = "SELECT POSITION_CODE, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE' FROM `base_target` WHERE "+where+" and MONTH between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31' group by POSITION_CODE";
        String achYTD = "SELECT POSITION_CODE, ROUND(SUM(TP_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE "+where+" and transaction_date between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31' group by POSITION_CODE";
        ArrayList<Object> objTargetYTD= HibernateUtil.getDBObjectsFromSQLQuery(targetYTD);
        ArrayList<Object> objAchYTD= HibernateUtil.getDBObjectsFromSQLQuery(achYTD);

        String FYTarget = "SELECT POSITION_CODE, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE' FROM `base_target` WHERE "+where+" and MONTH between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Integer.valueOf(HibernateUtil.getFiscalYearStart()+1)+"-06-30' group by POSITION_CODE";
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
                String nameFromPositionCode  = getNameFromPositionCode(partner);
                spoProgress.setName(nameFromPositionCode);
                spoProgress.setPosition_code(partner);
                targetM = getNumberAgainstPartner(partner, objTargetMTD);

                achM = getNumberAgainstPartner(partner, objAchMTD);

                targetY = getNumberAgainstPartner(partner, objTargetYTD);
                achY = getNumberAgainstPartner(partner, objAchYTD);

                targetFY = getNumberAgainstPartner(partner, objFYTarget);


                spoProgress.setMtdTarget(Codes.nonDecimal.format(targetM));
                spoProgress.setMtdAch(Codes.nonDecimal.format(achM));
                spoProgress.setYtdTarget(Codes.nonDecimal.format(targetY));
                spoProgress.setYtdAch(Codes.nonDecimal.format(achY));
                spoProgress.setMtdPerc(String.valueOf(Codes.df.format((achM/(targetM==0?1:targetM))*100)));
                spoProgress.setYtdPerc(String.valueOf(Codes.df.format((achY/(targetY==0?1:targetY)*100))));
                spoProgress.setFYTarget(String.valueOf(Codes.nonDecimal.format(targetFY)));
                spoProgress.setBalance(String.valueOf(Codes.nonDecimal.format(targetFY-achY)));
                spoProgress.setRMA(String.valueOf(Codes.nonDecimal.format((targetFY-achY)/6)));
                spoProgress.setCMA(String.valueOf(Codes.nonDecimal.format((achY)/6)));

                spoProgressList.add(spoProgress);
            }
        }
        return new Gson().toJson(spoProgressList);
    }

    private String getNameFromPositionCode(String partner) {
        ArrayList<EmployeeIDPositionIDMapping> mapping = (ArrayList<EmployeeIDPositionIDMapping>) HibernateUtil.getDBObjects("from EmployeeIDPositionIDMapping where POSITION_ID='"+partner+"'");

        if(mapping!=null && mapping.size()>0){
            EmployeeIDPositionIDMapping employeeIDPositionIDMapping = mapping.get(0);
            ArrayList<Employee> emps = (ArrayList<Employee>) HibernateUtil.getDBObjects("from Employee where id = '"+employeeIDPositionIDMapping.getEMPLOYEE_ID()+"'");
            if(emps!=null && emps.size()>0){
                return emps.get(0).getNAME();
            }
        }
        return "";
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getTeamProgress", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    private String getTeamProgress(String token){

        String mtdQuery = "";
        String ytdQuery = "";

        String targetMTD = "SELECT TEAM, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE', td.alias FROM base_team_dept td  INNER JOIN base_target t ON td.NAME = t.TEAM  WHERE MONTH='"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-01' group by TEAM";
        String achMTD = "SELECT TEAM, ROUND(SUM(TP_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE  transaction_date like '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"%' group by TEAM";
        ArrayList<Object> objTargetMTD= HibernateUtil.getDBObjectsFromSQLQuery(targetMTD);
        ArrayList<Object> objAchMTD= HibernateUtil.getDBObjectsFromSQLQuery(achMTD);

        String targetYTD = "SELECT TEAM, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE', td.name FROM base_team_dept td INNER JOIN base_target t ON td.NAME = t.TEAM WHERE MONTH between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31' group by TEAM";
        String achYTD = "SELECT TEAM, ROUND(SUM(TP_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE transaction_date between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31' group by TEAM";
        ArrayList<Object> objTargetYTD= HibernateUtil.getDBObjectsFromSQLQuery(targetYTD);
        ArrayList<Object> objAchYTD= HibernateUtil.getDBObjectsFromSQLQuery(achYTD);

        String FYTarget = "SELECT TEAM, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE' FROM `base_target` WHERE MONTH between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Integer.valueOf(HibernateUtil.getFiscalYearStart()+1)+"-06-30' group by TEAM";
        ArrayList<Object> objFYTarget= HibernateUtil.getDBObjectsFromSQLQuery(FYTarget);


        List<String> teams = getTeams();
        SPOProgress spoProgress = new SPOProgress();
        List<SPOProgress> spoProgressList = new ArrayList<>();


        if(teams!=null){
            for (String team : teams){
                double achM = 0;
                double targetM = 0;
                double targetY = 0;
                double achY = 0;
                double targetFY = 0;
                String name = "";
                spoProgress = new SPOProgress();
                spoProgress.setName(getName(team, objTargetMTD));
                spoProgress.setPosition_code(team);
                targetM = getNumberAgainstPartner(team, objTargetMTD);

                achM = getNumberAgainstPartner(team, objAchMTD);

                targetY = getNumberAgainstPartner(team, objTargetYTD);
                achY = getNumberAgainstPartner(team, objAchYTD);

                targetFY = getNumberAgainstPartner(team, objFYTarget);


                spoProgress.setMtdTarget(Codes.nonDecimal.format(targetM));
                spoProgress.setMtdAch(Codes.nonDecimal.format(achM));
                spoProgress.setYtdTarget(Codes.nonDecimal.format(targetY));
                spoProgress.setYtdAch(Codes.nonDecimal.format(achY));
                spoProgress.setMtdPerc(String.valueOf(Codes.df.format((achM/(targetM==0?1:targetM))*100)));
                spoProgress.setYtdPerc(String.valueOf(Codes.df.format((achY/(targetY==0?1:targetY)*100))));
                spoProgress.setFYTarget(String.valueOf(Codes.nonDecimal.format(targetFY)));
                spoProgress.setBalance(String.valueOf(Codes.nonDecimal.format(targetFY-achY)));
                spoProgress.setRMA(String.valueOf(Codes.nonDecimal.format((targetFY-achY)/6)));
                spoProgress.setCMA(String.valueOf(Codes.nonDecimal.format((achY)/6)));

                spoProgressList.add(spoProgress);
            }
        }
        return new Gson().toJson(spoProgressList);
    }

    private List<String> getTeams() {
        ArrayList<String> teams = new ArrayList<>();
        ArrayList<TeamDepartment> teamDepartments = (ArrayList<TeamDepartment>) HibernateUtil.getDBObjects("from TeamDepartment");

        if(teamDepartments!=null && teamDepartments.size()>0){
            for(TeamDepartment teamDepartment : teamDepartments){
                teams.add(teamDepartment.getNAME());
            }
        }
        return teams;
    }


    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getSPOProgressProductWise", method = RequestMethod.GET,params={"token", "position_code"})
    @ResponseBody
    private String getSPOProgressProductWise(String token, String position_code){
        String where = getWhereClause(token);

        String mtdQuery = "";
        String ytdQuery = "";

        String targetMTD = "SELECT GROUP_ON_ID, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE', e.name FROM `base_target` t INNER JOIN base_empid_positionid_mapping ep ON ep.POSITION_ID = t.POSITION_CODE INNER JOIN base_employee e on e.ID = ep.EMPLOYEE_ID  WHERE position_code='"+position_code+"' and MONTH='"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-01' group by GROUP_ON_ID";
        String achMTD = "SELECT GROUPON, ROUND(SUM(TP_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE position_code='"+position_code+"' and transaction_date like '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"%' group by GROUPON";
        ArrayList<Object> objTargetMTD= HibernateUtil.getDBObjectsFromSQLQuery(targetMTD);
        ArrayList<Object> objAchMTD= HibernateUtil.getDBObjectsFromSQLQuery(achMTD);

        String targetYTD = "SELECT GROUP_ON_ID, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE' FROM `base_target` WHERE position_code='"+position_code+"' and MONTH between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-01' group by GROUP_ON_ID";
        String achYTD = "SELECT GROUPON, ROUND(SUM(TP_SALE_VALUE), 0) FROM SALE_DETAIL_TEMP WHERE position_code='"+position_code+"' and transaction_date between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Codes.CURRENT_YEAR_NUMBER+"-"+Codes.CURRENT_MONTH_NUMBER+"-31' group by GROUPON";
        ArrayList<Object> objTargetYTD= HibernateUtil.getDBObjectsFromSQLQuery(targetYTD);
        ArrayList<Object> objAchYTD= HibernateUtil.getDBObjectsFromSQLQuery(achYTD);

        String FYTarget = "SELECT GROUP_ON_ID, ROUND(SUM(TGT_TP_VALUE), 0) as 'TARGET_TP_VALUE' FROM `base_target` WHERE position_code='"+position_code+"' and MONTH between '"+HibernateUtil.getFiscalYearStart()+"-07-01' and '"+Integer.valueOf(HibernateUtil.getFiscalYearStart()+1)+"-06-30' group by GROUP_ON_ID";
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
                spoProgress.setPersonName(getPersonName(groupon, objTargetMTD));
                targetY = getNumberAgainstPartner(groupon, objTargetYTD);
                achY = getNumberAgainstPartner(groupon, objAchYTD);

                targetFY = getNumberAgainstPartner(groupon, objFYTarget);

                spoProgress.setMtdTarget(Codes.nonDecimal.format(targetM));
                spoProgress.setMtdAch(Codes.nonDecimal.format(achM));
                spoProgress.setYtdTarget(Codes.nonDecimal.format(targetY));
                spoProgress.setYtdAch(Codes.nonDecimal.format(achY));
                spoProgress.setMtdPerc(String.valueOf(Codes.df.format((achM/(targetM==0?1:targetM))*100)));
                spoProgress.setYtdPerc(String.valueOf(Codes.df.format((achY/(targetY==0?1:targetY)*100))));
                spoProgress.setFYTarget(String.valueOf(Codes.nonDecimal.format(targetFY)));
                spoProgress.setBalance(String.valueOf(Codes.nonDecimal.format(targetFY-achY)));

                spoProgress.setRMA(String.valueOf(Codes.nonDecimal.format((targetFY-achY)/6)));
                spoProgress.setCMA(String.valueOf(Codes.nonDecimal.format((achY)/6)));

                spoProgressList.add(spoProgress);
            }

        }



        return new Gson().toJson(spoProgressList);
    }

    private String getPersonName(String groupon, ArrayList<Object> objTargetMTD) {
        String name = "";
        if(objTargetMTD!=null){
            for (Object mtd : objTargetMTD){
                Object[] mtdArray = (Object[]) mtd;
                if(mtdArray!=null){
                    if(!groupon.equals(mtdArray[0].toString())){
                        continue;
                    }else{
                        name = mtdArray[1]==null?"0":mtdArray[2].toString();
                        break;
                    }
                }

            }
        }
        return name;
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
                    if(!partner.equals(mtdArray[0]==null?"":mtdArray[0].toString())){
                        continue;
                    }else{
                        number = Double.valueOf(mtdArray[1]==null?"0":mtdArray[1].toString());
                        break;
                    }
                }

            }
        }
        return number;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getUCCTable", method = RequestMethod.GET,params={"token", "positionCode", "productGroup", "reportingMonth"})
    @ResponseBody
    private String getUCCTable(String token, String positionCode, String productGroup, String reportingMonth){
        String whereClause = getWhereClause(token);
        ArrayList<UCCDetail> uccDetails = new ArrayList<>();

        String queryPositionCode = "";
        String queryProductGroup = "";
        String queryReportingMonth = "";

        if(positionCode!=null && !"".equals(positionCode)){
            queryPositionCode = " POSITION_CODE = '"+positionCode + "' ";
        }else{
            queryPositionCode = " 1=1 ";
        }

        if(productGroup!=null && !"".equals(productGroup)){
            queryProductGroup = " productGroup = '"+productGroup + "' ";
        }else{
            queryProductGroup = " 1=1 ";
        }

        if(reportingMonth!=null && !"".equals(reportingMonth)){
            queryReportingMonth = " reportingMonth = '"+reportingMonth+"' ";
        }else{
            queryReportingMonth = " 1=1 ";
        }

        String query = "SELECT cust_number, cust_name, section_code, section_name, TP_SALE_VALUE from UCC_POSITION_PRODUCT_WISE where "+queryPositionCode + " and "+ queryProductGroup +" and " + queryReportingMonth +" and IS_UCC>0 order by TP_SALE_VALUE desc";
        List<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);
        UCCDetail uccDetail = null;
        if(objs!=null && objs.size()>0){
            for(Object obj : objs){
                uccDetail = new UCCDetail();
                if(obj!=null){
                    Object[] objArray = (Object[]) obj;
                    uccDetail.setCustomerNumber(objArray[0]==null?"":objArray[0].toString());
                    uccDetail.setCustomerName(objArray[1]==null?"":objArray[1].toString());
                    uccDetail.setSectionCode(objArray[2]==null?"":objArray[2].toString());
                    uccDetail.setSectionName(objArray[3]==null?"":objArray[3].toString());
                    uccDetail.setTpValue(objArray[4]==null?"":Codes.nonDecimal.format(Double.valueOf(objArray[4].toString())));
                }
                uccDetails.add(uccDetail);
            }
        }

        return new Gson().toJson(uccDetails);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getLastTransactionDate", method = RequestMethod.GET)
    @ResponseBody
    private String getLastTransactionDate(){
        String tran_date = HibernateUtil.getSingleString("SELECT DATE_FORMAT(MAX(transaction_date), '%d %M %Y') FROM SALE_DETAIL_TEMP");

        return tran_date;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/UCCUniverseAchievement", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    private String UCCUniverseAchievement(String token){

        String where = getWhereClause(token);


        String achievedUCCQuery = "SELECT POSITION_CODE, SUM(IS_UCC), name FROM `ucc_position_wise` WHERE "+where+" and REPORTINGMONTH ='"+Codes.monthNames[Codes.CALENDER_CURRENT_MONTH_NUMBER-1]+","+Codes.CURRENT_YEAR_NUMBER+"' group by POSITION_CODE order by POSITION_CODE ASC";
        ArrayList<Object> achievedUCCResult= HibernateUtil.getDBObjectsFromSQLQuery(achievedUCCQuery);

        String targetUCC = "SELECT POSITION_CODE, count(*) FROM `universe` WHERE "+where+" group by POSITION_CODE order by position_code ASC";
        ArrayList<Object> targetUCCResult= HibernateUtil.getDBObjectsFromSQLQuery(targetUCC);


        List<String> partners = getPartnersArray(token);
        UCCUniverseAchievement universeAchievement = new UCCUniverseAchievement();
        List<UCCUniverseAchievement> uccUniverseAchievements = new ArrayList<>();

        double targetUniverse = 0;
        double achievedUCC = 0;

        if(partners!=null){
            for (String partner : partners){
                String name = "";
                universeAchievement = new UCCUniverseAchievement();
                universeAchievement.setName(getName(partner, achievedUCCResult));
                universeAchievement.setPosition_code(partner);
                targetUniverse = getNumberAgainstPartner(partner, targetUCCResult);
                achievedUCC = getNumberAgainstPartner(partner, achievedUCCResult);

                universeAchievement.setUcc(Codes.nonDecimal.format(achievedUCC));
                universeAchievement.setTotalCustomers(Codes.nonDecimal.format(targetUniverse));
                universeAchievement.setCoverage(String.valueOf(Codes.df.format((achievedUCC/(targetUniverse==0?1:targetUniverse))*100)));


                uccUniverseAchievements.add(universeAchievement);
            }

        }



        return new Gson().toJson(uccUniverseAchievements);
    }



}
