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
@Table(name="BASE_DIST_TEHSIL")
public class DistrictTehsil {

    @Id
    @Column(name="DIST_ID")
    private String DIST_ID;
    @Column(name="TEHSIL_ID")
    private String TEHSIL_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getDIST_ID() {
        return DIST_ID;
    }

    public void setDIST_ID(String DIST_ID) {
        this.DIST_ID = DIST_ID;
    }

    public String getTEHSIL_ID() {
        return TEHSIL_ID;
    }

    public void setTEHSIL_ID(String TEHSIL_ID) {
        this.TEHSIL_ID = TEHSIL_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
