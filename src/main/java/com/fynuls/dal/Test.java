package com.fynuls.dal;

import com.fynuls.entity.SaleDetailTemp;
import com.fynuls.entity.base.Employee;

import java.util.ArrayList;
import java.util.List;

public class Test {
    List<Employee> employeeList = new ArrayList<>();
    List<SaleDetailTemp> saleDetailTemps = new ArrayList<>();

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public List<SaleDetailTemp> getSaleDetailTemps() {
        return saleDetailTemps;
    }

    public void setSaleDetailTemps(List<SaleDetailTemp> saleDetailTemps) {
        this.saleDetailTemps = saleDetailTemps;
    }
}
