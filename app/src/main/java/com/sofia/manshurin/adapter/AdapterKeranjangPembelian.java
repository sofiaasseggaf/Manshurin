package com.sofia.manshurin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofia.manshurin.R;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelPembelian;
import com.sofia.manshurin.model.ModelPenjualan;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterKeranjangPembelian extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelPembelian> dataItemList;
    List<ModelBarang> dataBarang;
    DecimalFormat formatter;

    public AdapterKeranjangPembelian(List<ModelPembelian> dataItemList, List<ModelBarang> dataBarang) {
        this.dataItemList = dataItemList;
        this.dataBarang = dataBarang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.z_list_keranjang, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung)holder).txt_nama_barang.setText(dataBarang.get(position).getNama_barang());
        ((Penampung)holder).txt_jml_beli.setText(String.valueOf(dataItemList.get(position).getJumlah()));
        //int total = Integer.valueOf(dataItemList.get(position).getHarga()) * dataItemList.get(position).getJumlah();
        String a = checkDesimal(String.valueOf(dataItemList.get(position).getJumlah()));
        ((Penampung)holder).txt_total_harga_beli.setText(a);
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_nama_barang, txt_jml_beli, txt_total_harga_beli;
        public Penampung(View itemView) {
            super(itemView);
            txt_nama_barang = itemView.findViewById(R.id.txt_nama_barang);
            txt_jml_beli = itemView.findViewById(R.id.txt_jml_jual);
            txt_total_harga_beli = itemView.findViewById(R.id.txt_total_harga_jual);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + txt_nama_barang.getText());
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
