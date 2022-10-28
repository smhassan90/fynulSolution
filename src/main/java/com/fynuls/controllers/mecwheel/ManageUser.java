package com.fynuls.controllers.mecwheel;


import com.fynuls.controllers.greensales.Codes;
import com.fynuls.dal.Response;
import com.fynuls.entity.crb.User;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 8th September, 2022
 */

@Controller
public class ManageUser {
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET,params={"userId"})
    @ResponseBody
    public String getUserInfo(String userId){
        Response response = new Response();

        if(userId!=null && !userId.equals("") && userId.length()<20){
            String query = "from User where id ='"+userId+"'";
            List<User> users = (List<User>) HibernateUtil.getDBObjectsOracle(query);
            if(users!=null && users.size()==1){
                User user = users.get(0);
                response.setStatus(Codes.ALL_OK);
                response.setUser(user);
                response.setMessage("User Attached");
            }else{
                response.setStatus(Codes.NOT_FOUND);
                response.setMessage("User not found");
            }

        }else{
            response.setMessage("Invalid request");
            response.setStatus(Codes.SOMETHING_WENT_WRONG);
        }

        return new Gson().toJson(response);

    }

    @RequestMapping(value = "/insertUser", method = RequestMethod.GET,params={"userString"})
    @ResponseBody
    public String insertUser(String userString){
        Response response = new Response();
        User user = new Gson().fromJson(userString, User.class);
        if(user!=null && user.getEmail()!=null && !user.getEmail().equals("")
            && user.getName()!=null && !user.getName().equals("")
            && user.getPhoneNumber()!=null && !user.getPhoneNumber().equals("")){
            String userId = String.valueOf(Calendar.getInstance().getTimeInMillis());
            user.setId("CRB-"+userId.substring(userId.length()-10));
            List<User> users = (List<User>) HibernateUtil.getDBObjectsOracle("from User where email = '"+user.getEmail()+"' and phoneNumber='"+user.getPhoneNumber()+"' and name='"+user.getName()+"'");
            if(users!=null && users.size()>0){
                response.setMessage("User Already Exists");
                response.setStatus(Codes.ALREADY_EXISTS);
            }else {
                Boolean isSuccessful = HibernateUtil.saveOracle(user);
                if (isSuccessful) {
                    response.setMessage("User Attached");
                    response.setStatus(Codes.ALL_OK);
                    response.setUser(user);
                } else {
                    response.setStatus(Codes.SOMETHING_WENT_WRONG);
                }
            }
        }else{
            response.setStatus(Codes.SOMETHING_WENT_WRONG);
        }
        return new Gson().toJson(response);

    }
}
