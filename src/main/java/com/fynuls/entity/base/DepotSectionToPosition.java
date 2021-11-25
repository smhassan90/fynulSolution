package com.fynuls.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Syed Muhammad Hassan
 * 24th November, 2021
 */
@Entity
@Table(name="base_depot_section_to_position")
public class DepotSectionToPosition {
    @Id
    @Column(name="id")
    private int id;
    @Column(name="PositionCode")
    private String PositionCode;
    @Column(name="NewSectionCode")
    private String NewSectionCode;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNewSectionCode() {
        return NewSectionCode;
    }

    public void setNewSectionCode(String newSectionCode) {
        NewSectionCode = newSectionCode;
    }

    public String getPositionCode() {
        return PositionCode;
    }

    public void setPositionCode(String positionCode) {
        PositionCode = positionCode;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
