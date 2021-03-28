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
import com.example.moviehub.databinding.FragmentMovieTinderBinding
import com.example.moviehub.viewModels.SharedViewModel
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MovieTinderFragment : Fragment() {

    private var _binding: FragmentMovieTinderBinding? = null
    private val binding get() = _binding!!


    private var al = ArrayList<String>()
    private var arrayAdapter: ArrayAdapter<String>? = null
    private var i = 0

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        al.add("php")
        al.add("c")
        al.add("python")
        al.add("java")

        Timber.d("share ${sharedViewModel.movieList}")


        //choose your favorite adapter
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.movie_tinder_item, R.id.mtMovieName, al)


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
                Log.d("LIST", "removed object!")
                al.removeAt(0)
                arrayAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(requireContext(), "Left!", Toast.LENGTH_SHORT).show()
            }

            override fun onRightCardExit(p0: Any?) {
                Toast.makeText(requireContext(), "Right!", Toast.LENGTH_SHORT).show()
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
                // Ask for more data here
                al.add("XML $i")
                arrayAdapter?.notifyDataSetChanged()
                Log.d("LIST", "notified")
                i++
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