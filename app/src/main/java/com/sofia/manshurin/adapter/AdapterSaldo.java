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
import com.sofia.manshurin.model.ModelSaldo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterSaldo extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelSaldo> dataItemList;
    DecimalFormat formatter;

    public AdapterSaldo(List<ModelSaldo> dataItemList) {
        this.dataItemList = dataItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.z_list_data_saldo, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung)holder).nama_saldo.setText(dataItemList.get(position).getNama_saldo());
        ((Penampung)holder).deskripsi_saldo.setText("Deskripsi Saldo : "+dataItemList.get(position).getDesk_saldo());

        int nom = Integer.valueOf(dataItemList.get(position).getNominal_saldo());
        String a = checkDesimal(String.valueOf(nom));
        ((Penampung)holder).nominal_saldo.setText("Nominal Saldo : "+a);

    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    static class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nama_saldo, nominal_saldo, deskripsi_saldo;
        public Penampung(View itemView) {
            super(itemView);
            nama_saldo = itemView.findViewById(R.id.nama_saldo);
            nominal_saldo = itemView.findViewById(R.id.nominal_saldo);
            deskripsi_saldo = itemView.findViewById(R.id.deskripsi_saldo);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + nama_saldo.getText());
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
