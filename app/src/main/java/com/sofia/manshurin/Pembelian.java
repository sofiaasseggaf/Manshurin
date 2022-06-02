package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Pembelian extends AppCompatActivity {

    EditText txt_jml_barang, txt_harga_beli, txt_jumlah_katul, txt_harga_katul;
    Spinner sp_barang;
    TextView txt_total_biaya, txtload;
    ImageButton btn_masukkan_keranjang, btn_cek_keranjang;
    LinearLayout ll_barang_baru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembelian);

        txt_jml_barang = findViewById(R.id.txt_jml_barang);
        txt_harga_beli = findViewById(R.id.txt_harga_beli);
        txt_jumlah_katul = findViewById(R.id.txt_jumlah_katul);
        txt_harga_katul = findViewById(R.id.txt_harga_katul);
        sp_barang = findViewById(R.id.sp_barang);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        btn_masukkan_keranjang = findViewById(R.id.btn_masukkan_keranjang);
        btn_cek_keranjang = findViewById(R.id.btn_cek_keranjang);
        ll_barang_baru = findViewById(R.id.ll_barang_baru);
        txtload = findViewById(R.id.textloading);

        start();

        // set on click ke spinner, ketika barang dipilih, get id, nama, dan harganya
        // kalau barang yg dicari gaada, barti klik barang baru di value spinner paling bawah,
        // trus set ll_barang_baru visibilitinya visible
        // kalau klik selain itu, visibilitinya gone in lagi

        btn_masukkan_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cek id, nama, harga, jumlah, harga katul, jumlah katul, total
                simpan();
            }
        });

        btn_cek_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToKeranjang();
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

    private void getDataBarang(){
        //get data barang dulu, kalo done baru gini
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                setSpinner();
            }
        });
    }

    private void setSpinner(){
        // set spinner barang
    }

    private void simpan(){
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
                masukkkanKeranjang();
            }
        }).start();
    }

    private void masukkkanKeranjang(){
        //simpan data barang ke table keranjang, kalo done baru gini
        // pake int aja utk tau uda berapa kali, kaya int++
        //nanti pas berhasil transaksi, data di table keranjang apus semua, kaya refresh gituwes
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                Toast.makeText(Pembelian.this, "Berhasil Tambah Barang ke Keranjang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToHome(){
        Intent a = new Intent(Pembelian.this, Home.class);
        startActivity(a);
        finish();
    }

    private void goToKeranjang(){
        Intent a = new Intent(Pembelian.this, PembelianKeranjang.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batalkan Transaksi Pembelian ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        goToHome();
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