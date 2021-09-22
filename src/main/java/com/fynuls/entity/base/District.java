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
@Table(name="BASE_DISTRICT_MASTER")
public class District {
    @Id
    @Column(name="ID")
    private String ID;
    @Column(name="NAME")
    private String NAME;
    @Column(name="DONOR")
    private String DONOR;
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

    public String getDONOR() {
        return DONOR;
    }

    public void setDONOR(String DONOR) {
        this.DONOR = DONOR;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
