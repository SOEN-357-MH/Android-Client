package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.databinding.MovieRowItemsBinding
import com.example.moviehub.models.MediaBody

class MovieItemAdapter(
    private val movieItemList: ArrayList<MediaBody>) :
    RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>() {

    inner class MovieItemViewHolder(val binding: MovieRowItemsBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(movieItem: MediaBody){
            binding.mriMovieImage.load(movieItem.poster_path)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemAdapter.MovieItemViewHolder {
        return MovieItemViewHolder(MovieRowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        //Called by the layout manager when it needs new data in an existing view
        val movieItem = movieItemList[position]
        holder.bind(movieItem)
    }

    override fun getItemCount(): Int = movieItemList.size


}