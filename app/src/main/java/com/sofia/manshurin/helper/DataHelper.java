package com.sofia.manshurin.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.sofia.manshurin.model.ModelBarang;
import com.sofia.manshurin.model.ModelKranjang;
import com.sofia.manshurin.model.ModelPenjualan;
import com.sofia.manshurin.model.ModelRiwayatPenjualan;
import com.sofia.manshurin.model.ModelSaldo;
import com.sofia.manshurin.model.ModelUser;

import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "manshurin2.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // TABLE : USER, BARANG, SALDO, KERANJANG, RIWAYAT PEMBELIAN, RIWAYAT PENJUALAN

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_USER = "create table user(id_user integer primary key, username text null, password text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_USER);

        String CREATE_TABLE_BARANG = "create table barang(id_barang integer primary key, nama_barang text null, harga_barang text null, jml_barang integer null, desk_barang text null, tgl_input text null, tgl_update text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_BARANG);
        db.execSQL(CREATE_TABLE_BARANG);

        String CREATE_TABLE_SALDO = "create table saldo(id_saldo integer primary key, nama_saldo text null, jenis_saldo text null, nominal_saldo text null, desk_saldo text null, tgl_input text null, tgl_update text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_SALDO);
        db.execSQL(CREATE_TABLE_SALDO);

        String CREATE_TABLE_PEMBELIAN = "create table pembelian(id_pembelian integer primary key, id_riwayat integer null, id_barang integer null, jumlah integer null, harga text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_PEMBELIAN);
        db.execSQL(CREATE_TABLE_PEMBELIAN);

        String CREATE_TABLE_PENJUALAN = "create table penjualan(id_penjualan integer primary key, id_riwayat integer null, id_barang integer null, jumlah integer null, harga text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_PENJUALAN);
        db.execSQL(CREATE_TABLE_PENJUALAN);

        String CREATE_TABLE_KRNJNG = "create table kranjang(idtransaksi integer primary key, idkranjang integer null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_KRNJNG);
        db.execSQL(CREATE_TABLE_KRNJNG);

        String CREATE_TABLE_RIWAYAT_PEMBELIAN = "create table riwayatpembelian(id_riwayat integer null, harga text null, harga_katul text null, jenis_pembayaran text null, tgl_input text null, tgl_update text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_RIWAYAT_PEMBELIAN);
        db.execSQL(CREATE_TABLE_RIWAYAT_PEMBELIAN);

        String CREATE_TABLE_RIWAYAT_PENJUALAN = "create table riwayatpenjualan(id_riwayat integer null, harga text null, harga_katul text null, jenis_pembayaran text null, tgl_input text null, tgl_update text null);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_RIWAYAT_PENJUALAN);
        db.execSQL(CREATE_TABLE_RIWAYAT_PENJUALAN);


        // --------------- INSERT DATA TO TABLE -----------

        String INSERT_INTO_USER = "INSERT INTO user(id_user, username, password) VALUES ('0001', 'Sofia', '1234');";
        db.execSQL(INSERT_INTO_USER);

        String INSERT_INTO_BARANG = "INSERT INTO barang(id_barang, nama_barang, harga_barang, jml_barang, desk_barang, tgl_input, tgl_update) VALUES ('0001', 'Beras ABC', '200.000', '12', 'bla bla', '12/02/2022', '12/02/2022');";
        db.execSQL(INSERT_INTO_BARANG);

        String INSERT_INTO_SALDO = "INSERT INTO saldo(id_saldo, nama_saldo, jenis_saldo, nominal_saldo, desk_saldo, tgl_input, tgl_update) VALUES ('0001', 'Saldo A', '600.000', 'Pinjaman', 'bla bla', '12/02/2022', '12/02/2022');";
        db.execSQL(INSERT_INTO_SALDO);

    }

    public List<ModelUser> getAllUser() {
        List<ModelUser> modelUserList = new ArrayList<ModelUser>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM user", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelUser profileModel = new ModelUser(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2));

                modelUserList.add(profileModel);
            } while (cursor.moveToNext());
        }

        // return student list
        return modelUserList;
    }

    public List<ModelBarang> getAllBarang() {
        List<ModelBarang> modelBarangList = new ArrayList<ModelBarang>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM barang", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelBarang barangModel = new ModelBarang(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));

                modelBarangList.add(barangModel);
            } while (cursor.moveToNext());
        }

        // return student list
        return modelBarangList;
    }

    public List<ModelSaldo> getAllSaldo() {
        List<ModelSaldo> modelSaldoList = new ArrayList<ModelSaldo>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM saldo", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelSaldo saldoModel = new ModelSaldo(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));

                modelSaldoList.add(saldoModel);
            } while (cursor.moveToNext());
        }

        // return student list
        return modelSaldoList;
    }

    //get barang with id
    public ModelBarang getBarang(int id_barang) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM barang WHERE id_barang = '" + id_barang + "'", null);
        String[] barang = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            barang[cc] = cursor.getString(0).toString();
        }
        ModelBarang baranglogged = new ModelBarang(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));

        //Return Barang
        return baranglogged;
    }

    //get saldo with id
    public ModelSaldo getSaldo(int id_saldo) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM saldo WHERE id_saldo = '" + id_saldo + "'", null);
        String[] saldo = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            saldo[cc] = cursor.getString(0).toString();
        }
        ModelSaldo saldologged = new ModelSaldo(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6));

        //Return Saldo
        return saldologged;
    }

    public List<ModelPenjualan> getAllPenjualan() {
        List<ModelPenjualan> modelPenjualanList = new ArrayList<ModelPenjualan>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM penjualan", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelPenjualan penjualanModel = new ModelPenjualan(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4));
                modelPenjualanList.add(penjualanModel);
            } while (cursor.moveToNext());
        }

        // return student list
        return modelPenjualanList;
    }

    public List<ModelKranjang> getAllKranjang(){
        // kranjang(idtransaksi integer primary key, idkranjang integer null);";
        List<ModelKranjang> modelKranjangList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM kranjang", null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                ModelKranjang kranjangModel = new ModelKranjang(
                        cursor.getInt(0),
                        cursor.getInt(1));
                modelKranjangList.add(kranjangModel);
            } while (cursor.moveToNext());
        }

        return modelKranjangList;
    }

    public List<ModelRiwayatPenjualan> getAllRiwayatPenjualan(){
        // riwayatpenjualan(id_riwayat integer null, harga text null,
        // harga_katul text null, jenis_pembayaran text null, tgl_input text null, tgl_update text null);";

        List<ModelRiwayatPenjualan> modelRiwayatPenjualanList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM riwayatpenjualan", null);
        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                ModelRiwayatPenjualan riwayatPenjualanModel = new ModelRiwayatPenjualan(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
                modelRiwayatPenjualanList.add(riwayatPenjualanModel);
            } while (cursor.moveToNext());
        }

        return modelRiwayatPenjualanList;
    }


    //get riwayat penjualan with id
    public ModelRiwayatPenjualan getRiwayatPenjualan(int id_riwayat_penjualan) {

        // riwayatpenjualan(id_riwayat integer null, harga text null,
        // harga_katul text null, jenis_pembayaran text null, tgl_input text null, tgl_update text null);";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM riwayatpenjualan WHERE id_riwayat = '" + id_riwayat_penjualan + "'", null);
        String[] rpenjualan = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            rpenjualan[cc] = cursor.getString(0).toString();
        }
        ModelRiwayatPenjualan rpenjualanLogged = new ModelRiwayatPenjualan(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5));

        //Return Riwayat Penjualan
        return rpenjualanLogged;
    }





    /*public List<BarangModel> getAllBarangFilter(String itemDecs) {
        List<BarangModel> barangModelListFiltered = new ArrayList<BarangModel>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM barang WHERE item_desc = '" + itemDecs + "'", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BarangModel barangModel = new BarangModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4));

                barangModelListFiltered.add(barangModel);
            } while (cursor.moveToNext());
        }

        // return barang model list
        return barangModelListFiltered;
    }

    public List<BarangGudangModel> getAllBarangGudangFilter(String itemDesc) {
        List<BarangGudangModel> barangGudangListFiltered = new ArrayList<BarangGudangModel>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM persediaan_gudang_brg WHERE item_desc = '" + itemDesc + "'", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BarangGudangModel barangGudangModel = new BarangGudangModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5));

                barangGudangListFiltered.add(barangGudangModel);
            } while (cursor.moveToNext());
        }

        return barangGudangListFiltered;
    }

    public List<BarangGudangModel> getAllBarangGudangFilterDate(String tanggal) {
        List<BarangGudangModel> barangGudangListFiltered = new ArrayList<BarangGudangModel>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM persediaan_gudang_brg WHERE tanggal = '" + tanggal + "'", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BarangGudangModel barangGudangModel = new BarangGudangModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5));

                barangGudangListFiltered.add(barangGudangModel);
            } while (cursor.moveToNext());
        }

        return barangGudangListFiltered;
    }

    public List<BarangModel> getAllBarangFilterID(String itemCD) {
        List<BarangModel> barangModelListFiltered = new ArrayList<BarangModel>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM barang WHERE item_cd = '" + itemCD + "'", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BarangModel barangModel = new BarangModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4));

                barangModelListFiltered.add(barangModel);
            } while (cursor.moveToNext());
        }

        // return barang model list
        return barangModelListFiltered;
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
