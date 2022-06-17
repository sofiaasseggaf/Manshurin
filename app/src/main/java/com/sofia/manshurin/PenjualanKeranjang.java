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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sofia.manshurin.adapter.AdapterBarang;
import com.sofia.manshurin.adapter.AdapterKeranjangPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKeranjang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.utility.PreferenceUtils;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PenjualanKeranjang extends AppCompatActivity {

    ImageButton btn_bayar;
    RecyclerView rvKeranjangPenjualan;
    TextView txt_total_biaya, txtload;
    EditText txt_jumlah_katul, txt_harga_katul;
    public static PenjualanKeranjang dataMaster;
    List<ModelKeranjang> listModelKeranjang;
    List<ModelKeranjang> listModelKeranjang2;
    List<ModelPenjualan> listModelPenjualan;
    DataHelper dbCenter;
    AdapterKeranjangPenjualan itemList;
    String totalKatul, hutang, now;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penjualan_keranjang);

        rvKeranjangPenjualan = findViewById(R.id.rvKeranjangPenjualan);
        btn_bayar = findViewById(R.id.btn_bayar);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        txt_jumlah_katul = findViewById(R.id.txt_jumlah_katul);
        txt_harga_katul = findViewById(R.id.txt_harga_katul);
        txtload = findViewById(R.id.textloading);

        dataMaster = this;
        dbCenter = new DataHelper(this);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        now = formatter.format(new Date());

        getDataKeranjang();

        btn_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataRiwayatPenjualan();
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
                getDataKeranjang();
            }
        }).start();
    }*/

    public void getDataKeranjang(){
        listModelKeranjang = dbCenter.getAllKeranjang();
        if (listModelKeranjang.size()>0){
            getDataPenjualan();
        }
    }

    public void getDataPenjualan(){
        listModelPenjualan = dbCenter.getAllPenjualan();
        if (listModelPenjualan.size()>0){
            setData();
        }
    }

    private void setData(){
        itemList = new AdapterKeranjangPenjualan(listModelPenjualan);
        rvKeranjangPenjualan.setLayoutManager(new LinearLayoutManager(PenjualanKeranjang.this));
        rvKeranjangPenjualan.setAdapter(itemList);
        rvKeranjangPenjualan.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvKeranjangPenjualan,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int posisi) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanKeranjang.this);
                        builder.setMessage("Hapus Item ?")
                                .setCancelable(true)
                                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        int idp = listModelPenjualan.get(posisi).getId_penjualan();
                                        hapusPenjualandanKeranjang(idp);
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

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    private void hapusPenjualandanKeranjang(int id){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("delete from penjualan where id_penjualan = '"+id+"'");
        for(int i=0; i<listModelKeranjang.size(); i++){
            if(listModelKeranjang.get(i).getId_traksaksi()==id){
                db.execSQL("delete from keranjang where id_keranjang = '"+listModelKeranjang.get(i).getId_keranjang()+"'");
            }
        }
        getDataKeranjang();
    }

    private void sendDataRiwayatPenjualan(){
        int idr = Integer.valueOf(PreferenceUtils.getIDRiwayat(getApplicationContext()));
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        // id_riwayat integer null, harga text null, harga_katul text null, jenis_pembayaran text null, tgl_input text null, tgl_update text null
        db.execSQL("insert into riwayatpenjualan(id_riwayat, harga, harga_katul, jenis_pembayaran, tgl_input, tgl_update) values('" +
                idr + "','" +
                txt_total_biaya.getText().toString() +
                totalKatul +
                hutang +
                now +
                now +"')");
        PreferenceUtils.saveIDRiwayat("", getApplicationContext());
        hapusDataKeranjang();
    }

    private void hapusDataKeranjang(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        for(int i=0; i<listModelKeranjang.size(); i++){
            db.execSQL("delete from keranjang where id_keranjang = '"+listModelKeranjang.get(i).getId_keranjang()+"'");
        }
        listModelKeranjang2 = dbCenter.getAllKeranjang();
        if (listModelKeranjang2.size()==0){

            AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanKeranjang.this);
            builder.setMessage("Pembayaran Berhasil")
                    .setPositiveButton("Ke Riwayat Penjualan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent a = new Intent(PenjualanKeranjang.this, RiwayatPenjualan.class);
                            startActivity(a);
                            finish();
                        }
                    })
                    .setNegativeButton("Kembali", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            goToPenjualan();
                        }
                    })
                    .create()
                    .show();

        }
    }

    private void goToPenjualan(){
        Intent a = new Intent(PenjualanKeranjang.this, Penjualan.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tambah Penjualan ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        goToPenjualan();
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