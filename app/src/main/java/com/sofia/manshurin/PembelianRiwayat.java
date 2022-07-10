package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterKeranjangPembelian;
import com.sofia.manshurin.adapter.AdapterKeranjangPenjualan;
import com.sofia.manshurin.adapter.AdapterRiwayatPembelian;
import com.sofia.manshurin.adapter.AdapterRiwayatPembelianList;
import com.sofia.manshurin.adapter.AdapterRiwayatPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPembelian;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPembelian;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PembelianRiwayat extends AppCompatActivity {

    RecyclerView rvRiwayatPembelian;
    TextView txtload;
    DataHelper dbCenter;
    List<ModelRiwayatPembelian> listModelRiwayat = new ArrayList<>();
    List<ModelPembelian> listModelPembelian = new ArrayList<ModelPembelian>();
    List<ModelBarang> listModelBarang = new ArrayList<>();
    List<ModelBarang> listModelBarang2 = new ArrayList<>();
    AdapterRiwayatPembelian itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_pembelian);

        rvRiwayatPembelian = findViewById(R.id.rvRiwayatPembelian);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);

        getDataRiwayatPembelian();

    }

    private void getDataRiwayatPembelian(){
        listModelRiwayat = dbCenter.getAllRiwayatPembelian();
        if (listModelRiwayat==null){
            Toast.makeText(PembelianRiwayat.this, "Riwayat Pembelian NULL", Toast.LENGTH_SHORT).show();
        }
        if(listModelRiwayat.size()>0){
            getDataPembelian();
        } else {
            Toast.makeText(PembelianRiwayat.this, "Tidak Memiliki Riwayat Pembelian", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataPembelian(){
        listModelPembelian = dbCenter.getAllPembelian();
        if (listModelPembelian==null){
            Toast.makeText(PembelianRiwayat.this, "Data Pembelian NULL", Toast.LENGTH_SHORT).show();
        }
        if(listModelPembelian.size()>0){
            getDataBarang();
        } else {
            Toast.makeText(PembelianRiwayat.this, "Tidak Memiliki Riwayat Pembelian", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        if (listModelBarang.size()>0){
            for(int i=0; i<listModelPembelian.size(); i++){
                for (int j=0; j<listModelBarang.size(); j++){
                    if (listModelPembelian.get(i).getId_barang() == listModelBarang.get(j).getId_barang()) {
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
        itemList = new AdapterRiwayatPembelian(listModelRiwayat, listModelPembelian, listModelBarang2, getApplicationContext());
        rvRiwayatPembelian.setLayoutManager(new LinearLayoutManager(PembelianRiwayat.this));
        rvRiwayatPembelian.setAdapter(itemList);
        rvRiwayatPembelian.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvRiwayatPembelian,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent a = new Intent(PembelianRiwayat.this, PembelianRiwayatDetail.class);
                        a.putExtra("id", listModelRiwayat.get(position).getId_riwayat());
                        startActivity(a);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
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