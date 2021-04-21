package com.example.moviehub.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.moviehub.adapters.DetailsLogoAdapter
import com.example.moviehub.databinding.FragmentMovieDetailsBinding
import com.example.moviehub.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val args : MovieDetailsFragmentArgs by navArgs()

    private var logoAdapter = DetailsLogoAdapter(ArrayList())
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater,container, false)

        binding.mriMovieImage.load(args.mediaBody.poster_path)
        binding.backdropIMG.load(args.mediaBody.backdrop_path)
        binding.dTitle.text = args.mediaBody.title
        binding.dDesciption.text = args.mediaBody.overview

        if (args.mediaBody.adult){
            binding.dAdult.visibility = View.VISIBLE
        } else {
            binding.dAdult.visibility = View.GONE
        }

        var genres = "Genres: "
        for (item in args.mediaBody.genres){
            genres = "$genres$item, "
        }


        binding.dReleaseDate.text = "Release Date: ${args.mediaBody.release_date}"

        binding.dGenres.text = genres

        args.mediaBody.providers?.results?.let {
            it.CA?.flatrate?.let { providers ->
                logoAdapter = DetailsLogoAdapter(providers)
                binding.dLogosRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.dLogosRV.adapter = logoAdapter
            }
        } ?: fun() {
           binding.dNoStream.visibility = View.VISIBLE
        }()

        binding.watchlistButton.setOnClickListener{

            addMovieToWatchlist("Slayer42069", args.mediaBody.id)
            addShowToWatchlist("Slayer42069", args.mediaBody.id)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.mriMovieImage, "img_${args.mediaBody.poster_path}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addMovieToWatchlist(username: String, movieID: Int){
        viewModel.addMovieToWatchlist(username, movieID)
    }

    private fun addShowToWatchlist(username: String, showID: Int){
        viewModel.addShowToWatchlist(username, showID)
    }

}