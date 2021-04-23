package com.example.moviehub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.databinding.WatchListItemBinding
import com.example.moviehub.models.MediaBody


class WatchListAdapter(
    private val context: Context,
    private val watchList: ArrayList<MediaBody>,
    private val onCheckedListener: OnCheckedListener) :
    RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>() {

    private var logoAdapter = DetailsLogoAdapter(ArrayList())
    private var selectedPosition = -1


    inner class WatchListViewHolder(val binding: WatchListItemBinding, private val onCheckedListener: OnCheckedListener) :
        RecyclerView.ViewHolder(binding.root), CompoundButton.OnCheckedChangeListener {

        fun bind(watchListItem: MediaBody) {

            var genres = "Genres: "
            if (watchListItem != null && watchListItem.genres != null) {
                for (item in watchListItem.genres) {
                    genres = "$genres$item, "
                }
            }
            binding.wlMovieTitle.text = if (watchListItem.title.isNullOrEmpty()) watchListItem.name else watchListItem.title
            binding.wlMovieGenre.text = genres
            binding.wlImage.load(watchListItem.poster_path)

            watchListItem.providers?.results?.let {
                it.CA?.flatrate?.let { providers ->
                    logoAdapter = DetailsLogoAdapter(providers)
                    binding.wlLogoRV.layoutManager = LinearLayoutManager(
                        context,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.wlLogoRV.adapter = logoAdapter
                }
            }

            if (watchListItem.adult) {
                binding.wlAdult.visibility = View.VISIBLE
            } else {
                binding.wlAdult.visibility = View.GONE
            }


            if (binding.wlCheckBox.isChecked) {
                // What happens when checked
                binding.wlWatchedTV.visibility = View.VISIBLE
            } else {
                binding.wlWatchedTV.visibility = View.GONE
            }


        }

        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            binding.wlCheckBox.isClickable = false
            selectedPosition = layoutPosition
            onCheckedListener.onCheck(selectedPosition, isChecked)
        }
    }

    interface OnCheckedListener{
        fun onCheck(position: Int, isChecked: Boolean)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchListAdapter.WatchListViewHolder {
        return WatchListViewHolder(
            WatchListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ), onCheckedListener
        )

    }

    override fun onBindViewHolder(holder: WatchListAdapter.WatchListViewHolder, position: Int) {
        val watchListItem = watchList[position]
        holder.bind(watchListItem)

    }

    override fun getItemCount(): Int = watchList.size

}