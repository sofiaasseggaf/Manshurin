package com.sofia.manshurin.model;

public class ModelBarang {

    int id_barang, jml_barang;
    String nama_barang, harga_barang, desk_barang, txl_input, txl_update;

    public ModelBarang(int id_barang, String nama_barang, String harga_barang, int jml_barang, String desk_barang, String txl_input, String txl_update) {
        this.id_barang = id_barang;
        this.jml_barang = jml_barang;
        this.nama_barang = nama_barang;
        this.harga_barang = harga_barang;
        this.desk_barang = desk_barang;
        this.txl_input = txl_input;
        this.txl_update = txl_update;
    }

    public int getId_barang() {
        return id_barang;
    }

    public void setId_barang(int id_barang) {
        this.id_barang = id_barang;
    }

    public int getJml_barang() {
        return jml_barang;
    }

    public void setJml_barang(int jml_barang) {
        this.jml_barang = jml_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(String harga_barang) {
        this.harga_barang = harga_barang;
    }

    public String getDesk_barang() {
        return desk_barang;
    }

    public void setDesk_barang(String desk_barang) {
        this.desk_barang = desk_barang;
    }

    public String getTxl_input() {
        return txl_input;
    }

    public void setTxl_input(String txl_input) {
        this.txl_input = txl_input;
    }

    public String getTxl_update() {
        return txl_update;
    }

    public void setTxl_update(String txl_update) {
        this.txl_update = txl_update;
    }

}
