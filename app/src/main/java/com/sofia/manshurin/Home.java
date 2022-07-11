package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sofia.manshurin.adapter.AdapterBarangHome;
import com.sofia.manshurin.adapter.AdapterSaldo;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelSaldo;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {

    ImageButton btn_setting, btn_saldo, btn_barang, btn_pembelian, btn_penjualan, btn_riwayat_pembelian, btn_riwayat_penjualan;
    TextView txt_total_saldo, txt_sisa_saldo, txtload;
    RecyclerView rv_home;
    DataHelper dbCenter;
    public static Home dataMaster;
    List<ModelBarang> listModelBarang;
    List<ModelBarang> listModelBarang2 = new ArrayList<>();
    List<ModelSaldo> listModelSaldo;
    AdapterBarangHome itemList;
    int total;
    DecimalFormat formatter;

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
        txtload = findViewById(R.id.textloading);

        dataMaster = this;
        dbCenter = new DataHelper(this);

        getDataBarang();
        //start();

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
                Intent a = new Intent(Home.this, PembelianRiwayat.class);
                startActivity(a);
                finish();
            }
        });

        btn_riwayat_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(Home.this, PenjualanRiwayat.class);
                startActivity(a);
                finish();
            }
        });
    }

    /*private void start(){
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
    }*/

    public void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        getDataSaldo();
    }

    public void getDataSaldo(){
        /*SQLiteDatabase db = dbCenter.getWritableDatabase();
        listModelSaldo = dbCenter.getAllSaldo();
        for (int i=0; i<listModelSaldo.size(); i++){
            if(listModelSaldo.get(i).getNominal_saldo().equalsIgnoreCase("Pinjaman")){
                db.execSQL("delete from saldo where id_saldo = '"+listModelSaldo.get(i).getId_saldo()+"'");
            }
        }*/
        Log.d("DataSaldo", "get all saldo");
        listModelSaldo = dbCenter.getAllSaldo();
        if (listModelSaldo!=null){
            total = 0;
            for (int i=0; i<listModelSaldo.size(); i++){
                int a = Integer.valueOf(listModelSaldo.get(i).getNominal_saldo());
                total = total+a;
            }
            setDataSaldo();
        }
    }

    private void setDataSaldo(){
        String a = checkDesimal(String.valueOf(total));
        txt_total_saldo.setText("Total Saldo = "+ a);
        if (listModelBarang!=null){
            for (int i=0; i<listModelBarang.size(); i++){
                if (listModelBarang.get(i).getActive()==1){
                    listModelBarang2.add(listModelBarang.get(i));
                }
            }
            if (listModelBarang2.size()>0){
                setDataBarang();
            }
        }
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.framelayout).setVisibility(View.GONE);
                String a = checkDesimal(String.valueOf(total));
                txt_total_saldo.setText("Total Saldo = "+ a);
                if (listModelBarang!=null){
                    setDataBarang();
                }

            }
        });*/
    }

    private void setDataBarang(){

        itemList = new AdapterBarangHome(listModelBarang2);
        rv_home.setLayoutManager(new LinearLayoutManager(Home.this));
        rv_home.setAdapter(itemList);
        rv_home.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_home,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent a = new Intent(Home.this, DataBarangEdit.class);
                        a.putExtra("idbarang", listModelBarang2.get(position).getId_barang());
                        startActivity(a);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tutup Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        finish();
                        finishAffinity();
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