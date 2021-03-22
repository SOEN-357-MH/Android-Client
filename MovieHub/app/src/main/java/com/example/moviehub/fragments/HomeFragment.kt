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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.adapters.MovieItemAdapter
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.models.MediaBody
import com.example.moviehub.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class HomeFragment : Fragment(), MovieItemAdapter.ClickMediaListener {

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
        observeImageSize()
        observeMovieGenres()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        viewModel.getBaseImageUrl()
        viewModel.getMovieGenres()

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        //binding.homeRecyclerView.adapter = mainRecyclerAdapter


        return binding.root
    }

    private fun observeTrendingMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Success -> {
                        for (movie in viewModel.movieList) {
                            movie.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSize}${movie.poster_path}"
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
                            show.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSize}${show.poster_path}"
                            show.title = show.name
                        }
                        categoryList.add(AllCategory("Trending Shows", viewModel.showList))
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(),this@HomeFragment , categoryList)
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
                        viewModel.getImageSize()
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

    private fun observeImageSize() {
        lifecycleScope.launchWhenStarted {
            viewModel.getImageSizeResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetImageSizeEvent.Success -> {
                        Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                        viewModel.getTrendingMoviesByPage(1)
                    }
                    is HomeViewModel.GetImageSizeEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetImageSizeEvent.Loading -> {

                    }
                    is HomeViewModel.GetImageSizeEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
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
                        Timber.d(viewModel.movieGenres?.genres.toString())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(mediaBody: MediaBody, mriMovieImage: ImageView) {

        val action = HomeFragmentDirections.actionHomeFragmentToMovieDetailsFragment(mediaBody)
        Navigation.findNavController(binding.root).navigate(action)

    }

}