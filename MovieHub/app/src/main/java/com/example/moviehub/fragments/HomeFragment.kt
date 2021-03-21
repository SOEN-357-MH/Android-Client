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
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var mainRecyclerAdapter : MainRecyclerAdapter? = null
    private val categoryList: ArrayList<AllCategory> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeTrendingMoviesByPage()
        observeBaseImageUrl()
        observeImageSize()

//        //First Category
//        val movieItemList: MutableList<MovieItem> = ArrayList()
//        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
//
//
//        //Second Category
//        val movieItemList2: MutableList<MovieItem> = ArrayList()
//        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
//
//        //Third Category
//        val movieItemList3: MutableList<MovieItem> = ArrayList()
//        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))
//        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        viewModel.getBaseImageUrl()

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.homeRecyclerView.adapter = mainRecyclerAdapter


        return binding.root
    }

    private fun observeTrendingMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Success -> {
                        for(movie in viewModel.movieList!!){
                            movie.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSize}${movie.poster_path}"
                        }
                        categoryList.add(AllCategory("TOP 10", viewModel.movieList!!))
                        categoryList.add(AllCategory("My Favs", viewModel.movieList!!))
                        categoryList.add(AllCategory("ADULT (18+)", viewModel.movieList!!))

                        Timber.d("catergory:${categoryList.toString()}")
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryList)
                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}