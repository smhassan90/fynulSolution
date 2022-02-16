package com.fynuls.dal;

import com.fynuls.entity.base.PRDGroupOn;
import com.fynuls.entity.base.Universe;
import com.fynuls.entity.sale.*;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    List<Depot> depots;
    List<Town> towns;
    List<Universe> universeList;
    List<TownDepot> townDepots;
    List<TownCustomer> townCustomers;
    List<Status> statuses;
    List<PRDGroupOn> prdGroupOns;
    List<WorkWith> workWiths;
    Dashboard dashboard;

    public List<Depot> getDepots() {
        return depots;
    }

    public void setDepots(List<Depot> depots) {
        this.depots = depots;
    }

    public List<Town> getTowns() {
        return towns;
    }

    public void setTowns(List<Town> towns) {
        this.towns = towns;
    }

    public List<Universe> getUniverseList() {
        return universeList;
    }

    public void setUniverseList(List<Universe> universeList) {
        this.universeList = universeList;
    }

    public List<TownDepot> getTownDepots() {
        return townDepots;
    }

    public void setTownDepots(List<TownDepot> townDepots) {
        this.townDepots = townDepots;
    }

    public List<TownCustomer> getTownCustomers() {
        return townCustomers;
    }

    public void setTownCustomers(List<TownCustomer> townCustomers) {
        this.townCustomers = townCustomers;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public List<PRDGroupOn> getPrdGroupOns() {
        return prdGroupOns;
    }

    public void setPrdGroupOns(List<PRDGroupOn> prdGroupOns) {
        this.prdGroupOns = prdGroupOns;
    }

    public List<WorkWith> getWorkWiths() {
        return workWiths;
    }

    public void setWorkWiths(List<WorkWith> workWiths) {
        this.workWiths = workWiths;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
}
