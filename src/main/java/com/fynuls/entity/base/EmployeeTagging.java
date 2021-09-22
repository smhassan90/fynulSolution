package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Syed Muhammad Hassan
 * 29th April, 2021
 */
@Entity
@Table(name="BASE_EMP_TAGGING")
public class EmployeeTagging {
    @Id
    @Column(name="POSITION_ID")
    private String POSITION_ID;
    @Column(name="DESIGNATION_ID")
    private int DESIGNATION_ID;
    @Column(name="TAGGED_TO")
    private String TAGGED_TO;

    public String getPOSITION_ID() {
        return POSITION_ID;
    }

    public void setPOSITION_ID(String POSITION_ID) {
        this.POSITION_ID = POSITION_ID;
    }

    public int getDESIGNATION_ID() {
        return DESIGNATION_ID;
    }

    public void setDESIGNATION_ID(int DESIGNATION_ID) {
        this.DESIGNATION_ID = DESIGNATION_ID;
    }

    public String getTAGGED_TO() {
        return TAGGED_TO;
    }

    public void setTAGGED_TO(String TAGGED_TO) {
        this.TAGGED_TO = TAGGED_TO;
    }
}
