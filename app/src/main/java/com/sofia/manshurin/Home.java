package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    ImageButton btn_setting, btn_saldo, btn_barang, btn_pembelian, btn_penjualan, btn_riwayat_pembelian, btn_riwayat_penjualan;
    TextView txt_total_saldo, txt_sisa_saldo;
    RecyclerView rv_home;

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
                Intent a = new Intent(Home.this, RiwayatPembelian.class);
                startActivity(a);
                finish();
            }
        });

        btn_riwayat_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, RiwayatPenjualan.class);
                startActivity(a);
                finish();
            }
        });

    }
}