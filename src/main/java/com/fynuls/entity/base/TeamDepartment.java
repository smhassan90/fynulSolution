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
@Table(name="BASE_TEAM_DEPT")
public class TeamDepartment {
    @Id
    @Column(name="ID")
    private int ID;
    @Column(name="NAME")
    private String NAME;
    @Column(name="ALIAS")
    private String ALIAS;
    @Column(name="DEPT_ID")
    private int DEPT_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public int getDEPT_ID() {
        return DEPT_ID;
    }

    public void setDEPT_ID(int DEPT_ID) {
        this.DEPT_ID = DEPT_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
