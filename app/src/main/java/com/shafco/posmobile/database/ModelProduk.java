package com.shafco.posmobile.database;

/**
 * Created by BoTaXs on 08-06-2017.
 */



public class ModelProduk {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKode_poduk() {
        return kode_poduk;
    }

    public void setKode_poduk(String kode_poduk) {
        this.kode_poduk = kode_poduk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public int getHpj() {
        return hpj;
    }

    public void setHpj(int hpj) {
        this.hpj = hpj;
    }

    private long id;
    private String kode_poduk;
    private String nama_produk;
    private int hpj;
    private int r_produk_id;

    public int getR_produk_id() {
        return r_produk_id;
    }

    public void setR_produk_id(int r_produk_id) {
        this.r_produk_id = r_produk_id;
    }
}
