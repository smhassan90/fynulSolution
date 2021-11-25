package com.fynuls.controllers;

import com.fynuls.controllers.greensales.Codes;
import com.fynuls.dal.LoginResponse;
import com.fynuls.entity.login.User;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class LoginServer {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/loginServer", method = RequestMethod.GET,params={"username","password","staffType"})
    @ResponseBody
    public String index(String username, String password, int staffType){
        User user = getUser(username,password);
        LoginResponse loginResponse = new LoginResponse();
        if(user!=null){
            String token = generateToken(user.getUserName());

            loginResponse.setToken(token);
            loginResponse.setStatusCode(Codes.ALL_OK);
        }else{
            loginResponse.setToken("");
            loginResponse.setStatusCode(Codes.INVALID_CREDENTIALS);
        }
        String response = new Gson().toJson(loginResponse);
        return response;
    }

    private String generateToken(String username) {
        String token = username+new Date().getTime();
        return token;
    }

    private User getUser(String username, String password) {
        List<User> user = (List<User>) HibernateUtil.getDBObjects("from User where userName='"+username+"' and password='"+password+"'");
        if(user!=null && user.size()>0){
            return user.get(0);
        }
        return null;
    }
}
