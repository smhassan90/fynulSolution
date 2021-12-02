package com.fynuls.controllers.greensales;


import com.fynuls.entity.SDMonthlyFinalData;
import com.fynuls.entity.SaleDetail;
import com.fynuls.entity.SaleDetailTemp;
import com.fynuls.utils.HibernateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

/**
 * @author Syed Muhammad Hassan
 * 28th October, 2021
 */


@Controller
public class Transfer {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/saleTransfer", method = RequestMethod.GET,params={"transactionDate"})
    @ResponseBody
    public String distributeSales(String transactionDate){
        List<SDMonthlyFinalData> sdMonthlyFinalDataOracle = (List<SDMonthlyFinalData>) HibernateUtil.getDBObjectsOracle("from SDMonthlyFinalData where HUID = 95169107");
        return "Transfer was " + HibernateUtil.saveOrUpdateListMySQL(sdMonthlyFinalDataOracle);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/sale", method = RequestMethod.GET,params={"reportingMonth"})
    @ResponseBody
    public String sale(String reportingMonth){
        List<SaleDetailTemp> saleDetailTempList = (List<SaleDetailTemp>) HibernateUtil.getDBObjectsOracle("from SaleDetailTemp where REPORTINGMONTH = '"+reportingMonth+"'");


        return "Transfer was " + HibernateUtil.saveOrUpdateListMySQL(saleDetailTempList);
    }
}
