package com.ppmdev.agriman.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Handler;
import android.os.Looper;


import com.ppmdev.agriman.R;
import com.ppmdev.agriman.utils.FirebaseUtil;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splashscreen);
        //splashScreen.setKeepOnScreenCondition(() -> true);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        //Direct logIn or LogIn require
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseUtil.isLoggedIn()){
                    startActivity(new Intent(SplashScreen.this, HomePage.class));
                }else{
                    startActivity(new Intent(SplashScreen.this, LogIn.class));
                }

                finish();
            }
        },2000);
    }
}