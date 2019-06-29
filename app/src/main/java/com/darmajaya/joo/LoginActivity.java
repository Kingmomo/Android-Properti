package com.darmajaya.joo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.darmajaya.joo.utils.MySharedPreference;
import com.darmajaya.joo.utils.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MySharedPreference sharedPreference;
    private ProgressDialog pDialog;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreference = new MySharedPreference(this);
        pDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        TextView sign_up = (TextView) findViewById(R.id.sign_up);
        TextView submit = findViewById(R.id.submit);
        TextView title = findViewById(R.id.title);
        View garis = findViewById(R.id.garis);

        Animation frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        Animation fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        CardView menus = (CardView) findViewById(R.id.menu);
        menus.animate().alpha(100).setDuration(4000).setStartDelay(1000);
        garis.animate().alpha(100).setDuration(2000).setStartDelay(800).rotation(180);
        submit.startAnimation(frombottom);
        title.startAnimation(fromtop);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proseslogin();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    private void proseslogin() {
        //  pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);
        // pDialog.setIndeterminate(false);
        pDialog.show();
        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText password = findViewById(R.id.password);
        if (!Validate.cek(email) && !Validate.cek(password)) {
            mAuth.signInWithEmailAndPassword(email.getText().toString().toLowerCase(), password.getText().toString().trim())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toasty.success(LoginActivity.this, "Login Sukses!", Toast.LENGTH_LONG, true).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                sharedPreference.setUid(mAuth.getUid());
                                startActivity(intent);
                                pDialog.dismiss();

                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                pDialog.hide();
                                Toasty.error(LoginActivity.this, "Login Gagal", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            pDialog.hide();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            finish();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            sharedPreference.setUid(mAuth.getUid());
            finish();
            startActivity(intent);
        }
    }


}
