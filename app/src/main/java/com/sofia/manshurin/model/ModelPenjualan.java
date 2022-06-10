package com.sofia.manshurin.model;

public class ModelPenjualan {

    int id_penjualan, id_riwayat, id_barang, jumlah;
    String harga;

    public ModelPenjualan(int id_penjualan, int id_riwayat, int id_barang, int jumlah, String harga) {
        this.id_penjualan = id_penjualan;
        this.id_riwayat = id_riwayat;
        this.id_barang = id_barang;
        this.jumlah = jumlah;
        this.harga = harga;
    }

    public int getId_penjualan() {
        return id_penjualan;
    }

    public void setId_penjualan(int id_penjualan) {
        this.id_penjualan = id_penjualan;
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
