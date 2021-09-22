package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 24th May, 2021
 */

@Entity
@Table(name="BASE_PROVIDER_TEHSIL")
public class ProviderTehsil {
    @Id
    @Column(name="PROVIDERCODE")
    private int PROVIDERCODE;
    @Column(name="TEHSIL_ID")
    private int TEHSIL_ID;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getPROVIDERCODE() {
        return PROVIDERCODE;
    }

    public void setPROVIDERCODE(int PROVIDERCODE) {
        this.PROVIDERCODE = PROVIDERCODE;
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
