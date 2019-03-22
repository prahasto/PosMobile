package com.shafco.posmobile.database;

/**
 * Created by BoTaXs on 08-06-2017.
 */

public class ModelLokator {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKode_lokator() {
        return kode_lokator;
    }

    public void setKode_lokator(String kode_lokator) {
        this.kode_lokator = kode_lokator;
    }

    public String getNama_lokator() {
        return nama_lokator;
    }

    public void setNama_lokator(String nama_lokator) {
        this.nama_lokator = nama_lokator;
    }

    private long id;
    private String kode_lokator;
    private String nama_lokator;
}
