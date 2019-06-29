package com.darmajaya.joo.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;


public class Validate {

    public static boolean cek(EditText et) {
            View focusView = null;
            Boolean cancel=false;
            if (TextUtils.isEmpty(et.getText().toString().trim())) {
                et.setError("Inputan Harus Di Isi");
                focusView = et;
                cancel = true;
            }
            if (cancel) {
                focusView.requestFocus();
            }
            return cancel;
    }


    public static boolean cekPassword(TextInputEditText et, TextInputEditText et2){
        View focusView = null;
        Boolean cancel = false;
        if (TextUtils.isEmpty(et.getText().toString().trim())) {
            et.setError("Password tidak boleh kosong / < 4 digit");
            focusView = et;
            cancel = true;
        }else if(TextUtils.isEmpty(et2.getText().toString().trim())){
            et2.setError("Password tidak boleh kosong / < 4 digit");
            focusView = et2;
            cancel = true;
        } else if (et.getText().toString().trim() != et2.getText().toString().trim()) {
            et.setError("Password Tidak sama");
            focusView = et2;
            cancel = true;
        }else{
            cancel = false;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        return cancel;
    }
}
