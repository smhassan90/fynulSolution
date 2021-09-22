package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 21st September, 2021
 */
@Entity
@Table(name="BASE_EMP_PJP")
public class EmployeePJP {
    @Id
    @Column(name="PJP_CODE")
    private String PJP_CODE;
    @Column(name="POSITION_CODE")
    private String POSITION_CODE;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getPJP_CODE() {
        return PJP_CODE;
    }

    public void setPJP_CODE(String PJP_CODE) {
        this.PJP_CODE = PJP_CODE;
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
