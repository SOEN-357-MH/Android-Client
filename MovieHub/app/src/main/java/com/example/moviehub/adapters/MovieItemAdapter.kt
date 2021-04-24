package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.R
import com.example.moviehub.databinding.MovieRowItemsBinding
import com.example.moviehub.fragments.HomeFragmentDirections
import com.example.moviehub.models.MediaBody

class MovieItemAdapter(
    private val movieItemList: ArrayList<MediaBody>

) : RecyclerView.Adapter<MovieItemAdapter.MovieItemViewHolder>() {

    inner class MovieItemViewHolder(
        val binding: MovieRowItemsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, movieItem: MediaBody){
            ViewCompat.setTransitionName(binding.mriMovieImage, "img_${movieItem.poster_path}")
            binding.mriMovieImage.load(movieItem.poster_path)
            binding.root.setOnClickListener(listener)
        }

//        override fun onClick(v: View?) {
//            val extras = FragmentNavigatorExtras(binding.mriMovieImage to "rv_movie_item")
//            clickMediaListener.onClick(movieItemList[adapterPosition])
//
//        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieItemAdapter.MovieItemViewHolder {
        return MovieItemViewHolder(MovieRowItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        //Called by the layout manager when it needs new data in an existing view
        val movieItem = movieItemList[position]
        holder.bind(createOnClickListener(holder.binding, movieItem), movieItem)
    }

    override fun getItemCount(): Int = movieItemList.size

    private fun createOnClickListener(binding : MovieRowItemsBinding, movieItem: MediaBody): View.OnClickListener {
        return View.OnClickListener {
            val directions = HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(movieItem)
            val extras = FragmentNavigatorExtras(
                binding.mriMovieImage to "img_${movieItem.poster_path}")
            it.findNavController().navigate(directions, extras)
        }
    }

}