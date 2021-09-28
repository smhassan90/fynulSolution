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
@Table(name="BASE_TERRITORY_EMP_MAPPING")
public class TerritoryEmployeeMapping {

    @Column(name="TERRITORY_CODE")
    private String TERRITORY_CODE;
    @Id
    @Column(name="EMP_ID")
    private String EMP_ID;

    @Column(name="SHARING")
    private String SHARING;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getTERRITORY_CODE() {
        return TERRITORY_CODE;
    }

    public void setTERRITORY_CODE(String TERRITORY_CODE) {
        this.TERRITORY_CODE = TERRITORY_CODE;
    }

    public String getEMP_ID() {
        return EMP_ID;
    }

    public void setEMP_ID(String EMP_ID) {
        this.EMP_ID = EMP_ID;
    }

    public String getSHARING() {
        return SHARING;
    }

    public void setSHARING(String SHARING) {
        this.SHARING = SHARING;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
