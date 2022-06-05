package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class RiwayatPembelian extends AppCompatActivity {

    RecyclerView rvRiwayatPembelian;
    TextView txtload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_pembelian);

        rvRiwayatPembelian = findViewById(R.id.rvRiwayatPembelian);
        txtload = findViewById(R.id.textloading);

        start();

    }

    private void start(){
        findViewById(R.id.framelayout).setVisibility(View.VISIBLE);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                count++;
                if (count == 1) {
                    txtload.setText("Tunggu Sebentar Ya ."); }
                else if (count == 2) {
                    txtload.setText("Tunggu Sebentar Ya . ."); }
                else if (count == 3) {
                    txtload.setText("Tunggu Sebentar Ya . . ."); }
                if (count == 3)
                    count = 0;
                handler.postDelayed(this, 1500);
            }
        };
        handler.postDelayed(runnable, 1 * 1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getDataRiwayatPembelian();
            }
        }).start();
    }

    private void getDataRiwayatPembelian(){
        //get data riwayat pembelian dulu, kalo done baru gini
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                setData();
            }
        });
    }

    private void setData(){
        // set data di recycle view
    }

    private void goToHome(){
        Intent a = new Intent(RiwayatPembelian.this, Home.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }
}