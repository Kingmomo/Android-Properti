package com.darmajaya.joo.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreference {

    private SharedPreferences prefs;

    private Context context;

    public MySharedPreference(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(Constants.SHARED_UID, Context.MODE_PRIVATE);
    }

    public void addProductToTheCart(String product){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(Constants.PRODUCT_ID, product);
        edits.apply();
    }

    public String retrieveProductFromCart(){
        return prefs.getString(Constants.PRODUCT_ID, "");
    }

    public void deleteProductFromCart(){
        SharedPreferences.Editor edits = prefs.edit();
        edits.remove(Constants.PRODUCT_ID).apply();
    }

    public void setUid(String uid) {
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(Constants.SHARED_UID, uid);
        edits.apply();
    }

    public String getUid() {
        return prefs.getString(Constants.SHARED_UID, "");
    }

    public void setNama(String uid) {
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(Constants.NAMA, uid);
        edits.apply();
    }

    public String getNama() {
        return prefs.getString(Constants.NAMA, "");
    }

    public void setAlamat(String uid) {
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(Constants.ALAMAT, uid);
        edits.apply();
    }

    public String getAlamat() {
        return prefs.getString(Constants.ALAMAT, "");
    }

    public void setNotelp(String uid) {
        SharedPreferences.Editor edits = prefs.edit();
        edits.putString(Constants.NO_TELP, uid);
        edits.apply();
    }

    public String getNotelp() {
        return prefs.getString(Constants.NO_TELP, "");
    }

    public void addProductCount(int productCount){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putInt(Constants.PRODUCT_COUNT, productCount);
        edits.apply();
    }

    public int retrieveProductCount(){
        return prefs.getInt(Constants.PRODUCT_COUNT, 0);
    }

    public void addTotalHarga(int totalharga){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putInt(Constants.TOTAL_HARGA, totalharga);
        edits.apply();
    }

    public int retrieveTotalHarga(){
        return prefs.getInt(Constants.TOTAL_HARGA, 0);

    }

    public void deletedata(){
        SharedPreferences.Editor edits = prefs.edit();
        edits.clear().commit();
    }

}
