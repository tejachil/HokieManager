package edu.vt.teja.ece4564.hokiemanager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btnAuthenticate = (Button) findViewById(R.id.button_authenticate);

        btnAuthenticate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new AuthenticateTask().execute("Login");
            }
        });
        //CentralAuthenticationService CAS = new CentralAuthenticationService("tejachil", "*Nayana1992");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
}
