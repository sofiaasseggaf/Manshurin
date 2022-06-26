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

import com.sofia.manshurin.adapter.AdapterRiwayatPenjualan;
import com.sofia.manshurin.adapter.AdapterRiwayatPenjualanList;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelPenjualan;
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

public class PenjualanRiwayatDetail extends AppCompatActivity {

    TextView tgl_penjualan, txt_total_katul, txt_hutang,  txt_total_biaya;
    RecyclerView rv_list_riwayat;
    Button btn_cetak, btn_hapus;
    int id=0;
    ModelRiwayatPenjualan modelRiwayatPenjualan;
    DataHelper dbCenter;
    String now;
    DecimalFormat formatter;
    List<ModelPenjualan> listModelPenjualan = new ArrayList<ModelPenjualan>();
    List<ModelPenjualan> listModelPenjualan2 = new ArrayList<ModelPenjualan>();
    List<ModelBarang> listModelBarang = new ArrayList<>();
    List<ModelBarang> listModelBarang2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_penjualan_detail);

        tgl_penjualan = findViewById(R.id.tgl_penjualan);
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
        Log.d("DataRiwayatPenjualan", "get rpenjualan");
        modelRiwayatPenjualan = dbCenter.getRiwayatPenjualan(i);
        if (modelRiwayatPenjualan!=null){
            getDataPenjualan();
        } else {
            Toast.makeText(this, "Data Riwayat Tidak Ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataPenjualan(){
        listModelPenjualan = dbCenter.getAllPenjualan();
        if(listModelPenjualan.size()>0){
            for (int i=0; i<listModelPenjualan.size(); i++){
                if (listModelPenjualan.get(i).getId_riwayat() == id){
                    listModelPenjualan2.add(listModelPenjualan.get(i));
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
            for(int i=0; i<listModelPenjualan2.size(); i++){
                for (int j=0; j<listModelBarang.size(); j++){
                    if (listModelPenjualan2.get(i).getId_barang() == listModelBarang.get(j).getId_barang()) {
                        listModelBarang2.add(listModelBarang.get(j));
                    }
                }
            }
        }

        if (listModelBarang2.size()>0){
            setDataRPenjualan();
        }

    }

    private void setDataRPenjualan(){
        tgl_penjualan.setText(modelRiwayatPenjualan.getTgl_input());

        if (modelRiwayatPenjualan.getHarga_katul().equalsIgnoreCase("-")){
            txt_total_katul.setText(modelRiwayatPenjualan.getHarga_katul());
        } else {
            int a = Integer.valueOf(modelRiwayatPenjualan.getHarga_katul());
            String b = checkDesimal(String.valueOf(a));
            txt_total_katul.setText("Total Biaya Katul : " + b);
        }

        if (modelRiwayatPenjualan.getJenis_pembayaran().equalsIgnoreCase("-")){
            txt_hutang.setText(modelRiwayatPenjualan.getJenis_pembayaran());
        } else {
            int a = Integer.valueOf(modelRiwayatPenjualan.getJenis_pembayaran());
            String b = checkDesimal(String.valueOf(a));
            txt_hutang.setText("Hutang : " + b);
        }

        int a = Integer.valueOf(modelRiwayatPenjualan.getHarga());
        String b = checkDesimal(String.valueOf(a));
        txt_total_biaya.setText("Total Biaya : " + b);

        setDataRV();

    }

    private void setDataRV(){
        AdapterRiwayatPenjualanList itemList = new AdapterRiwayatPenjualanList(listModelPenjualan2, listModelBarang2);
        rv_list_riwayat.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_list_riwayat.setAdapter(itemList);
        rv_list_riwayat.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_list_riwayat,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // alert dialog with edittext
                        AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanRiwayatDetail.this);
                        View v = getLayoutInflater().inflate(R.layout.z_list_riwayat_data_update, null);
                        TextView nama_barang = v.findViewById(R.id.nama_barang);
                        EditText jumlah_barang = v.findViewById(R.id.jumlah_barang);
                        TextView harga_barang = v.findViewById(R.id.harga_barang);
                        builder.setView(v);
                        nama_barang.setText(listModelBarang2.get(position).getNama_barang());
                        jumlah_barang.setText(String.valueOf(listModelPenjualan2.get(position).getJumlah()));
                        harga_barang.setText(listModelPenjualan2.get(position).getHarga());
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
        db.execSQL("delete from riwayatpenjualan where id_riwayat = '"+id+"'");
        for(int i=0; i<listModelPenjualan2.size(); i++){
            db.execSQL("delete from penjualan where id_penjualan = '"+listModelPenjualan2.get(i).getId_penjualan()+"'");
        }
        Toast.makeText(this, "Data Riwayat Berhasil Di Hapus", Toast.LENGTH_SHORT).show();
        goToRiwayatPenjualan();
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

    private void goToHome(){
        Intent a = new Intent(PenjualanRiwayatDetail.this, Home.class);
        startActivity(a);
        finish();
    }

    private void goToRiwayatPenjualan(){
        Intent a = new Intent(PenjualanRiwayatDetail.this, PenjualanRiwayat.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToRiwayatPenjualan();
    }

}