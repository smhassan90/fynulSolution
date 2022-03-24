package com.fynuls.controllers.greensales;

import com.fynuls.dal.InfoActionPlanDAL;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Syed Muhammad Hassan
 * 14th March, 2022
 */

@Controller
public class ActionPlan {
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/getActionPlanInfo", method = RequestMethod.GET, params = {"token", "reportingMonth", "partnerID"})
    @ResponseBody
    public String getPartners(String token, String reportingMonth, String partnerID) {
        InfoActionPlanDAL infoActionPlanDAL = new InfoActionPlanDAL();
        infoActionPlanDAL.setEmployeeId("E-1009");
        infoActionPlanDAL.setEmployeeName("Anas Ahmed");
        infoActionPlanDAL.setNoOfShops(420);
        infoActionPlanDAL.setProductivityStatus(1);
        infoActionPlanDAL.setReachStatus(3);
        infoActionPlanDAL.setStrategicBrandStatus(2);
        infoActionPlanDAL.setRegion("Karachi");
        infoActionPlanDAL.setTeam("FMCG-1");

        return new Gson().toJson(infoActionPlanDAL);
    }
}