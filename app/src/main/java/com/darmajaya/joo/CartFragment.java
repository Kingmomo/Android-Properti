package com.darmajaya.joo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.darmajaya.joo.Adapter.CartAdapter;
import com.darmajaya.joo.Model.Produk;
import com.darmajaya.joo.utils.MySharedPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    FragmentActivity listener;
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private MySharedPreference sharedPreference;
    private Gson gson;
    private int mSubTotal = 0;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
            ((AppCompatActivity)context).getSupportActionBar().setTitle("Keranjang Belanja");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, parent, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Keranjang Belanja");

        sharedPreference = new MySharedPreference(getActivity());
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
        Produk[] addCartProducts = gson.fromJson(sharedPreference.retrieveProductFromCart(), Produk[].class);
        final List<Produk> productList = convertObjectArrayToListObject(addCartProducts);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        final TextView total = view.findViewById(R.id.total);

        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mGrid = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mGrid);
        recyclerView.setHasFixedSize(true);
        adapter = new CartAdapter(getActivity(), productList);
        recyclerView.setAdapter(adapter);

        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        mSubTotal = getTotalPrice(productList);

        total.setText((formatRupiah.format(Integer.parseInt(String.valueOf(mSubTotal)))));

        adapter.setOnDataChangeListener(new CartAdapter.OnDataChangeListener() {
            public void onDataChanged(int size) {
                total.setText((formatRupiah.format(Integer.parseInt(String.valueOf(size)))));

            }
        });


    }

    private List<Produk> convertObjectArrayToListObject(Produk[] allProducts) {
        List<Produk> mProduct = new ArrayList<Produk>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    private int getTotalPrice(List<Produk> mProducts) {
        int totalCost = 0;
        for (int i = 0; i < mProducts.size(); i++) {
            Produk pObject = mProducts.get(i);
            totalCost += (Integer.parseInt(pObject.getHarga())* pObject.getJumlah());
        }
        return totalCost;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }
}
