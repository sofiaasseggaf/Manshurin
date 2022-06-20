package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKeranjang;

import java.util.ArrayList;
import java.util.List;

public class Pembelian extends AppCompatActivity {

    EditText txt_jml_barang, txt_harga_beli, txt_jumlah_katul, txt_harga_katul;
    Spinner sp_barang;
    TextView txt_total_biaya, txtload;
    ImageButton btn_masukkan_keranjang, btn_cek_keranjang;
    LinearLayout ll_barang_baru;
    DataHelper dbCenter;
    List<ModelBarang> listModelBarang;
    List<String> namaBarang = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String nama, harga;
    int id;
    ModelBarang modelBarang;
    List<ModelKeranjang> listModelKeranjang;
    List<ModelKeranjang> listModelKeranjang2 = new ArrayList<ModelKeranjang>();

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

        dbCenter = new DataHelper(this);

        start();

        sp_barang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (namaBarang.size()>0){
                    nama = sp_barang.getSelectedItem().toString();
                    for (int a=0; a<listModelBarang.size(); a++){
                        try {
                            if (listModelBarang.get(a).getNama_barang().equalsIgnoreCase(nama)){
                                id = listModelBarang.get(a).getId_barang();
                                harga = listModelBarang.get(a).getHarga_barang();
                                modelBarang = listModelBarang.get(a);
                            }
                        } catch (Exception e){}
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });


        // kalau barang yg dicari gaada, barti klik barang baru di value spinner paling bawah,
        // trus set ll_barang_baru visibilitinya visible
        // kalau klik selain itu, visibilitinya gone in lagi

        btn_masukkan_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // cek id, nama, harga, jumlah, harga katul, jumlah katul, total
                // simpan();
                AlertDialog.Builder builder = new AlertDialog.Builder(Pembelian.this);
                builder.setMessage("Fitur Sedang Dikerjakan")
                        .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent a = new Intent(Pembelian.this, Pembelian.class);
                                startActivity(a);
                                finish();
                            }
                        })
                        .create()
                        .show();
            }
        });

        btn_cek_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delAllKeranjang();
                //goToKeranjang();
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
        if (listModelBarang.size()>0){
            for (int i=0; i<listModelBarang.size(); i++){
                namaBarang.add(listModelBarang.get(i).getNama_barang());
            }
            if(namaBarang.size()>0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.framelayout).setVisibility(View.GONE);
                        setDataSpinner();
                    }
                });
            }
        } else {
            findViewById(R.id.framelayout).setVisibility(View.GONE);
            Toast.makeText(Pembelian.this, "Anda Belum Memiliki Barang", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataSpinner(){
        adapter = new ArrayAdapter<String>(Pembelian.this, R.layout.z_spinner_list, namaBarang);
        adapter.setDropDownViewResource(R.layout.z_spinner_list);
        sp_barang.setAdapter(adapter);
    }


    private void delAllKeranjang(){
        listModelKeranjang = dbCenter.getAllKeranjang();
        AlertDialog.Builder alert = new AlertDialog.Builder(Pembelian.this);
        alert.setMessage("Hapus Semua Data Keranjang ?")
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener()                 {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = dbCenter.getWritableDatabase();
                        for(int i=0; i<listModelKeranjang.size(); i++){
                            db.execSQL("delete from keranjang where id_keranjang = '"+listModelKeranjang.get(i).getId_keranjang()+"'");
                            //db.execSQL("delete from keranjang where id_keranjang = '"+listModelKeranjang.get(i).getId_traksaksi()+"'");
                        }
                        listModelKeranjang2 = dbCenter.getAllKeranjang();
                        if(listModelKeranjang2.size()==0){
                            Toast.makeText(Pembelian.this, "Keranjang Kosong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alert1 = alert.create();
        alert1.show();
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
        // simpan data barang ke table keranjang, kalo done baru gini
        // pake int aja utk tau uda berapa kali, kaya int++
        // nanti pas berhasil transaksi, data di table keranjang apus semua, kaya refresh gituwes
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