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
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPenjualan;

import java.util.ArrayList;
import java.util.List;

public class RiwayatPembelian extends AppCompatActivity {

    RecyclerView rvRiwayatPembelian, rvRiwayatPembelian2;
    TextView txtload;
    DataHelper dbCenter;
    List<ModelPenjualan> listModelPenjualan = new ArrayList<ModelPenjualan>();
    List<ModelKranjang> listModelKeranjang = new ArrayList<>();
    AdapterKeranjangPenjualan itemList;
    AdapterKeranjang itemList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_pembelian);

        rvRiwayatPembelian = findViewById(R.id.rvRiwayatPembelian);
        rvRiwayatPembelian2 = findViewById(R.id.rvRiwayatPembelian2);
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
                getDataKeranjang();
                getDataPenjualan();
                //getDataRiwayatPembelian();
            }
        }).start();
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
                    Toast.makeText(RiwayatPembelian.this, "ga ada penjualan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setData(){
        itemList = new AdapterKeranjangPenjualan(listModelPenjualan);
        rvRiwayatPembelian.setLayoutManager(new LinearLayoutManager(RiwayatPembelian.this));
        rvRiwayatPembelian.setAdapter(itemList);
    }

    private void getDataKeranjang(){
        listModelKeranjang = dbCenter.getAllKranjang();
        if(listModelKeranjang.size()>0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData2();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(RiwayatPembelian.this, "ga ada keranjang", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setData2(){
        itemList2 = new AdapterKeranjang(listModelKeranjang);
        rvRiwayatPembelian2.setLayoutManager(new LinearLayoutManager(RiwayatPembelian.this));
        rvRiwayatPembelian2.setAdapter(itemList2);
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

   /* private void setData(){
        // set data di recycle view
    }*/

    private void goToHome(){
        Intent a = new Intent(RiwayatPembelian.this, Home.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }
}