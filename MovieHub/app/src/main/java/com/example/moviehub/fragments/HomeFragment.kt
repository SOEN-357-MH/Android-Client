package com.example.moviehub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviehub.R
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.models.MovieItem
import com.example.moviehub.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private var mainRecyclerAdapter : MainRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryList: MutableList<AllCategory> = ArrayList()

        observeTrendingMoviesByPage()

        //First Category
        val movieItemList: MutableList<MovieItem> = ArrayList()
        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList.add(MovieItem(1, R.drawable.placeholder_icon))


        //Second Category
        val movieItemList2: MutableList<MovieItem> = ArrayList()
        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList2.add(MovieItem(1, R.drawable.placeholder_icon))

        //Third Category
        val movieItemList3: MutableList<MovieItem> = ArrayList()
        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))
        movieItemList3.add(MovieItem(1, R.drawable.placeholder_icon))

        categoryList.add(AllCategory("TOP 10", movieItemList))
        categoryList.add(AllCategory("My Favs", movieItemList2))
        categoryList.add(AllCategory("ADULT (18+)", movieItemList3))

        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.homeRecyclerView.adapter = mainRecyclerAdapter

        viewModel.getTrendingMoviesByPage(1)


        return binding.root
    }

    private fun observeTrendingMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Success -> Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Loading -> {

                    }
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
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