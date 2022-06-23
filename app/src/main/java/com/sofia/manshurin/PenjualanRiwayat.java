package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterRiwayatPenjualan;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPenjualan;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PenjualanRiwayat extends AppCompatActivity {

    RecyclerView rvRiwayatPenjualan;
    TextView txtload;
    DataHelper dbCenter;
    List<ModelRiwayatPenjualan> listModelRiwayat = new ArrayList<ModelRiwayatPenjualan>();
    List<ModelPenjualan> listModelPenjualan = new ArrayList<ModelPenjualan>();
    List<ModelBarang> listModelBarang = new ArrayList<>();
    List<ModelBarang> listModelBarang2 = new ArrayList<>();
    AdapterRiwayatPenjualan itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.riwayat_penjualan);

        rvRiwayatPenjualan = findViewById(R.id.rvRiwayatPenjualan);
        txtload = findViewById(R.id.textloading);

        dbCenter = new DataHelper(this);

        getDataRiwayatPenjualan();

    }


    private void getDataRiwayatPenjualan(){
        listModelRiwayat = dbCenter.getAllRiwayatPenjualan();
        if(listModelRiwayat.size()>0){
            getDataPenjualan();
        } else {
            Toast.makeText(PenjualanRiwayat.this, "Tidak Memiliki Riwayat Penjualan", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataPenjualan(){
        listModelPenjualan = dbCenter.getAllPenjualan();
        if(listModelPenjualan.size()>0){
            getDataBarang();
        } else {
            Toast.makeText(PenjualanRiwayat.this, "Tidak Memiliki Riwayat Penjualan", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDataBarang(){
        Log.d("DataBarang", "get all barang");
        listModelBarang = dbCenter.getAllBarang();
        if (listModelBarang.size()>0){
            for(int i=0; i<listModelPenjualan.size(); i++){
                for (int j=0; j<listModelBarang.size(); j++){
                    if (listModelPenjualan.get(i).getId_barang() == listModelBarang.get(j).getId_barang()) {
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
        itemList = new AdapterRiwayatPenjualan(listModelRiwayat, listModelPenjualan, listModelBarang2, getApplicationContext());
        rvRiwayatPenjualan.setLayoutManager(new LinearLayoutManager(PenjualanRiwayat.this));
        rvRiwayatPenjualan.setAdapter(itemList);
        rvRiwayatPenjualan.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvRiwayatPenjualan,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent a = new Intent(PenjualanRiwayat.this, PenjualanRiwayatDetail.class);
                        a.putExtra("id", listModelRiwayat.get(position).getId_riwayat());
                        startActivity(a);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    private void goToHome(){
        Intent a = new Intent(PenjualanRiwayat.this, Home.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }

}