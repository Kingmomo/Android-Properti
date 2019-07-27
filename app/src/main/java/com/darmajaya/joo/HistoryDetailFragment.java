package com.darmajaya.joo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darmajaya.joo.Adapter.CartAdapter;
import com.darmajaya.joo.Adapter.HistoryAdapter;
import com.darmajaya.joo.Adapter.HistoryDetailAdapter;
import com.darmajaya.joo.Adapter.ProdukAdapter;
import com.darmajaya.joo.Model.Produk;
import com.darmajaya.joo.Model.Transaksi;
import com.darmajaya.joo.utils.MySharedPreference;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryDetailAdapter adapter;
    private MySharedPreference mySharedPreference;
    private FirebaseFirestore db;
    private Gson gson;
    private List<Produk> productList;
    private String name;


    FragmentActivity listener;

    public HistoryDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_detail, parent, false);

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Detail Transaksi");
        mySharedPreference = new MySharedPreference(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        final TextView pembayaran = view.findViewById(R.id.pembayaran);
        final TextView total = view.findViewById(R.id.total);

        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name = bundle.getString("total");
            String listproduk = bundle.getString("listproduk");
            total.setText(name);
            Produk[] addCartProducts = gson.fromJson(listproduk, Produk[].class);
            productList = convertObjectArrayToListObject(addCartProducts);
            LinearLayoutManager mGrid = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mGrid);
            recyclerView.setHasFixedSize(true);
            adapter = new HistoryDetailAdapter(getActivity(), productList);
            recyclerView.setAdapter(adapter);

        }




        db = FirebaseFirestore.getInstance();
        final DocumentReference docToko = db.collection("profil").document("dataprofil");
        docToko.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        pembayaran.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), Rekening_Activity.class);
                                intent.putExtra("total", name);
                                startActivity(intent);
                            }
                        });
                    } else {
                    }
                } else {
                }
            }
        });


    }


    private List<Produk> convertObjectArrayToListObject(Produk[] allProducts) {
        List<Produk> mProduct = new ArrayList<Produk>();
        if (allProducts != null) {
            Collections.addAll(mProduct, allProducts);
        }
        return mProduct;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
