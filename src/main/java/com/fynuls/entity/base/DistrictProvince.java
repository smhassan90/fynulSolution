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
@Table(name="BASE_DIST_PROVINCE")
public class DistrictProvince {
    @Id
    @Column(name="DIST_ID")
    private String DIST_ID;
    @Column(name="PROVINCE_ID")
    private String PROVINCE_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getDIST_ID() {
        return DIST_ID;
    }

    public void setDIST_ID(String DIST_ID) {
        this.DIST_ID = DIST_ID;
    }

    public String getPROVINCE_ID() {
        return PROVINCE_ID;
    }

    public void setPROVINCE_ID(String PROVINCE_ID) {
        this.PROVINCE_ID = PROVINCE_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
