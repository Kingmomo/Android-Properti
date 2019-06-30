package com.darmajaya.joo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

public class HistoryActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Daftar Produk");
        setSupportActionBar(toolbar);

        getSupportFragmentManager().
                beginTransaction().replace(R.id.fragment_container, new HistoryFragment()).commit();


    }
}
