package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKeranjang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.utility.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Penjualan extends AppCompatActivity {

    EditText txt_jml_barang, txt_harga_jual;
    Spinner sp_barang;
    TextView txt_total_biaya, txtload;
    ImageButton btn_masukkan_keranjang, btn_cek_keranjang;
    DataHelper dbCenter;
    List<ModelBarang> listModelBarang;
    List<String> namaBarang = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String nama, harga;
    int id;
    ModelBarang modelBarang;
    List<ModelKeranjang> listModelKeranjang;
    List<ModelPenjualan> listModelPenjualan;
    Random rand;
    int upperbound, id_penjualan, id_riwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penjualan);

        txt_jml_barang = findViewById(R.id.txt_jml_barang);
        txt_harga_jual = findViewById(R.id.txt_harga_jual);
        sp_barang = findViewById(R.id.sp_barang);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        btn_masukkan_keranjang = findViewById(R.id.btn_masukkan_keranjang);
        btn_cek_keranjang = findViewById(R.id.btn_cek_keranjang);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);
        rand = new Random(); //instance of random class
        upperbound = 1000000;
        //generate random values from 0-1000000

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

        btn_masukkan_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nama.equalsIgnoreCase("") && !txt_jml_barang.getText().toString().equalsIgnoreCase("") &&
                        !txt_harga_jual.getText().toString().equalsIgnoreCase("")){
                    inputTransaksi();
                } else {
                    Toast.makeText(Penjualan.this, "Lengkapi Field", Toast.LENGTH_SHORT).show();
                }
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
            Toast.makeText(Penjualan.this, "Anda Belum Memiliki Barang", Toast.LENGTH_SHORT).show();
        }
    }

    private void setDataSpinner(){
        adapter = new ArrayAdapter<String>(Penjualan.this, R.layout.z_spinner_list, namaBarang);
        adapter.setDropDownViewResource(R.layout.z_spinner_list);
        sp_barang.setAdapter(adapter);

        if (PreferenceUtils.getIDRiwayat(getApplicationContext()).equalsIgnoreCase("")){
            id_riwayat = rand.nextInt(upperbound);
            PreferenceUtils.saveIDRiwayat(String.valueOf(id_riwayat), getApplicationContext());
        } else {
            // do nothing
        }
    }

    private void inputTransaksi(){
        id_penjualan = rand.nextInt(upperbound);
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into penjualan(id_penjualan, id_riwayat, id_barang, jumlah, harga) values('" +
                id_penjualan + "','" +
                id_riwayat + "','" +
                id + "','" +
                Integer.valueOf(txt_jml_barang.getText().toString()) + "','" +
                txt_harga_jual.getText().toString() + "')");
        masukkanKeranjang();
    }

    private void masukkanKeranjang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into keranjang(id_keranjang, id_transaksi) values('" +
                1234 + "','" +
                id_penjualan + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Masukkan Keranjang", Toast.LENGTH_SHORT).show();
        //PenjualanKeranjang.dataMaster.getDataKeranjang();
        txt_harga_jual.setText("");
        txt_jml_barang.setText("");
    }

    private void back(){
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
                listModelKeranjang = dbCenter.getAllKeranjang();
                hapusDataPenjualan();
            }
        }).start();
    }

    private void goToHome(){
        Intent a = new Intent(Penjualan.this, Home.class);
        startActivity(a);
        finish();
    }

    private void goToKeranjang(){
        Intent a = new Intent(Penjualan.this, PenjualanKeranjang.class);
        startActivity(a);
        finish();
    }

    private void hapusDataPenjualan(){
        // hapus semua data penjualan yg id nya ada dikeranjang
        hapusDataKeranjang();
    }

    private void hapusDataKeranjang(){
        // kalo udah, hapus semua data keranjang
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                goToHome();
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batalkan Semua Transaksi Penjualan ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        back();
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