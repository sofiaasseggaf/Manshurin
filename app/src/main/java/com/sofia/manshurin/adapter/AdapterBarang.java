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

import java.util.List;

public class AdapterBarang extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelBarang> dataItemList;

    public AdapterBarang(List<ModelBarang> dataItemList) {
        this.dataItemList = dataItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.z_list_data_barang, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung)holder).nama_barang.setText(dataItemList.get(position).getNama_barang());
        ((Penampung)holder).harga_barang.setText(dataItemList.get(position).getHarga_barang());
        ((Penampung)holder).stok_barang.setText(dataItemList.get(position).getDesk_barang());
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nama_barang, harga_barang, stok_barang;
        public Penampung(View itemView) {
            super(itemView);
            nama_barang = itemView.findViewById(R.id.nama_barang);
            harga_barang = itemView.findViewById(R.id.harga_barang);
            stok_barang = itemView.findViewById(R.id.stok_barang);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + nama_barang.getText());
        }
    }

}
