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
import com.sofia.manshurin.model.ModelSaldo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DataSaldoInput extends AppCompatActivity {

    EditText txt_nama_saldo, txt_jenis_saldo, txt_nominal_saldo, txt_deskripsi_saldo;
    ImageButton btn_simpan;
    TextView txtload, txt_id_saldo;
    DataHelper dbCenter;
    List<ModelSaldo> listModelSaldo;
    String now;
    Random rand;
    int upperbound, id_random, id_saldo, checkID, checkNama;
    String id, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_saldo_input);

        txt_id_saldo = findViewById(R.id.txt_id_saldo);
        txt_jenis_saldo = findViewById(R.id.txt_jenis_saldo);
        txt_nama_saldo = findViewById(R.id.txt_nama_saldo);
        txt_nominal_saldo = findViewById(R.id.txt_nominal_saldo);
        txt_deskripsi_saldo = findViewById(R.id.txt_deskripsi_saldo);
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
                if(!txt_id_saldo.getText().toString().equalsIgnoreCase("") && !txt_nama_saldo.getText().toString().equalsIgnoreCase("") &&
                        !txt_jenis_saldo.getText().toString().equalsIgnoreCase("") && !txt_nominal_saldo.getText().toString().equalsIgnoreCase("") &&
                        !txt_deskripsi_saldo.getText().toString().equalsIgnoreCase("")) {
                    checkId();
                } else {
                    Toast.makeText(DataSaldoInput.this, "Lengkapi Field Terlebih Dahulu", Toast.LENGTH_SHORT).show();
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
                getDataSaldo();
            }
        }).start();
    }

    private void getDataSaldo(){
        Log.d("DataSaldo", "get all saldo");
        listModelSaldo = dbCenter.getAllSaldo();
        if (listModelSaldo.size()>0){
            for(int i =0; i<listModelSaldo.size(); i++){
                if(!String.valueOf(listModelSaldo.get(i).getId_saldo()).equalsIgnoreCase(String.valueOf(id_random))){
                    id_saldo = id_random;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            findViewById(R.id.framelayout).setVisibility(View.GONE);
                            txt_id_saldo.setText(String.valueOf(id_saldo));
                        }
                    });
                }
                break;
            }
        } else {
            findViewById(R.id.framelayout).setVisibility(View.GONE);
            id_saldo = id_random;
            txt_id_saldo.setText(String.valueOf(id_saldo));
        }
    }

    private void checkId(){
        if(listModelSaldo.size()>0){
            id = txt_id_saldo.getText().toString();
            //nama = txt_nama_barang.getText().toString();
            for(int i=0; i<listModelSaldo.size(); i++){
                if(String.valueOf(listModelSaldo.get(i).getId_saldo()).equalsIgnoreCase(id)) {
                    Toast.makeText(DataSaldoInput.this, "ID Saldo Sudah Terpakai", Toast.LENGTH_SHORT).show();
                    checkID = 1;
                    break;
                }
            }
        } else {
            simpanDataSaldo();
        }

        if (checkID!=1){
            checkID=0;
            checknama();
        } else {
            checkID=0;
        }
    }

    private void checknama(){
        if(listModelSaldo.size()>0){
            nama = txt_id_saldo.getText().toString();
            for(int i=0; i<listModelSaldo.size(); i++){
                if(String.valueOf(listModelSaldo.get(i).getNama_saldo()).equalsIgnoreCase(nama)) {
                    Toast.makeText(DataSaldoInput.this, "Nama Saldo Sudah Terpakai", Toast.LENGTH_SHORT).show();
                    checkNama = 1;
                    break;
                }
            }
        } else {
            simpanDataSaldo();
        }

        if (checkNama!=1){
            checkNama=0;
            simpanDataSaldo();
        } else {
            checkNama=0;
        }
    }

    private void simpanDataSaldo(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into saldo(id_saldo, nama_saldo, jenis_saldo, nominal_saldo, desk_saldo, tgl_input, tgl_update) values('" +
                txt_id_saldo.getText() + "','" +
                txt_nama_saldo.getText().toString() + "','" +
                txt_jenis_saldo.getText().toString() + "','" +
                txt_nominal_saldo.getText().toString() + "','" +
                txt_deskripsi_saldo.getText().toString() + "','" +
                now + "','" +
                now + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Tambah Saldo", Toast.LENGTH_SHORT).show();
        DataSaldo.dataMaster.getDataSaldo();
        goToDataSaldo();
    }

    private void goToDataSaldo(){
        Intent a = new Intent(DataSaldoInput.this, DataSaldo.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batal Input Saldo ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        goToDataSaldo();
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