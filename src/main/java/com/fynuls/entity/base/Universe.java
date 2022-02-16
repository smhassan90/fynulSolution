package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 2nd February, 2022
 */
@Entity
@Table(name="UNIVERSE")
public class Universe {
    @Id
    @Column(name="ID")
    private long ID;
    @Column(name="DEPOT_ID")
    private String DEPOT_ID;
    @Column(name="SECTION_CODE")
    private String SECTION_CODE;
    @Column(name="SECTION_NAME")
    private String SECTION_NAME;
    @Column(name="CUSTOMER_ID")
    private String CUSTOMER_ID;
    @Column(name="CUSTOMER_NAME")
    private String CUSTOMER_NAME;
    @Column(name="POSITION_CODE")
    private String POSITION_CODE;
    @Column(name="SAS_ID")
    private String SAS_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getDEPOT_ID() {
        return DEPOT_ID;
    }

    public void setDEPOT_ID(String DEPOT_ID) {
        this.DEPOT_ID = DEPOT_ID;
    }

    public String getSECTION_CODE() {
        return SECTION_CODE;
    }

    public void setSECTION_CODE(String SECTION_CODE) {
        this.SECTION_CODE = SECTION_CODE;
    }

    public String getSECTION_NAME() {
        return SECTION_NAME;
    }

    public void setSECTION_NAME(String SECTION_NAME) {
        this.SECTION_NAME = SECTION_NAME;
    }

    public String getCUSTOMER_ID() {
        return CUSTOMER_ID;
    }

    public void setCUSTOMER_ID(String CUSTOMER_ID) {
        this.CUSTOMER_ID = CUSTOMER_ID;
    }

    public String getCUSTOMER_NAME() {
        return CUSTOMER_NAME;
    }

    public void setCUSTOMER_NAME(String CUSTOMER_NAME) {
        this.CUSTOMER_NAME = CUSTOMER_NAME;
    }

    public String getPOSITION_CODE() {
        return POSITION_CODE;
    }

    public void setPOSITION_CODE(String POSITION_CODE) {
        this.POSITION_CODE = POSITION_CODE;
    }

    public String getSAS_ID() {
        return SAS_ID;
    }

    public void setSAS_ID(String SAS_ID) {
        this.SAS_ID = SAS_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
