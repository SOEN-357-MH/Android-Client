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
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.viewModels.HomeViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var mainRecyclerAdapter : MainRecyclerAdapter? = null
    private val categoryList: ArrayList<AllCategory> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeTrendingMoviesByPage()
        observeTrendingShowsByPage()
        observeBaseImageUrl()
        observeImageSizes()
        observeMovieGenres()
        observeMovieProviders()
        observeShowProviders()
        observeDiscoverMovie()

        viewModel.getBaseImageUrl()
        viewModel.getMovieGenres()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Movies" -> {
                        //TODO : Put Movies
                    }
                    "TV Shows" -> {
                        //TODO : Put TV Shows
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

            mainRecyclerAdapter?.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.homeRecyclerView.adapter = mainRecyclerAdapter



        postponeEnterTransition()

        binding.homeRecyclerView.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }


    }

    private fun observeTrendingMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Success -> {
                        for (movie in viewModel.movieList) {
                            movie.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.poster_path}"
                            movie.backdrop_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.backdrop_path}"
                            viewModel.getMovieProviders(movie.id)
                        }
                        categoryList.add(AllCategory("Trending Movies", viewModel.movieList))
                        viewModel.getTrendingShowsByPage(1)
                        Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                    }
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Loading -> {

                    }
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeTrendingShowsByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingShowsByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingShowsByPageEvent.Success -> {
                        for(show in viewModel.showList){
                            show.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${show.poster_path}"
                            show.backdrop_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${show.backdrop_path}"
                            show.title = show.name
                            show.release_date = show.first_air_date
                            viewModel.getShowProviders(show.id)
                        }
                        categoryList.add(AllCategory("Trending Shows", viewModel.showList))
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryList)
                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
                        Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                    }
                    is HomeViewModel.GetTrendingShowsByPageEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetTrendingShowsByPageEvent.Loading -> {

                    }
                    is HomeViewModel.GetTrendingShowsByPageEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeBaseImageUrl() {
        lifecycleScope.launchWhenStarted {
            viewModel.getBaseImageUrlResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetBaseImageUrlEvent.Success -> {
                        Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                        viewModel.getImageSizes()
                    }
                    is HomeViewModel.GetBaseImageUrlEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetBaseImageUrlEvent.Loading -> {

                    }
                    is HomeViewModel.GetBaseImageUrlEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeImageSizes() {
        lifecycleScope.launchWhenStarted {
            viewModel.getImageSizeResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetImageSizesEvent.Success -> {
                        Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                        viewModel.getTrendingMoviesByPage(1)
                    }
                    is HomeViewModel.GetImageSizesEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetImageSizesEvent.Loading -> {

                    }
                    is HomeViewModel.GetImageSizesEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeMovieGenres(){
        lifecycleScope.launchWhenStarted {
            viewModel.getMovieGenresResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetMovieGenresEvent.Success -> {
                        Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                    }
                    is HomeViewModel.GetMovieGenresEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetMovieGenresEvent.Loading -> {

                    }
                    is HomeViewModel.GetMovieGenresEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeMovieProviders() {
        lifecycleScope.launchWhenStarted {
            viewModel.getMovieProvidersResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetMovieProvidersEvent.Success -> {

                    }
                    is HomeViewModel.GetMovieProvidersEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetMovieProvidersEvent.Loading -> {

                    }
                    is HomeViewModel.GetMovieProvidersEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }
    private fun observeShowProviders() {
        lifecycleScope.launchWhenStarted {
            viewModel.getShowProvidersResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetShowProvidersEvent.Success -> {

                    }
                    is HomeViewModel.GetShowProvidersEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetShowProvidersEvent.Loading -> {

                    }
                    is HomeViewModel.GetShowProvidersEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeDiscoverMovie(){
        lifecycleScope.launchWhenStarted {
            viewModel.discoverMovieResponse.collect { event ->
                when(event){
                    is HomeViewModel.DiscoverMovieEvent.Success -> {

                    }
                    is HomeViewModel.DiscoverMovieEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.DiscoverMovieEvent.Loading -> {

                    }
                    is HomeViewModel.DiscoverMovieEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}