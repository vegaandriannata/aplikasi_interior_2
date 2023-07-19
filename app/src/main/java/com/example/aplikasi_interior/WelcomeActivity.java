package com.example.aplikasi_interior;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    Button bLogin, bRegister;
    Animation aLogin, aRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bLogin = findViewById(R.id.wLogin);
        bRegister = findViewById(R.id.wRegister);

        aLogin = AnimationUtils.loadAnimation(this, R.anim.atgsc1);
        bLogin.startAnimation(aLogin);

        aRegister = AnimationUtils.loadAnimation(this, R.anim.atgsc12);
        bRegister.setAnimation(aRegister);

        click();
    }

    private void click() {
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, Login.class));
            }
        });
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
            }
        });
    }
}
