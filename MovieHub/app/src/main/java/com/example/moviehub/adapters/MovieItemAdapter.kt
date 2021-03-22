package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.databinding.MovieRowItemsBinding
import com.example.moviehub.models.MediaBody

class MovieItemAdapter(
    private val movieItemList: ArrayList<MediaBody>,
    private val clickMediaListener: ClickMediaListener
) : RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>() {

    interface ClickMediaListener {
        fun onClick(mediaBody: MediaBody, mriMovieImage: ImageView)
    }

    inner class MovieItemViewHolder(
        val binding: MovieRowItemsBinding,
        private val clickMediaListener: ClickMediaListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(movieItem: MediaBody){
            binding.mriMovieImage.load(movieItem.poster_path)
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            clickMediaListener.onClick(movieItemList[adapterPosition], binding.mriMovieImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemAdapter.MovieItemViewHolder {
        return MovieItemViewHolder(MovieRowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickMediaListener)
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        //Called by the layout manager when it needs new data in an existing view
        val movieItem = movieItemList[position]
        holder.bind(movieItem)
    }

    override fun getItemCount(): Int = movieItemList.size


}