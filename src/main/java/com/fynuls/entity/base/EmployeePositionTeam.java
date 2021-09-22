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
@Table(name="BASE_EMP_POSITION_TEAM")
public class EmployeePositionTeam {
    @Id
    @Column(name="TEAM_ID")
    private int TEAM_ID;
    @Column(name="POSITION_ID")
    private String POSITION_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getTEAM_ID() {
        return TEAM_ID;
    }

    public void setTEAM_ID(int TEAM_ID) {
        this.TEAM_ID = TEAM_ID;
    }

    public String getPOSITION_ID() {
        return POSITION_ID;
    }

    public void setPOSITION_ID(String POSITION_ID) {
        this.POSITION_ID = POSITION_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
