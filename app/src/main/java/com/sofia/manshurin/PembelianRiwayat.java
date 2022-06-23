package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.sofia.manshurin.adapter.AdapterKeranjang;
import com.sofia.manshurin.adapter.AdapterKeranjangPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPenjualan;

import java.util.ArrayList;
import java.util.List;

public class PembelianRiwayat extends AppCompatActivity {

    RecyclerView rvRiwayatPembelian, rvRiwayatPembelian2;
    TextView txtload;
    DataHelper dbCenter;
    List<ModelPenjualan> listModelPenjualan = new ArrayList<ModelPenjualan>();
    List<ModelKranjang> listModelKeranjang = new ArrayList<>();
    AdapterKeranjangPenjualan itemList;
    AdapterKeranjang itemList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_pembelian);

        rvRiwayatPembelian = findViewById(R.id.rvRiwayatPembelian);
        rvRiwayatPembelian2 = findViewById(R.id.rvRiwayatPembelian2);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);

        getDataRiwayatPembelian();

    }



    /*private void getDataPenjualan(){
        listModelPenjualan = dbCenter.getAllPenjualan();
        if(listModelPenjualan.size()>0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(RiwayatPembelian.this, "ga ada penjualan", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setData(){
        List<ModelBarang> listModelBarang = dbCenter.getAllBarang();
        itemList = new AdapterKeranjangPenjualan(listModelPenjualan, listModelBarang);
        rvRiwayatPembelian.setLayoutManager(new LinearLayoutManager(RiwayatPembelian.this));
        rvRiwayatPembelian.setAdapter(itemList);
        rvRiwayatPembelian.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvRiwayatPembelian,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int posisi) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RiwayatPembelian.this);
                        builder.setMessage("Hapus Item ?")
                                .setCancelable(true)
                                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        int idp = listModelPenjualan.get(posisi).getId_penjualan();
                                        SQLiteDatabase db = dbCenter.getWritableDatabase();
                                        db.execSQL("delete from penjualan where id_penjualan = '"+idp+"'");
                                        goToHome();
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

    private void getDataKeranjang(){
        listModelKeranjang = dbCenter.getAllKranjang();
        if(listModelKeranjang.size()>0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData2();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    Toast.makeText(RiwayatPembelian.this, "ga ada keranjang", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setData2(){
        itemList2 = new AdapterKeranjang(listModelKeranjang);
        rvRiwayatPembelian2.setLayoutManager(new LinearLayoutManager(RiwayatPembelian.this));
        rvRiwayatPembelian2.setAdapter(itemList2);
    }*/

    private void getDataRiwayatPembelian(){
        //get data riwayat pembelian dulu, kalo done baru gini
        setData();
    }

    private void setData(){
        // set data di recycle view
    }

    private void goToHome(){
        Intent a = new Intent(PembelianRiwayat.this, Home.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }
}