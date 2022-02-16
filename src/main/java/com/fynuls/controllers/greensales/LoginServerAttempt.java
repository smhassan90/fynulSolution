package com.fynuls.controllers.greensales;


import com.fynuls.dal.LoginResponse;
import com.fynuls.entity.base.Employee;
import com.fynuls.entity.base.EmployeeIDPositionIDMapping;
import com.fynuls.entity.login.LoginLog;
import com.fynuls.entity.login.LoginStatus;
import com.fynuls.utils.HibernateUtil;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

/**
 * @author Syed Muhammad Hassan
 * 8th November, 2021
 */

@Controller
public class LoginServerAttempt {
    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/loginServerAttempt", method = RequestMethod.GET,params={"username","password"})
    @ResponseBody
    public String loginServerAttempt(String username, String password){
        String secret = "secret";

        Employee employee = new Employee();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStatusCode(Codes.SOMETHING_WENT_WRONG);
        try {

            List<Employee> employees = (List<Employee>) HibernateUtil.getDBObjects("from Employee where id ='" + username + "' and pwd ='" + password + "'");
            if(employees!=null && employees.size()>0){

                LoginStatus loginStatus = new LoginStatus();
                employee = employees.get(0);
                String positionCode = "";
                List<EmployeeIDPositionIDMapping> employeeIDPositionIDMappings = (List<EmployeeIDPositionIDMapping>) HibernateUtil.getDBObjects("from EmployeeIDPositionIDMapping where EMPLOYEE_ID='"+employee.getID()+"'");
                if(employeeIDPositionIDMappings!=null && employeeIDPositionIDMappings.size()>0){
                    positionCode = employeeIDPositionIDMappings.get(0).getPOSITION_ID();
                    loginStatus.setPOSITION_CODE(positionCode);
                }

                loginStatus.setUsername(employee.getID());
                loginStatus.setStatus(1);
                String token = UUID.randomUUID().toString();
                loginStatus.setToken(token);
                boolean isSuccessful = HibernateUtil.saveOrUpdate(loginStatus);
                if(isSuccessful){
                    loginResponse.setStatusCode(Codes.ALL_OK);
                    loginResponse.setToken(token);
                    loginResponse.setEmployee(employee);
                    loginResponse.setPositionCode(positionCode);
                    loginResponse.setDesignation_id(employee.getDESIGNATION_ID());
                }
            }else{
                loginResponse.setStatusCode(Codes.INVALID_CREDENTIALS);
            }
        }catch(Exception e){
            loginResponse.setStatusCode(Codes.SOMETHING_WENT_WRONG);
        }
        return new Gson().toJson(loginResponse);
    }

    @CrossOrigin(origins = "*" )
    @RequestMapping(value = "/loginLog", method = RequestMethod.GET,params={"token"})
    @ResponseBody
    public boolean loginLog(String token){
        String username = "";
        ArrayList<LoginStatus> loginStatuses = (ArrayList<LoginStatus>) HibernateUtil.getDBObjects("from LoginStatus where token = '"+token+"'");
        if(loginStatuses!=null){
            if(loginStatuses.size()>0){
                username = loginStatuses.get(0).getUsername();
            }
        }
        LoginLog loginLog = new LoginLog();
        if(!"".equals(token)){
            loginLog.setTOKEN(token);
            loginLog.setEMP_ID(username);
            return HibernateUtil.save(loginLog);
        }else{
            return false;
        }
    }
}
