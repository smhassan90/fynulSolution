package com.fynuls.dal;

import com.fynuls.entity.base.Employee;

public class CompleteEmployee {
    String positionCode;
    Employee employee;

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

}
