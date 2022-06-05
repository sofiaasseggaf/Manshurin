package com.sofia.manshurin.model;

public class ModelSaldo {

    int id_saldo;
    String nama_saldo, jenis_saldo, nominal_saldo, desk_saldo, tgl_input, tgl_update;

    public ModelSaldo(int id_saldo, String nama_saldo, String jenis_saldo, String nominal_saldo, String desk_saldo, String tgl_input, String tgl_update) {
        this.id_saldo = id_saldo;
        this.nama_saldo = nama_saldo;
        this.jenis_saldo = jenis_saldo;
        this.nominal_saldo = nominal_saldo;
        this.desk_saldo = desk_saldo;
        this.tgl_input = tgl_input;
        this.tgl_update = tgl_update;
    }

    public int getId_saldo() {
        return id_saldo;
    }

    public void setId_saldo(int id_saldo) {
        this.id_saldo = id_saldo;
    }

    public String getNama_saldo() {
        return nama_saldo;
    }

    public void setNama_saldo(String nama_saldo) {
        this.nama_saldo = nama_saldo;
    }

    public String getJenis_saldo() {
        return jenis_saldo;
    }

    public void setJenis_saldo(String jenis_saldo) {
        this.jenis_saldo = jenis_saldo;
    }

    public String getNominal_saldo() {
        return nominal_saldo;
    }

    public void setNominal_saldo(String nominal_saldo) {
        this.nominal_saldo = nominal_saldo;
    }

    public String getDesk_saldo() {
        return desk_saldo;
    }

    public void setDesk_saldo(String desk_saldo) {
        this.desk_saldo = desk_saldo;
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
