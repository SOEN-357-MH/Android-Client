package com.example.moviehub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.databinding.MovieRecyclerRowItemBinding
import com.example.moviehub.models.AllCategory


class MainRecyclerAdapter(
    private val context: Context,
    private val clickMediaListener: MovieItemAdapter.ClickMediaListener,
    private val allCategory: ArrayList<AllCategory>) :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    inner class MainViewHolder(val binding: MovieRecyclerRowItemBinding, private val clickMediaListener: MovieItemAdapter.ClickMediaListener): RecyclerView.ViewHolder(binding.root){

        fun bind(categoryItem: AllCategory){
            binding.mrCategoryTitle.text = categoryItem.categoryTitle
            binding.mrMovieRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            binding.mrMovieRecyclerView.adapter = MovieItemAdapter(categoryItem.movieItems, clickMediaListener)
            binding.mrMovieRecyclerView.hasFixedSize()
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerAdapter.MainViewHolder {
        return MainViewHolder(MovieRecyclerRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickMediaListener)
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        //Called by the layout manager when it needs new data in an existing view
        val categoryItem = allCategory[position]
        holder.bind(categoryItem)
    }

    override fun getItemCount(): Int = allCategory.size


}