package com.shafco.posmobile.database;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class ModelToko {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKode_toko() {
        return kode_toko;
    }

    public void setKode_toko(String kode_toko) {
        this.kode_toko = kode_toko;
    }

    public String getNama_toko() {
        return nama_toko;
    }

    public void setNama_toko(String nama_toko) {
        this.nama_toko = nama_toko;
    }

    private long id;
    private String kode_toko;
    private String nama_toko;
}
