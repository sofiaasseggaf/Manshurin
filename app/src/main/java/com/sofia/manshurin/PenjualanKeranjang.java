package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterKeranjangPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.utility.NumberTextWatcher;
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
    TextView txt_total_biaya, txt_total_katul, txtload;
    EditText txt_jumlah_katul, txt_harga_katul, txt_pinjaman;
    public static PenjualanKeranjang dataMaster;
    List<ModelPenjualan> listModelPenjualan;
    List<ModelPenjualan> listModelPenjualan2 = new ArrayList<ModelPenjualan>();
    List<ModelPenjualan> listModelPenjualan3 = new ArrayList<ModelPenjualan>();
    List<ModelKranjang> listModelKeranjang = new ArrayList<>();
    List<ModelKranjang> listModelKeranjang2 = new ArrayList<>();
    List<ModelBarang> listModelBarang;
    List<ModelBarang> listModelBarang2 = new ArrayList<ModelBarang>();
    List<Integer> idtrans = new ArrayList<Integer>();
    DataHelper dbCenter;
    AdapterKeranjangPenjualan itemList;
    String totalKatul, hutang, now, totalBiaya;
    DecimalFormat formatter;
    int hrg, jml, idr;
    int total=0;
    RadioButton rb_hutang_plus, rb_hutang_min;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.penjualan_keranjang);

        rvKeranjangPenjualan = findViewById(R.id.rvKeranjangPenjualan);
        btn_bayar = findViewById(R.id.btn_bayar);
        txt_total_biaya = findViewById(R.id.txt_total_biaya);
        txt_jumlah_katul = findViewById(R.id.txt_jumlah_katul);
        txt_harga_katul = findViewById(R.id.txt_harga_katul);
        txt_total_katul = findViewById(R.id.txt_total_katul);
        txtload = findViewById(R.id.textloading);
        rb_hutang_plus = findViewById(R.id.rb_hutang_plus);
        rb_hutang_min = findViewById(R.id.rb_hutang_min);
        txt_pinjaman = findViewById(R.id.txt_pinjaman);

        dataMaster = this;
        dbCenter = new DataHelper(this);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        now = formatter.format(new Date());

        getDataKeranjang();

        rb_hutang_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_hutang_plus.isChecked()) {
                    rb_hutang_min.setChecked(false);
                }
            }
        });

        rb_hutang_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_hutang_min.isChecked()) {
                    rb_hutang_plus.setChecked(false);
                }
            }
        });

        txt_harga_katul.addTextChangedListener(new NumberTextWatcher(txt_harga_katul));

        txt_pinjaman.addTextChangedListener(new NumberTextWatcher(txt_pinjaman));

        txt_jumlah_katul.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txt_jumlah_katul.getText().toString().equalsIgnoreCase("")){
                    txt_total_katul.setText("Total Katul : -");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (!txt_jumlah_katul.getText().toString().equalsIgnoreCase("")&
                            !txt_harga_katul.getText().toString().equalsIgnoreCase("")){
                        hrg = Integer.valueOf(txt_harga_katul.getText().toString().replaceAll("[^0-9]", ""));
                        jml = Integer.valueOf(txt_jumlah_katul.getText().toString());
                    }
                }catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable editable) {
                total = hrg*jml;
                //txt_total_biaya.setText(String.valueOf(total));
                String a = checkDesimal(String.valueOf(total));
                txt_total_katul.setText("Total Katul : " + a);
            }
        });

        btn_bayar.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanKeranjang.this);
                builder.setMessage("Lakukan Transaksi ?")
                        .setCancelable(true)
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                checkKatul();
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
        totalBiaya = String.valueOf(sum).replaceAll("[^0-9]", "");
    }

    private void hapusPenjualandanKeranjang(int id){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("delete from penjualan where id_penjualan = '"+id+"'");
        db.execSQL("delete from kranjang where idtransaksi = '"+id+"'");
        Toast.makeText(dataMaster, "Berhasil Hapus Data Transaksi", Toast.LENGTH_SHORT).show();
        goToPenjualan();
    }

    private void checkKatul(){
        if (total==0){
            totalKatul = "0";
            checkPinjaman();
        } else {
            totalKatul = String.valueOf(total).replaceAll("[^0-9]", "");
            checkPinjaman();
        }
    }

    private void checkPinjaman(){
        if(rb_hutang_plus.isChecked()){
           // rb_hutang_min.setChecked(false);
            if (!txt_pinjaman.getText().toString().equalsIgnoreCase("")){
                hutang = txt_pinjaman.getText().toString().replaceAll("[^0-9]", "");

                /*int a = Integer.valueOf(totalBiaya);
                int b = Integer.valueOf(hutang);
                int c = a-b;
                String d = checkDesimal(String.valueOf(c));
                txt_total_biaya.setText("Total Biaya : " + d);
                totalBiaya = String.valueOf(c);*/

                // update table saldo dulu

                sendDataRiwayatPenjualan();
            } else {
                Toast.makeText(PenjualanKeranjang.this, "Isi Biaya Pinjaman", Toast.LENGTH_SHORT).show();
            }
        } else if(rb_hutang_min.isChecked()){
           // rb_hutang_plus.setChecked(false);
            if (!txt_pinjaman.getText().toString().equalsIgnoreCase("")){
                hutang = txt_pinjaman.getText().toString().replaceAll("[^0-9]", "");

               /*int a = Integer.valueOf(totalBiaya);
                int b = Integer.valueOf(hutang);
                int c = a-b;
                String d = checkDesimal(String.valueOf(c));
                txt_total_biaya.setText("Total Biaya : " + d);
                totalBiaya = String.valueOf(c);*/

                // update table saldo dulu

                sendDataRiwayatPenjualan();
            } else {
                Toast.makeText(PenjualanKeranjang.this, "Isi Biaya Pinjaman", Toast.LENGTH_SHORT).show();
            }
        } else if(!rb_hutang_plus.isChecked() && !rb_hutang_min.isChecked()){
            hutang = "0";
            sendDataRiwayatPenjualan();
        }
    }

    private void sendDataRiwayatPenjualan(){
        idr = Integer.valueOf(PreferenceUtils.getIDRiwayat(getApplicationContext()));
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        // id_riwayat integer null, harga text null, harga_katul text null, jenis_pembayaran text null, tgl_input text null, tgl_update text null
        db.execSQL("insert into riwayatpenjualan(id_riwayat, harga, harga_katul, jenis_pembayaran, tgl_input, tgl_update) values('" +
                idr + "','" +
                totalBiaya + "','" +
                totalKatul + "','" +
                hutang + "','" +
                now + "','" +
                now +"')");

        hapusSemua();
    }

    private void hapusSemua(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        idtrans.clear();
        for(int i=0; i<listModelKeranjang.size(); i++){
            db.execSQL("delete from kranjang where idtransaksi = '"+listModelKeranjang.get(i).getIdtransaksi()+"'");
        }
        listModelKeranjang2 = dbCenter.getAllKranjang();
        if(listModelKeranjang2.size()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanKeranjang.this);
            builder.setMessage("Cek Riwayat Penjualan ?")
                    .setPositiveButton("Cek Riwayat Penjualan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            PreferenceUtils.saveIDRiwayat("", getApplicationContext());
                            goToRiwayatPenjualan(idr);
                        }
                    })
                    .setNegativeButton("Kembali Ke Beranda", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PreferenceUtils.saveIDRiwayat("", getApplicationContext());
                            goToHome();
                        }
                    })
                    .create()
                    .show();
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

    private void goToPenjualan(){
        Intent a = new Intent(PenjualanKeranjang.this, Penjualan.class);
        startActivity(a);
        finish();
    }

    private void goToHome(){
        Intent a = new Intent(PenjualanKeranjang.this, Home.class);
        startActivity(a);
        finish();
    }

    private void goToRiwayatPenjualan(int id){
        Intent a = new Intent(PenjualanKeranjang.this, PenjualanRiwayat.class);
        a.putExtra("id", idr);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToPenjualan();
    }

}