package com.lesmtech.colonistswallet.Model;

import java.io.Serializable;

/**
 * Created by Te on 1/23/15.
 */
public class Record implements Serializable{

    String ID;
    String ITEM;
    String CATEGORY;
    String MONEY;
    String DATE;

    public Record(String ID, String ITEM, String CATEGORY, String MONEY, String DATE) {
        this.ID = ID;
        this.ITEM = ITEM;
        this.CATEGORY = CATEGORY;
        this.MONEY = MONEY;
        this.DATE = DATE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getITEM() {
        return ITEM;
    }

    public void setITEM(String ITEM) {
        this.ITEM = ITEM;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public void setCATEGORY(String CATEGORY) {
        this.CATEGORY = CATEGORY;
    }

    public String getMONEY() {
        return MONEY;
    }

    public void setMONEY(String MONEY) {
        this.MONEY = MONEY;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
