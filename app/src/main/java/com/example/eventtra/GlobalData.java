package com.example.eventtra;

import android.app.Application;
import android.util.Log;

public class GlobalData extends Application {
     MyUser globalUser;

    public GlobalData() {
        this.globalUser = new MyUser();
    }

    public MyUser getGlobalUser() {
        Log.d("global user in get", "getUser: "+this.globalUser.toString());
        return this.globalUser;
    }
    public void setglobalUser(MyUser u) {
        this.globalUser = u;
    }

}
