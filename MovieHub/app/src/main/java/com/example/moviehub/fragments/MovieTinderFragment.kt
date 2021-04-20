package com.example.moviehub.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.moviehub.R
import com.example.moviehub.adapters.MovieTinderAdapter
import com.example.moviehub.databinding.FragmentMovieTinderBinding
import com.example.moviehub.models.MediaBody
import com.example.moviehub.viewModels.SharedViewModel
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieTinderFragment : Fragment() {

    private var _binding: FragmentMovieTinderBinding? = null
    private val binding get() = _binding!!


    private var movieArray = mutableListOf<MediaBody>()
    private var arrayAdapter: MovieTinderAdapter? = null


    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("share ${sharedViewModel.movieList}")
        movieArray = sharedViewModel.movieList
        arrayAdapter = MovieTinderAdapter(requireContext(),R.layout.movie_tinder_item, movieArray)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieTinderBinding.inflate(inflater, container, false)

        //set the listener and the adapter
        binding.frame.adapter = arrayAdapter
        binding.frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                movieArray.removeAt(0)
                arrayAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                //TODO: Anything? Don't Show again?
                Toast.makeText(requireContext(), "Left!", Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(p0: Any?) {
                //TODO: Add to Watchlist
                Toast.makeText(requireContext(), "Right!", Toast.LENGTH_SHORT).show()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                // Ask for more data here
                movieArray.addAll(sharedViewModel.movieList)
                arrayAdapter?.notifyDataSetChanged()
            }

            override fun onScroll(p0: Float) {

            }

        })

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}