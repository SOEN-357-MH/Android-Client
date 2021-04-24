package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.databinding.LogosItemBinding
import com.example.moviehub.models.AdModel

class DetailsLogoAdapter(
    private val logos: ArrayList<AdModel>) : RecyclerView.Adapter<DetailsLogoAdapter.DetailsLogoViewHolder>() {


    inner class DetailsLogoViewHolder(val binding: LogosItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(logoItem: AdModel){
          binding.liLogo.load(logoItem.logo_path)
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailsLogoAdapter.DetailsLogoViewHolder {
        return DetailsLogoViewHolder(LogosItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: DetailsLogoAdapter.DetailsLogoViewHolder, position: Int) {
        val logoItem = logos[position]
        holder.bind(logoItem)
    }

    override fun getItemCount(): Int = logos.size


}