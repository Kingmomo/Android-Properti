package com.darmajaya.joo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darmajaya.joo.Adapter.ProdukAdapter;
import com.darmajaya.joo.Model.Produk;
import com.darmajaya.joo.utils.SpacesItemDecoration;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProdukFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProdukAdapter adapter;

    FragmentActivity listener;

    public ProdukFragment() {
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
        return inflater.inflate(R.layout.fragment_produk, parent, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Daftar Produk");

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        recyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(2, 12, false));
        starter();

        TextView filter = view.findViewById(R.id.filtersort);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.filter_bottom, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
        LinearLayout tertinggi = view.findViewById(R.id.tertinggi);
        LinearLayout terendah = view.findViewById(R.id.terendah);

        tertinggi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort("desc");
                dialog.hide();
            }
        });
        terendah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sort("asc");
                dialog.hide();
            }
        });
        dialog.show();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    private void starter() {
        Query query = FirebaseFirestore.getInstance().collection("produk");

        FirestoreRecyclerOptions<Produk> options = new FirestoreRecyclerOptions.Builder<Produk>()
                .setQuery(query, Produk.class)
                .build();

        adapter = new ProdukAdapter(options, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void sort(String data){
         Query query;
         query = FirebaseFirestore.getInstance().collection("produk");
        if (data.equals("asc")){
            query = FirebaseFirestore.getInstance().collection("produk").orderBy("harga", Query.Direction.ASCENDING);
        }else{
            query = FirebaseFirestore.getInstance().collection("produk").orderBy("harga", Query.Direction.DESCENDING);
        }

        FirestoreRecyclerOptions<Produk> options = new FirestoreRecyclerOptions.Builder<Produk>()
                .setQuery(query, Produk.class)
                .build();

        adapter = new ProdukAdapter(options, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        invalidateCart();

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    private void invalidateCart() {
        getActivity().invalidateOptionsMenu();
    }


}
