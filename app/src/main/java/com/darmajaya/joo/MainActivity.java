package com.darmajaya.joo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.darmajaya.joo.utils.MySharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String TAG = "DATASTORE";
    private MySharedPreference mySharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        CardView produk = findViewById(R.id.produk);
        CardView profile = findViewById(R.id.profile);
        CardView history = findViewById(R.id.history);
        CardView caraorder = findViewById(R.id.caraorder);
        Button sign_out = findViewById(R.id.sign_out);
        final TextView namatoko = findViewById(R.id.namatoko);
        final ImageView logo = findViewById(R.id.logo);

        produk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProdukActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        caraorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CaraOrder.class));
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });


        db = FirebaseFirestore.getInstance();
        mySharedPreference = new MySharedPreference(this);
        System.out.println("UID saya" + mySharedPreference.getUid());
        final DocumentReference docRef = db.collection("user").document(mySharedPreference.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        mySharedPreference.setNama(document.getString("nama"));
                        mySharedPreference.setAlamat(document.getString("alamat"));
                        mySharedPreference.setNotelp(document.getString("notelp"));
                    } else {
                        mySharedPreference.setNama("");
                        mySharedPreference.setAlamat("");
                        mySharedPreference.setNotelp("");
                    }
                } else {
                    mySharedPreference.setNama("");
                    mySharedPreference.setAlamat("");
                    mySharedPreference.setNotelp("");
                }
            }
        });

        final DocumentReference docToko = db.collection("profil").document("dataprofil");
        docToko.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        namatoko.setText(document.getString("nama_toko"));
                        Glide.with(MainActivity.this)
                                .load(document.get("foto_logo"))
                                .apply(
                                        new RequestOptions()
                                                .placeholder(R.color.blue_800)
                                                .fitCenter())
                                .into(logo);                        mySharedPreference.setNama(document.getString("nama"));
                        mySharedPreference.setLokasi(document.getString("koordinat"));
                        mySharedPreference.setATM(document.getString("atm_bank"));
                        mySharedPreference.setNamaRek(document.getString("atm_nama"));
                        mySharedPreference.setNoRek(document.getString("atm_nomor"));
                    } else {
                        mySharedPreference.setLokasi("-5.422359, 105.258188");
                    }
                } else {
                    mySharedPreference.setLokasi("-5.422359, 105.258188");
                }
            }
        });

    }


    private void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda ingin Keluar")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // Create the AlertDialog object and return it
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        dialog();
    }
}
