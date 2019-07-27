package com.darmajaya.joo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "DocSnippets";
    private FirebaseFirestore db;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        context = this;

        final ImageView imageheader = findViewById(R.id.image_header);
        final ImageView imageprofile = findViewById(R.id.profile_image);
        final TextView namatoko = findViewById(R.id.namatoko);
        final TextView tentang = findViewById(R.id.tentang);
        final TextView lokasi = findViewById(R.id.lokasi);
        final TextView kontak = findViewById(R.id.kontak);

        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("profil").document("dataprofil");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.get("foto_logo"));
                        Glide.with(context)
                                .load(document.get("foto_toko"))
                                .apply(
                                        new RequestOptions()
                                                .placeholder(R.color.blue_500)
                                                .fitCenter())
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .into(imageheader);
                        Glide.with(context)
                                .load(document.get("foto_logo"))
                                .apply(
                                        new RequestOptions()
                                                .placeholder(R.color.blue_500)
                                                .fitCenter())
                                .into(imageprofile);

                        namatoko.setText(document.get("nama_toko").toString());
                        lokasi.setText(document.get("alamat").toString());
                        tentang.setText(document.get("deskripsi").toString());
                        kontak.setText(document.get("no_hp").toString());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
