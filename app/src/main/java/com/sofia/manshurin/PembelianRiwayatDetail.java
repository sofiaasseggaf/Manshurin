package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterRiwayatPembelianList;
import com.sofia.manshurin.adapter.AdapterRiwayatPenjualanList;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelPembelian;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPembelian;
import com.sofia.manshurin.model.ModelRiwayatPenjualan;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PembelianRiwayatDetail extends AppCompatActivity {

    TextView tgl_pembelian, txt_total_katul, txt_hutang,  txt_total_biaya;
    RecyclerView rv_list_riwayat;
    Button btn_cetak, btn_hapus;
    int id=0;
    ModelRiwayatPembelian modelRiwayatPembelian;
    DataHelper dbCenter;
    String now;
    DecimalFormat formatter;
    List<ModelPembelian> listModelPembelian = new ArrayList<ModelPembelian>();
    List<ModelPembelian> listModelPembelian2 = new ArrayList<ModelPembelian>();
    List<ModelBarang> listModelBarang = new ArrayList<>();
    List<ModelBarang> listModelBarang2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_pembelian_detail);

        tgl_pembelian = findViewById(R.id.tgl_pembelian);
        txt_total_katul = findViewById(R.id.txt_total_katul);
        txt_hutang = findViewById(R.id.txt_hutang);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        rv_list_riwayat = findViewById(R.id.rv_list_riwayat);
        btn_cetak = findViewById(R.id.btn_cetak);
        btn_hapus = findViewById(R.id.btn_hapus);

        dbCenter = new DataHelper(this);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        now = formatter.format(new Date());

        if(id==0){
            Toast.makeText(this, "Data Riwayat Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }else{
            getDataRiwayat(id);
        }

        btn_cetak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cetak();
            }
        });

        btn_hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusSemua();
            }
        });

    }


    private void getDataRiwayat(int i){
        Log.d("DataRiwayatPembelian", "get rpembelian");
        modelRiwayatPembelian = dbCenter.getRiwayatPembelian(i);
        if (modelRiwayatPembelian!=null){
            getDataPembelian();
        } else {
            Toast.makeText(this, "Data Riwayat Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataPembelian(){
        listModelPembelian = dbCenter.getAllPembelian();
        if(listModelPembelian.size()>0){
            for (int i=0; i<listModelPembelian.size(); i++){
                if (listModelPembelian.get(i).getId_riwayat() == id){
                    listModelPembelian2.add(listModelPembelian.get(i));
                }
            }
            getDataBarang();
        } else {
            Toast.makeText(this, "Data Riwayat Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        if (listModelBarang.size()>0){
            for(int i=0; i<listModelPembelian2.size(); i++){
                for (int j=0; j<listModelBarang.size(); j++){
                    if (listModelPembelian2.get(i).getId_barang() == listModelBarang.get(j).getId_barang()) {
                        listModelBarang2.add(listModelBarang.get(j));
                    }
                }
            }
        }

        if (listModelBarang2.size()>0){
            setDataRPembelian();
        }
    }

    private void setDataRPembelian(){
        tgl_pembelian.setText(modelRiwayatPembelian.getTgl_input());

        if (modelRiwayatPembelian.getHarga_katul().equalsIgnoreCase("-")){
            txt_total_katul.setText(modelRiwayatPembelian.getHarga_katul());
        } else {
            int a = Integer.valueOf(modelRiwayatPembelian.getHarga_katul());
            String b = checkDesimal(String.valueOf(a));
            txt_total_katul.setText("Total Biaya Katul : " + b);
        }

        if (modelRiwayatPembelian.getJenis_pembayaran().equalsIgnoreCase("-")){
            txt_hutang.setText(modelRiwayatPembelian.getJenis_pembayaran());
        } else {
            int a = Integer.valueOf(modelRiwayatPembelian.getJenis_pembayaran());
            String b = checkDesimal(String.valueOf(a));
            txt_hutang.setText("Hutang : " + b);
        }

        int a = Integer.valueOf(modelRiwayatPembelian.getHarga());
        String b = checkDesimal(String.valueOf(a));
        txt_total_biaya.setText("Total Biaya : " + b);

        setDataRV();

    }

    private void setDataRV(){
        AdapterRiwayatPembelianList itemList = new AdapterRiwayatPembelianList(listModelPembelian2, listModelBarang2);
        rv_list_riwayat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_list_riwayat.setAdapter(itemList);
        rv_list_riwayat.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_list_riwayat,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // alert dialog with edittext
                        AlertDialog.Builder builder = new AlertDialog.Builder(PembelianRiwayatDetail.this);
                        View v = getLayoutInflater().inflate(R.layout.z_list_riwayat_data_update, null);
                        TextView nama_barang = v.findViewById(R.id.nama_barang);
                        EditText jumlah_barang = v.findViewById(R.id.jumlah_barang);
                        TextView harga_barang = v.findViewById(R.id.harga_barang);
                        builder.setView(v);
                        nama_barang.setText(listModelBarang2.get(position).getNama_barang());
                        jumlah_barang.setText(String.valueOf(listModelPembelian2.get(position).getJumlah()));
                        harga_barang.setText(listModelPembelian2.get(position).getHarga());
                        builder.setMessage("Edit Transaksi ?")
                                .setCancelable(true)
                                .setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        // UPDATE DATA TRANSAKSI DAN TOTAL RIWAYAT TRANSAKSI
                                        Toast.makeText(getApplicationContext(), "update data transaksi ini " +
                                                listModelBarang2.get(position).getNama_barang(), Toast.LENGTH_SHORT).show();
                                    }
                                })

                                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    private void cetak(){

    }

    private void hapusSemua(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("delete from riwayatpembelian where id_riwayat = '"+id+"'");
        for(int i=0; i<listModelPembelian2.size(); i++){
            db.execSQL("delete from pembelian where id_pembelian = '"+listModelPembelian2.get(i).getId_pembelian()+"'");
        }
        Toast.makeText(this, "Data Riwayat Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
        goToRiwayatPembelian();
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

    private void goToRiwayatPembelian(){
        Intent a = new Intent(PembelianRiwayatDetail.this, PembelianRiwayat.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToRiwayatPembelian();
    }

}