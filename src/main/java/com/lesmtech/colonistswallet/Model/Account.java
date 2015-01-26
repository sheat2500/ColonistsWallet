package com.lesmtech.colonistswallet.Model;

import java.io.Serializable;

/**
 * Created by Te on 1/22/15.
 */
public class Account implements Serializable {

    String ID;
    String USERNAME;
    String PASSWORD;
    String COUNTRY;
    String STATE;
    String UNIVERSITY;

    public Account(String ID, String USERNAME, String PASSWORD, String COUNTRY, String STATE, String UNIVERSITY) {
        this.ID = ID;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        this.COUNTRY = COUNTRY;
        this.STATE = STATE;
        this.UNIVERSITY = UNIVERSITY;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getUNIVERSITY() {
        return UNIVERSITY;
    }

    public void setUNIVERSITY(String UNIVERSITY) {
        this.UNIVERSITY = UNIVERSITY;
    }
}
