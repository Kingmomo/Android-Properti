package com.darmajaya.joo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darmajaya.joo.Adapter.HistoryAdapter;
import com.darmajaya.joo.Model.Transaksi;
import com.darmajaya.joo.utils.MySharedPreference;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private MySharedPreference mySharedPreference;

    FragmentActivity listener;

    public HistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, parent, false);

    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Daftar Transaksi");
        mySharedPreference = new MySharedPreference(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        Button hubungi = view.findViewById(R.id.hubungi);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mGrid = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);
        starter();


    }


    private void starter() {
        Query query = FirebaseFirestore.getInstance().collection("transaksi").whereEqualTo("iduser", mySharedPreference.getUid()).orderBy("tanggal", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Transaksi> options = new FirestoreRecyclerOptions.Builder<Transaksi>()
                .setQuery(query, Transaksi.class)
                .build();

        adapter = new HistoryAdapter(options, getActivity());
        recyclerView.setAdapter(adapter);
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


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
