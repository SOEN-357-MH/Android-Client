package com.example.moviehub.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviehub.adapters.DetailsLogoAdapter
import com.example.moviehub.adapters.FilterProvidersAdapter
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import com.example.moviehub.models.LoginBody
import com.example.moviehub.models.MediaBody
import com.example.moviehub.viewModels.HomeViewModel
import com.example.moviehub.viewModels.SharedViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.lang.Thread.sleep


@AndroidEntryPoint
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var mainRecyclerAdapter : MainRecyclerAdapter? = null
    private var filterAdapter : FilterProvidersAdapter? = null
    private val categoryMovieList: ArrayList<AllCategory> = ArrayList()
    private val categoryShowList: ArrayList<AllCategory> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeTrendingMoviesByPage()
        observeGenreShowsByPage()
        observeGenreMoviesByPage()

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
        binding.homeTabLayout.getTabAt(viewModel.selectedTab)?.select()
        binding.filterview.visibility = View.GONE

        filterAdapter = FilterProvidersAdapter(viewModel.providers)
        Toast.makeText(requireContext(), viewModel.providers.size.toString(), Toast.LENGTH_SHORT).show()

        binding.FiltersRV.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.FiltersRV.adapter = filterAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.text) {
                    "Movies" -> {
                        Timber.d("movie selected")
                        viewModel.selectedTab = 0
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryMovieList)
                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
                    }
                    "TV Shows" -> {
                        viewModel.selectedTab = 1
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryShowList)
                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
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

        binding.filterButton.setOnClickListener{

            binding.filterview.visibility = if (binding.filterview.visibility == View.VISIBLE) View.GONE else View.VISIBLE

        }

    }

    private fun observeTrendingMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getTrendingMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetTrendingMoviesByPageEvent.Success -> {
                        sharedViewModel.movieList = viewModel.movieList
                        for (movie in viewModel.movieList) {
                            movie.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.poster_path}"
                            movie.backdrop_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.backdrop_path}"
                            movie.media_type = "m"
                            viewModel.getMovieProviders(movie.id)
                        }
                        categoryMovieList.add(AllCategory("Trending Movies", viewModel.movieList))
                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryMovieList)
                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
                        viewModel.getTrendingShowsByPage(15)
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

    private fun observeGenreMoviesByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getGenreMoviesByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetGenreMoviesByPageEvent.Success -> {

                        if (viewModel.genresLoadedMovies == 15) {

                            sharedViewModel.listOfGenresMovies = viewModel.listOfGenresMovies

                            for ((key, value) in viewModel.listOfGenresMovies) {
                                //FOR EVERY GENRE IN LIST OF GENRES
                                for (movie in value) {
                                    //FOR EVERY MOVIE IN THAT GENRE
                                    movie.poster_path =
                                        "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.poster_path}"
                                    movie.backdrop_path =
                                        "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${movie.backdrop_path}"
                                    viewModel.getMovieProviders(movie.id)
                                        movie.media_type = "m"
                                }

                                //TODO:Need to map GENRE ID codes to Strings
                                categoryMovieList.add(
                                    AllCategory(
                                        viewModel.movieGenreMap[key]!!,
                                        value as ArrayList<MediaBody>
                                    )
                                )
                            }

                            mainRecyclerAdapter =
                                MainRecyclerAdapter(requireContext(), categoryMovieList)
                            binding.homeRecyclerView.adapter = mainRecyclerAdapter
                            //viewModel.getGenreShowsByPage(1)
                            Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    is HomeViewModel.GetGenreMoviesByPageEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetGenreMoviesByPageEvent.Loading -> {

                    }
                    is HomeViewModel.GetGenreMoviesByPageEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
                    else -> Unit
                }
            }
        }
    }

    private fun observeGenreShowsByPage(){
        lifecycleScope.launchWhenStarted {
            viewModel.getGenreShowsByPageResponse.collect { event ->
                when(event){
                    is HomeViewModel.GetGenreShowsByPageEvent.Success -> {

                        if (viewModel.genresLoadedShows == 15) {

                            sharedViewModel.listOfGenresShows = viewModel.listOfGenresShows

                            for ((key, value) in viewModel.listOfGenresShows) {
                                //FOR EVERY GENRE IN LIST OF GENRES
                                for (show in value) {
                                    //FOR EVERY SHOW IN THAT GENRE
                                    show.poster_path =
                                        "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${show.poster_path}"
                                    show.backdrop_path =
                                        "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${show.backdrop_path}"
                                    viewModel.getShowProviders(show.id)
                                    show.media_type = "s"
                                }

                                //TODO:Need to map GENRE ID codes to Strings
                                categoryShowList.add(
                                    AllCategory(
                                        viewModel.showGenreMap[key]!!,
                                        value as ArrayList<MediaBody>
                                    )
                                )
                            }

                            mainRecyclerAdapter =
                                MainRecyclerAdapter(requireContext(), categoryShowList)
                            binding.homeRecyclerView.adapter = mainRecyclerAdapter
                            //viewModel.getGenreShowsByPage(1)
                            Toast.makeText(requireContext(), event.resultText, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    is HomeViewModel.GetGenreShowsByPageEvent.Failure -> Toast.makeText(requireContext(), event.errorText, Toast.LENGTH_SHORT).show()
                    is HomeViewModel.GetGenreShowsByPageEvent.Loading -> {

                    }
                    is HomeViewModel.GetGenreShowsByPageEvent.Exception -> Toast.makeText(requireContext(), event.exceptionText, Toast.LENGTH_SHORT).show()
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
                        sharedViewModel.showList = viewModel.showList
                        for(show in viewModel.showList){
                            show.poster_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${show.poster_path}"
                            show.backdrop_path = "${viewModel.baseImageUrl}${viewModel.imageSizes[6]}${show.backdrop_path}"
                            show.title = show.name
                            show.release_date = show.first_air_date
                                show.media_type = "s"
                            viewModel.getShowProviders(show.id)
                        }
                        categoryShowList.add(AllCategory("Trending Shows", viewModel.showList))
//                        mainRecyclerAdapter = MainRecyclerAdapter(requireContext(), categoryShowList)
//                        binding.homeRecyclerView.adapter = mainRecyclerAdapter
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
                        viewModel.getTrendingMoviesByPage(15)

                        viewModel.getGenreShowsByPage(10, "10759")
                        viewModel.getGenreShowsByPage(11, "10759")
                        viewModel.getGenreShowsByPage(12, "10759")

                        viewModel.getGenreShowsByPage(10, "16")
                        viewModel.getGenreShowsByPage(11, "16")
                        viewModel.getGenreShowsByPage(12, "16")

                        viewModel.getGenreShowsByPage(10, "35")
                        viewModel.getGenreShowsByPage(11, "35")
                        viewModel.getGenreShowsByPage(12, "35")

                        viewModel.getGenreShowsByPage(10, "80")
                        viewModel.getGenreShowsByPage(11, "80")
                        viewModel.getGenreShowsByPage(12, "80")

                        viewModel.getGenreShowsByPage(10, "99")
                        viewModel.getGenreShowsByPage(11, "99")
                        viewModel.getGenreShowsByPage(12, "99")

                        viewModel.getGenreMoviesByPage(10, "35")
                        viewModel.getGenreMoviesByPage(11, "35")
                        viewModel.getGenreMoviesByPage(12, "35")

                        viewModel.getGenreMoviesByPage(10, "28")
                        viewModel.getGenreMoviesByPage(11, "28")
                        viewModel.getGenreMoviesByPage(12, "28")

                        viewModel.getGenreMoviesByPage(10, "12")
                        viewModel.getGenreMoviesByPage(11, "12")
                        viewModel.getGenreMoviesByPage(12, "12")

                        viewModel.getGenreMoviesByPage(10, "16")
                        viewModel.getGenreMoviesByPage(11, "16")
                        viewModel.getGenreMoviesByPage(12, "16")

                        viewModel.getGenreMoviesByPage(10, "80")
                        viewModel.getGenreMoviesByPage(11, "80")
                        viewModel.getGenreMoviesByPage(12, "80")


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