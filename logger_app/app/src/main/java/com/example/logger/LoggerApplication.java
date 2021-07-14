package com.example.logger;

import android.app.Application;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LoggerApplication extends Application {

    private String mServerIp = "3.134.88.27:3000";
    private String mInitNodeId = "5f7f8a00afb260649723b339";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm").build();

    }

    public String get_Server_IP(){
        return mServerIp;
    }
    public String get_InitNode_Id(){ return mInitNodeId; }
}
