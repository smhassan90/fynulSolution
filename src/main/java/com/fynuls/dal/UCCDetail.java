package com.fynuls.dal;

/**
 * @author Syed Muhammad Hassan
 * 11th January, 2022
 */
public class UCCDetail {
    private String customerNumber;
    private String customerName;
    private String sectionCode;
    private String sectionName;
    private String depot;
    private String tehsil;
    private String district;
    private String tpValue;

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getDepot() {
        return depot;
    }

    public void setDepot(String depot) {
        this.depot = depot;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTpValue() {
        return tpValue;
    }

    public void setTpValue(String tpValue) {
        this.tpValue = tpValue;
    }
}
