package com.sofia.manshurin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterBarang;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.util.List;

public class DataBarang extends AppCompatActivity {

    ImageButton btn_tambah_barang;
    RecyclerView rvDataBarang;
    TextView txtload;
    DataHelper dbCenter;
    public static DataBarang dataMaster;
    List<ModelBarang> listModelBarang;
    AdapterBarang itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_barang);

        btn_tambah_barang = findViewById(R.id.btn_tambah_barang);
        rvDataBarang = findViewById(R.id.rvDataBarang);
        txtload = findViewById(R.id.textloading);

        dataMaster = this;
        dbCenter = new DataHelper(this);
        start();

        btn_tambah_barang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTambahBarang();
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
        if (listModelBarang!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData();
                }
            });
        } else {
            findViewById(R.id.framelayout).setVisibility(View.GONE);
            Toast.makeText(dataMaster, "Anda Belum Memiliki Barang", Toast.LENGTH_SHORT).show();
        }

    }

    private void setData(){
        itemList = new AdapterBarang(listModelBarang);
        rvDataBarang.setLayoutManager(new LinearLayoutManager(DataBarang.this));
        rvDataBarang.setAdapter(itemList);
        rvDataBarang.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvDataBarang,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent a = new Intent(DataBarang.this, DataBarangEdit.class);
                        a.putExtra("idbarang", listModelBarang.get(position).getId_barang());
                        startActivity(a);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    private void goToHome(){
        Intent a = new Intent(DataBarang.this, Home.class);
        startActivity(a);
        finish();
    }

    private void goToTambahBarang(){
        Intent a = new Intent(DataBarang.this, DataBarangInput.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }

}