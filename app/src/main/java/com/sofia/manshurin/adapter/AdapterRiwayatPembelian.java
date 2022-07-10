package com.sofia.manshurin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sofia.manshurin.R;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelPembelian;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPembelian;
import com.sofia.manshurin.model.ModelRiwayatPenjualan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterRiwayatPembelian extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelRiwayatPembelian> dataItemList;
    List<ModelPembelian> dataPembelian;
    List<ModelPembelian> dataPembelian2 = new ArrayList<ModelPembelian>();
    List<ModelBarang> dataBarang = new ArrayList<ModelBarang>();
    List<ModelBarang> dataBarang2 = new ArrayList<ModelBarang>();
    Context context;
    DecimalFormat formatter;

    public AdapterRiwayatPembelian(List<ModelRiwayatPembelian> dataItemList, List<ModelPembelian> dataPembelian, List<ModelBarang> dataBarang, Context context) {
        this.dataItemList = dataItemList;
        this.dataPembelian = dataPembelian;
        this.dataBarang  = dataBarang;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.z_list_riwayat, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        dataPembelian2.clear();
        dataBarang2.clear();


        ((Penampung)holder).tgl_pembelian.setText(dataItemList.get(position).getTgl_input());

        int total = Integer.valueOf(dataItemList.get(position).getHarga());
        String a = checkDesimal(String.valueOf(total));
        ((Penampung)holder).total_pembelian.setText("Total Harga : Rp. " + a);


        for (int i=0; i<dataPembelian.size(); i++){
            if (dataPembelian.get(i).getId_riwayat() == dataItemList.get(position).getId_riwayat()){
                dataPembelian2.add(dataPembelian.get(i));
            }
        }
        if (dataBarang.size()>0){
            for(int i=0; i<dataPembelian2.size(); i++){
                for (int j=0; j<dataBarang.size(); j++){
                    if (dataPembelian2.get(i).getId_barang() == dataBarang.get(j).getId_barang()) {
                        dataBarang2.add(dataBarang.get(j));
                    }
                }
            }
        }
        AdapterRiwayatPembelianList itemList = new AdapterRiwayatPembelianList(dataPembelian2, dataBarang2);
        ((Penampung)holder).rv_list_riwayat.setLayoutManager(new LinearLayoutManager(context));
        ((Penampung)holder).rv_list_riwayat.setAdapter(itemList);
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tgl_pembelian, total_pembelian;
        RecyclerView rv_list_riwayat;
        public Penampung(View itemView) {
            super(itemView);
            tgl_pembelian = itemView.findViewById(R.id.tgl_pembelian);
            rv_list_riwayat = itemView.findViewById(R.id.rv_list_riwayat);
            total_pembelian = itemView.findViewById(R.id.total_pembelian);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + tgl_pembelian.getText());
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
}
