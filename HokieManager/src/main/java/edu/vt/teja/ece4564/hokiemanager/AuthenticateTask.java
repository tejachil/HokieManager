package edu.vt.teja.ece4564.hokiemanager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by teja on 10/9/13.
 */
public class AuthenticateTask extends AsyncTask<String, Void, String> {
    private CentralAuthenticationService cas_;
    private Context context_;
    private boolean loginSuccess_;
    final ProgressBar progressLogin_;

    public AuthenticateTask(Context context, CentralAuthenticationService CAS, ProgressBar progress) {
        loginSuccess_ = false;
        context_ = context;
        cas_ = CAS;
        progressLogin_ = progress;
    }

    @Override
    protected void onPreExecute() {
        progressLogin_.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... params){
        Log.d("Location", "Reached Do In background: " + params[0] + " " + params[1]);
        try{
            loginSuccess_ = cas_.login(params[0], params[1]);
        }
        catch (IOException e){

        }
        return "ret";
    }

    @Override
    protected void onPostExecute(String result) {
        progressLogin_.setVisibility(View.INVISIBLE);
        if(loginSuccess_)
            Toast.makeText(context_, "Succesfully logged in to Virginia Tech CAS!", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context_, "Unable to log in to Virginia Tech CAS", Toast.LENGTH_LONG).show();
    }



}
