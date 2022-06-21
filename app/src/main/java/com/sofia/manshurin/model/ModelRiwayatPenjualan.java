package com.sofia.manshurin.model;

public class ModelRiwayatPenjualan {

    int id_riwayat;
    String harga, harga_katul, jenis_pembayaran, tgl_input, tgl_update;

    public ModelRiwayatPenjualan(int id_riwayat, String harga, String harga_katul, String jenis_pembayaran, String tgl_input, String tgl_update) {
        this.id_riwayat = id_riwayat;
        this.harga = harga;
        this.harga_katul = harga_katul;
        this.jenis_pembayaran = jenis_pembayaran;
        this.tgl_input = tgl_input;
        this.tgl_update = tgl_update;
    }

    public int getId_riwayat() {
        return id_riwayat;
    }

    public void setId_riwayat(int id_riwayat) {
        this.id_riwayat = id_riwayat;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getHarga_katul() {
        return harga_katul;
    }

    public void setHarga_katul(String harga_katul) {
        this.harga_katul = harga_katul;
    }

    public String getJenis_pembayaran() {
        return jenis_pembayaran;
    }

    public void setJenis_pembayaran(String jenis_pembayaran) {
        this.jenis_pembayaran = jenis_pembayaran;
    }

    public String getTgl_input() {
        return tgl_input;
    }

    public void setTgl_input(String tgl_input) {
        this.tgl_input = tgl_input;
    }

    public String getTgl_update() {
        return tgl_update;
    }

    public void setTgl_update(String tgl_update) {
        this.tgl_update = tgl_update;
    }
}
