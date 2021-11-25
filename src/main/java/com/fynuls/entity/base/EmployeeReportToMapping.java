package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 29th April, 2021
 */
@Entity
@Table(name="base_emp_reportto_mapping")
public class EmployeeReportToMapping implements Serializable {
    @Id
    @Column(name="ID")
    private int ID;
    @Column(name="POSITION_ID")
    private String POSITION_ID;
    @Column(name="REPORTTO_ID")
    private String REPORTTO_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPOSITION_ID() {
        return POSITION_ID;
    }

    public void setPOSITION_ID(String POSITION_ID) {
        this.POSITION_ID = POSITION_ID;
    }

    public String getREPORTTO_ID() {
        return REPORTTO_ID;
    }

    public void setREPORTTO_ID(String REPORTTO_ID) {
        this.REPORTTO_ID = REPORTTO_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
