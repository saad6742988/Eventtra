package com.example.eventtra;

import android.app.Application;

public class GlobalData extends Application {
    MyUser user;

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
