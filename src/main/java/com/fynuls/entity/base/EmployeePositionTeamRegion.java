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
@Table(name="BASE_EMP_POSITION_TEAMREGION")
public class EmployeePositionTeamRegion {
    @Id
    @Column(name="POSITION_ID")
    private String POSITION_ID;
    @Column(name="TEAMREGION_ID")
    private String TEAMREGION_ID;

    @Column(name="UPDATE_DATE")
    private Date updateDate;

    public String getTEAMREGION_ID() {
        return TEAMREGION_ID;
    }

    public void setTEAMREGION_ID(String TEAMREGION_ID) {
        this.TEAMREGION_ID = TEAMREGION_ID;
    }

    public String getPOSITION_ID() {
        return POSITION_ID;
    }

    public void setPOSITION_ID(String POSITION_ID) {
        this.POSITION_ID = POSITION_ID;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
