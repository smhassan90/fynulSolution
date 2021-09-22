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
@Table(name="BASE_EMPID_POSITIONID_MAPPING")
public class EmployeeIDPositionIDMapping {
    @Id
    @Column(name="EMPLOYEE_ID")
    private String EMPLOYEE_ID;
    @Column(name="POSITION_ID")
    private String POSITION_ID;
    @Column(name="SAS_ID")
    private String SAS_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getEMPLOYEE_ID() {
        return EMPLOYEE_ID;
    }

    public void setEMPLOYEE_ID(String EMPLOYEE_ID) {
        this.EMPLOYEE_ID = EMPLOYEE_ID;
    }

    public String getPOSITION_ID() {
        return POSITION_ID;
    }

    public void setPOSITION_ID(String POSITION_ID) {
        this.POSITION_ID = POSITION_ID;
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
