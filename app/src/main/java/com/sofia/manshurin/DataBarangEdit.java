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
import com.sofia.manshurin.utility.NumberTextWatcher;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBarangEdit extends AppCompatActivity {

    EditText txt_nama_barang, txt_harga_barang, txt_deskripsi_barang, txt_jumlah_barang;
    ImageButton btn_simpan, btn_hapus;
    TextView txt_id_barang, txtload;
    ModelBarang modelBarang;
    List<ModelBarang> listModelBarang;
    int id, checkNama;
    DataHelper dbCenter;
    String now, namaAwal, namaAkhir;
    DecimalFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_barang_edit);

        txt_id_barang = findViewById(R.id.txt_id_barang);
        txt_nama_barang = findViewById(R.id.txt_nama_barang);
        txt_harga_barang = findViewById(R.id.txt_harga_barang);
        txt_deskripsi_barang = findViewById(R.id.txt_deskripsi_barang);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_hapus = findViewById(R.id.btn_hapus);
        txt_jumlah_barang = findViewById(R.id.txt_jumlah_barang);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("idbarang", 0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        now = formatter.format(new Date());

        start();

        txt_harga_barang.addTextChangedListener(new NumberTextWatcher(txt_harga_barang));

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txt_nama_barang.getText().toString().equalsIgnoreCase("") && !txt_harga_barang.getText().toString().equalsIgnoreCase("") &&
                        !txt_deskripsi_barang.getText().toString().equalsIgnoreCase("") && !txt_jumlah_barang.getText().toString().equalsIgnoreCase("")){
                    checkNama();
                } else {
                    Toast.makeText(DataBarangEdit.this, "Lengkapi Field Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(DataBarangEdit.this);
                alert.setMessage("Hapus Data Barang ?")
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener()                 {
                            public void onClick(DialogInterface dialog, int which) {
                                hapusDataBarang();
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
                getDataBarang();
            }
        }).start();
    }

    private void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        getDataBarangWithID(id);
    }

    private void getDataBarangWithID(int i){
        Log.d("DataBarang", "get barang");
        modelBarang = dbCenter.getBarang(i);
        if (modelBarang!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setDataBarang();
                }
            });
        }
    }

    private void setDataBarang(){
        namaAwal = txt_nama_barang.getText().toString();
        txt_id_barang.setText(String.valueOf(modelBarang.getId_barang()));
        txt_nama_barang.setText(modelBarang.getNama_barang());
        txt_jumlah_barang.setText(String.valueOf(modelBarang.getJml_barang()));
        txt_deskripsi_barang.setText(modelBarang.getDesk_barang());

        //int nom = Integer.valueOf(modelBarang.getHarga_barang());
        String a = checkDesimal(modelBarang.getHarga_barang());
        txt_harga_barang.setText(a);
    }

    private void checkNama(){
        namaAkhir = txt_nama_barang.getText().toString();
        if (namaAkhir.equalsIgnoreCase(namaAkhir)){
            updateDataBarang();
        } else {
            if(listModelBarang.size()>0){
                for(int i=0; i<listModelBarang.size(); i++){
                    if(String.valueOf(listModelBarang.get(i).getNama_barang()).equalsIgnoreCase(namaAkhir)) {
                        Toast.makeText(DataBarangEdit.this, "Nama Barang Sudah Terpakai", Toast.LENGTH_SHORT).show();
                        checkNama = 1;
                        break;
                    }
                }
            } else {
                updateDataBarang();
            }
        }


        if (checkNama!=1){
            checkNama=0;
            updateDataBarang();
        } else {
            checkNama=0;
        }

    }

    private void updateDataBarang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("update barang set nama_barang='"+txt_nama_barang.getText().toString()+
                "', harga_barang='"+txt_harga_barang.getText().toString().replaceAll("[^0-9]", "")+
                "', jml_barang='"+txt_jumlah_barang.getText()+
                "', desk_barang='"+txt_deskripsi_barang.getText().toString()+
                "', tgl_update='"+now+"' " +
                "where id_barang='"+ modelBarang.getId_barang() +"'");
        DataBarang.dataMaster.getDataBarang();
        Home.dataMaster.getDataBarang();
        Toast.makeText(this, "Berhasil Update Data Barang", Toast.LENGTH_SHORT).show();
        goToDataBarang();
    }

    private void hapusDataBarang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("update barang set active='"+"0"+
                "', tgl_update='"+now+"' " +
                "where id_barang='"+ modelBarang.getId_barang() +"'");
//        db.execSQL("delete from barang where id_barang = '"+modelBarang.getId_barang()+"'");
        DataBarang.dataMaster.getDataBarang();
        Home.dataMaster.getDataBarang();
        Toast.makeText(this, "Berhasil Hapus Data Barang", Toast.LENGTH_SHORT).show();
        goToDataBarang();
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

    private void goToDataBarang(){
        Intent a = new Intent(DataBarangEdit.this, DataBarang.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batal Edit Barang ?")
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