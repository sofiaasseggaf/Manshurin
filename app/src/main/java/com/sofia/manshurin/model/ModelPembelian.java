package com.sofia.manshurin.model;

public class ModelPembelian {

    int id_pembelian, id_riwayat, id_barang, jumlah;
    String harga;

    public ModelPembelian(int id_pembelian, int id_riwayat, int id_barang, int jumlah, String harga) {
        this.id_pembelian = id_pembelian;
        this.id_riwayat = id_riwayat;
        this.id_barang = id_barang;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    public int getId_pembelian() {
        return id_pembelian;
    }

    public void setId_pembelian(int id_pembelian) {
        this.id_pembelian = id_pembelian;
    }

    public int getId_riwayat() {
        return id_riwayat;
    }

    public void setId_riwayat(int id_riwayat) {
        this.id_riwayat = id_riwayat;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

}
