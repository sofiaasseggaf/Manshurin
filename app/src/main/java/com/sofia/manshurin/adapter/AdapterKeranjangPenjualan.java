package com.sofia.manshurin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofia.manshurin.R;
import com.sofia.manshurin.model.ModelKeranjang;
import com.sofia.manshurin.model.ModelPenjualan;

import java.util.List;

public class AdapterKeranjangPenjualan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelPenjualan> dataItemList;

    public AdapterKeranjangPenjualan(List<ModelPenjualan> dataItemList) {
        this.dataItemList = dataItemList;
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
        ((Penampung)holder).txt_nama_barang.setText(String.valueOf(dataItemList.get(position).getId_barang()));
        ((Penampung)holder).txt_jml_jual.setText(String.valueOf(dataItemList.get(position).getJumlah()));
        int total = Integer.valueOf(dataItemList.get(position).getHarga()) * dataItemList.get(position).getJumlah();
        ((Penampung)holder).txt_total_harga_jual.setText(String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txt_nama_barang, txt_jml_jual, txt_total_harga_jual;
        public Penampung(View itemView) {
            super(itemView);
            txt_nama_barang = itemView.findViewById(R.id.txt_nama_barang);
            txt_jml_jual = itemView.findViewById(R.id.txt_jml_jual);
            txt_total_harga_jual = itemView.findViewById(R.id.txt_total_harga_jual);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + txt_nama_barang.getText());
        }
    }
}
