package com.fynuls.dal;

/**
 * @author Syed Muhammad Hassan
 * 24th February, 2022
 */
public class StrategyDAL {
    private String empID;
    private String name;
    private String team;
    private String region;
    private int productivityStatus;
    private int uccStatus;
    private int strategicStatus;
    private String status;

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getUccStatus() {
        return uccStatus;
    }

    public void setUccStatus(int uccStatus) {
        this.uccStatus = uccStatus;
    }

    public int getStrategicStatus() {
        return strategicStatus;
    }

    public void setStrategicStatus(int strategicStatus) {
        this.strategicStatus = strategicStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
