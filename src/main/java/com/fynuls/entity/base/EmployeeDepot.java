package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="BASE_EMP_DEPOT_MAPPING")
public class EmployeeDepot {
    @Id
    @Column(name="DEPOT_CODE")
    private String DEPOT_CODE;
    @Column(name="POSITION_CODE")
    private String POSITION_CODE;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getDEPOT_CODE() {
        return DEPOT_CODE;
    }

    public void setDEPOT_CODE(String DEPOT_CODE) {
        this.DEPOT_CODE = DEPOT_CODE;
    }

    public String getPOSITION_CODE() {
        return POSITION_CODE;
    }

    public void setPOSITION_CODE(String POSITION_CODE) {
        this.POSITION_CODE = POSITION_CODE;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
