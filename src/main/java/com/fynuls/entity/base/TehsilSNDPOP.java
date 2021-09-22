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
@Table(name="BASE_TEHSIL_SNDPOP")
public class TehsilSNDPOP {
    @Id
    @Column(name="SNDPOP_ID")
    private String SNDPOP_ID;
    @Column(name="TEHSIL_ID")
    private int TEHSIL_ID;

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
}
