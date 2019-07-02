package com.darmajaya.joo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.darmajaya.joo.Model.Produk;
import com.darmajaya.joo.R;

import java.util.List;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.MyViewHolder> {

    private Context context;
    private List<Produk> produkList;

    public HistoryDetailAdapter(Context context, List<Produk> produkList) {
        this.context = context;
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public HistoryDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_detail, parent, false);
        return new HistoryDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailAdapter.MyViewHolder holder, int position) {
        Produk produk = produkList.get(position);

        holder.nama.setText(produk.getNama_produk());
        holder.harga.setText(produk.getHarga());
        holder.jumlah.setText("Jumlah: "+ produk.getJumlah());
        Glide.with(context)
                .load(produk.getFoto())
                .apply(
                        new RequestOptions()
                                .placeholder(R.color.pink_200)
                                .fitCenter())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.gambar);
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView nama, harga, jumlah;
        ImageView gambar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            nama = mView.findViewById(R.id.nama);
            harga = mView.findViewById(R.id.harga);
            gambar = mView.findViewById(R.id.gambar);
            jumlah = mView.findViewById(R.id.jumlah);


        }
    }
}
