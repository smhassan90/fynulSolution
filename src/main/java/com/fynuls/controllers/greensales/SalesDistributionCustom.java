package com.fynuls.controllers.greensales;

import com.fynuls.entity.SDMonthlyFinalData;
import com.fynuls.entity.SaleDetailTemp;
import com.fynuls.entity.SaleDetailWorking;
import com.fynuls.entity.base.*;
import com.fynuls.utils.HibernateUtil;
import com.fynuls.utils.LogToFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author Syed Muhammad Hassan
 * 10th February, 2022
 */

@Controller
public class SalesDistributionCustom {
    final static Logger LOG = Logger.getLogger("SalesDistribution");
    int autoIncrement = 1;
    String remarks = "";
    String missingData = "";
    @RequestMapping(value = "/automate", method = RequestMethod.GET, params={"startingYear"})
    @ResponseBody
    public String automateSale(int startingYear){
        long startCurrentMilis = Calendar.getInstance().getTimeInMillis();
        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'July,20"+startingYear+"'");
        distributeSales("JUL-"+startingYear,true, 2);
        distributeSales("JUL-"+startingYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'August,20"+startingYear+"'");
        distributeSales("AUG-"+startingYear,true, 2);
        distributeSales("AUG-"+startingYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'September,20"+startingYear+"'");
        distributeSales("SEP-"+startingYear,true, 2);
        distributeSales("SEP-"+startingYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'October,20"+startingYear+"'");
        distributeSales("OCT-"+startingYear,true, 2);
        distributeSales("OCT-"+startingYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'November,20"+startingYear+"'");
        distributeSales("NOV-"+startingYear,true, 2);
        distributeSales("NOV-"+startingYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'December,20"+startingYear+"'");
        distributeSales("DEC-"+startingYear,true, 2);
        distributeSales("DEC-"+startingYear,false, 2);

        int nextYear = startingYear++;

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'January,20"+nextYear+"'");
        distributeSales("JAN-"+nextYear,true, 2);
        distributeSales("JAN-"+nextYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'February,20"+nextYear+"'");
        distributeSales("FEB-"+nextYear,true, 2);
        distributeSales("FEB-"+nextYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'March,20"+nextYear+"'");
        distributeSales("MAR-"+nextYear,true, 2);
        distributeSales("MAR-"+nextYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'April,20"+nextYear+"'");
        distributeSales("APR-"+nextYear,true, 2);
        distributeSales("APR-"+nextYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'May,20"+nextYear+"'");
        distributeSales("MAY-"+nextYear,true, 2);
        distributeSales("MAY-"+nextYear,false, 2);

        HibernateUtil.executeQueryMySQL("DELETE FROM SALE_DETAIL_WORKING WHERE reportingMonth = 'June,20"+nextYear+"'");
        distributeSales("JUN-"+nextYear,true, 2);
        distributeSales("JUN-"+nextYear,false, 2);

        long endCurrentMilis = Calendar.getInstance().getTimeInMillis();
        long totalMilis = ((endCurrentMilis-startCurrentMilis)/60000)/60;
        return "Chul gai or total time laga "+totalMilis+" hours" ;
    }
    @RequestMapping(value = "/SalesDistributionCustom", method = RequestMethod.GET,params={"huid","isMNPSale", "type"})
    @ResponseBody
    public String distributeSales(String huid, boolean isMNPSale, int type){
        PRDGroupOn prdgrpon = null;

        /*
            nature not null
        */
        String condition = "";
        String queryOracle ="";
        if(isMNPSale){
            condition = " nature is null ";
        }else{
            condition = " nature is not null ";
        }
        condition +=" and ";

        if(type==1){
            //It is HUID
            condition += " HUID = "+huid;
        }else if (type==2){
            // It is MTD
            condition+=" transaction_date like '%"+huid+"' ";
        }else{
            condition += " 1=1 ";
        }
        queryOracle = "from SDMonthlyFinalData where "+condition;
        List<SDMonthlyFinalData> sdMonthlyFinalDataList = new ArrayList<>();

        sdMonthlyFinalDataList = (List<SDMonthlyFinalData>) HibernateUtil.getDBObjectsOracle(queryOracle);

        Calendar cal = Calendar.getInstance();
        String reportingMonth ="";
        long startCurrentMilis = Calendar.getInstance().getTimeInMillis();
        int i =0;
        String specialCharactersString = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";
        String batchNumber = HibernateUtil.getNextSaleBatchNumber();
        if(sdMonthlyFinalDataList!=null && sdMonthlyFinalDataList.size()>0) {
            for (SDMonthlyFinalData sdMonthlyFinalData : sdMonthlyFinalDataList) {
                cal.setTime(sdMonthlyFinalData.getTRANSACTION_DATE());
                int month = cal.get(Calendar.MONTH);
                reportingMonth = Codes.monthNames[month] + ","+cal.get(Calendar.YEAR);
                missingData = "";
                ArrayList<PRDGroupOn> prdGroupOns = (ArrayList<PRDGroupOn>) HibernateUtil.getDBObjects("from PRDGroupOn where PRD_NO='" + sdMonthlyFinalData.getPRODUCT_NO() + "'");
                PRDGroupOn prdGroupOn = new PRDGroupOn();
                if (prdGroupOns != null && prdGroupOns.size() > 0) {
                    prdGroupOn = prdGroupOns.get(0);
                }else{
                    remarks+="prdGroupOn was null";
                }
                if(prdGroupOn!=null ) {
                    if (prdGroupOn.getPRD_GRP()!=null && !prdGroupOn.getPRD_GRP().equals("Promotion")) {
                        remarks = "";
                        SaleDetailWorking saleDetail = new SaleDetailWorking();
                        saleDetail.setREPORTINGMONTH(reportingMonth);
                        saleDetail.setHUID(sdMonthlyFinalData.getHUID());
                        saleDetail.setSUID(sdMonthlyFinalData.getSUID());
                        saleDetail.setSNDPOP(sdMonthlyFinalData.getSNDPOP());
                        saleDetail.setCUST_NUMBER(sdMonthlyFinalData.getCUST_NUMBER());
                        saleDetail.setCUST_NAME(sdMonthlyFinalData.getCUST_NAME());
                        String cleanAddress = "";
                        if(sdMonthlyFinalData.getADDRESS()!=null){
                            cleanAddress = sdMonthlyFinalData.getADDRESS().replaceAll("\\P{Print}", "");
                        }
                        saleDetail.setADDRESS(cleanAddress);
                        saleDetail.setPROVIDER_CODE(sdMonthlyFinalData.getPROVIDER_CODE());
                        saleDetail.setLETTER(sdMonthlyFinalData.getLETTER());
                        saleDetail.setTERRITORY(sdMonthlyFinalData.getTERRITORY());
                        saleDetail.setTERRITORY_NAME(sdMonthlyFinalData.getTERRITORY_NAME());
                        saleDetail.setDEPOT(sdMonthlyFinalData.getDEPOT());
                        saleDetail.setSGP(sdMonthlyFinalData.getSGP());
                        saleDetail.setGRP_NAME(sdMonthlyFinalData.getGRP_NAME());
                        saleDetail.setPRODUCT_NO(sdMonthlyFinalData.getPRODUCT_NO());
                        saleDetail.setPRD_NAME(sdMonthlyFinalData.getPRD_NAME());
                        saleDetail.setPRD_SIZE(sdMonthlyFinalData.getPRD_SIZE());
                        saleDetail.setPRICE(sdMonthlyFinalData.getPRICE());

                        saleDetail.setTRANSACTION_DATE(sdMonthlyFinalData.getTRANSACTION_DATE());

                        saleDetail.setINVOICE_NO(sdMonthlyFinalData.getINVOICE_NO());
                        saleDetail.setBATCH(sdMonthlyFinalData.getBATCH());

//              saleDetail.setEXPIRY(sdMonthlyFinalData.getEXPIRY());

                        saleDetail.setCLASS(sdMonthlyFinalData.getCLASS());
                        String DIT_UNIT = "";
                        double NET_VALUE = 0;
                        double NET_QTY_NUM = 0;
                        int unit = 0;
                        String formula = "";
                        formula = getSingleString("SELECT FORMULA FROM BASE_PRD_GRP_ON where PRD_NO='" + sdMonthlyFinalData.getPRODUCT_NO() + "'");

                        String CYP_CON_STR = getSingleString("SELECT CYP_CONVERSION FROM BASE_PRD_GRP_ON where PRD_NO='" + sdMonthlyFinalData.getPRODUCT_NO() + "'");

                        double CYP_CON = 0;
                        if (CYP_CON_STR == null || CYP_CON_STR.equals("")) {
                            CYP_CON = 0;
                        } else {
                            CYP_CON = Double.valueOf(CYP_CON_STR);
                        }
                        DIT_UNIT = getSingleString("SELECT QTY_CONVERSION FROM BASE_PRD_GRP_ON where PRD_NO='" + sdMonthlyFinalData.getPRODUCT_NO() + "'");

                        if (DIT_UNIT != null && !DIT_UNIT.equals("")) {
                            unit = Integer.valueOf(DIT_UNIT);
                        }
                        double sum = Double.valueOf(sdMonthlyFinalData.getNET_QTY()) + sdMonthlyFinalData.getBONUS();

                        NET_QTY_NUM = Double.valueOf(sdMonthlyFinalData.getNET_QTY());
                        NET_VALUE = sdMonthlyFinalData.getNET_VALUE();
                        double bonus = sdMonthlyFinalData.getBONUS();
                        double discounts = sdMonthlyFinalData.getDISCOUNTS();
                        double BONUS_VALUE = sdMonthlyFinalData.getBONUS_VALUE();
                        double bonus_discount = 0;

                        if (sdMonthlyFinalData.getTYPE().equals("R")) {
                            NET_QTY_NUM = NET_QTY_NUM * -1;
                            NET_VALUE = NET_VALUE * -1;
                            bonus = bonus * -1;
                            discounts = discounts * -1;
                            BONUS_VALUE = BONUS_VALUE * -1;
                            sum = sum * -1;
                        }
                        bonus_discount = (discounts+BONUS_VALUE);
                        double EACH_QTY_NUM = sum * unit;
                        double cyp = 0;
                        if (formula!=null &&  formula.equals("D")) {
                            cyp = EACH_QTY_NUM / CYP_CON;
                        } else if (formula.equals("M")) {
                            cyp = EACH_QTY_NUM * CYP_CON;
                        } else if (formula.equals("MD")) {
                            cyp = (EACH_QTY_NUM * 10) / CYP_CON;
                        }

                        double TP_SALE_VALUE = NET_VALUE + BONUS_VALUE;
                        double mnpCommision = 0.0;
                        if (sdMonthlyFinalData.getNATURE() == null) {
                            mnpCommision = TP_SALE_VALUE * 0.1;
                        }
                        double NET_SALE_VALUE = TP_SALE_VALUE - BONUS_VALUE - discounts - mnpCommision;

                        saleDetail.setMNP_COMMISSION(mnpCommision);
                        saleDetail.setNET_SALE_VALUE(NET_SALE_VALUE);
                        saleDetail.setTP_SALE_VALUE(TP_SALE_VALUE);
                        saleDetail.setNET_QTY(NET_QTY_NUM);
                        saleDetail.setNET_VALUE(NET_VALUE);
                        saleDetail.setE_QTY(EACH_QTY_NUM);
                        saleDetail.setBONUS(bonus);
                        saleDetail.setBONUS_VALUE(BONUS_VALUE);
                        saleDetail.setDISCOUNTS(discounts);
                        saleDetail.setCYP(cyp);
                        saleDetail.setGSM_PRODUCT_CODE(sdMonthlyFinalData.getGSM_PRODUCT_CODE());

                        saleDetail.setGSM_TOWN(sdMonthlyFinalData.getGSM_TOWN());
                        saleDetail.setSALEDISTRICT(sdMonthlyFinalData.getDISTRICT());
                        saleDetail.setSALETEHSIL(sdMonthlyFinalData.getTEHSIL());
                        saleDetail.setTYPE(sdMonthlyFinalData.getTYPE());
                        saleDetail.setREMARKS(sdMonthlyFinalData.getREMARKS());
                        saleDetail.setVID(sdMonthlyFinalData.getVID());
                        //saleDetail.setSYSTEM_DATE(sdMonthlyFinalData.getSYSTEM_DATE());

                        saleDetail.setBONUS_DISCOUNT(bonus_discount);
                        saleDetail.setFOC_DISCOUNT(sdMonthlyFinalData.getFOC_DISCOUNT());
                        saleDetail.setBOOKED_BY(sdMonthlyFinalData.getBOOKED_BY());
                        saleDetail.setSECTION_CODE(sdMonthlyFinalData.getSECTION_CODE());
                        saleDetail.setSECTION_NAME(sdMonthlyFinalData.getSECTION_NAME());
                        saleDetail.setNATURE(sdMonthlyFinalData.getNATURE());

                        //VIEW
                        try {
                            String POSITION_ID = "";

                            String tehsil_id = "";

                            if (sdMonthlyFinalData.getPROVIDER_CODE() != null) {
                                tehsil_id = getSingleString("SELECT TEHSIL_ID from BASE_PROVIDER_TEHSIL where PROVIDERCODE = '" + sdMonthlyFinalData.getPROVIDER_CODE() + "'");
                                if (tehsil_id==null || tehsil_id.equals("")) {
                                    remarks += "Tehsil Mapping required";
                                }
                            } else {
                                tehsil_id = getSingleString("SELECT MAX(tehsil_id) from BASE_TEHSIL_SNDPOP where sndpop_id = CONCAT(SUBSTR('" + getCompleteSNDPOP(sdMonthlyFinalData.getSNDPOP()) + "',1,9), SUBSTR('" + sdMonthlyFinalData.getDEPOT() + "',1,3) ,'" + sdMonthlyFinalData.getTERRITORY() + "')");
                                if (tehsil_id==null || tehsil_id.equals("")) {
                                    remarks += "sndpop-TEHSIL Mapping required";
                                }
                            }

                            setLocationDetailsFromTehsilID(tehsil_id, saleDetail, sdMonthlyFinalData.getCLASS());

                            prdgrpon = null;
                            // Fetch Group on against each Sale record
                            String query = "from PRDGroupOn where PRD_NO='" + sdMonthlyFinalData.getPRODUCT_NO() + "'";
                            List<PRDGroupOn> prdgrpons = (List<PRDGroupOn>) HibernateUtil.getDBObjects(query);

                            if (prdgrpons != null && prdgrpons.size() > 0) {
                                prdgrpon = new PRDGroupOn();
                                prdgrpon = prdgrpons.get(0);
                            } else {
                                missingData += "PRDGroupOn is null: HUID=" + sdMonthlyFinalData.getHUID() + "Table : PRDGroupOn PRD_NAME=" + sdMonthlyFinalData.getPRD_NAME()+"\n";
                            }

                            saleDetail.setPOSITION_CODE(POSITION_ID);

                            saleDetail.setGROUPON(prdGroupOn.getGROUP_ON());
                            saleDetail.setGRP(prdGroupOn.getGRP());
                            saleDetail.setGRP_CATEGORY(prdGroupOn.getPRD_CAT());
                            saleDetail.setPRODUCTGROUP(prdGroupOn.getPRD_GRP());
                            saleDetail.setPROVIDERCODE(batchNumber);
                            String positionCode = "";
                            if(saleDetail.getPRD_NAME().contains("WELLMA")){
                                List<String> wellmaTaggedTo = new ArrayList<>();
                                wellmaTaggedTo.add("-HYST-");
                                wellmaTaggedTo.add("-GCC-");


                                List<EmployeeCustomer> employeeCustomers = new ArrayList<>();
                                if (saleDetail.getNATURE() != null && saleDetail.getPROVIDER_CODE() == null) {
                                    employeeCustomers = getPositionCodeFromEmployeeCustomerMapping(saleDetail, wellmaTaggedTo);
                                    if (employeeCustomers != null && employeeCustomers.size() > 0) {
                                        for (EmployeeCustomer employeeCustomer : employeeCustomers) {
                                            positionCode = employeeCustomer.getPOSITION_CODE();
                                        }
                                    }
                                }
                                if (positionCode.equals("")) {

                                    positionCode = getPositionCodeFromProviderCode(saleDetail.getPROVIDER_CODE(), wellmaTaggedTo);
                                    if (positionCode == null || positionCode.equals("")) {
                                        if (saleDetail.getPROVIDER_CODE() != null) {
                                            positionCode = getPOSITION_CODEFromDepot(saleDetail.getDEPOT(), wellmaTaggedTo);
                                            saleDetail.setPOSITION_CODE(positionCode);
                                        } else {
                                            positionCode = getPOSITION_CODEFromTerritoryMapping(sdMonthlyFinalData.getTERRITORY(), wellmaTaggedTo, sdMonthlyFinalData.getHUID());
                                            if (positionCode != null && !positionCode.equals("")) {
                                                saleDetail.setPOSITION_CODE(positionCode);
                                            }
                                        }
                                    }
                                }
                            }else{
                                if(saleDetail.getNATURE()!=null && saleDetail.getPROVIDER_CODE()==null){
                                    List<EmployeeCustomer> empCustomers = getPositionCodeFromEmployeeCustomerMapping(saleDetail,null);
                                    if(empCustomers!=null && empCustomers.size()>0){
                                        saleDetail.setPOSITION_CODE(empCustomers.get(0).getPOSITION_CODE());
                                    }
                                }else if((sdMonthlyFinalData.getPRD_NAME()!=null &&  (sdMonthlyFinalData.getPRD_NAME().contains("OEM")
                                        || sdMonthlyFinalData.getPRD_NAME().contains("ZINKUP")
                                        || sdMonthlyFinalData.getPRD_NAME().contains("FERAVI INJECTION")))
                                        || (saleDetail.getGRP()!=null && saleDetail.getGRP().equals("Nutraceutical"))){


                                    saleDetail = setPositionCodeNotMIOFromProviderCode(saleDetail, sdMonthlyFinalData);
                                }else if((saleDetail.getPRODUCTGROUP().toLowerCase().contains("sathi") ||
                                        saleDetail.getPRD_NAME().toLowerCase().contains("do ")) &&
                                        saleDetail.getPROVIDER_CODE()!=null) {

                                    positionCode = "NATL-UNMAP-HOD-01";

                                }else{
                                    //Deal Others
                                    saleDetail = dealOthers(saleDetail, sdMonthlyFinalData, prdgrpon);
                                }
                            }

                            if(!positionCode.equals("")){
                                saleDetail.setPOSITION_CODE(positionCode);
                            }
                            saleDetail = saveEmployeeDetailsFromPositionCode(saleDetail);
                            saleDetail = getManagedChannel(saleDetail);
                            saleDetail.setGSM_REMARKS(remarks);
                            if(sdMonthlyFinalData.getPROVIDER_CODE()!=null){
                                saleDetail.setUCC_COLUMN(sdMonthlyFinalData.getPROVIDER_CODE());
                            }else{
                                saleDetail.setUCC_COLUMN(sdMonthlyFinalData.getCUST_NUMBER().trim()+sdMonthlyFinalData.getCUST_NAME().trim());
                            }

                            HibernateUtil.save(saleDetail);

                        } catch (Exception e) {
                            //LOG.severe(e.getMessage());
                            printLog("Address:"+sdMonthlyFinalData.getADDRESS()+" HUID:"+sdMonthlyFinalData.getHUID()+" exception:"+e.getMessage()+"\n");
                            //  printError(e, sdMonthlyFinalData.getHUID());
                        }
                    }
                }
                if (!missingData.equals("")) {
                    printLog(missingData);
                }
            }
        }

        long endCurrentMilis = Calendar.getInstance().getTimeInMillis();
        long duration = endCurrentMilis - startCurrentMilis;

        return String.valueOf(duration);
    }

    private SaleDetailWorking dealOthers(SaleDetailWorking saleDetail, SDMonthlyFinalData sdMonthlyFinalData, PRDGroupOn prdgrpon) {
        String POSITION_ID = "";
        if (sdMonthlyFinalData.getPROVIDER_CODE() != null) {
            //When provider code is not null
            if (prdgrpon.getPRD_GRP() !=null && (prdgrpon.getPRD_GRP().contains("Novaject")
                    || prdgrpon.getPRD_GRP().contains("Femi Ject")
                    || prdgrpon.getPRD_GRP().contains("Enofer")
                    || prdgrpon.getPRD_GRP().contains("Enoject"))) {


                List<String> taggedTo = new ArrayList<>();
                taggedTo.add("-PHR-MIO-");
                taggedTo.add("PHR-PS-");
                saleDetail = setPositionCodeFromProviderCode(saleDetail, sdMonthlyFinalData, taggedTo);
                if (saleDetail.getPOSITION_CODE()!=null && !saleDetail.getPOSITION_CODE().contains("PHR-MIO-")
                        && !saleDetail.getPOSITION_CODE().contains("PHR-PS-")) {
                    String territory = sdMonthlyFinalData.getTERRITORY();
                    remarks += "Forcefully sales belongs to MIO";
                    //Fetching Employee information
                    saleDetail = getSaleDetailObject(saleDetail, taggedTo, territory);
                }

            } else {
                List<String> taggedTo = new ArrayList<>();
                taggedTo.add("PHR-MIO");
                taggedTo.add("PHR-PS-");
                taggedTo.add("SF-MIO");
                taggedTo.add("GCC");

                taggedTo.add("SF-SSB");
                taggedTo.add("-SF-SFE-");
                taggedTo.add("-SF-SFSB-");
                taggedTo.add("-SF-QAM-");
                taggedTo.add("-SF-AM-");
                taggedTo.add("UNMAP");


                String whereInClause = getTaggedToWhereClause(taggedTo, "POSITION_ID");

                int count = Integer.valueOf(getSingleString("SELECT count(*) FROM BASE_EMP_TAGGING where " + whereInClause + " tagged_to = '" + sdMonthlyFinalData.getPROVIDER_CODE() + "'"));

                if (count > 1) {
                    taggedTo = new ArrayList<>();
                    taggedTo.add("SF-SSB");
                    taggedTo.add("-SF-SFE-");
                    taggedTo.add("-SF-SFSB-");
                    taggedTo.add("-SF-QAM-");
                    taggedTo.add("-SF-AM-");
                    taggedTo.add("UNMAP");

                } else if (count == 0) {
                    missingData += "BASE_EMP_TAGGING is null: HUID=" + sdMonthlyFinalData.getHUID() + "Table : BASE_EMP_TAGGING " + "SELECT count(*) FROM BASE_EMP_TAGGING where " + whereInClause + " tagged_to = '" + sdMonthlyFinalData.getPROVIDER_CODE() + "'"+"\n";

                }
                saleDetail = setPositionCodeFromProviderCode(saleDetail, sdMonthlyFinalData, taggedTo);


                //  Depends on tagging
                remarks += "Depends on tagging";
                if (saleDetail.getPOSITION_CODE()!=null && saleDetail.getPOSITION_CODE().equals("")) {
                    taggedTo = new ArrayList<>();
                    taggedTo.add("PHR-MIO");
                    taggedTo.add("PHR-PS-");
                    saleDetail = getSaleDetailObject(saleDetail, taggedTo, sdMonthlyFinalData.getTERRITORY());
                    //  Depends on tagging
                    remarks += "Left over products if provider code available. Tagged from Territory Mapping";
                }
            }

        } else {
            //When provider code is null
            /*
                    1.	Sathi 2s, 7s, 8s, of M&P True will be tagged with “NATL-FMCG-HOD-01” by default.
                     */
            if (sdMonthlyFinalData.getPROVIDER_CODE() == null && sdMonthlyFinalData.getNATURE() == null &&
                    (prdgrpon.getGROUP_ON().equals("Sathi 7s") ||
                            prdgrpon.getGROUP_ON().equals("Sathi 8s") ||
                            prdgrpon.getGROUP_ON().equals("Sathi 2s"))) {
                POSITION_ID = "NATL-FMCG-HOD-01";
            }else {


                String nature = sdMonthlyFinalData.getNATURE();
                if (nature != null) {
                    List<EmployeeCustomer> employeeCustomers = new ArrayList<>();
                    //Employee Customer mapping
                    List<String> taggedTo = new ArrayList<>();
                    employeeCustomers = getPositionCodeFromEmployeeCustomerMapping(saleDetail, taggedTo);
                    if (employeeCustomers != null) {
                        if (employeeCustomers.size() > 1) {
                            if (sdMonthlyFinalData.getPRD_NAME() != null && (sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("do ") ||
                                    sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("sathi"))) {
                                taggedTo = new ArrayList<>();
                                taggedTo.add("-FMCG01-");

                            } else if (sdMonthlyFinalData.getPRD_NAME() != null && (sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("touch"))) {
                                taggedTo = new ArrayList<>();
                                taggedTo.add("-FMCG02-");
                            }
                            employeeCustomers = getPositionCodeFromEmployeeCustomerMapping(saleDetail, taggedTo);
                            if (employeeCustomers != null && employeeCustomers.size() == 1) {
                                POSITION_ID = employeeCustomers.get(0).getPOSITION_CODE();
                            }
                        } else if (employeeCustomers.size() == 1) {
                            POSITION_ID = employeeCustomers.get(0).getPOSITION_CODE();

                        }
                    } else {
                        remarks += "Employee Customers mapping not found in direct sales";
                    }

                } else if (sdMonthlyFinalData.getSGP() > 830 && prdgrpon.getGRP() != null
                        && prdgrpon.getGRP().equals("CONDOM")) {
                    if (sdMonthlyFinalData.getPRD_NAME() != null && (sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("do ") ||
                            sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("sathi"))) {

                        /*
                    SELECT * FROM EMPLOYEECUSTOMER WHERE
                     */
                        List<EmployeeCustomer> employeeCustomers = new ArrayList<>();
                        String query = "from EmployeeCustomer where (POSITION_CODE LIKE '%FMCG-%' OR POSITION_CODE LIKE '%FMCG01%') and LOWER(CUSTOMER_CODE)='" + saleDetail.getCUST_NUMBER() + saleDetail.getCUST_NAME().replaceAll("\\s+", "").toLowerCase() + "'";

                        employeeCustomers = (List<EmployeeCustomer>) HibernateUtil.getDBObjects(query);

                        if (employeeCustomers != null) {
                            if (employeeCustomers.size() > 0) {
                                POSITION_ID = employeeCustomers.get(0).getPOSITION_CODE();
                            }
                        }

                        if (POSITION_ID.equals("")) {
                            if (sdMonthlyFinalData.getSECTION_NAME() != null) {
                                POSITION_ID = getSingleString("SELECT POSITIONCODE FROM BASE_DEPOT_SECTION_TO_POSITION WHERE (POSITIONCODE LIKE '%FMCG-%' OR POSITIONCODE LIKE '%FMCG01%') AND NewSectionCode = '" + saleDetail.getDEPOT() + saleDetail.getSECTION_NAME().trim().replaceAll("\\s", "") + "'");
                            } else {
                                remarks += "HUID: " + sdMonthlyFinalData.getHUID() + " Section Name is null";
                            }
                            if (POSITION_ID.equals("")) {
                                remarks += "BASE_DEPOT_SECTION_TO_POSITION Mapping required";
                            }
                        }


                    } else if (sdMonthlyFinalData.getPRD_NAME() != null && sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("touch")) {
                        List<EmployeeCustomer> employeeCustomers = new ArrayList<>();
                        String query = "from EmployeeCustomer where (POSITION_CODE LIKE '%FMCG-%' OR POSITION_CODE LIKE '%FMCG02%') and LOWER(CUSTOMER_CODE)='" + saleDetail.getCUST_NUMBER() + saleDetail.getCUST_NAME().replaceAll("\\s+", "").toLowerCase() + "'";

                        employeeCustomers = (List<EmployeeCustomer>) HibernateUtil.getDBObjects(query);

                        if (employeeCustomers != null) {
                            if (employeeCustomers.size() > 0) {
                                POSITION_ID = employeeCustomers.get(0).getPOSITION_CODE();
                            }
                        }
                        if (POSITION_ID.equals("")) {
                            if (sdMonthlyFinalData.getSECTION_NAME() != null) {
                                POSITION_ID = getSingleString("SELECT POSITIONCODE FROM BASE_DEPOT_SECTION_TO_POSITION WHERE (POSITIONCODE LIKE '%FMCG-%' OR POSITIONCODE LIKE '%FMCG02%') AND NewSectionCode = '" + saleDetail.getDEPOT() + saleDetail.getSECTION_NAME().trim().replaceAll("\\s", "") + "'");

                            } else {
                                remarks += "HUID: " + sdMonthlyFinalData.getHUID() + " Section Name is null";
                            }
                            if (POSITION_ID.equals("")) {
                                remarks += "BASE_DEPOT_SECTION_TO_POSITION Mapping required";
                            }
                        }
                    }
                    saleDetail.setPOSITION_CODE(POSITION_ID);
                    remarks += "Tagging from new Section Mapping with concatenation";
                } else if (sdMonthlyFinalData.getSGP() < 830) {
                    if (prdgrpon.getGRP() != null && prdgrpon.getGRP().equals("CONDOM")) {
                        if (sdMonthlyFinalData.getPRD_NAME() != null && sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("do ") ||
                                sdMonthlyFinalData.getPRD_NAME().toLowerCase().contains("sathi")) {
                            List<EmployeeCustomer> employeeCustomers = new ArrayList<>();
                            String query = "from EmployeeCustomer where (POSITION_CODE LIKE '%FMCG-%' OR POSITION_CODE LIKE '%FMCG01%') and LOWER(CUSTOMER_CODE)='" + saleDetail.getCUST_NUMBER() + saleDetail.getCUST_NAME().replaceAll("\\s+", "").toLowerCase() + "'";

                            employeeCustomers = (List<EmployeeCustomer>) HibernateUtil.getDBObjects(query);

                            if (employeeCustomers != null) {
                                if (employeeCustomers.size() > 0) {
                                    POSITION_ID = employeeCustomers.get(0).getPOSITION_CODE();
                                }
                            }

                            if (POSITION_ID.equals("")) {
                                POSITION_ID = getSingleString("SELECT position_code FROM base_depot_territory_to_position WHERE (position_code LIKE '%-FMCG-%' OR position_code LIKE '%-FMCG01-%') AND depot_territory = '" + saleDetail.getDEPOT() + "" + saleDetail.getTERRITORY() + "'");

                                saleDetail.setPOSITION_CODE(POSITION_ID);
                                if (saleDetail.getPOSITION_CODE().equals("")) {
                                    POSITION_ID = getSingleString("SELECT position_code FROM base_depot_territory_to_position WHERE position_code LIKE '%ASM%' AND depot_territory = '" + saleDetail.getDEPOT() + "" + saleDetail.getTERRITORY() + "'");
                                    saleDetail.setPOSITION_CODE(POSITION_ID);
                                    if (saleDetail.getPOSITION_CODE().equals("")) {
                                        remarks += "Territory mapping required with ASM or SPO";
                                    }
                                }
                            }
                        } else {
                            ArrayList<String> taggedTo = new ArrayList<>();
                            taggedTo.add("PHR-MIO");
                            taggedTo.add("PHR-PS-");
                            saleDetail = getSaleDetailObject(saleDetail, taggedTo, saleDetail.getTERRITORY());
                        }
                    } else {
                        ArrayList<String> taggedTo = new ArrayList<>();
                        taggedTo.add("PHR-MIO");
                        taggedTo.add("PHR-PS-");
                        saleDetail = getSaleDetailObject(saleDetail, taggedTo, saleDetail.getTERRITORY());
                        remarks += "Town mapping for MIO";

                    }
                }

            }
        }

        if(!POSITION_ID.equals("")){
            saleDetail.setPOSITION_CODE(POSITION_ID);
        }

        return saleDetail;
    }

    private List<EmployeeCustomer> getPositionCodeFromEmployeeCustomerMapping(SaleDetailWorking saleDetail, List<String> taggedTo) {
        List<EmployeeCustomer> employeeCustomers = new ArrayList<>();
        String query = "";
        String whereClause = "";
        if(taggedTo!=null && taggedTo.size()>0){
            whereClause = getTaggedToWhereClause(taggedTo,"POSITION_CODE");
        }
        query = "from EmployeeCustomer where "+whereClause+" LOWER(CUSTOMER_CODE)='"+saleDetail.getCUST_NUMBER()+saleDetail.getCUST_NAME().replaceAll("\\s+","").toLowerCase()+saleDetail.getINVOICE_NO()+"'";

        employeeCustomers = (List<EmployeeCustomer>) HibernateUtil.getDBObjects(query);
        return employeeCustomers;
    }

    private String getPOSITION_CODEFromTerritoryMapping(String territory, double HUID) {
        String positionCode = "";
        positionCode = getSingleString("SELECT MAX(EMP_ID) FROM base_territory_emp_mapping where (EMP_ID like '%PHR-MIO-%' OR EMP_ID like '%PHR-PS-%')  AND territory_code ='"+territory+"'");

        if("".equals(positionCode)){
            missingData += "HUID:"+HUID+" base_territory_emp_mapping is null: query = " + "SELECT MAX(EMP_ID) FROM base_territory_emp_mapping where EMP_ID like '%MIO%' AND territory_code ='"+territory+"'"+"\n";
        }
        return positionCode;
    }


    private String getPOSITION_CODEFromTerritoryMapping(String territory, List<String> taggedTo, double HUID) {
        String positionCode = "";
        String whereClause = getTaggedToWhereClause(taggedTo, "EMP_ID");
        positionCode = getSingleString("SELECT MAX(EMP_ID) FROM base_territory_emp_mapping where "+whereClause+"  territory_code ='"+territory+"'");

        if("".equals(positionCode)){
            missingData +="HUID:"+HUID+ " base_territory_emp_mapping is null: query = " + "SELECT MAX(EMP_ID) FROM base_territory_emp_mapping where "+whereClause+" EMP_ID like '%MIO%' AND territory_code ='"+territory+"'"+"\n";
        }
        return positionCode;
    }

    private String getTeam(String prd_grp) {
        String team = "";
        if(prd_grp.contains("Sathi") ||
                prd_grp.contains("Do")){
            team = "FMCG01";

        }else if(prd_grp.contains("Touch")){
            team = "FMCG02";
        }
        return team;
    }

    private String getPOSITION_CODEFromDepot(double depot, List<String> taggedTo) {
        String whereClause = getTaggedToWhereClause(taggedTo,"POSITION_CODE");

        String POSITION_ID = getSingleString("SELECT POSITION_CODE FROM BASE_EMP_DEPOT_MAPPING where "+whereClause+" DEPOT_CODE="+depot);

        return POSITION_ID;
    }

    private String getPOSITION_CODEFromDepot(double depot, String team) {
        String POSITION_ID = getSingleString("SELECT POSITION_CODE FROM BASE_EMP_DEPOT_MAPPING where POSITION_CODE LIKE '%"+team+"%' and DEPOT_CODE="+depot);

        return POSITION_ID;
    }

    private void setMissingData(String query, double HUID){
        missingData += "HUID:"+HUID +" Query : "+query+"\n";
    }

    private SaleDetailWorking setLocationDetailsFromTehsilID(String tehsil_id, SaleDetailWorking saleDetail, String CLASS ) {
        String query = "";
        query = "SELECT name from base_tehsil_master where id = " + tehsil_id;
        String TEHSIL = getSingleString(query);

        query="SELECT dist_id from base_dist_tehsil where tehsil_id = " + tehsil_id;
        String dist_id = getSingleString(query);

        query = "SELECT name from base_district_master where id = " + dist_id;
        String DISTRICT = getSingleString("SELECT name from base_district_master where id = " + dist_id);

        query = "SELECT region_id from base_dist_region_mapping where dist_id = '" + dist_id + "'";
        String region_id = getSingleString(query);

        query = "SELECT name from base_region_master where id = '" + region_id + "'";
        String region = getSingleString(query);

        query = "SELECT province_id from base_dist_province where dist_id = '" + dist_id + "'";
        String province_id = getSingleString(query);

        query = "SELECT name from base_province_master where id = '" + province_id + "'";
        String province = getSingleString(query);

        query = "SELECT channel from base_mnp_channel_mapping where class_code = '" + CLASS + "'";
        String channel = getSingleString(query);


        saleDetail.setDISTRICT(DISTRICT);
        saleDetail.setTEHSIL(TEHSIL);
        saleDetail.setPROVINCE(province);
        saleDetail.setREGION(region);
        saleDetail.setCHANNEL(channel);
        return saleDetail;
    }

    private String getSingleString(String query){
        String data = "";
        data = HibernateUtil.getSingleString(query);
        if(data!=null && data.equals("")){
            missingData += query + "\n";
        }
        return data;
    }

    private SaleDetailWorking saveEmployeeDetailsFromPositionCode(SaleDetailWorking saleDetail) {
        String POSITION_ID = saleDetail.getPOSITION_CODE();
        if(POSITION_ID!=null && !POSITION_ID.equals("")) {
            String query = "";
            query = "SELECT sas_id from BASE_EMPID_POSITIONID_MAPPING where position_id = '" + POSITION_ID + "'";
            String sas_id = getSingleString(query);


            query = "SELECT EMPLOYEE_ID from BASE_EMPID_POSITIONID_MAPPING where position_id = '" + POSITION_ID + "'";
            String EMPLOYEE_ID = getSingleString(query);



            query = "SELECT teamregion_id from base_emp_position_teamregion where position_id = '" + POSITION_ID + "'";
            String teamregion_id = getSingleString(query);



            String teamregion = getSingleString("SELECT NAME from base_team_region where id =" + teamregion_id);
            String TEAM_ID = getSingleString("SELECT TEAM_ID from BASE_EMP_POSITION_TEAM where POSITION_ID = '" + POSITION_ID + "'");
            String TEAM = getSingleString("SELECT name from BASE_TEAM_DEPT where id = " + TEAM_ID);
            String zone_id = getSingleString("SELECT zone_id from base_emp_zone_mapping where position_id = '" + POSITION_ID + "'");
            String zone = getSingleString("SELECT zone from BASE_ZONE where id = " + zone_id);
            String subzone = getSingleString("SELECT subzone from BASE_ZONE where id = " + zone_id);

            saleDetail.setSASCODE(sas_id);
            saleDetail.setEMPLOYEEID(EMPLOYEE_ID);
            saleDetail.setTEAMREGION(teamregion);
            saleDetail.setZONE(zone);
            saleDetail.setSUBZONE(subzone);
            saleDetail.setTEAM(TEAM);
            saleDetail.setPOSITION_CODE(POSITION_ID);
        }
        return saleDetail;
    }

    private void printLog(String mesg){
        try{
            FileWriter fw=new FileWriter("D:\\log\\log.txt",true);
            fw.write(mesg);
            fw.close();
        }catch(Exception e){System.out.println(e);}
        System.out.println("Success...");
    }

    private void printError(Exception e, double huid) {
        LogToFile.log(null,"severe", "e.getMessage() : "+ e.getMessage() +
                " \n e.getCause() : "+ e.getCause() +
                " \n e.e.getStackTrace()"+e.getStackTrace().toString() +
                " \n HUID : "+huid
        );

    }

    private SaleDetailWorking getManagedChannel(SaleDetailWorking saleDetail) {
        if(saleDetail.getPROVIDER_CODE()!=null && !saleDetail.getPROVIDER_CODE().equals("")
                && !saleDetail.getCHANNEL().equals("Provider")){
            saleDetail.setCHANNEL("Provider");
        } else if(saleDetail.getCHANNEL().equals("Provider")
                && saleDetail.getPROVIDER_CODE()==null){
            saleDetail.setCHANNEL("Pharmacies");
        }
        return saleDetail;
    }

    private SaleDetailWorking getSaleDetailObject(SaleDetailWorking saleDetail, List<String> saleTo, String territory){
        String employeePositionId = "";
        //Fetching Employee information
        String where = getTaggedToWhereClause(saleTo,"emp_id");

        String countSharing =  getSingleString("SELECT COUNT(*) FROM BASE_TERRITORY_EMP_MAPPING where "+where+" territory_code='"+territory+"'");
        if(Integer.valueOf(countSharing)==0){
            employeePositionId = getPOSITION_CODEFromDepot(saleDetail.getDEPOT(), saleTo);
            saleDetail = setLocationInSales(employeePositionId, saleDetail);
        }else if(Integer.valueOf(countSharing)==1){
            employeePositionId = getSingleString("SELECT EMP_ID FROM BASE_TERRITORY_EMP_MAPPING where "+where+" territory_code='"+territory+"'");
            saleDetail = setLocationInSales(employeePositionId, saleDetail);
        }else{
            boolean isFirstTime = true;
            double id = 0;
            ArrayList<TerritoryEmployeeMapping> territoryEmployeeMappings = (ArrayList<TerritoryEmployeeMapping>) HibernateUtil.getDBObjects("from TerritoryEmployeeMapping where "+where+" TERRITORY_CODE='"+territory+"'");
            for(int i =0 ; i<territoryEmployeeMappings.size();i++){
                SaleDetailWorking copySale = new SaleDetailWorking();
                try {
                    copySale = (SaleDetailWorking)saleDetail.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                TerritoryEmployeeMapping territoryEmployeeMapping = new TerritoryEmployeeMapping();
                territoryEmployeeMapping = territoryEmployeeMappings.get(i);
                copySale = breakSalesOnPercentage(territoryEmployeeMapping, copySale, territoryEmployeeMappings.size());
                copySale = setLocationInSales(territoryEmployeeMapping.getEMP_ID(), copySale);
                copySale = saveEmployeeDetailsFromPositionCode( copySale);
                autoIncrement ++;

                copySale.setHUID(saleDetail.getHUID());
                if(i+1<territoryEmployeeMappings.size()){
                    copySale = getManagedChannel(copySale);
                    HibernateUtil.save(copySale);
                }else{
                    saleDetail = copySale;
                }
            }
        }
        return saleDetail;
    }

    private SaleDetailWorking setLocationInSales(String employeePositionId, SaleDetailWorking saleDetail) {
        if(employeePositionId!=null && !employeePositionId.equals("")) {
            String employeeID = getSingleString("SELECT EMPLOYEE_ID FROM BASE_EMPID_POSITIONID_MAPPING where POSITION_ID='" + employeePositionId + "'");
            ArrayList<Employee> employees =
                    (ArrayList<Employee>) HibernateUtil.getDBObjects("from Employee where ID='" + employeeID + "'");
            Employee employee = new Employee();
            if (employees != null && employees.size() > 0) {
                employee = employees.get(0);
            }
            String teamRegionId = getSingleString("SELECT TEAMREGION_ID FROM BASE_EMP_POSITION_TEAMREGION where POSITION_ID='" + employeePositionId + "'");
            String teamRegion = getSingleString("SELECT NAME FROM BASE_TEAM_REGION where id= " + teamRegionId);
            String zoneId = getSingleString("SELECT ZONE_ID from BASE_EMP_ZONE_MAPPING where POSITION_ID='" + employeePositionId + "'");
            ArrayList<Zone> zoneList = (ArrayList<Zone>) HibernateUtil.getDBObjects("from Zone where id =  " + zoneId);
            Zone zone = new Zone();
            if (zoneList != null && zoneList.size() > 0) {
                zone = zoneList.get(0);
            }else{
                if(!zoneId.equals(""))
                    setMissingData("from Zone where id =  " + zoneId, saleDetail.getHUID());
            }
            String teamId = getSingleString("SELECT TEAM_ID FROM BASE_EMP_POSITION_TEAM where POSITION_ID='" + employeePositionId + "'");
            String team = getSingleString("SELECT NAME FROM BASE_TEAM_DEPT where id= " + teamId);
            String strZone = zone.getZONE();
            String strSubZone = zone.getSUBZONE();
            saleDetail.setTEAM(team);
            saleDetail.setZONE(strZone);
            saleDetail.setSUBZONE(strSubZone);
            saleDetail.setPOSITION_CODE(employeePositionId);
            saleDetail.setEMPLOYEEID(employeeID);
            saleDetail.setTEAMREGION(teamRegion);
        }
        return saleDetail;
    }
    private double roundOff(double val)
    {
        DecimalFormat f = new DecimalFormat("##.00");
        return Double.valueOf(f.format(val));
    }

    private SaleDetailWorking breakSalesOnPercentage(TerritoryEmployeeMapping territoryEmployeeMapping, SaleDetailWorking saleDetail, double count) {

        double percSharing = 1/count;
        saleDetail.setPRICE(roundOff(saleDetail.getPRICE()*percSharing));
        saleDetail.setNET_QTY(roundOff(saleDetail.getNET_QTY() * percSharing));
        saleDetail.setNET_VALUE(roundOff(saleDetail.getNET_VALUE() * percSharing));
        saleDetail.setBONUS(roundOff(saleDetail.getBONUS()*percSharing));
        saleDetail.setBONUS_VALUE(roundOff(saleDetail.getBONUS_VALUE()*percSharing));
        saleDetail.setDISCOUNTS(roundOff(saleDetail.getDISCOUNTS()*percSharing));
        saleDetail.setCYP(roundOff(saleDetail.getCYP()*percSharing));
        saleDetail.setBONUS_DISCOUNT(roundOff(saleDetail.getBONUS_DISCOUNT()*percSharing));
        saleDetail.setFOC_DISCOUNT(roundOff(saleDetail.getFOC_DISCOUNT()*percSharing));
        saleDetail.setE_QTY(roundOff(saleDetail.getE_QTY()*percSharing));
        saleDetail.setTP_SALE_VALUE(roundOff(saleDetail.getTP_SALE_VALUE()*percSharing));
        saleDetail.setNET_SALE_VALUE(roundOff(saleDetail.getNET_SALE_VALUE()*percSharing));
        saleDetail.setMNP_COMMISSION(roundOff(saleDetail.getMNP_COMMISSION()*percSharing));
        return saleDetail;
    }
    /*
    POSITION_ID
     */
    private String getTaggedToWhereClause(List<String> taggedTo, String columnName){
        boolean isFirst = true;
        String where= "";
        if(taggedTo!=null && taggedTo.size()>0){
            for(String tagged : taggedTo){
                if(!isFirst){
                    where +=" OR ";

                }else{
                    where += "(";
                    isFirst = false;
                }
                where += " "+columnName+" LIKE '%"+tagged+"%' ";
            }
            where +=") AND ";
        }
        return where;
    }

    private SaleDetailWorking setPositionCodeFromProviderCode(SaleDetailWorking saleDetail, SDMonthlyFinalData sdMonthlyFinalData, List<String> taggedTo){
        String whereInClause = getTaggedToWhereClause(taggedTo, "POSITION_ID");


        String query = "SELECT position_id from BASE_EMP_TAGGING where "+whereInClause+" tagged_to = '" + sdMonthlyFinalData.getPROVIDER_CODE() + "'";

        String POSITION_ID = getSingleString(query);
        if(!POSITION_ID.equals("")){
            remarks+="Depends on tagging";
        }else{
            POSITION_ID = getPOSITION_CODEFromTerritoryMapping(sdMonthlyFinalData.getTERRITORY(), sdMonthlyFinalData.getHUID());
            if(!POSITION_ID.equals("")) {
                remarks+="Provider tagging not found, territory mapping used for position code";
            }else{
                List<String> phrMapping = new ArrayList<>();
                phrMapping.add("PHR-MIO-");
                phrMapping.add("PHR-PS-");
                POSITION_ID = getPOSITION_CODEFromDepot(sdMonthlyFinalData.getDEPOT(), phrMapping);
                remarks+="Provider tagging not found, territory mapping not found, depot mapping used for position code";
            }
        }
        saleDetail.setPOSITION_CODE(POSITION_ID);
        saleDetail = saveEmployeeDetailsFromPositionCode(saleDetail);

        return saleDetail;
    }

    private String getPositionCodeFromProviderCode(String providerCode, List<String> taggedTo){
        String whereClause = getTaggedToWhereClause(taggedTo, "POSITION_ID");
        String positionCode = "";
        String query = "SELECT position_id from BASE_EMP_TAGGING where "+whereClause+" tagged_to = '" + providerCode + "'";
        List<Object> objs = HibernateUtil.getDBObjectsFromSQLQuery(query);

        if(objs!=null){
            for(Object obj : objs){
                positionCode = obj.toString();
            }
        }

        return positionCode;
    }

/*
1. ORS ZINK OEM DEPo queen
	Provider null or not null
	it will distribute against IPC and HS

	HS will be given if there is direct mapping with providercode
	if not found in HS team then
	IPC-CHO
	IPC-SIA
	IPC-SIO
	It will find against provider
	then territory
	then depot


2. left over neutraciticals
	It will find against IPC providers.
	IPC-SIA, IPC-SIA, IPC-IPCO

	left over will be given to HS from
	1. Provider
	2. Territory
	3. Depot

 */

    private SaleDetailWorking setPositionCodeNotMIOFromProviderCode(SaleDetailWorking saleDetail, SDMonthlyFinalData sdMonthlyFinalData){
        String POSITION_ID = "";
        List<String> taggedTo = new ArrayList<>();
        if(sdMonthlyFinalData.getPROVIDER_CODE() !=null && !sdMonthlyFinalData.getPROVIDER_CODE().equals("")) {
            taggedTo = new ArrayList<>();
            taggedTo.add("-GCC-");
            POSITION_ID = getPositionCodeFromProviderCode(sdMonthlyFinalData.getPROVIDER_CODE(), taggedTo);

        }
        if(POSITION_ID.equals("")) {
            if (sdMonthlyFinalData.getPRD_NAME().contains("OEM")
                    || sdMonthlyFinalData.getPRD_NAME().contains("ZINKUP")
                    || sdMonthlyFinalData.getPRD_NAME().contains("FERAVI INJECTION")) {


                if (sdMonthlyFinalData.getPROVIDER_CODE() !=null && !sdMonthlyFinalData.getPROVIDER_CODE().equals("")) {
                    taggedTo = new ArrayList<>();
                    taggedTo.add("-SF-");
                    POSITION_ID = getPositionCodeFromProviderCode(sdMonthlyFinalData.getPROVIDER_CODE(), taggedTo);

                }
                if (POSITION_ID.equals("")) {
                    taggedTo = new ArrayList<>();
                    taggedTo.add("SF-SSB");
                    taggedTo.add("-SF-SFE-");
                    taggedTo.add("-SF-SFSB-");
                    taggedTo.add("-SF-QAM-");
                    taggedTo.add("-SF-AM-");
                    POSITION_ID = getPositionCodeFromProviderCode(sdMonthlyFinalData.getPROVIDER_CODE(), taggedTo);
                    if (POSITION_ID.equals("")) {
                        POSITION_ID = getPOSITION_CODEFromTerritoryMapping(sdMonthlyFinalData.getTERRITORY(), taggedTo, sdMonthlyFinalData.getHUID());
                        if (POSITION_ID.equals("")) {
                            POSITION_ID = getPOSITION_CODEFromDepot(sdMonthlyFinalData.getDEPOT(), taggedTo);
                        }
                    }
                }
            } else if (saleDetail.getGRP() != null && saleDetail.getGRP().equals("Nutraceutical")) {
                taggedTo = new ArrayList<>();

                if (sdMonthlyFinalData.getPROVIDER_CODE() != null && !sdMonthlyFinalData.getPROVIDER_CODE().equals("")) {
                    taggedTo.add("SF-SSB");
                    taggedTo.add("-SF-SFE-");
                    taggedTo.add("-SF-SFSB-");
                    taggedTo.add("-SF-QAM-");
                    taggedTo.add("-SF-AM-");

                    POSITION_ID = getPositionCodeFromProviderCode(sdMonthlyFinalData.getPROVIDER_CODE(), taggedTo);

                }
                if (POSITION_ID.equals("")) {
                    taggedTo = new ArrayList<>();
                    taggedTo.add("-SF-");
                    POSITION_ID = getPositionCodeFromProviderCode(sdMonthlyFinalData.getPROVIDER_CODE(), taggedTo);
                    if (POSITION_ID.equals("")) {
                        POSITION_ID = getPOSITION_CODEFromTerritoryMapping(sdMonthlyFinalData.getTERRITORY(), taggedTo, sdMonthlyFinalData.getHUID());
                        if (POSITION_ID.equals("")) {
                            POSITION_ID = getPOSITION_CODEFromDepot(sdMonthlyFinalData.getDEPOT(), taggedTo);
                        }
                    }
                }
            }
        }


        if(POSITION_ID.equals("")){
            remarks+="provider mapping not found";
        }else{
            saleDetail.setPOSITION_CODE(POSITION_ID);
        }

        return saleDetail;
    }

    private String getCompleteSNDPOP(String finalSNDPOP){
        String processAble = "";

        if(isNumeric(finalSNDPOP) && (finalSNDPOP.substring(finalSNDPOP.length() - 3,finalSNDPOP.length()-2).equals("E") || finalSNDPOP.substring(finalSNDPOP.length() - 4,finalSNDPOP.length()-3).equals("E"))){
            processAble = String.valueOf(new BigDecimal(finalSNDPOP).longValue());
        }else{
            processAble = finalSNDPOP;
        }

        final String trailingZero = "0";

        for(int i = 0; processAble.length()<15; i++){
            processAble =trailingZero+processAble;
        }


        return processAble;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
