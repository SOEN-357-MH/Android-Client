package com.example.moviehub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviehub.adapters.MainRecyclerAdapter
import com.example.moviehub.databinding.FragmentHomeBinding
import com.example.moviehub.models.AllCategory
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = "HomeFrag"
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val categoryList: MutableList<AllCategory> = ArrayList()
    private var mainRecyclerAdapter = MainRecyclerAdapter(categoryList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryList: MutableList<AllCategory> = ArrayList()

        categoryList.add(AllCategory("TOP 10"))
        categoryList.add(AllCategory("My Favs"))
        categoryList.add(AllCategory("ADULT (18+)"))
        mainRecyclerAdapter = MainRecyclerAdapter(categoryList)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.homeRecyclerView.adapter = mainRecyclerAdapter


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}