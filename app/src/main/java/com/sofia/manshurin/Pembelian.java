package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPembelian;
import com.sofia.manshurin.utility.NumberTextWatcher;
import com.sofia.manshurin.utility.PreferenceUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Pembelian extends AppCompatActivity {

    EditText txt_jml_barang, txt_harga_beli, txt_nama_barang;
    Spinner sp_barang;
    TextView txt_total_biaya, txtload, txt_total_barang, txt_alert;;
    ImageButton btn_masukkan_keranjang, btn_cek_keranjang;
    LinearLayout ll_barang_baru;
    DataHelper dbCenter;
    List<ModelBarang> listModelBarang;
    List<String> namaBarang = new ArrayList<>();
    List<Integer> idtrans = new ArrayList<Integer>();
    List<ModelKranjang> listModelKeranjang = new ArrayList<>();
    List<ModelKranjang> listModelKeranjang2 = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String nama, harga;
    int id;
    ModelBarang modelBarang;
    List<ModelPembelian> listModelPembelian;
    Random rand;
    int upperbound, id_pembelian, id_riwayat, hrg, jml, total, jml_total_barang;
    DecimalFormat formatter;
    int barangbaru = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pembelian);

        txt_jml_barang = findViewById(R.id.txt_jml_barang);
        txt_harga_beli = findViewById(R.id.txt_harga_beli);
        sp_barang = findViewById(R.id.sp_barang);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        btn_masukkan_keranjang = findViewById(R.id.btn_masukkan_keranjang);
        btn_cek_keranjang = findViewById(R.id.btn_cek_keranjang);
        ll_barang_baru = findViewById(R.id.ll_barang_baru);
        txt_nama_barang = findViewById(R.id.txt_nama_barang);
        txt_total_barang = findViewById(R.id.txt_total_barang);
        txt_alert = findViewById(R.id.txt_alert);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);
        rand = new Random(); //instance of random class
        upperbound = 1000000;

        start();

        txt_harga_beli.addTextChangedListener(new NumberTextWatcher(txt_harga_beli));

        sp_barang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                barangbaru=0;
                if (namaBarang.size()>0){
                    nama = sp_barang.getSelectedItem().toString();
                    if (nama.equalsIgnoreCase("BARANG BELUM TERDAFTAR")){
                        barangbaru = 1;
                        ll_barang_baru.setVisibility(View.VISIBLE);
                        txt_total_barang.setText("Total Barang : -");
                    } else {
                        ll_barang_baru.setVisibility(View.GONE);
                        barangbaru = 0;
                        for (int a=0; a<listModelBarang.size(); a++){
                            try {
                                if (listModelBarang.get(a).getNama_barang().equalsIgnoreCase(nama)){
                                    id = listModelBarang.get(a).getId_barang();
                                    harga = listModelBarang.get(a).getHarga_barang();
                                    modelBarang = listModelBarang.get(a);
                                    jml_total_barang = listModelBarang.get(a).getJml_barang();
                                    txt_total_barang.setText("Total Barang : "+String.valueOf(jml_total_barang));
                                }
                            } catch (Exception e){}
                        }
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        txt_jml_barang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txt_jml_barang.getText().toString().equalsIgnoreCase("")){
                    txt_total_biaya.setText("Total Biaya : -");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (barangbaru==1){
                        txt_jml_barang.setTextColor(Color.parseColor("#000000"));
                        txt_alert.setVisibility(View.GONE);
                        if (!txt_jml_barang.getText().toString().equalsIgnoreCase("")&
                                !txt_harga_beli.getText().toString().equalsIgnoreCase("")){
                            hrg = Integer.valueOf(txt_harga_beli.getText().toString().replaceAll("[^0-9]", ""));
                            jml = Integer.valueOf(txt_jml_barang.getText().toString());
                        }
                    } else if(barangbaru==0){
                        if (!txt_jml_barang.getText().toString().equalsIgnoreCase("")&
                                !txt_harga_beli.getText().toString().equalsIgnoreCase("")){
                            hrg = Integer.valueOf(txt_harga_beli.getText().toString().replaceAll("[^0-9]", ""));
                            jml = Integer.valueOf(txt_jml_barang.getText().toString());
                            if (jml>jml_total_barang){
                                txt_jml_barang.setTextColor(Color.parseColor("#FF0000"));
                                txt_alert.setVisibility(View.VISIBLE);
                            } else {
                                txt_jml_barang.setTextColor(Color.parseColor("#000000"));
                                txt_alert.setVisibility(View.GONE);
                            }
                        }
                    }
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable editable) {
                total = hrg*jml;
                //txt_total_biaya.setText(String.valueOf(total));
                String a = checkDesimal(String.valueOf(total));
                txt_total_biaya.setText("Total Biaya : " + a);
            }
        });

        btn_masukkan_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt_jml_barang.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(Pembelian.this, "Jumlah Barang Tidak Boleh 0", Toast.LENGTH_SHORT).show();
                } else if (!nama.equalsIgnoreCase("") && !txt_jml_barang.getText().toString().equalsIgnoreCase("") &&
                        !txt_harga_beli.getText().toString().equalsIgnoreCase("")){
                    checkBarangBaru();
                }  else {
                    Toast.makeText(Pembelian.this, "Lengkapi Field", Toast.LENGTH_SHORT).show();
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

    public void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        if (listModelBarang.size()>0){
            for (int i=0; i<listModelBarang.size(); i++){
                if (listModelBarang.get(i).getActive()==1){
                    namaBarang.add(listModelBarang.get(i).getNama_barang());
                }
            }
            namaBarang.add("BARANG BELUM TERDAFTAR");
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

        if (PreferenceUtils.getIDRiwayat(getApplicationContext()).equalsIgnoreCase("")){
            id_riwayat = rand.nextInt(upperbound);
            PreferenceUtils.saveIDRiwayat(String.valueOf(id_riwayat), getApplicationContext());
        } else {
            // do nothing
        }
    }

    private void checkBarangBaru(){
        if (barangbaru==0){
            inputTransaksi();
        } else if(barangbaru==1){
            checkNama();
        }
    }

    private void checkNama(){
        int checkNama = 0;
        if(listModelBarang.size()>0){
            for(int i=0; i<listModelBarang.size(); i++){
                if(String.valueOf(listModelBarang.get(i).getNama_barang()).equalsIgnoreCase(txt_nama_barang.getText().toString())) {
                    Toast.makeText(Pembelian.this, "Nama Barang Sudah Terpakai", Toast.LENGTH_SHORT).show();
                    checkNama = 1;
                    break;
                }
            }
        } else {
            saveBarangBaru();
        }

        if (checkNama!=1){
            checkNama=0;
            saveBarangBaru();
        } else {
            checkNama=0;
        }
    }

    private void saveBarangBaru(){
        Random r = new Random(); //instance of random class
        upperbound = 1000000;
        id = r.nextInt(upperbound);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        String now = formatter.format(new Date());

        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into barang(id_barang, nama_barang, harga_barang, jml_barang, desk_barang, tgl_input, tgl_update, active) values('" +
                id + "','" +
                txt_nama_barang.getText().toString() + "','" +
                txt_harga_beli.getText().toString().replaceAll("[^0-9]", "") + "','" +
                txt_jml_barang.getText() + "','" +
                " " + "','" +
                now + "','" +
                now + "','" +
                1 + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Tambah Barang", Toast.LENGTH_SHORT).show();
        inputTransaksi();
    }

    private void inputTransaksi(){
        id_pembelian = rand.nextInt(upperbound);
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into pembelian(id_pembelian, id_riwayat, id_barang, jumlah, harga) values('" +
                id_pembelian + "','" +
                Integer.valueOf(PreferenceUtils.getIDRiwayat(getApplicationContext())) + "','" +
                id + "','" +
                Integer.valueOf(txt_jml_barang.getText().toString()) + "','" +
                txt_harga_beli.getText().toString().replaceAll("[^0-9]", "") + "')");
        masukkanKeranjang();
    }

    private void masukkanKeranjang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into kranjang(idtransaksi, idkranjang) values('" +
                id_pembelian + "','" +
                12345 + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Masukkan Keranjang", Toast.LENGTH_SHORT).show();
        txt_nama_barang.setText("");
        ll_barang_baru.setVisibility(View.GONE);
        txt_harga_beli.setText("");
        txt_jml_barang.setText("");
    }

    private void hapusPenjualandanKeranjang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        idtrans.clear();
        for(int i=0; i<listModelKeranjang.size(); i++){
            idtrans.add(listModelKeranjang.get(i).getIdtransaksi());
            // kranjang(idtransaksi, idkranjang)
            db.execSQL("delete from kranjang where idtransaksi = '"+listModelKeranjang.get(i).getIdtransaksi()+"'");
        }
        listModelKeranjang2 = dbCenter.getAllKranjang();
        listModelPembelian = dbCenter.getAllPembelian();
        if(listModelKeranjang2.size()==0){
            for(int j=0; j<idtrans.size(); j++){
                for(int i=0; i<listModelPembelian.size(); i++){
                    if(idtrans.get(j) == listModelPembelian.get(i).getId_pembelian()){
                        db.execSQL("delete from pembelian where id_pembelian = '"+listModelPembelian.get(i).getId_pembelian()+"'");
                    }
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(Pembelian.this, "Transaksi Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                    idtrans.clear();
                    goToHome();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(Pembelian.this, "ANEHNYA KERANJANG MASIH ADA ISINYA", Toast.LENGTH_SHORT).show();
                    goToHome();
                }
            });
        }
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

  /*  private void delAllKeranjang(){
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
    }*/


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
        builder.setMessage("Batalkan Semua Transaksi ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        listModelKeranjang = dbCenter.getAllKranjang();
                        if (listModelKeranjang.size()>0){
                            hapusPenjualandanKeranjang();
                        } else {
                            goToHome();
                        }

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