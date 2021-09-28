package com.fynuls.controllers;

import com.fynuls.dal.ResponseBtnAdd;
import com.fynuls.entity.base.Channel;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @RequestMapping(value = "/testc", method = RequestMethod.GET)
    @ResponseBody
    public String index(){
        String className = "tableName";
        ResponseBtnAdd responseBtnAdd = new ResponseBtnAdd();

        Channel channel = new Channel();
        channel.setID("1");
        channel.setNAME("pharma");

        responseBtnAdd.setTablename("Channel");
        responseBtnAdd.setData(new Gson().toJson(channel));
        return new Gson().toJson(responseBtnAdd);
    }
}
