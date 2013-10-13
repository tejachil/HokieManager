package edu.vt.teja.ece4564.hokiemanager;

import android.app.Application;

/**
 * Created by teja on 10/13/13.
 */
public class GlobalApplication extends Application {
    public static CentralAuthenticationService CAS_ = new CentralAuthenticationService();

    @Override
    public void onCreate() {
        super.onCreate();
        //initialize myObject here, if needed
    }
}
