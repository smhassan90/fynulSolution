package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Syed Muhammad Hassan
 * 7th February, 2022
 */
@Entity
@Table(name="BASE_TARGET")
public class Target {

    @Id
    @Column(name="ID")
    private int ID;
    @Column(name="POSITION_CODE")
    private String POSITION_CODE;

    @Column(name="TEAM")
    private String TEAM;
    @Column(name="GROUP_ON_ID")
    private String GROUP_ON_ID;
    @Column(name="MONTH")
    private String MONTH;
    @Column(name="TARGET")
    private int TARGET;
    @Column(name="TGT_TP_VALUE")
    private double TGT_TP_VALUE;
    @Column(name="ADD_BON_DISC")
    private double ADD_BON_DISC;
    @Column(name="TGT_DIST_COMM")
    private double TGT_DIST_COMM;
    @Column(name="TGT_NET_VALUE")
    private double TGT_NET_VALUE;

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

    public String getTEAM() {
        return TEAM;
    }

    public void setTEAM(String TEAM) {
        this.TEAM = TEAM;
    }

    public String getGROUP_ON_ID() {
        return GROUP_ON_ID;
    }

    public void setGROUP_ON_ID(String GROUP_ON_ID) {
        this.GROUP_ON_ID = GROUP_ON_ID;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public int getTARGET() {
        return TARGET;
    }

    public void setTARGET(int TARGET) {
        this.TARGET = TARGET;
    }

    public double getTGT_TP_VALUE() {
        return TGT_TP_VALUE;
    }

    public void setTGT_TP_VALUE(double TGT_TP_VALUE) {
        this.TGT_TP_VALUE = TGT_TP_VALUE;
    }

    public double getADD_BON_DISC() {
        return ADD_BON_DISC;
    }

    public void setADD_BON_DISC(double ADD_BON_DISC) {
        this.ADD_BON_DISC = ADD_BON_DISC;
    }

    public double getTGT_DIST_COMM() {
        return TGT_DIST_COMM;
    }

    public void setTGT_DIST_COMM(double TGT_DIST_COMM) {
        this.TGT_DIST_COMM = TGT_DIST_COMM;
    }

    public double getTGT_NET_VALUE() {
        return TGT_NET_VALUE;
    }

    public void setTGT_NET_VALUE(double TGT_NET_VALUE) {
        this.TGT_NET_VALUE = TGT_NET_VALUE;
    }
}
