package com.gerentec.manutencao.auxiliar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gerentec.manutencao.R;

import java.io.File;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    String pathfoto = BaseDados.getPathFotos();
    List<String> listFotos;
    private OnNoteListener mOnNoteListener;

    public RecyclerAdapter(List<String> listFotos,  OnNoteListener onNoteListener){
        this.listFotos = listFotos;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pictures, parent, false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view, mOnNoteListener);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String pathCompleto = BaseDados.getPathFotos() + listFotos.get(position);
        holder.pictureTextView.setText(listFotos.get(position));
        Glide.with(holder.itemView.getContext()).load(new File(pathCompleto)).into(holder.pictureImageView);
    }

    @Override
    public int getItemCount() {
        return listFotos.size();
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pictureImageView;
        TextView pictureTextView;
        OnNoteListener onNoteListener;

        public ImageViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            pictureImageView = itemView.findViewById(R.id.pictureImageView);
            pictureTextView = itemView.findViewById(R.id.pictureTextView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

}
