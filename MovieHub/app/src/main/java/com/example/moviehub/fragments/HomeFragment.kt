package com.example.moviehub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.moviehub.R
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.adapters.MovieItemAdapter
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.models.MediaBody
import com.example.moviehub.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber


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

        viewModel.getBaseImageUrl()
        viewModel.getMovieGenres()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.homeRecyclerView.adapter = mainRecyclerAdapter
        binding.homeRecyclerView.setHasFixedSize(true)


        return binding.root
    }

    private fun observeTrendingMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Success -> {
                        for (movie in viewModel.movieList) {
                            movie.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.poster_path}"
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
                            show.title = show.name
                        }
                        categoryList.add(AllCategory("Trending Shows", viewModel.showList))
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryList)
                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
                            postponeEnterTransition()
                        binding.homeRecyclerView.viewTreeObserver.addOnPreDrawListener {
                                startPostponedEnterTransition()
                                true
                            }
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
//                        viewModel.movieProvidersList[viewModel.movieProvidersList.size - 1]?.let {
//                            Timber.d("${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${viewModel.movieProvidersList[viewModel.movieProvidersList.size - 1]?.results?.CA!!.rent[0].logo_path}")
//                            binding.imageView.load( "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${viewModel.movieProvidersList[viewModel.movieProvidersList.size - 1]?.results?.CA!!.rent[0].logo_path}")
//                        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}