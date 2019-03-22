package com.shafco.posmobile.database;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class ModelOpname {
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

    public String getKode_alokator() {
        return kode_alokator;
    }

    public void setKode_alokator(String kode_alokator) {
        this.kode_alokator = kode_alokator;
    }

    public String getKode_produk() {
        return kode_produk;
    }

    public void setKode_produk(String kode_produk) {
        this.kode_produk = kode_produk;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private long id;
    private String  kode_toko;
    private String  kode_alokator;
    private String kode_produk;
    private String team;
    private int qty;
    private String tanggal;
    private String jam;
    private String status;
}
