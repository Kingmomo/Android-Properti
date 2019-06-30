package com.darmajaya.joo.Model;

public class Transaksi {
    private String iduser, nama, alamat, notelp, tanggal, koordinat, total, listproduk, status;

    public Transaksi() {
    }

    public Transaksi(String iduser, String nama, String alamat, String notelp, String tanggal, String koordinat, String total, String listproduk, String status) {
        this.iduser = iduser;
        this.nama = nama;
        this.alamat = alamat;
        this.notelp = notelp;
        this.tanggal = tanggal;
        this.koordinat = koordinat;
        this.total = total;
        this.listproduk = listproduk;
        this.status = status;
    }

    public String getIduser() {
        return iduser;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNotelp() {
        return notelp;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKoordinat() {
        return koordinat;
    }

    public String getTotal() {
        return total;
    }

    public String getListproduk() {
        return listproduk;
    }

    public String getStatus() {
        return status;
    }
}
