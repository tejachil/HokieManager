package edu.vt.teja.ece4564.hokiemanager;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LoginActivity extends Activity {

    private static CentralAuthenticationService cas_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cas_ = GlobalApplication.CAS_;
        final Button btnAuthenticate = (Button) findViewById(R.id.button_authenticate);
        final EditText fieldPID = (EditText) findViewById(R.id.editText_PID);
        final EditText fieldPassword = (EditText) findViewById(R.id.editText_password);
        final ProgressBar progressLogin = (ProgressBar) findViewById(R.id.progressBar_login);

        btnAuthenticate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                AuthenticateTask authThread = new AuthenticateTask(LoginActivity.this, cas_, progressLogin);
                authThread.execute(fieldPID.getText().toString(), fieldPassword.getText().toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
}
