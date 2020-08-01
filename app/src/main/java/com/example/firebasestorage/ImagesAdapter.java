package com.example.firebasestorage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {

    private ArrayList<Model> listImages;
    private Context mcontext;

    public ImagesAdapter(ArrayList<Model> listImages, Context mcontext) {
        this.listImages = listImages;
        this.mcontext = mcontext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.ivItemImage);
            itemName = itemView.findViewById(R.id.tvItemName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(mcontext).inflate(R.layout.itemimage,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mcontext).load(listImages.get(position).getImageUrl()).centerCrop().placeholder(R.mipmap.img_demo).into(holder.itemImage);
        holder.itemName.setText(listImages.get(position).getImageUrl());

    }


    @Override
    public int getItemCount() {
        return listImages.size();
    }
}
