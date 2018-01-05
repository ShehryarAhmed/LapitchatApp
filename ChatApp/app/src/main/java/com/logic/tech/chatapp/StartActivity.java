package com.logic.tech.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by android on 12/31/2017.
 */

public class StartActivity extends AppCompatActivity {
    private Button reg_btn;
    private Button login_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        reg_btn = (Button) findViewById(R.id.reg_btn);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_Activity = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(reg_Activity);
            }
        });

        login_btn = (Button) findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login_activity = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(login_activity);
            }
        });


    }
}
