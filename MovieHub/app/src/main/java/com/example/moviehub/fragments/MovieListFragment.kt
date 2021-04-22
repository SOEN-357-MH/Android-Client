package com.example.moviehub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.adapters.WatchListAdapter
import com.example.moviehub.databinding.FragmentMovieListBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.models.MediaBody
import com.example.moviehub.viewModels.HomeViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private var watchListAdapter: WatchListAdapter? = null

    private var movieWatchlist: ArrayList<MediaBody> = ArrayList()
    private var showWatchlist: ArrayList<MediaBody> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getImageSizes()
        viewModel.getBaseImageUrl()
        observeMovieWatchList()
        observeShowWatchList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)

        binding.mlRecyclerView.layoutManager = LinearLayoutManager(activity)

        binding.tabLayout.getTabAt(viewModel.selectedTab)?.select()

        viewModel.getMovieWatchlist("Slayer42069")
        viewModel.getShowWatchlist("Slayer42069")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Movies" -> {
                        Timber.d("movie selected")
                        viewModel.selectedTab = 0
                        movieWatchlist = viewModel.movieWatchList
                        watchListAdapter = WatchListAdapter(requireContext(), movieWatchlist)
                        binding.mlRecyclerView.adapter = watchListAdapter
                    }
                    "TV Shows" -> {
                        viewModel.selectedTab = 1
                        showWatchlist = viewModel.showWatchList
                        watchListAdapter = WatchListAdapter(requireContext(), showWatchlist)
                        binding.mlRecyclerView.adapter = watchListAdapter
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.mlRecyclerView.adapter = watchListAdapter

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeMovieWatchList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getMovieWatchlistResponse.collect { event ->
                when (event) {
                    is HomeViewModel.GetMovieWatchlistEvent.Success -> {
                            for (movie in viewModel.movieWatchList) {
                                movie.poster_path =
                                    "${viewModel.baseImageUrl}original${movie.poster_path}"
                                movie.backdrop_path =
                                    "${viewModel.baseImageUrl}original${movie.backdrop_path}"
                                viewModel.getMovieProviders(movie.id)
                            }
                        watchListAdapter =
                            WatchListAdapter(requireContext(), viewModel.movieWatchList)
                        binding.mlRecyclerView.adapter = watchListAdapter

                    }
                    is HomeViewModel.GetMovieWatchlistEvent.Failure -> Toast.makeText(
                        requireContext(),
                        event.errorText,
                        Toast.LENGTH_SHORT
                    ).show()
                    is HomeViewModel.GetMovieWatchlistEvent.Loading -> {

                    }
                    is HomeViewModel.GetMovieWatchlistEvent.Exception -> Toast.makeText(
                        requireContext(),
                        event.exceptionText,
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeShowWatchList() {
        lifecycleScope.launchWhenStarted {
            viewModel.getShowWatchlistResponse.collect { event ->
                when (event) {
                    is HomeViewModel.GetShowWatchlistEvent.Success -> {
                            for (show in viewModel.showWatchList) {
                                show.poster_path =
                                    "${viewModel.baseImageUrl}original${show.poster_path}"
                                show.backdrop_path =
                                    "${viewModel.baseImageUrl}original${show.backdrop_path}"
                                viewModel.getShowProviders(show.id)
                            }

                        watchListAdapter =
                            WatchListAdapter(requireContext(), viewModel.showWatchList)
                        binding.mlRecyclerView.adapter = watchListAdapter

                    }
                    is HomeViewModel.GetShowWatchlistEvent.Failure -> Toast.makeText(
                        requireContext(),
                        event.errorText,
                        Toast.LENGTH_SHORT
                    ).show()
                    is HomeViewModel.GetShowWatchlistEvent.Loading -> {

                    }
                    is HomeViewModel.GetShowWatchlistEvent.Exception -> Toast.makeText(
                        requireContext(),
                        event.exceptionText,
                        Toast.LENGTH_SHORT
                    ).show()
                    else -> Unit
                }
            }
        }
    }

}