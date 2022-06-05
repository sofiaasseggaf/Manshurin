package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class PembelianKeranjang extends AppCompatActivity {

    ImageButton btn_bayar;
    RecyclerView rvKeranjangPembelian;
    TextView txt_total_biaya, txtload;
    EditText txt_jumlah_katul, txt_harga_katul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembelian_keranjang);

        rvKeranjangPembelian = findViewById(R.id.rvKeranjangPembelian);
        btn_bayar = findViewById(R.id.btn_bayar);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        txt_jumlah_katul = findViewById(R.id.txt_jumlah_katul);
        txt_harga_katul = findViewById(R.id.txt_harga_katul);
        txtload = findViewById(R.id.textloading);

        start();

        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bayar();
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
                getDataKeranjang();
            }
        }).start();
    }

    private void getDataKeranjang(){
        //get data keranjang dulu, kalo done baru gini
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                setData();
            }
        });
    }

    private void setData(){
        // set data keranjang di recycle view
    }

    private void bayar(){
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
                sendDataPembelian();
                // kode atau id pembeliannya samain aja biar satu riwayat
            }
        }).start();
    }

    private void sendDataPembelian(){
        //send data pembelian
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                // kalo done tampilkan notif pembayaran berhasil
                // pakai notif alert dialog trus kasih opsi goToRiwayatPembelian
            }
        });
    }

    private void goToPembelian(){
        Intent a = new Intent(PembelianKeranjang.this, Pembelian.class);
        startActivity(a);
        // harus pake kode yg di save selama transaksi di sharedpreference
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tambah Pembelian ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        goToPembelian();
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