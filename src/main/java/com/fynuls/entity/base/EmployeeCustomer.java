package com.fynuls.entity.base;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 9th December, 2021
 */
@Entity
@Table(name="base_emp_customer")
public class EmployeeCustomer {
    @Id
    @Column(name="ID")
    private int ID;
    @Column(name="POSITION_CODE")
    private String POSITION_CODE;
    @Column(name="CUSTOMER_CODE")
    private String CUSTOMER_CODE;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPOSITION_CODE() {
        return POSITION_CODE;
    }

    public void setPOSITION_CODE(String POSITION_CODE) {
        this.POSITION_CODE = POSITION_CODE;
    }

    public String getCUSTOMER_CODE() {
        return CUSTOMER_CODE;
    }

    public void setCUSTOMER_CODE(String CUSTOMER_CODE) {
        this.CUSTOMER_CODE = CUSTOMER_CODE;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
