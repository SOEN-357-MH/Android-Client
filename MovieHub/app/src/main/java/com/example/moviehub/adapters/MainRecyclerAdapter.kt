package com.example.moviehub.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.databinding.MovieRecyclerRowItemBinding
import com.example.moviehub.models.AllCategory

class MainRecyclerAdapter(
    private val context: Context,
    private val allCategory: List<AllCategory>) :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    private val TAG = "MainAdapter"


    inner class MainViewHolder(val binding: MovieRecyclerRowItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(categoryItem: AllCategory){
            binding.mrCategoryTitle.text = categoryItem.categoryTitle
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerAdapter.MainViewHolder {
        return MainViewHolder(MovieRecyclerRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        //Called by the layout manager when it needs new data in an existing view


        val categoryItem = allCategory[position]


        holder.bind(categoryItem)
    }

    override fun getItemCount(): Int = allCategory.size



}