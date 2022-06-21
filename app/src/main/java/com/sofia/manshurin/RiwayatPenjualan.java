package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterKeranjang;
import com.sofia.manshurin.adapter.AdapterKeranjangPenjualan;
import com.sofia.manshurin.adapter.AdapterRiwayatPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPenjualan;

import java.util.ArrayList;
import java.util.List;

public class RiwayatPenjualan extends AppCompatActivity {

    RecyclerView rvRiwayatPenjualan;
    TextView txtload;
    DataHelper dbCenter;
    List<ModelRiwayatPenjualan> listModelRiwayat = new ArrayList<ModelRiwayatPenjualan>();
    List<ModelPenjualan> listModelPenjualan = new ArrayList<ModelPenjualan>();
    AdapterRiwayatPenjualan itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_penjualan);

        rvRiwayatPenjualan = findViewById(R.id.rvRiwayatPenjualan);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);

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
                getDataRiwayatPenjualan();
            }
        }).start();
    }

    private void getDataRiwayatPenjualan(){
        listModelRiwayat = dbCenter.getAllRiwayatPenjualan();
        if(listModelRiwayat.size()>0){
            getDataPenjualan();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(RiwayatPenjualan.this, "Tidak Memiliki Riwayat Penjualan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getDataPenjualan(){
        listModelPenjualan = dbCenter.getAllPenjualan();
        if(listModelPenjualan.size()>0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(RiwayatPenjualan.this, "Tidak Memiliki Riwayat Penjualan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setData(){
        itemList = new AdapterRiwayatPenjualan(listModelRiwayat, listModelPenjualan, getApplicationContext());
        rvRiwayatPenjualan.setLayoutManager(new LinearLayoutManager(RiwayatPenjualan.this));
        rvRiwayatPenjualan.setAdapter(itemList);
    }

    private void goToHome(){
        Intent a = new Intent(RiwayatPenjualan.this, Home.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }

}