package com.fynuls.entity.base;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Syed Muhammad Hassan
 * 31st December, 2021
 */

@Entity
@Table(name="MTDYTDPERFORMANCE")
public class MTDYTDPERFORMANCE implements Serializable {
    @Id
    @Column(name="position_code")
    private String position_code;
    @Id
    @Column(name="month")
    private String month;

    @Column(name="E_QTY")
    private String E_QTY;

    @Column(name="TARGET_E_QTY")
    private String TARGET_E_QTY;

    @Column(name="QTY_PERC")
    private String QTY_PERC;

    @Column(name="TP_SALE_VALUE")
    private String TP_SALE_VALUE;

    @Column(name="TARGET_TP_VALUE")
    private String TARGET_TP_VALUE;

    @Column(name="TP_PERC")
    private String TP_PERC;

    @Column(name="ADDITIONAL_BONUS_DISCOUNT")
    private String ADDITIONAL_BONUS_DISCOUNT;

    @Column(name="MNP_COMMISSION_TARGET")
    private String MNP_COMMISSION_TARGET;

    @Column(name="NET_TARGET_VALUE")
    private String NET_TARGET_VALUE;

    @Column(name="NET_SALE_VALUE")
    private String NET_SALE_VALUE;

    @Column(name="Current_month_average")
    private String Current_month_average;

    @Column(name="required_month_average")
    private String required_month_average;

    public String getPosition_code() {
        return position_code;
    }

    public void setPosition_code(String position_code) {
        this.position_code = position_code;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getE_QTY() {
        return E_QTY;
    }

    public void setE_QTY(String e_QTY) {
        E_QTY = e_QTY;
    }

    public String getTARGET_E_QTY() {
        return TARGET_E_QTY;
    }

    public void setTARGET_E_QTY(String TARGET_E_QTY) {
        this.TARGET_E_QTY = TARGET_E_QTY;
    }

    public String getQTY_PERC() {
        return QTY_PERC;
    }

    public void setQTY_PERC(String QTY_PERC) {
        this.QTY_PERC = QTY_PERC;
    }

    public String getTP_SALE_VALUE() {
        return TP_SALE_VALUE;
    }

    public void setTP_SALE_VALUE(String TP_SALE_VALUE) {
        this.TP_SALE_VALUE = TP_SALE_VALUE;
    }

    public String getTARGET_TP_VALUE() {
        return TARGET_TP_VALUE;
    }

    public void setTARGET_TP_VALUE(String TARGET_TP_VALUE) {
        this.TARGET_TP_VALUE = TARGET_TP_VALUE;
    }

    public String getTP_PERC() {
        return TP_PERC;
    }

    public void setTP_PERC(String TP_PERC) {
        this.TP_PERC = TP_PERC;
    }

    public String getADDITIONAL_BONUS_DISCOUNT() {
        return ADDITIONAL_BONUS_DISCOUNT;
    }

    public void setADDITIONAL_BONUS_DISCOUNT(String ADDITIONAL_BONUS_DISCOUNT) {
        this.ADDITIONAL_BONUS_DISCOUNT = ADDITIONAL_BONUS_DISCOUNT;
    }

    public String getMNP_COMMISSION_TARGET() {
        return MNP_COMMISSION_TARGET;
    }

    public void setMNP_COMMISSION_TARGET(String MNP_COMMISSION_TARGET) {
        this.MNP_COMMISSION_TARGET = MNP_COMMISSION_TARGET;
    }

    public String getNET_TARGET_VALUE() {
        return NET_TARGET_VALUE;
    }

    public void setNET_TARGET_VALUE(String NET_TARGET_VALUE) {
        this.NET_TARGET_VALUE = NET_TARGET_VALUE;
    }

    public String getNET_SALE_VALUE() {
        return NET_SALE_VALUE;
    }

    public void setNET_SALE_VALUE(String NET_SALE_VALUE) {
        this.NET_SALE_VALUE = NET_SALE_VALUE;
    }

    public String getCurrent_month_average() {
        return Current_month_average;
    }

    public void setCurrent_month_average(String current_month_average) {
        Current_month_average = current_month_average;
    }

    public String getRequired_month_average() {
        return required_month_average;
    }

    public void setRequired_month_average(String required_month_average) {
        this.required_month_average = required_month_average;
    }
}
