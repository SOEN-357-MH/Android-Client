package com.example.moviehub.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.moviehub.adapters.DetailsLogoAdapter
import com.example.moviehub.databinding.FragmentMovieDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val args : MovieDetailsFragmentArgs by navArgs()

    private var logoAdapter = DetailsLogoAdapter(ArrayList())

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

        args.mediaBody.providers?.results?.let {
            it.CA?.flatrate?.let { providers ->
                logoAdapter = DetailsLogoAdapter(args.mediaBody.providers?.results?.CA?.flatrate!!)
                binding.dLogosRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                binding.dLogosRV.adapter = logoAdapter
            }
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

}