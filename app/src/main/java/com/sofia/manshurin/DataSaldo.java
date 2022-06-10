package com.sofia.manshurin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.manshurin.adapter.AdapterBarang;
import com.sofia.manshurin.adapter.AdapterSaldo;
import com.sofia.manshurin.helper.DataHelper;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelSaldo;
import com.sofia.manshurin.utility.RecyclerItemClickListener;

import java.util.List;

public class DataSaldo extends AppCompatActivity {

    ImageButton btn_tambah_saldo;
    RecyclerView rvDataSaldo;
    TextView txtload;
    DataHelper dbCenter;
    public static DataSaldo dataMaster;
    List<ModelSaldo> listModelSaldo;
    AdapterSaldo itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_saldo);

        btn_tambah_saldo = findViewById(R.id.btn_tambah_saldo);
        rvDataSaldo = findViewById(R.id.rvDataSaldo);
        txtload = findViewById(R.id.textloading);

        dataMaster = this;
        dbCenter = new DataHelper(this);
        start();

        btn_tambah_saldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTambahSaldo();
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
                getDataSaldo();
            }
        }).start();
    }

    public void getDataSaldo(){
        Log.d("DataSaldo", "get all saldo");
        listModelSaldo = dbCenter.getAllSaldo();
        if (listModelSaldo!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    findViewById(R.id.framelayout).setVisibility(View.GONE);
                    setData();
                }
            });
        } else {
            findViewById(R.id.framelayout).setVisibility(View.GONE);
            Toast.makeText(dataMaster, "Anda Belum Memiliki Saldo", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        itemList = new AdapterSaldo(listModelSaldo);
        rvDataSaldo.setLayoutManager(new LinearLayoutManager(DataSaldo.this));
        rvDataSaldo.setAdapter(itemList);
        rvDataSaldo.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvDataSaldo,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent a = new Intent(DataSaldo.this, DataSaldoEdit.class);
                        a.putExtra("idsaldo", listModelSaldo.get(position).getId_saldo());
                        startActivity(a);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
    }

    private void goToHome(){
        Intent a = new Intent(DataSaldo.this, Home.class);
        startActivity(a);
        finish();
    }

    private void goToTambahSaldo(){
        Intent a = new Intent(DataSaldo.this, DataSaldoInput.class);
        startActivity(a);
        finish();
    }

    public void onBackPressed() {
        goToHome();
    }
}