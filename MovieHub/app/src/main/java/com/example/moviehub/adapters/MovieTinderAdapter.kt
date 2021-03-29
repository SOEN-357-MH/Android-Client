package com.example.moviehub.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import coil.load
import com.example.moviehub.databinding.MovieTinderItemBinding
import com.example.moviehub.models.MediaBody


class MovieTinderAdapter(context: Context?, resourceId: Int, items: MutableList<MediaBody>?) : ArrayAdapter<MediaBody?>(context!!, resourceId,
    items!! as List<MediaBody?>
) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val binding : MovieTinderItemBinding = convertView?.tag as? MovieTinderItemBinding ?: MovieTinderItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)

        binding.mtMovieImage.load(getItem(position)?.poster_path)
        binding.root.tag = binding

        return binding.root

    }
}