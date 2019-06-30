package com.darmajaya.joo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.darmajaya.joo.Model.Transaksi;
import com.darmajaya.joo.R;
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

    public HistoryAdapter(@NonNull FirestoreRecyclerOptions<Transaksi> options, Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull Transaksi model) {
        holder.nama.setText(model.getNama());
        holder.total.setText(model.getTotal());
        holder.tanggal.setText(model.getTanggal());
        holder.status.setText(model.getStatus());
        holder.idtransaksi.setText(getSnapshots().getSnapshot(i).getId());
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            nama = view.findViewById(R.id.nama);
            idtransaksi = view.findViewById(R.id.idtransaksi);
            total = view.findViewById(R.id.total);
            tanggal = view.findViewById(R.id.tanggal);
            status = view.findViewById(R.id.status);
        }
    }
}
