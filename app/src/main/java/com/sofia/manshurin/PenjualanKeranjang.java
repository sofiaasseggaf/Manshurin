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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterBarang;
import com.sofia.manshurin.adapter.AdapterKeranjangPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.utility.PreferenceUtils;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PenjualanKeranjang extends AppCompatActivity {

    ImageButton btn_bayar;
    RecyclerView rvKeranjangPenjualan;
    TextView txt_total_biaya, txtload;
    EditText txt_jumlah_katul, txt_harga_katul;
    public static PenjualanKeranjang dataMaster;
    List<ModelPenjualan> listModelPenjualan;
    List<ModelPenjualan> listModelPenjualan2 = new ArrayList<ModelPenjualan>();
    List<ModelKranjang> listModelKeranjang = new ArrayList<>();
    List<ModelBarang> listModelBarang;
    List<ModelBarang> listModelBarang2 = new ArrayList<ModelBarang>();
    DataHelper dbCenter;
    AdapterKeranjangPenjualan itemList;
    String totalKatul, hutang, now;
    DecimalFormat formatter;


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
                //sendDataRiwayatPenjualan();
            }
        });

    }

    public void getDataKeranjang(){
        listModelKeranjang = dbCenter.getAllKranjang();
        if (listModelKeranjang.size()>0){
            getDataPenjualan();
        }
    }

    public void getDataPenjualan(){
        listModelPenjualan = dbCenter.getAllPenjualan();
        if(listModelPenjualan.size()>0) {
            for (int i = 0; i < listModelKeranjang.size(); i++) {
                for (int j = 0; j < listModelPenjualan.size(); j++) {
                    if (listModelKeranjang.get(i).getIdtransaksi() == listModelPenjualan.get(j).getId_penjualan()) {
                        listModelPenjualan2.add(listModelPenjualan.get(j));
                    }
                }
            }
        }

        if (listModelPenjualan2.size() > 0) {
            getDataBarang();
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
            setData();
        }

    }

    private void setData(){
        itemList = new AdapterKeranjangPenjualan(listModelPenjualan2, listModelBarang2);
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
                                        int idp = listModelPenjualan2.get(posisi).getId_penjualan();
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

        hitungTotal();

    }

    private void hitungTotal(){
        List<Integer> list = new ArrayList<Integer>();
        list.clear();
        for (int i=0; i<listModelPenjualan2.size(); i++){
            int harga = Integer.valueOf(listModelPenjualan2.get(i).getHarga());
            int jml = listModelPenjualan2.get(i).getJumlah();
            int total = harga*jml;
            list.add(total);
        }

        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sum = list.stream().mapToInt(Integer::intValue).sum();
        }
        txt_total_biaya.setText("TOTAL BIAYA : "+String.valueOf(sum));*/


        int sum = 0;
        for (int x : list) {
            sum += x;
        }

        //txt_total_biaya.setText("TOTAL BIAYA : "+String.valueOf(sum));

        String a = checkDesimal(String.valueOf(sum));
        txt_total_biaya.setText("Total Biaya : " + a);

    }

    private void hapusPenjualandanKeranjang(int id){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("delete from penjualan where id_penjualan = '"+id+"'");
        db.execSQL("delete from kranjang where idtransaksi = '"+id+"'");
        Toast.makeText(dataMaster, "Berhasil Hapus Data Transaksi", Toast.LENGTH_SHORT).show();
        goToPenjualan();
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
        // HAPUS SEMUA ISI KERANJANG
        PreferenceUtils.saveIDRiwayat("", getApplicationContext());
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

    private void goToPenjualan(){
        Intent a = new Intent(PenjualanKeranjang.this, Penjualan.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToPenjualan();
    }

}