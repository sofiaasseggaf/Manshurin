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
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPenjualan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterRiwayatPenjualan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelRiwayatPenjualan> dataItemList;
    List<ModelPenjualan> dataPenjualan;
    List<ModelPenjualan> dataPenjualan2 = new ArrayList<ModelPenjualan>();
    List<ModelBarang> dataBarang = new ArrayList<ModelBarang>();
    Context context;
    DecimalFormat formatter;

    public AdapterRiwayatPenjualan(List<ModelRiwayatPenjualan> dataItemList, List<ModelPenjualan> dataPenjualan, List<ModelBarang> dataBarang, Context context) {
        this.dataItemList = dataItemList;
        this.dataPenjualan = dataPenjualan;
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
        ((Penampung)holder).tgl_pembelian.setText(dataItemList.get(position).getTgl_input());

        int total = Integer.valueOf(dataItemList.get(position).getHarga());
        String a = checkDesimal(String.valueOf(total));
        ((Penampung)holder).total_pembelian.setText("Total Harga : Rp. " + a);


        for (int i=0; i<dataPenjualan.size(); i++){
            if (dataPenjualan.get(i).getId_riwayat() == dataItemList.get(position).getId_riwayat()){
                dataPenjualan2.add(dataPenjualan.get(i));
            }
        }
        AdapterRiwayatPenjualanList itemList = new AdapterRiwayatPenjualanList(dataPenjualan2, dataBarang);
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
