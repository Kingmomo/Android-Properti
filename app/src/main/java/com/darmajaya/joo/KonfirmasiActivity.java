package com.darmajaya.joo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class KonfirmasiActivity extends AppCompatActivity {

    private static final String TAG = "Uploadctivity";
    private ImageView konfirmasi;
    private Button btnkonfirmasi;
    private static final String FB_STORAGE_PATH = "konfirmasi/";
    private static final int GALLERY_REQUEST_CODE = 5;
    private int MAP = 3;
    private StorageReference storage;
    private Uri imgUri;
    private UploadTask uploadTask;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        konfirmasi = (ImageView) findViewById(R.id.konfirmasi);
        btnkonfirmasi = findViewById(R.id.btnkonfirmasi);
        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance().getReference();
        konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
        clickupload();

        if (getIntent().getStringExtra("KonfirmasiActivity") != null) {
            Glide.with(KonfirmasiActivity.this)
                    .load(getIntent().getStringExtra("foto"))
                    .apply(
                            new RequestOptions()
                                    .placeholder(R.color.pink_200)
                                    .fitCenter())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(konfirmasi);
        }

    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                konfirmasi.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void clickupload() {
        btnkonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgUri != null) {

                    final ProgressDialog dialog = new ProgressDialog(KonfirmasiActivity.this);
                    dialog.setTitle("Uploading image");
                    dialog.show();

                    final StorageReference ref = storage.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));
                    uploadTask = ref.putFile(imgUri);
                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Lanjutkan dengan Task untuk mendapat Url Gambar
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                dialog.dismiss();

                                Map<String, Object> toko = new HashMap<>();
                                toko.put("bukti_transfer", downloadUri.toString());
                                toko.put("status", "Konfirmasi Pembayaran Di Proses");

                                DocumentReference updatekonfimasi = db.collection("transaksi").document(getIntent().getStringExtra("konfirmasi"));
                                updatekonfimasi.update(toko);

                                Toasty.success(KonfirmasiActivity.this, "Update KonfirmasiActivity Sukses", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                dialog.dismiss();
                                Toasty.warning(KonfirmasiActivity.this, "Update KonfirmasiActivity Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + progress + "%");
                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("Upload is paused");
                        }
                    });
                } else {
                    Toasty.warning(KonfirmasiActivity.this, "Pilih Gambar Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
