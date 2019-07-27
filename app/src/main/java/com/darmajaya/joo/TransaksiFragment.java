package com.darmajaya.joo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.darmajaya.joo.Model.Transaksi;
import com.darmajaya.joo.utils.MySharedPreference;
import com.darmajaya.joo.utils.Validate;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class TransaksiFragment extends Fragment {
    FragmentActivity listener;
    private int MAP = 2;
    private EditText koordinat;
    private Number number;
    private MySharedPreference mySharedPreference;
    private ProgressBar progress_bar;
    private ProgressDialog pDialog;

    public TransaksiFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaksi, parent, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Form Transaksi");

        setHasOptionsMenu(false);
        koordinat = view.findViewById(R.id.koordinat);
        final EditText total = view.findViewById(R.id.harga);
        final EditText nama = view.findViewById(R.id.nama);
        final EditText alamat = view.findViewById(R.id.alamat);
        final EditText notelp = view.findViewById(R.id.notelp);
        final EditText date = view.findViewById(R.id.date);
        Button submit = view.findViewById(R.id.btntransaksi);
        final CheckBox checkbox = view.findViewById(R.id.checkbox);
        pDialog = new ProgressDialog(getActivity());

        mySharedPreference = new MySharedPreference(getActivity());

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String tgl = formatter.format(todayDate);
        date.setText(tgl);


        Validate.dismissKeyboard(koordinat, getActivity());
        koordinat.clearFocus();
        koordinat.setFocusable(false);
        Validate.dismissKeyboard(date, getActivity());
        date.clearFocus();
        date.setFocusable(false);
        Validate.dismissKeyboard(total, getActivity());
        total.clearFocus();
        total.setFocusable(false);
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            String name = bundle.getString("total");
            try {
                number = formatRupiah.parse(name);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Random rand = new Random();
            int randomnilai = rand.nextInt(100);
            number = Integer.parseInt(number.toString()) + randomnilai;

            total.setText(formatRupiah.format(Integer.parseInt(String.valueOf(number))));
        }


        koordinat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                koordinat.setEnabled(false);
                startActivityForResult(intent, MAP);
            }
        });


        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkbox.isChecked()) {
                    nama.setText(mySharedPreference.getNama());
                    alamat.setText(mySharedPreference.getAlamat());
                    notelp.setText(mySharedPreference.getNotelp());

                } else {
                    nama.setText("");
                    alamat.setText("");
                    notelp.setText("");
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(koordinat, nama, alamat, notelp)) {
                    Toasty.warning(getActivity(), "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    pDialog.setMessage("Loading");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    String[] exp = mySharedPreference.getLokasi().split(",");
                    String[] expuser = koordinat.getText().toString().split(",");

                    Location lokasitoko = new Location("point A");
                    lokasitoko.setLatitude(Double.parseDouble(exp[0]));
                    lokasitoko.setLongitude(Double.parseDouble(exp[1]));

                    Location locationB = new Location("point B");
                    locationB.setLatitude(Double.parseDouble(expuser[0]));
                    locationB.setLongitude(Double.parseDouble(expuser[1]));

                    double distance = lokasitoko.distanceTo(locationB)/1000;


                    Transaksi transaksi = new Transaksi(mySharedPreference.getUid(), nama.getText().toString(), alamat.getText().toString(), notelp.getText().toString(), date.getText().toString(), koordinat.getText().toString(), total.getText().toString(), mySharedPreference.retrieveProductFromCart(), "Belum Di Konfirmasi", String.format("%.3f", distance), null);

                    db.collection("transaksi").document()
                            .set(transaksi)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toasty.success(getActivity(), "Pemesanan Berhasil", Toast.LENGTH_LONG).show();
                                        getActivity().finish();
                                        mySharedPreference.deleteProductdancount();
                                        Intent intent = new Intent(getActivity(), Rekening_Activity.class);
                                        intent.putExtra("total", total.getText().toString());
                                        startActivity(intent);
                                        pDialog.hide();
                                    } else {
                                        pDialog.hide();
                                        Toasty.success(getActivity(), "Pemesanan Gagal", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pDialog.hide();
                                    Toasty.success(getActivity(), "Pemesanan Gagal", Toast.LENGTH_LONG).show();
                                }
                            });


                }


            }
        });

    }

    private boolean isEmpty(EditText etText, EditText etText2, EditText etText3, EditText etText4) {
        if (etText.getText().toString().trim().length() > 0 && etText2.getText().toString().trim().length() > 0 && etText3.getText().toString().trim().length() > 0 && etText4.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP) {

            if (resultCode == Activity.RESULT_OK) {
                double lat = (double) data.getDoubleExtra("location_lat", 0);
                double lng = (double) data.getDoubleExtra("location_lng", 0);
                System.out.println("mantap soul " + lat + " " + lng);
                koordinat.setText(String.valueOf(lat) + ", " + String.valueOf(lng));
            }


        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        koordinat.setEnabled(true);
    }
}
