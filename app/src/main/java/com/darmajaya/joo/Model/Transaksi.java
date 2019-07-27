package com.darmajaya.joo.Model;

public class Transaksi {
    private String iduser, nama, alamat, notelp, tanggal, koordinat, total, listproduk, status, jarak, bukti_transfer;

    public Transaksi() {
    }

    public Transaksi(String iduser, String nama, String alamat, String notelp, String tanggal, String koordinat, String total, String listproduk, String status, String jarak, String bukti_transfer) {
        this.iduser = iduser;
        this.nama = nama;
        this.alamat = alamat;
        this.notelp = notelp;
        this.tanggal = tanggal;
        this.koordinat = koordinat;
        this.total = total;
        this.listproduk = listproduk;
        this.status = status;
        this.jarak = jarak;
        this.bukti_transfer = bukti_transfer;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKoordinat() {
        return koordinat;
    }

    public void setKoordinat(String koordinat) {
        this.koordinat = koordinat;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getListproduk() {
        return listproduk;
    }

    public void setListproduk(String listproduk) {
        this.listproduk = listproduk;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getBukti_transfer() {
        return bukti_transfer;
    }

    public void setBukti_transfer(String bukti_transfer) {
        this.bukti_transfer = bukti_transfer;
    }
}
