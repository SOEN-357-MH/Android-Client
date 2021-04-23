package com.example.moviehub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.moviehub.R
import com.example.moviehub.adapters.MovieTinderAdapter
import com.example.moviehub.databinding.FragmentMovieTinderBinding
import com.example.moviehub.models.MediaBody
import com.example.moviehub.viewModels.HomeViewModel
import com.example.moviehub.viewModels.SharedViewModel
import com.google.android.material.tabs.TabLayout
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieTinderFragment : Fragment(){

    private var _binding: FragmentMovieTinderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var mediaArray = mutableListOf<MediaBody>()

    private var movieArray = mutableListOf<MediaBody>()
    private var arrayAdapter: MovieTinderAdapter? = null

    private var showArray = mutableListOf<MediaBody>()
    private var showArrayAdapter: MovieTinderAdapter? = null

    lateinit var lastItem: MediaBody

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d("share ${sharedViewModel.movieList}")
        movieArray = sharedViewModel.movieList
        showArray = sharedViewModel.showList
//        mediaArray.addAll(movieArray)
        arrayAdapter = MovieTinderAdapter(requireContext(),R.layout.movie_tinder_item, movieArray)

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieTinderBinding.inflate(inflater, container, false)

        //set the listener and the adapter

        binding.TinderTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Movies" -> {
                        Timber.d("movie selected")
                        viewModel.selectedTab = 0
                        Toast.makeText(requireContext(), "You Hit Movies Tab!", Toast.LENGTH_SHORT).show()

                        arrayAdapter = MovieTinderAdapter(requireContext(),R.layout.movie_tinder_item, movieArray)
                        binding.frame.adapter = arrayAdapter
                        binding.frame.topCardListener.selectLeft()

                    }
                    "TV Shows" -> {
                        viewModel.selectedTab = 1
                        Toast.makeText(requireContext(), "You Hit TV Shows Tab!", Toast.LENGTH_SHORT).show()

                        arrayAdapter = MovieTinderAdapter(requireContext(),R.layout.movie_tinder_item, showArray)
                        binding.frame.adapter = arrayAdapter
                        binding.frame.topCardListener.selectLeft()

                    }
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        //Start adapter as movie adapter
        binding.frame.adapter = arrayAdapter
//        lastItem = mediaArray[0]

        binding.mtLikeBTN.setOnClickListener{
            binding.frame.topCardListener.selectRight()
        }

        binding.mtDislikeBTN.setOnClickListener{
            binding.frame.topCardListener.selectLeft()
        }

        binding.frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {


            override fun removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
//                    lastItem = mediaArray.removeAt(0)
//                    arrayAdapter?.notifyDataSetChanged()

                    if (viewModel.selectedTab == 0){
                        lastItem = movieArray.removeAt(0)
                        arrayAdapter?.notifyDataSetChanged()
                    } else {
                        lastItem = showArray.removeAt(0)
                        arrayAdapter?.notifyDataSetChanged()
                    }

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
                if (viewModel.selectedTab == 0) {
                    viewModel.addMovieToWatchlist("Slayer42069", lastItem.id)
                } else {
                    viewModel.addShowToWatchlist("Slayer42069", lastItem.id)
                }
//
//                if (mediaArray[0].media_type == "m") {
//                    viewModel.addMovieToWatchlist("Slayer42069", lastItem.id)
//                } else {
//                    viewModel.addShowToWatchlist("Slayer42069", lastItem.id)
//                }



            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                // Ask for more data here
                //movieArray.addAll(sharedViewModel.movieList)
                //movieArrayAdapter?.notifyDataSetChanged()
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