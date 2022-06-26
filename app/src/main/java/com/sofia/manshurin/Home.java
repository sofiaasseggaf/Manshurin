package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sofia.manshurin.adapter.AdapterBarangHome;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.util.List;

public class Home extends AppCompatActivity {

    ImageButton btn_setting, btn_saldo, btn_barang, btn_pembelian, btn_penjualan, btn_riwayat_pembelian, btn_riwayat_penjualan;
    TextView txt_total_saldo, txt_sisa_saldo, txtload;
    RecyclerView rv_home;
    DataHelper dbCenter;
    public static Home dataMaster;
    List<ModelBarang> listModelBarang;
    AdapterBarangHome itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        btn_setting = findViewById(R.id.btn_setting);
        btn_saldo = findViewById(R.id.btn_saldo);
        btn_barang = findViewById(R.id.btn_barang);
        btn_pembelian = findViewById(R.id.btn_pembelian);
        btn_penjualan = findViewById(R.id.btn_penjualan);
        btn_riwayat_pembelian = findViewById(R.id.btn_riwayat_pembelian);
        btn_riwayat_penjualan = findViewById(R.id.btn_riwayat_penjualan);
        txt_total_saldo = findViewById(R.id.txt_total_saldo);
        txt_sisa_saldo = findViewById(R.id.txt_sisa_saldo);
        rv_home = findViewById(R.id.rv_home);
        txtload = findViewById(R.id.textloading);

        dataMaster = this;
        dbCenter = new DataHelper(this);

        start();

        btn_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, DataBarang.class);
                startActivity(a);
                finish();
            }
        });

        btn_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, DataSaldo.class);
                startActivity(a);
                finish();
            }
        });

        btn_pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, Pembelian.class);
                startActivity(a);
                finish();
            }
        });

        btn_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, Penjualan.class);
                startActivity(a);
                finish();
            }
        });

        btn_riwayat_pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, PembelianRiwayat.class);
                startActivity(a);
                finish();
            }
        });

        btn_riwayat_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, PenjualanRiwayat.class);
                startActivity(a);
                finish();
            }
        });
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
                getDataBarang();
            }
        }).start();
    }

    public void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        if (listModelBarang!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData();
                }
            });
        } else {
            findViewById(R.id.framelayout).setVisibility(View.GONE);
        }
    }

    private void setData(){
        itemList = new AdapterBarangHome(listModelBarang);
        rv_home.setLayoutManager(new LinearLayoutManager(Home.this));
        rv_home.setAdapter(itemList);
        rv_home.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_home,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent a = new Intent(Home.this, DataBarangEdit.class);
                        a.putExtra("idbarang", listModelBarang.get(position).getId_barang());
                        startActivity(a);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tutup Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finish();
                        finishAffinity();
                    }
                })

                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }
}