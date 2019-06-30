package com.darmajaya.joo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.darmajaya.joo.Model.Produk;
import com.darmajaya.joo.ProdukActivity;
import com.darmajaya.joo.R;
import com.darmajaya.joo.utils.MySharedPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private MySharedPreference sharedPreference;
    private Gson gson;
    private Context context;
    private List<Produk> produkList;
    private OnDataChangeListener mOnDataChangeListener;


    public CartAdapter(Context context, List<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.MyViewHolder holder, final int position) {
        sharedPreference = new MySharedPreference(context);
        GsonBuilder builder = new GsonBuilder();

        gson = builder.create();

        final Produk produk = produkList.get(position);

        final long harga = Long.parseLong(produk.getHarga());
        Locale localeID = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.harga.setText(formatRupiah.format(Double.parseDouble(String.valueOf(harga * produk.getJumlah()))));
        holder.nama.setText(produk.getNama_produk());
        holder.jumlah.setText(String.valueOf(produk.getJumlah()));
        Glide.with(context)
                .load(produk.getFoto())
                .apply(
                        new RequestOptions()
                                .placeholder(R.color.pink_200)
                                .fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.gambar);

        holder.tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produk.getJumlah() >= 1) {
                    produk.setJumlah(produk.getJumlah() + 1);
                    List<Produk> checkdata = checkdata(produk, +1);
                    String addAndStoreNewProduct = gson.toJson(checkdata);
                    sharedPreference.deleteProductFromCart();
                    sharedPreference.addProductToTheCart(addAndStoreNewProduct);
                }

                holder.jumlah.setText(String.valueOf(produk.getJumlah()));
                holder.harga.setText(formatRupiah.format(Double.parseDouble(String.valueOf(harga * produk.getJumlah()))));
                doButtonOneClickActions(produkList);


            }
        });

        holder.kurang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produk.getJumlah() > 1) {
                    produk.setJumlah(produk.getJumlah() - 1);
                    List<Produk> checkdata = checkdata(produk, -1);
                    String addAndStoreNewProduct = gson.toJson(checkdata);
                    sharedPreference.deleteProductFromCart();
                    sharedPreference.addProductToTheCart(addAndStoreNewProduct);
                }
                holder.jumlah.setText(String.valueOf(produk.getJumlah()));
                holder.harga.setText(formatRupiah.format(Double.parseDouble(String.valueOf(harga * produk.getJumlah()))));
                doButtonOneClickActions(produkList);


            }
        });

        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produkList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, produkList.size());

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                sharedPreference = new MySharedPreference(context);

                Produk[] addCartProducts = gson.fromJson(sharedPreference.retrieveProductFromCart(), Produk[].class);
                List<Produk> allNewProduct = convertObjectArrayToListObject(addCartProducts);

                allNewProduct.remove(position);
                String addAndStoreNewProduct = gson.toJson(allNewProduct);
                sharedPreference.deleteProductFromCart();
                sharedPreference.addProductToTheCart(addAndStoreNewProduct);
                sharedPreference.addProductCount(allNewProduct.size());
                invalidateCart();
                doButtonOneClickActions(produkList);

            }
        });
        doButtonOneClickActions(produkList);
    }

    @Override
    public int getItemCount() {
        if (produkList == null)
            return 0;
        else
            return produkList.size();
    }

    private List<Produk> convertObjectArrayToListObject(Produk[] allProducts) {
        List<Produk> mProduct = new ArrayList<Produk>();
        Collections.addAll(mProduct, allProducts);
        return mProduct;
    }

    private List<Produk> checkdata(Produk produk, int number) {
        String productsInCart = sharedPreference.retrieveProductFromCart();
        Produk[] storedProducts = gson.fromJson(productsInCart, Produk[].class);
        List<Produk> mProduct = convertObjectArrayToListObject(storedProducts);
        Produk produks = new Produk(produk.getNama_produk(), produk.getFoto(), produk.getHarga(), produk.getDeskripsi(), 1);

        int d = 0;
        while (d < mProduct.size()) {
            if (mProduct.get(d).getNama_produk().equals(produk.getNama_produk())) {
                produks = new Produk(produk.getNama_produk(), produk.getFoto(), produk.getHarga(), produk.getDeskripsi(), (mProduct.get(d).getJumlah() + number));
                mProduct.remove(d);
                break;
            }
            d++;

        }

        mProduct.add(d, produks);


        return mProduct;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView nama, harga, jumlah;
        ImageButton tambah, kurang;
        Button hapus;
        View view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nama = view.findViewById(R.id.nama);
            harga = view.findViewById(R.id.harga);
            jumlah = view.findViewById(R.id.jumlah);
            gambar = view.findViewById(R.id.gambar);
            tambah = view.findViewById(R.id.tambah);
            kurang = view.findViewById(R.id.kurang);
            hapus = view.findViewById(R.id.hapus);
        }
    }

    private int getTotalPrice(List<Produk> mProducts) {
        int totalCost = 0;
        for (int i = 0; i < mProducts.size(); i++) {
            Produk pObject = mProducts.get(i);
            totalCost += (Integer.parseInt(pObject.getHarga()) * pObject.getJumlah());
        }
        return totalCost;

    }

    public interface OnDataChangeListener {
        public void onDataChanged(int size);

    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        mOnDataChangeListener = onDataChangeListener;
    }

    private void doButtonOneClickActions(List<Produk> mProducts) {
        if (mOnDataChangeListener != null) {
            mOnDataChangeListener.onDataChanged(getTotalPrice(mProducts));
        }
    }
    private void invalidateCart() {
        ((ProdukActivity) context).invalidateOptionsMenu();
    }


}
