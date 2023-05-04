package com.fynuls.dal;

import com.fynuls.entity.SaleDetailTemp;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class SalesSerialization implements JsonSerializer<SaleDetailTemp> {

    @Override
    public JsonElement serialize(SaleDetailTemp saleDetailTemp, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.add("ID", context.serialize(saleDetailTemp.getId()));
        object.add("HUID", context.serialize(saleDetailTemp.getHUID()));
        object.add("SUID", context.serialize(saleDetailTemp.getSUID()));
        object.add("SNDPOP", context.serialize(saleDetailTemp.getSNDPOP()));
        object.add("CUST_NUMBER", context.serialize(saleDetailTemp.getCUST_NUMBER()));
        object.add("CUST_NAME", context.serialize(saleDetailTemp.getCUST_NAME()));
        object.add("ADDRESS", context.serialize(saleDetailTemp.getADDRESS()));
        object.add("PROVIDER_CODE", context.serialize(saleDetailTemp.getPROVIDER_CODE()));
        object.add("LETTER", context.serialize(saleDetailTemp.getLETTER()));
        object.add("TERRITORY", context.serialize(saleDetailTemp.getTERRITORY()));
        object.add("TERRITORY_NAME", context.serialize(saleDetailTemp.getTERRITORY_NAME()));
        object.add("DEPOT", context.serialize(saleDetailTemp.getDEPOT()));
        object.add("SGP", context.serialize(saleDetailTemp.getSGP()));
        object.add("GRP_NAME", context.serialize(saleDetailTemp.getGRP_NAME()));
        object.add("PRODUCT_NO", context.serialize(saleDetailTemp.getPRODUCT_NO()));
        object.add("PRD_NAME", context.serialize(saleDetailTemp.getPRD_NAME()));
        object.add("PRD_SIZE", context.serialize(saleDetailTemp.getPRD_SIZE()));
        object.add("PRICE", context.serialize(saleDetailTemp.getPRICE()));
        object.add("TRANSACTION_DATE", context.serialize(saleDetailTemp.getTRANSACTION_DATE()));
        object.add("INVOICE_NO", context.serialize(saleDetailTemp.getINVOICE_NO()));
        object.add("BATCH", context.serialize(saleDetailTemp.getBATCH()));
        object.add("EXPIRY", context.serialize(saleDetailTemp.getEXPIRY()));
        object.add("CLASS", context.serialize(saleDetailTemp.getCLASS()));
        object.add("CHANNEL", context.serialize(saleDetailTemp.getCHANNEL()));
        object.add("NET_QTY", context.serialize(saleDetailTemp.getNET_QTY()));
        object.add("NET_VALUE", context.serialize(saleDetailTemp.getNET_VALUE()));
        object.add("BONUS", context.serialize(saleDetailTemp.getBONUS()));
        object.add("BONUS_VALUE", context.serialize(saleDetailTemp.getBONUS_VALUE()));
        object.add("DISCOUNTS", context.serialize(saleDetailTemp.getDISCOUNTS()));
        object.add("CYP", context.serialize(saleDetailTemp.getCYP()));
        object.add("GSM_PRODUCT_CODE", context.serialize(saleDetailTemp.getGSM_PRODUCT_CODE()));
        object.add("GSM_TOWN", context.serialize(saleDetailTemp.getGSM_TOWN()));
        object.add("SALE_DISTRICT", context.serialize(saleDetailTemp.getDISTRICT()));
        object.add("SALE_TEHSIL", context.serialize(saleDetailTemp.getTEHSIL()));
        object.add("TYPE", context.serialize(saleDetailTemp.getTYPE()));
        object.add("REMARKS", context.serialize(saleDetailTemp.getREMARKS()));
        object.add("VID", context.serialize(saleDetailTemp.getVID()));
        object.add("SYSTEM_DATE", context.serialize(saleDetailTemp.getSYSTEM_DATE()));
        object.add("FOC_DISCOUNT", context.serialize(saleDetailTemp.getFOC_DISCOUNT()));
        object.add("BOOKED_BY", context.serialize(saleDetailTemp.getBOOKED_BY()));
        object.add("SECTION_CODE", context.serialize(saleDetailTemp.getSECTION_CODE()));
        object.add("SECTION_NAME", context.serialize(saleDetailTemp.getSECTION_NAME()));
        object.add("NATURE", context.serialize(saleDetailTemp.getNATURE()));
        object.add("POSITION_CODE", context.serialize(saleDetailTemp.getPOSITION_CODE()));
        object.add("SASCODE", context.serialize(saleDetailTemp.getSASCODE()));
        object.add("EMPLOYEEID", context.serialize(saleDetailTemp.getEMPLOYEEID()));
        object.add("TEAMREGION", context.serialize(saleDetailTemp.getTEAMREGION()));
        object.add("ZONE", context.serialize(saleDetailTemp.getZONE()));
        object.add("SUBZONE", context.serialize(saleDetailTemp.getSUBZONE()));
        object.add("DISTRICT", context.serialize(saleDetailTemp.getDISTRICT()));
        object.add("TEHSIL", context.serialize(saleDetailTemp.getTEHSIL()));
        object.add("PROVINCE", context.serialize(saleDetailTemp.getPROVINCE()));
        object.add("REGION", context.serialize(saleDetailTemp.getREGION()));
        object.add("TEAM", context.serialize(saleDetailTemp.getTEAM()));
        object.add("GRP_CATEGORY", context.serialize(saleDetailTemp.getGRP_CATEGORY()));
        object.add("GRP", context.serialize(saleDetailTemp.getGRP()));
        object.add("PRODUCTGROUP", context.serialize(saleDetailTemp.getPRODUCTGROUP()));
        object.add("GROUPON", context.serialize(saleDetailTemp.getGROUPON()));
        object.add("PRODUCT", context.serialize(saleDetailTemp.getPRODUCT()));
        object.add("PROVIDERCODE", context.serialize(saleDetailTemp.getPROVIDERCODE()));
        object.add("E_QTY", context.serialize(saleDetailTemp.getE_QTY()));
        object.add("TP_SALE_VALUE", context.serialize(saleDetailTemp.getTP_SALE_VALUE()));
        object.add("BONUS_DISCOUNT", context.serialize(saleDetailTemp.getBONUS_DISCOUNT()));
        object.add("MNP_COMMISSION", context.serialize(saleDetailTemp.getMNP_COMMISSION()));
        object.add("NET_SALE_VALUE", context.serialize(saleDetailTemp.getNET_SALE_VALUE()));
        object.add("REPORTINGMONTH", context.serialize(saleDetailTemp.getREPORTINGMONTH()));
        object.add("GSM_REMARKS", context.serialize(saleDetailTemp.getGSM_REMARKS()));
        object.add("UCC_COLUMN", context.serialize(saleDetailTemp.getUCC_COLUMN()));

        return object;
    }

}