package edu.vt.teja.ece4564.hokiemanager;

import android.os.AsyncTask;

/**
 * Created by teja on 10/9/13.
 */
public class AuthenticateTask extends AsyncTask<String, Void, String> {

    public AuthenticateTask() {

    }

    @Override
    protected String doInBackground(String... params){
        CentralAuthenticationService CAS = new CentralAuthenticationService("tejachil", "*Nayana1992");
        return "ret";
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onPreExecute() {

    }



}
