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
import com.sofia.manshurin.utility.NumberTextWatcher;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataSaldoEdit extends AppCompatActivity {

    EditText txt_jenis_saldo, txt_nominal_saldo, txt_deskripsi_saldo, txt_nama_saldo;
    ImageButton btn_simpan, btn_hapus;
    TextView txt_id_saldo, txtload;
    ModelSaldo modelSaldo;
    List<ModelSaldo> listModelSaldo;
    int id, checkNama;
    DataHelper dbCenter;
    String now, namaAwal, namaAkhir;
    DecimalFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_saldo_edit);

        txt_id_saldo = findViewById(R.id.txt_id_saldo);
        txt_jenis_saldo = findViewById(R.id.txt_jenis_saldo);
        txt_nominal_saldo = findViewById(R.id.txt_nominal_saldo);
        txt_deskripsi_saldo = findViewById(R.id.txt_deskripsi_saldo);
        txt_nama_saldo = findViewById(R.id.txt_nama_saldo);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_hapus = findViewById(R.id.btn_hapus);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("idsaldo", 0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        now = formatter.format(new Date());


        start();

        txt_nominal_saldo.addTextChangedListener(new NumberTextWatcher(txt_nominal_saldo));

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_jenis_saldo.getText().toString().equalsIgnoreCase("") && !txt_deskripsi_saldo.getText().toString().equalsIgnoreCase("") &&
                        !txt_nominal_saldo.getText().toString().equalsIgnoreCase("")){
                    checkNama();
                } else {
                    Toast.makeText(DataSaldoEdit.this, "Lengkapi Field Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DataSaldoEdit.this);
                alert.setMessage("Hapus Data Saldo ?")
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener()                 {
                            public void onClick(DialogInterface dialog, int which) {
                                hapusDataSaldo();
                            }
                        }).setNegativeButton("Cancel", null);

                AlertDialog alert1 = alert.create();
                alert1.show();
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

    public void getDataSaldo(){
        Log.d("DataSaldo", "get all saldo");
        listModelSaldo = dbCenter.getAllSaldo();
        getDataSaldoWithID(id);
    }

    private void getDataSaldoWithID(int i){
        Log.d("DataSaldo", "get saldo");
        modelSaldo = dbCenter.getSaldo(i);
        if (modelSaldo!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setDataSaldo();
                }
            });
        }
    }

    private void setDataSaldo(){
        namaAwal = txt_nama_saldo.getText().toString();
        txt_id_saldo.setText(String.valueOf(modelSaldo.getId_saldo()));
        txt_jenis_saldo.setText(modelSaldo.getJenis_saldo());
        txt_nama_saldo.setText(modelSaldo.getNama_saldo());
        txt_deskripsi_saldo.setText(modelSaldo.getDesk_saldo());

        int nom = Integer.valueOf(modelSaldo.getNominal_saldo());
        String a = checkDesimal(String.valueOf(nom));
        txt_nominal_saldo.setText(a);

    }

    private void checkNama(){
        namaAkhir = txt_nama_saldo.getText().toString();
        if (namaAkhir.equalsIgnoreCase(namaAkhir)){
            updateDataSaldo();
        } else {
            if(listModelSaldo.size()>0){
                for(int i=0; i<listModelSaldo.size(); i++){
                    if(String.valueOf(listModelSaldo.get(i).getNama_saldo()).equalsIgnoreCase(namaAkhir)) {
                        Toast.makeText(DataSaldoEdit.this, "Nama Saldo Sudah Terpakai", Toast.LENGTH_SHORT).show();
                        checkNama = 1;
                        break;
                    }
                }
            } else {
                updateDataSaldo();
            }
        }


        if (checkNama!=1){
            checkNama=0;
            updateDataSaldo();
        } else {
            checkNama=0;
        }

    }

    private void updateDataSaldo(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("update saldo set nama_saldo='"+txt_nama_saldo.getText().toString()+
                "', jenis_saldo='"+txt_jenis_saldo.getText().toString()+
                "', nominal_saldo='"+txt_nominal_saldo.getText().toString().replaceAll("[^0-9]", "")+
                "', desk_saldo='"+txt_deskripsi_saldo.getText().toString()+
                "', tgl_update='"+now+"' " +
                "where id_saldo='"+ modelSaldo.getId_saldo() +"'");
        DataSaldo.dataMaster.getDataSaldo();
        Toast.makeText(this, "Berhasil Update Data Saldo", Toast.LENGTH_SHORT).show();
        goToDataSaldo();
    }

    private void hapusDataSaldo(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("delete from saldo where id_saldo = '"+modelSaldo.getId_saldo()+"'");
        DataSaldo.dataMaster.getDataSaldo();
        goToDataSaldo();
    }

    private String checkDesimal(String a){

        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator('.');
        formatter = new DecimalFormat("###,###.##", symbols);

        if(a!=null || !a.equalsIgnoreCase("")){
            if(a.length()>3){
                a = formatter.format(Double.valueOf(a));
            }
        }
        return a;
    }

    private void goToDataSaldo(){
        Intent a = new Intent(DataSaldoEdit.this, DataSaldo.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batal Edit Saldo ?")
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