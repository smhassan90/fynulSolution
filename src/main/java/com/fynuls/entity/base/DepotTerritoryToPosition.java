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
@Table(name="base_depot_territory_to_position")
    public class DepotTerritoryToPosition {
    @Id
    @Column(name="id")
    private int id;
    @Column(name="depot_territory")
    private String depot_territory;
    @Column(name="position_code")
    private String position_code;
    @Column(name="share")
    private int share;
    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepot_territory() {
        return depot_territory;
    }

    public void setDepot_territory(String depot_territory) {
        this.depot_territory = depot_territory;
    }

    public String getPosition_code() {
        return position_code;
    }

    public void setPosition_code(String position_code) {
        this.position_code = position_code;
    }

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
