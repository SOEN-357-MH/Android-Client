package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.databinding.MovieRowItemsBinding
import com.example.moviehub.models.MovieItem

class MovieItemAdapter(
    private val movieItemList: List<MovieItem>) :
    RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>() {

    inner class MovieItemViewHolder(val binding: MovieRowItemsBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(movieItem: MovieItem){
            binding.mriMovieImage.setImageResource(movieItem.imageUrl)
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