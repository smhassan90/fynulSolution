package com.fynuls.entity.crb;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Syed Muhammad Hassan
 * 8th September, 2022
 */

@Entity
@Table(name="CRB_USER")
public class User {
    @Id
    @Column(name="ID")
    String id;
    @Column(name="NAME")
    String name;
    @Column(name="EMAIL")
    String email;
    @Column(name="PHONENUMBER")
    String phoneNumber;
    @Column(name="UPDATE_DATE")
    String updateDate;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
