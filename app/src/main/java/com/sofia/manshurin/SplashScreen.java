package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

public class SplashScreen extends AppCompatActivity {

    ImageButton btn_masuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        btn_masuk = findViewById(R.id.btn_masuk);
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });

    }

    public void goToHome(){
        Intent a = new Intent(SplashScreen.this, Home.class);
        startActivity(a);
        finish();
    }
}