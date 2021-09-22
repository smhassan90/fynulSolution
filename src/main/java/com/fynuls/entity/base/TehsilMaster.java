package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 29th April, 2021
 */
@Entity
@Table(name="BASE_TEHSIL_MASTER")
public class TehsilMaster {
    @Id
    @Column(name="ID")
    private String ID;
    @Column(name="NAME")
    private String NAME;
    @Column(name="URBAN_PERC")
    private double URBAN_PERC;
    @Column(name="RURAL_PERC")
    private double RURAL_PERC;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public double getURBAN_PERC() {
        return URBAN_PERC;
    }

    public void setURBAN_PERC(double URBAN_PERC) {
        this.URBAN_PERC = URBAN_PERC;
    }

    public double getRURAL_PERC() {
        return RURAL_PERC;
    }

    public void setRURAL_PERC(double RURAL_PERC) {
        this.RURAL_PERC = RURAL_PERC;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
