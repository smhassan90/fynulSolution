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
@Table(name="BASE_TEHSIL_SNDPOP")
public class TehsilSNDPOP {
    @Id
    @Column(name="SNDPOP_ID")
    private String SNDPOP_ID;
    @Column(name="TEHSIL_ID")
    private int TEHSIL_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getSNDPOP_ID() {
        return SNDPOP_ID;
    }

    public void setSNDPOP_ID(String SNDPOP_ID) {
        this.SNDPOP_ID = SNDPOP_ID;
    }

    public int getTEHSIL_ID() {
        return TEHSIL_ID;
    }

    public void setTEHSIL_ID(int TEHSIL_ID) {
        this.TEHSIL_ID = TEHSIL_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
