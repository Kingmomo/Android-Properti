package com.darmajaya.joo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.darmajaya.joo.Model.Users;
import com.darmajaya.joo.utils.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        pDialog = new ProgressDialog(this);


        TextView submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesdaftar();
            }
        });

    }

    private void prosesdaftar() {
        //  pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        // pDialog.setIndeterminate(false);
        pDialog.show();

        final TextInputEditText email = findViewById(R.id.email);
        TextInputEditText password = findViewById(R.id.password);
        final TextInputEditText nama = findViewById(R.id.nama);
        final TextInputEditText alamat = findViewById(R.id.alamat);
        final TextInputEditText notelp = findViewById(R.id.notelp);

        if (!Validate.cek(email) && !Validate.cek(nama) && !Validate.cek(alamat)&& !Validate.cek(notelp) && !Validate.cek(password)) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString().toLowerCase().trim(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Users users = new Users(email.getText().toString().toLowerCase().trim(), nama.getText().toString().trim(), alamat.getText().toString().trim(), notelp.getText().toString().trim());
                                db.collection("user").document(mAuth.getUid()).set(users);
                                pDialog.dismiss();
                                Toasty.success(RegisterActivity.this, "Pendaftaran Sukses!", Toast.LENGTH_SHORT, true).show();
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                pDialog.hide();
                                Toast.makeText(RegisterActivity.this, "Registrasi Gagal", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }else{
            pDialog.hide();
        }
    }



}
