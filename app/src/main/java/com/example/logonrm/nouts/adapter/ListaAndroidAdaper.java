package com.example.logonrm.nouts.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.logonrm.nouts.R;
import com.example.logonrm.nouts.dao.Notes;

import java.util.List;

public class ListaAndroidAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<Notes> notes;

    public ListaAndroidAdaper(Context context, List<Notes> notes){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_list_row, parent, false);
        return new AndroidItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        AndroidItemHolder androidItemHolder = (AndroidItemHolder) holder;
        androidItemHolder.tvCategoria.setText(notes.get(position).getCategoria().getNome());
        androidItemHolder.tvDescricao.setText(notes.get(position).getDescricao());
        androidItemHolder.id.setText(String.valueOf(notes.get(position).getId()));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private class AndroidItemHolder extends RecyclerView.ViewHolder{

        TextView tvCategoria, tvDescricao, id;

        public AndroidItemHolder(View itemView) {
            super(itemView);

            id = (TextView) itemView.findViewById(R.id.id);
            tvCategoria = (TextView) itemView.findViewById(R.id.tvCategoria);
            tvDescricao = (TextView) itemView.findViewById(R.id.tvDescricao);

        }
    }

}
