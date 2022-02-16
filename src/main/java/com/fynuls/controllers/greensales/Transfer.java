package com.fynuls.controllers.greensales;


import com.fynuls.entity.SDMonthlyFinalData;
import com.fynuls.entity.SaleDetail;
import com.fynuls.entity.SaleDetailTemp;
import com.fynuls.entity.SaleDetailTempIKON;
import com.fynuls.entity.base.Employee;
import com.fynuls.entity.base.Target;
import com.fynuls.utils.HibernateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
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
    @RequestMapping(value = "/sale", method = RequestMethod.GET,params={"transactionDate"})
    @ResponseBody
    public String sale(String transactionDate){

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
        List<SaleDetailTemp> saleDetailTempList = (List<SaleDetailTemp>) HibernateUtil.getDBObjects("from SaleDetailTemp where transaction_date like '%"+transactionDate+"%'");
        List<SaleDetailTempIKON> saleDetailTempIKONS = new ArrayList<>();
        SaleDetailTempIKON saleDetailTempIKON = new SaleDetailTempIKON();
        for(SaleDetailTemp sdt : saleDetailTempList){
            saleDetailTempIKON = new SaleDetailTempIKON();
            saleDetailTempIKON.setADDRESS(sdt.getADDRESS());
            saleDetailTempIKON.setBATCH(sdt.getBATCH());
            saleDetailTempIKON.setBONUS(sdt.getBONUS());
            saleDetailTempIKON.setBONUS_DISCOUNT(sdt.getBONUS_DISCOUNT());
            saleDetailTempIKON.setBONUS_VALUE(sdt.getBONUS_VALUE());
            saleDetailTempIKON.setBOOKED_BY(sdt.getBOOKED_BY());
            saleDetailTempIKON.setCHANNEL(sdt.getCHANNEL());
            saleDetailTempIKON.setCLASS(sdt.getCLASS());
            saleDetailTempIKON.setCUST_NAME(sdt.getCUST_NAME());
            saleDetailTempIKON.setCYP(sdt.getCYP());
            saleDetailTempIKON.setCUST_NUMBER(sdt.getCUST_NUMBER());
            saleDetailTempIKON.setDEPOT(sdt.getDEPOT());
            saleDetailTempIKON.setDISCOUNTS(sdt.getDISCOUNTS());
            saleDetailTempIKON.setDISTRICT(sdt.getDISTRICT());
            saleDetailTempIKON.setE_QTY(sdt.getE_QTY());
            saleDetailTempIKON.setEMPLOYEEID(sdt.getEMPLOYEEID());
            saleDetailTempIKON.setEXPIRY(sdt.getEXPIRY());
            saleDetailTempIKON.setFOC_DISCOUNT(sdt.getFOC_DISCOUNT());
            saleDetailTempIKON.setGROUPON(sdt.getGROUPON());
            saleDetailTempIKON.setGRP(sdt.getGRP());
            saleDetailTempIKON.setGRP_CATEGORY(sdt.getGRP_CATEGORY());
            saleDetailTempIKON.setGRP_NAME(sdt.getGRP_NAME());
            saleDetailTempIKON.setGSM_PRODUCT_CODE(sdt.getGSM_PRODUCT_CODE());
            saleDetailTempIKON.setGSM_REMARKS(sdt.getGSM_REMARKS());
            saleDetailTempIKON.setGSM_TOWN(sdt.getGSM_TOWN());
            saleDetailTempIKON.setHUID(sdt.getHUID());
            saleDetailTempIKON.setId(sdt.getId());
            saleDetailTempIKON.setINVOICE_NO(sdt.getINVOICE_NO());
            saleDetailTempIKON.setLETTER(sdt.getLETTER());
            saleDetailTempIKON.setMNP_COMMISSION(sdt.getMNP_COMMISSION());
            saleDetailTempIKON.setNATURE(sdt.getNATURE());
            saleDetailTempIKON.setNET_QTY(sdt.getNET_QTY());
            saleDetailTempIKON.setNET_SALE_VALUE(sdt.getNET_SALE_VALUE());
            saleDetailTempIKON.setNET_VALUE(sdt.getNET_VALUE());
            saleDetailTempIKON.setPOSITION_CODE(sdt.getPOSITION_CODE());
            saleDetailTempIKON.setPRD_NAME(sdt.getPRD_NAME());
            saleDetailTempIKON.setPRD_SIZE(sdt.getPRD_SIZE());
            saleDetailTempIKON.setPRICE(sdt.getPRICE());
            saleDetailTempIKON.setPRODUCT(sdt.getPRODUCT());
            saleDetailTempIKON.setPRODUCT_NO(sdt.getPRODUCT_NO());
            saleDetailTempIKON.setPRODUCTGROUP(sdt.getPRODUCTGROUP());
            saleDetailTempIKON.setPRODUCT(sdt.getPRODUCT());
            saleDetailTempIKON.setPROVIDER_CODE(sdt.getPROVIDER_CODE());
            saleDetailTempIKON.setPROVIDERCODE(sdt.getPROVIDERCODE());
            saleDetailTempIKON.setPROVINCE(sdt.getPROVINCE());
            saleDetailTempIKON.setREGION(sdt.getREGION());
            saleDetailTempIKON.setREMARKS(sdt.getREMARKS());
            saleDetailTempIKON.setREPORTINGMONTH(sdt.getREPORTINGMONTH());
            saleDetailTempIKON.setSALEDISTRICT(sdt.getSALEDISTRICT());
            saleDetailTempIKON.setSALETEHSIL(sdt.getSALETEHSIL());
            saleDetailTempIKON.setSASCODE(sdt.getSASCODE());
            saleDetailTempIKON.setSECTION_CODE(sdt.getSECTION_CODE());
            saleDetailTempIKON.setSECTION_NAME(sdt.getSECTION_NAME());
            saleDetailTempIKON.setSGP(sdt.getSGP());
            saleDetailTempIKON.setSNDPOP(sdt.getSNDPOP());
            saleDetailTempIKON.setSUBZONE(sdt.getSUBZONE());
            saleDetailTempIKON.setSUID(sdt.getSUID());
            saleDetailTempIKON.setSYSTEM_DATE(sdt.getSYSTEM_DATE());
            saleDetailTempIKON.setTEAM(sdt.getTEAM());
            saleDetailTempIKON.setTEAMREGION(sdt.getTEAMREGION());
            saleDetailTempIKON.setTEHSIL(sdt.getTEHSIL());
            saleDetailTempIKON.setTERRITORY(sdt.getTERRITORY());
            saleDetailTempIKON.setTERRITORY_NAME(sdt.getTERRITORY_NAME());
            saleDetailTempIKON.setTP_SALE_VALUE(sdt.getTP_SALE_VALUE());
            saleDetailTempIKON.setTRANSACTION_DATE(sdf.format(sdt.getTRANSACTION_DATE()));
            saleDetailTempIKON.setTYPE(sdt.getTYPE());
            saleDetailTempIKON.setVID(sdt.getVID());
            saleDetailTempIKON.setZONE(sdt.getZONE());
            HibernateUtil.saveOracle(saleDetailTempIKON);
        }
        HibernateUtil.executeQueryOracle("DELETE FROM BASE_TARGET");
        List<Target> target = (List<Target>) HibernateUtil.getDBObjects("from Target");
        HibernateUtil.saveOrUpdateListOracle(target);


        return "Transfer was true";
    }
}
