package com.fynuls.dal;

public class InfoActionPlanDAL {
    private String employeeId;
    private String employeeName;
    private String team;
    private String region;
    private int productivityStatus;
    private int reachStatus;
    private int strategicBrandStatus;
    /*
    status 1 : Acceptable
    status 2 : Average
    status 3 : Low Performance
     */
    private int noOfShops;


    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getProductivityStatus() {
        return productivityStatus;
    }

    public void setProductivityStatus(int productivityStatus) {
        this.productivityStatus = productivityStatus;
    }

    public int getReachStatus() {
        return reachStatus;
    }

    public void setReachStatus(int reachStatus) {
        this.reachStatus = reachStatus;
    }

    public int getStrategicBrandStatus() {
        return strategicBrandStatus;
    }

    public void setStrategicBrandStatus(int strategicBrandStatus) {
        this.strategicBrandStatus = strategicBrandStatus;
    }

    public int getNoOfShops() {
        return noOfShops;
    }

    public void setNoOfShops(int noOfShops) {
        this.noOfShops = noOfShops;
    }
}
