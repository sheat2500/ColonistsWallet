package com.lesmtech.colonistswallet.Activity;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Te on 1/25/15.
 */
public class ColonistsWalletApplication extends Application {

    boolean LogIn = false;

    private static ColonistsWalletApplication singleton;

    public static ColonistsWalletApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "WBUDf2WzMMtafXHLgnpuuNV1hpQpBjYufiCPJ526", "k9CKDWPlheau77PjRfw3GPTN0WePnkAb866lOYOR");
        singleton = this;
    }

    public void setLogIn(boolean flag) {
        LogIn = flag;
    }

    public boolean getLogIn() {
        return LogIn;
    }

}
