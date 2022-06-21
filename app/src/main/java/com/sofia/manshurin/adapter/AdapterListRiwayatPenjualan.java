package com.sofia.manshurin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofia.manshurin.R;
import com.sofia.manshurin.model.ModelPenjualan;

import java.util.List;

public class AdapterListRiwayatPenjualan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelPenjualan> dataItemList;

    public AdapterListRiwayatPenjualan(List<ModelPenjualan> dataItemList) {
        this.dataItemList = dataItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.z_list_data_riwayat, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung)holder).nama_barang.setText(dataItemList.get(position).getId_barang());
        ((Penampung)holder).jumlah_barang.setText("Harga : " + dataItemList.get(position).getHarga());
        ((Penampung)holder).harga_barang.setText("Jumlah : " + dataItemList.get(position).getJumlah());
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nama_barang, jumlah_barang, harga_barang;
        public Penampung(View itemView) {
            super(itemView);
            nama_barang = itemView.findViewById(R.id.nama_barang);
            jumlah_barang = itemView.findViewById(R.id.jumlah_barang);
            harga_barang = itemView.findViewById(R.id.harga_barang);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + nama_barang.getText());
        }
    }

}
