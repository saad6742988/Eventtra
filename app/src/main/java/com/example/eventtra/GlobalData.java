package com.example.eventtra;

import android.app.Application;
import android.util.Log;

public class GlobalData extends Application {
     MyUser globalUser;
     MyEvent globalEvent;
     subEventsModel globalSubEvent;

    public GlobalData() {
        this.globalUser = new MyUser();
    }

    public MyUser getGlobalUser() {
        return this.globalUser;
    }
    public void setglobalUser(MyUser u) {
        this.globalUser = u;
    }

}
