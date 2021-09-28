package com.fynuls.controllers;

import com.fynuls.controllers.greensales.Codes;
import com.fynuls.dal.AddDeleteResponse;
import com.fynuls.dal.DropDownResponse;
import com.fynuls.dal.KeyValue;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AdminPanelRequests {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getAdminData", method = RequestMethod.GET, params = {"tableName"})
    @ResponseBody
    public String getAdminData(String tableName){
        String response = "";

        List<Object> objs = new ArrayList<>();
        objs = (List<Object>) HibernateUtil.getDBObjects("from "+tableName+" order by updateDate desc" );
        response = new Gson().toJson(objs);

        return response;
    }
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/deleteAdminData", method = RequestMethod.GET, params = {"tablename","id","primarykey"})
    @ResponseBody
    public String deleteChannel(String tablename, String id, String primarykey){
        AddDeleteResponse addDeleteResponse = new AddDeleteResponse();
        if(HibernateUtil.deleteObject("DELETE FROM "+tablename+" WHERE "+primarykey+"='"+id+"'")){
               addDeleteResponse.setStatus(Codes.ALL_OK);
               addDeleteResponse.setTableName(tablename);
        }
        return new Gson().toJson(addDeleteResponse);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getAdminDropDownData", method = RequestMethod.GET, params = {"tableName","column1", "column2"})
    @ResponseBody
    public String getAdminDropDownData(String tableName, String column1, String column2){
        DropDownResponse dropDownResponse = new DropDownResponse();

        String query = "SELECT "+column1+" AS key, "+column2+" AS value FROM " +tableName + " order by " + column1;

        List<KeyValue> keyValueList = new ArrayList<>();
        KeyValue keyValue = new KeyValue();

        List<Object> objects= (List<Object>) HibernateUtil.getDBObjects(query);
        int i =0;
        if(objects!=null){
            for(Object object : objects){
                Object[] obj = (Object[]) object;
                keyValue = new KeyValue();
                keyValue.setKey(String.valueOf(obj[0]));
                keyValue.setValue(String.valueOf(obj[1]));
                keyValueList.add(keyValue);
            }
        }

        dropDownResponse.setKeyValueList(keyValueList);
        dropDownResponse.setStatus("200");

        return new Gson().toJson(dropDownResponse);
    }
}
