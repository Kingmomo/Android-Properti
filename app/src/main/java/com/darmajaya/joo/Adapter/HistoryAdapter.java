package com.darmajaya.joo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.darmajaya.joo.HistoryActivity;
import com.darmajaya.joo.HistoryDetailFragment;
import com.darmajaya.joo.KonfirmasiActivity;
import com.darmajaya.joo.Model.Transaksi;
import com.darmajaya.joo.R;
import com.darmajaya.joo.utils.MySharedPreference;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HistoryAdapter extends FirestoreRecyclerAdapter<Transaksi, HistoryAdapter.MyViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    private Context context;
    private MySharedPreference mySharedPreference;


    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<Transaksi> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int i, @NonNull final Transaksi model) {
        mySharedPreference = new MySharedPreference(context);
        holder.nama.setText(model.getNama());
        holder.total.setText(model.getTotal());
        holder.tanggal.setText(model.getTanggal());
        holder.status.setText(model.getStatus());
        holder.idtransaksi.setText(getSnapshots().getSnapshot(i).getId());
        holder.hubungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialup(v);
            }
        });
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryDetailFragment fragment = new HistoryDetailFragment();
                Bundle args = new Bundle();
                args.putString("total", model.getTotal());
                args.putString("listproduk", model.getListproduk());
                fragment.setArguments(args);
                FragmentTransaction transaction = ((HistoryActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

            }
        });
        if (model.getStatus().equals("Belum Di Konfirmasi"))   {
            holder.konfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, KonfirmasiActivity.class);
                    intent.putExtra("konfirmasi", getSnapshots().getSnapshot(i).getId());
                    context.startActivity(intent);
                }
            });
        }

    }

    private void dialup(View view) {
        Intent callIntent = new Intent(Intent.ACTION_VIEW);
        callIntent.setData(Uri.parse("tel:" + mySharedPreference.getNotelp()));
        context.startActivity(callIntent);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama, idtransaksi, total, tanggal, status;
        View view;
        ImageView detail;
        Button hubungi, konfirmasi;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            detail = view.findViewById(R.id.detail);
            nama = view.findViewById(R.id.nama);
            idtransaksi = view.findViewById(R.id.idtransaksi);
            total = view.findViewById(R.id.total);
            tanggal = view.findViewById(R.id.tanggal);
            status = view.findViewById(R.id.status);
            hubungi = view.findViewById(R.id.hubungi);
            konfirmasi = view.findViewById(R.id.konfirmasi);
        }
    }

}
