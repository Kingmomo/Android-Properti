package com.darmajaya.joo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.darmajaya.joo.Model.Produk;
import com.darmajaya.joo.ProdukActivity;
import com.darmajaya.joo.R;
import com.darmajaya.joo.utils.MySharedPreference;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ProdukAdapter extends FirestoreRecyclerAdapter<Produk, ProdukAdapter.MyViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private MySharedPreference sharedPreference;
    private Gson gson;
    private Context context;
    private int cartProductNumber = 0;


    public ProdukAdapter(@NonNull FirestoreRecyclerOptions<Produk> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int i, @NonNull final Produk model) {
        sharedPreference = new MySharedPreference(context);
        GsonBuilder builder = new GsonBuilder();

        gson = builder.create();

        holder.harga.setText(model.getHarga());
        holder.nama.setText(model.getNama_produk());
        holder.deskripsi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDeskripsi(model.getDeskripsi());
            }
        });
        Glide.with(context)
                .load(model.getFoto())
                .apply(
                        new RequestOptions()
                                .placeholder(R.color.pink_200)
                                .fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.foto);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        holder.harga.setText(formatRupiah.format(Double.parseDouble(model.getHarga())));

//      String id = adapter.getSnapshots().getSnapshot(position).getId();


        holder.beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Produk produk = new Produk(model.getNama_produk(), model.getFoto(), model.getHarga(), model.getDeskripsi(), 1);
                String singleProductt = gson.toJson(produk);
                final Produk singleProduct = gson.fromJson(singleProductt, Produk.class);

                //increase product count
                String productsFromCart = sharedPreference.retrieveProductFromCart();


                if (productsFromCart.equals("")) {
                    List<Produk> cartProductList = new ArrayList<Produk>();
                    cartProductList.add(singleProduct);
                    String cartValue = gson.toJson(cartProductList);
                    sharedPreference.addProductToTheCart(cartValue);
                    cartProductNumber = cartProductList.size();
                } else {
                    List<Produk> checkdata = checkdata(model,1);
                    String addAndStoreNewProduct = gson.toJson(checkdata);
                    sharedPreference.deleteProductFromCart();
                    sharedPreference.addProductToTheCart(addAndStoreNewProduct);
                    cartProductNumber = checkdata.size();
                }
                sharedPreference.addProductCount(cartProductNumber);
                invalidateCart();

                Toasty.info(context, model.getNama_produk() + " telah di tambahkan", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_produk, parent, false);
        return new ProdukAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView nama, harga, deskripsi;
        Button beli;
        ImageView foto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            nama = mView.findViewById(R.id.nama);
            harga = mView.findViewById(R.id.harga);
            foto = mView.findViewById(R.id.foto);
            beli = mView.findViewById(R.id.beli);
            deskripsi = mView.findViewById(R.id.deskripsi);


        }
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
                produks = new Produk(produk.getNama_produk(), produk.getFoto(), produk.getHarga(), produk.getDeskripsi(), (mProduct.get(d).getJumlah()+number));
                mProduct.remove(d);
                break;
            }
            d++;

        }

        mProduct.add(produks);


        return mProduct;
    }

//    private void showDialogDeskripsi(String deskripsi) {
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.dialog_produk_deskripsi);
//        TextView des = dialog.findViewById(R.id.deskripsi);
//
//        des.setText(deskripsi);
//
//        dialog.setCancelable(true);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.cancel();
//            }
//        });
//        dialog.show();
//    }



    public void showDialogDeskripsi(String deskripsi) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.dialog_produk_deskripsi, null);

        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(view);

        TextView des = dialog.findViewById(R.id.deskripsi);
        des.setText(deskripsi);

        dialog.show();

    }

    private void invalidateCart() {
        ((ProdukActivity) context).invalidateOptionsMenu();
    }



}
