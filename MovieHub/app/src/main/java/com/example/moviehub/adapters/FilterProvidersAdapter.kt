package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.databinding.LogosItemBinding
import com.example.moviehub.databinding.ProvidersItemBinding
import com.example.moviehub.databinding.WatchListItemBinding
import com.example.moviehub.models.AdModel
import com.example.moviehub.models.MediaBody

class FilterProvidersAdapter(
    private val providers: ArrayList<AdModel>) : RecyclerView.Adapter<FilterProvidersAdapter.FilterProvidersViewHolder>() {


    inner class FilterProvidersViewHolder(val binding: ProvidersItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(providerItem: AdModel){
            binding.liLogo.load(providerItem.logo_path)
            binding.liName.text = providerItem.provider_name
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilterProvidersAdapter.FilterProvidersViewHolder {
        return FilterProvidersViewHolder(ProvidersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FilterProvidersAdapter.FilterProvidersViewHolder, position: Int) {
        val providerItem = providers[position]
        holder.bind(providerItem)
    }

    override fun getItemCount(): Int = providers.size

    private fun createOnClickListener(binding : WatchListItemBinding, providerItem: MediaBody): View.OnClickListener {
        return View.OnClickListener {

        }
    }


}