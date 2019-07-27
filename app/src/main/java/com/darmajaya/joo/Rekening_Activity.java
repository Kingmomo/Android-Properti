package com.darmajaya.joo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.darmajaya.joo.utils.MySharedPreference;

public class Rekening_Activity extends AppCompatActivity {
    private MySharedPreference mySharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening);
        mySharedPreference = new MySharedPreference(this);

        TextView pembayaran = findViewById(R.id.pembayaran);
        TextView total = findViewById(R.id.total);
        pembayaran.setText("Lakukan Transfer Ke No Rekening "+ mySharedPreference.gettNoRek() +" Bank "+ mySharedPreference.getATM()+ " A.n " + mySharedPreference.getNamaRek());
        total.setText(getIntent().getStringExtra("total"));

    }
}
