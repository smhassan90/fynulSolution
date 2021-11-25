package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 19th November, 2021
 */
@Entity
@Table(name="MISSING_MAPPING")
public class MissingMapping {
    @Id
    @Column(name="FIRST")
    private String FIRST;
    @Column(name="SECOND")
    private String SECOND;
    @Column(name="TYPE")
    private int type;

    public String getFIRST() {
        return FIRST;
    }

    public void setFIRST(String FIRST) {
        this.FIRST = FIRST;
    }

    public String getSECOND() {
        return SECOND;
    }

    public void setSECOND(String SECOND) {
        this.SECOND = SECOND;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
