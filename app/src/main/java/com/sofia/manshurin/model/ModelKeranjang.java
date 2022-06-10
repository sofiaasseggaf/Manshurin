package com.sofia.manshurin.model;

public class ModelKeranjang {

    int id_keranjang, id_traksaksi;

    public ModelKeranjang(int id_keranjang, int id_traksaksi) {
        this.id_keranjang = id_keranjang;
        this.id_traksaksi = id_traksaksi;
    }

    public int getId_keranjang() {
        return id_keranjang;
    }

    public void setId_keranjang(int id_keranjang) {
        this.id_keranjang = id_keranjang;
    }

    public int getId_traksaksi() {
        return id_traksaksi;
    }

    public void setId_traksaksi(int id_traksaksi) {
        this.id_traksaksi = id_traksaksi;
    }
}
