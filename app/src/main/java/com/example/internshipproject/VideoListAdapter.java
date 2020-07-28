package com.example.internshipproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.internshipproject.databinding.ItemRecyclerViewBinding;
import com.example.internshipproject.entities.Film;
import com.example.internshipproject.ui.FilmInformationFragment;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {
    private List<Film> filmList;
    private Context context;
    public static int sSelectedHolderPosition = -1;
    public static String FILM_ID = "FILM_ID";

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
        } else {
            Log.e("Object is null", "onBindViewHolder: filmList is null");
        }
    }

    @Override
    public int getItemCount() {
        return filmList != null  && filmList.size() > 0 ? filmList.size() : 0;
    }

    public void setFilmList(List<Film> filmList) {
        this.filmList = filmList;
        notifyDataSetChanged();
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
            if (getAdapterPosition() != RecyclerView.NO_POSITION){
                FilmPlayer.cleanPlayer();
                FilmInformationFragment.VIDEO_PLAYER_IS_SHOWED = false;
                sSelectedHolderPosition = getAdapterPosition();
                Bundle bundle = new Bundle();
                bundle.putSerializable(FILM_ID, filmList.get(sSelectedHolderPosition).getImdbID());
                Navigation.findNavController(v).navigate(R.id.action_recyclerViewFragment2_to_filmInformationFragment, bundle);
            }
        }
    }
}