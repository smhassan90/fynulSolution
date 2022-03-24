package com.fynuls.controllers.greensales;

import com.fynuls.dal.StrategyDAL;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Syed Muhammad Hassan
 * 24th February, 2022
 */

@Controller
public class Strategy {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getStrategyDescription", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public String getStrategyDescription(String token){
        StrategyDAL strategyDAL = new StrategyDAL();
        strategyDAL.setEmpID("E-0540");
        strategyDAL.setName("Muhammad");
        strategyDAL.setProductivityStatus(1);
        strategyDAL.setStrategicStatus(2);
        strategyDAL.setUccStatus(3);
        strategyDAL.setRegion("Islamabad");
        strategyDAL.setTeam("FMCG-01");
        strategyDAL.setStatus(Codes.ALL_OK);

        return new Gson().toJson(strategyDAL);
    }
}