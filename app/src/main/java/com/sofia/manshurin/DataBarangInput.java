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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataBarangInput extends AppCompatActivity {

    EditText txt_nama_barang, txt_harga_barang, txt_deskripsi_barang;
    ImageButton btn_simpan;
    TextView txtload, txt_id_barang;
    DataHelper dbCenter;
    List<ModelBarang> listModelBarang;
    String now;
    Random rand;
    int upperbound, id_random, id_saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_barang_input);

        txt_id_barang = findViewById(R.id.txt_id_barang);
        txt_nama_barang = findViewById(R.id.txt_nama_barang);
        txt_harga_barang = findViewById(R.id.txt_harga_barang);
        txt_deskripsi_barang = findViewById(R.id.txt_deskripsi_barang);
        btn_simpan = findViewById(R.id.btn_simpan);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        now = formatter.format(new Date());

        rand = new Random(); //instance of random class
        upperbound = 1000000;
        //generate random values from 0-1000000
        id_random = rand.nextInt(upperbound);

        start();

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_id_barang.getText().toString().equalsIgnoreCase("") && !txt_nama_barang.getText().toString().equalsIgnoreCase("") &&
                        !txt_harga_barang.getText().toString().equalsIgnoreCase("") && !txt_deskripsi_barang.getText().toString().equalsIgnoreCase("")){
                    if(listModelBarang.size()>0){
                        for(int i=0; i<listModelBarang.size(); i++){
                            String id = txt_id_barang.getText().toString();
                            String nama = txt_nama_barang.getText().toString();
                            if(!String.valueOf(listModelBarang.get(i).getId_barang()).equalsIgnoreCase(id)){
                                if (!listModelBarang.get(i).getNama_barang().equalsIgnoreCase(nama)){
                                    simpanDataBarang();
                                } else {
                                    Toast.makeText(DataBarangInput.this, "Nama Barang Sudah Terpakai", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(DataBarangInput.this, "ID Barang Sudah Terpakai", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        simpanDataBarang();
                    }
                } else {
                    Toast.makeText(DataBarangInput.this, "Lengkapi Field Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
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
        if (listModelBarang!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(int i =0; i<listModelBarang.size(); i++){
                        if(String.valueOf(listModelBarang.get(i).getId_barang()).equalsIgnoreCase(String.valueOf(id_random))){
                            id_saldo = id_random;
                            findViewById(R.id.framelayout).setVisibility(View.GONE);
                            txt_id_barang.setText(id_saldo);
                        }
                    }

                }
            });
        } else {
            findViewById(R.id.framelayout).setVisibility(View.GONE);
        }
    }

    private void simpanDataBarang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into barang(id_barang, nama_barang, harga_barang, jml_barang, desk_barang, tgl_input, tgl_update) values('" +
                txt_id_barang.getText() + "','" +
                txt_nama_barang.getText().toString() + "','" +
                txt_harga_barang.getText().toString() + "','" +
                "0" + "','" +
                txt_deskripsi_barang.getText().toString() + "','" +
                now + "','" +
                now + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Tambah Barang", Toast.LENGTH_SHORT).show();
        DataBarang.dataMaster.getDataBarang();
        Home.dataMaster.getDataBarang();
        goToDataBarang();
    }

    private void goToDataBarang(){
        Intent a = new Intent(DataBarangInput.this, DataBarang.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batal Input Barang ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        goToDataBarang();
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