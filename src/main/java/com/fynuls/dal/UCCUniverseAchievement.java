package com.fynuls.dal;


/**
 * @author Syed Muhammad Hassan
 * 31st January, 2022
 */
public class UCCUniverseAchievement {
    String name;
    String position_code;
    String totalCustomers;
    String ucc;
    String coverage;
    String quarterUCC;
    private String empId;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition_code() {
        return position_code;
    }

    public void setPosition_code(String position_code) {
        this.position_code = position_code;
    }

    public String getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(String totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public String getUcc() {
        return ucc;
    }

    public void setUcc(String ucc) {
        this.ucc = ucc;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getQuarterUCC() {
        return quarterUCC;
    }

    public void setQuarterUCC(String quarterUCC) {
        this.quarterUCC = quarterUCC;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
