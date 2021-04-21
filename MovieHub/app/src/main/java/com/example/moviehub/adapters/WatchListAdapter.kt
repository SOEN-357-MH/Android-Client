package com.example.moviehub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.moviehub.databinding.WatchListItemBinding
import com.example.moviehub.models.MediaBody

class WatchListAdapter(
    private val watchList: ArrayList<MediaBody>
) :
    RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>() {

    inner class WatchListViewHolder(val binding: WatchListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(watchListItem: MediaBody) {
            var genres = "Genres: "
            for (item in watchListItem.genres) {
                genres = "$genres$item, "
            }

            binding.wlMovieTitle.text = watchListItem.title
            binding.wlMovieGenre.text = genres
            binding.wlImage.load(watchListItem.poster_path)

            if (watchListItem.adult) {
                binding.wlAdult.visibility = View.VISIBLE
            } else {
                binding.wlAdult.visibility = View.GONE
            }

            if (binding.checkBox.isChecked) {
                // What happens when checked
                binding.wlWatchedTV.visibility = View.VISIBLE
            } else {
                binding.wlWatchedTV.visibility = View.GONE
            }


        }
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
            )
        )
    }

    override fun onBindViewHolder(holder: WatchListAdapter.WatchListViewHolder, position: Int) {
        val watchListItem = watchList[position]
        holder.bind(watchListItem)
    }

    override fun getItemCount(): Int = watchList.size

}