package com.ksusha.everneticaapi.base.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ksusha.everneticaapi.R;
import com.ksusha.everneticaapi.base.SetWallpaperActivity;
import com.ksusha.everneticaapi.feature.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private ArrayList<ImageModel> wallpaperList;

    public Adapter(Context context, ArrayList<ImageModel> wallpaperList) {
        this.context = context;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false); //!!!
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Glide
                .with(context)
                .load(wallpaperList
                .get(position)
                .getUrls()
                .getRegular())
                .into(holder.imageView);
        holder.imageView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SetWallpaperActivity.class);
            intent.putExtra("image", wallpaperList.get(position).getUrls().getRegular());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            v.getContext().startActivity(intent);
        });
    }

    public void updateAdapter(List<ImageModel> newList) { //обновление адаптера
        wallpaperList.clear(); //очищение старого списка
        wallpaperList.addAll(newList); //добавление новых элементов списка
        notifyDataSetChanged(); //сообщение адаптеру об обновлении
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

    }
}
