package com.fynuls.controllers.greensales;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class SectionShops {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/getActiveUCC", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    private String getSPOProgressProductWise(String token){
        return "";

    }
}
