package com.fynuls.controllers.greensales;


import com.fynuls.entity.SDMonthlyFinalData;
import com.fynuls.entity.base.MissingMapping;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Syed Muhammad Hassan
 * 28th October, 2021
 */

@Controller
public class MissingData {

    @RequestMapping(value = "/missingTehsil", method = RequestMethod.GET,params={"transaction_date"})
    @ResponseBody
    public String distributeSales(String transaction_date){
        List<SDMonthlyFinalData> sdMonthlyFinalDataList = (List<SDMonthlyFinalData>) HibernateUtil.getDBObjectsOracle("from SDMonthlyFinalData where PROVIDER_CODE is not null and transaction_date  like '%"+transaction_date+"%'");

        long startCurrentMilis = Calendar.getInstance().getTimeInMillis();
        int i =0;
        int issueCounter = 0;
        MissingMapping missingMapping = new MissingMapping();
        List<MissingMapping> missingMappingList = new ArrayList<>();
        if(sdMonthlyFinalDataList!=null && sdMonthlyFinalDataList.size()>0) {
            for (SDMonthlyFinalData sdMonthlyFinalData : sdMonthlyFinalDataList) {

                String tehsil_id = "";
                tehsil_id = HibernateUtil.getSingleString("SELECT TEHSIL_ID from BASE_PROVIDER_TEHSIL where PROVIDERCODE = '" + sdMonthlyFinalData.getPROVIDER_CODE() + "'");

                if (tehsil_id == null || tehsil_id.equals("")) {
                    issueCounter++;
                    missingMapping = new MissingMapping();
                    missingMapping.setFIRST(sdMonthlyFinalData.getPROVIDER_CODE());
                    missingMapping.setSECOND(String.valueOf(sdMonthlyFinalData.getHUID()));
                    missingMapping.setType(1);
                    boolean duplicate = false;
                    for (MissingMapping item: missingMappingList) {
                        if (item.getFIRST().contains(sdMonthlyFinalData.getPROVIDER_CODE())) {
                            duplicate = true;
                        }

                    }
                    if(!duplicate){
                        missingMappingList.add(missingMapping);
                    }

                }
            }
        }
        boolean isSuccessful = HibernateUtil.saveOrUpdateListOracle(missingMappingList);
        return "Issue counter "+ issueCounter;
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getInvalidProviderCodes", method = RequestMethod.GET,params={"transaction_date","doUpdate"})
    @ResponseBody
    public String getInvalidProviderCodes(String transaction_date, boolean doUpdate) {
        if(doUpdate) {
            String updateQuery = "UPDATE SD_MONTHLY_FINAL_DATA set provider_code = RTRIM(LTRIM(REGEXP_REPLACE(provider_code,'(^[[:space:]]*|[[:space:]]*$)'))) where TRANSACTION_DATE LIKE '%" + transaction_date + "%'";
            HibernateUtil.executeQueryOracle(updateQuery);
        }

        String query = "SELECT * FROM SD_MONTHLY_FINAL_DATA WHERE TRANSACTION_DATE LIKE '%"+transaction_date+"%' and provider_code not in (SELECT MPV_CODE FROM SAL_SM_PROVIDER)";

        ArrayList<Object> sdMonthlyFinalData =  HibernateUtil.getDBObjectsFromSQLQueryClassOracle(query, SDMonthlyFinalData.class);
        if(sdMonthlyFinalData!=null){
            for(Object obj : sdMonthlyFinalData){
                int uhhu = 9;
            }
        }
        return new Gson().toJson(sdMonthlyFinalData);
    }

}
