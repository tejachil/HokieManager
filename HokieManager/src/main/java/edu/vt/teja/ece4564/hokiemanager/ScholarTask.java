package edu.vt.teja.ece4564.hokiemanager;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

/**
 * Created by teja on 10/9/13.
 */
public class ScholarTask extends AsyncTask<String, Void, String> {
    private CentralAuthenticationService cas_;

    public ScholarTask(CentralAuthenticationService CAS) {
        cas_ = CAS;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params){
        Log.d("Location", "Reached Do In background of Scholar: " + params[0] + " " + params[1]);
        try{
            cas_.login(params[0], params[1]);
        }
        catch (IOException e){

        }
        return "ret";
    }

    @Override
    protected void onPostExecute(String result) {

    }



}
