package com.sofia.manshurin.model;

public class ModelKranjang {

    int idtransaksi, idkranjang;

    public ModelKranjang(int idtransaksi, int idkranjang) {
        this.idtransaksi = idtransaksi;
        this.idkranjang = idkranjang;
    }

    public int getIdtransaksi() {
        return idtransaksi;
    }

    public void setIdtransaksi(int idtransaksi) {
        this.idtransaksi = idtransaksi;
    }

    public int getIdkranjang() {
        return idkranjang;
    }

    public void setIdkranjang(int idkranjang) {
        this.idkranjang = idkranjang;
    }
}
