package com.example.internshipproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.internshipproject.databinding.ItemRecyclerViewBinding;
import com.example.internshipproject.entities.Film;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<Film> filmList;
    private Context context;

    public VideoListAdapter(List<Film> filmList) {
        this.filmList = filmList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ItemRecyclerViewBinding binding = ItemRecyclerViewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (filmList != null) {
            Film currentFilm = filmList.get(position);
            holder.binding.filmTitle.setText(currentFilm.getTitle());
            holder.binding.year.setText(currentFilm.getYear());
            Glide.
                    with(context).
                    load(currentFilm.getPoster())
                    .centerCrop()
                    .into(holder.binding.imageView);
            Log.d("+++++++++++++++", "onBindViewHolder: " + currentFilm.toString());
        } else {
            Log.e("Object is null", "onBindViewHolder: filmList is null");
        }

    }

    @Override
    public int getItemCount() {
        return filmList != null  && filmList.size() > 0 ? filmList.size() : 0;
        //return filmList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ItemRecyclerViewBinding binding;
        public ViewHolder(ItemRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
