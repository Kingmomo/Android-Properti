package com.darmajaya.joo.Model;

public class Produk {
    String nama_produk, foto, harga, deskripsi;
    int jumlah;

    public Produk(String nama_produk, String foto, String harga, String deskripsi, int jumlah) {
        this.nama_produk = nama_produk;
        this.foto = foto;
        this.harga = harga;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
    }

    public Produk() {
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
