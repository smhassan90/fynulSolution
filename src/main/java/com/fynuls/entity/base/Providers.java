package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Syed Muhammad Hassan
 * 24/06/2019
 */

@Entity
@Table(name="HS_PROVIDERS")
public class Providers {

    @Id
    @Column(name="CODE")
    private String code;

    @Column(name="NAME")
    private String name;

    @Column(name="STATUS")
    private String status;

    @Column(name="DONOR")
    private String donor;

    @Column(name="DISTRICT")
    private String district;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
