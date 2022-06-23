package com.sofia.manshurin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofia.manshurin.R;
import com.sofia.manshurin.model.ModelKranjang;

import java.util.List;

public class AdapterKeranjang extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        List<ModelKranjang> dataItemList;

        public AdapterKeranjang(List<ModelKranjang> dataItemList) {
                this.dataItemList = dataItemList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.z_z_keranjang, parent, false);
                Penampung penampung = new Penampung(view);
                return penampung;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ((Penampung)holder).txt_id_transaksi.setText(String.valueOf(dataItemList.get(position).getIdtransaksi()));
                ((Penampung)holder).txt_id_keranjang.setText(String.valueOf(dataItemList.get(position).getIdkranjang()));
        }

        @Override
        public int getItemCount() {
                return dataItemList == null ? 0 : dataItemList.size();
        }


        static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
                public TextView txt_id_transaksi, txt_id_keranjang;
                public Penampung(View itemView) {
                        super(itemView);
                        txt_id_transaksi = itemView.findViewById(R.id.txt_id_transaksi);
                        txt_id_keranjang = itemView.findViewById(R.id.txt_id_keranjang);
                }
                @Override
                public void onClick(View v) {
                        Log.d("onclick", "onClick " + getLayoutPosition() + " " + txt_id_transaksi.getText());
                }
        }

}
