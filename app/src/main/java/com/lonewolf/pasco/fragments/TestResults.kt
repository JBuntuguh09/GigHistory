package com.lonewolf.pasco.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.adaptors.TestAdaptor
import com.lonewolf.pasco.databinding.FragmentTestBinding
import com.lonewolf.pasco.databinding.FragmentTestResultsBinding
import com.lonewolf.pasco.resources.Storage

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TestResults.newInstance] factory method to
 * create an instance of this fragment.
 */
class TestResults : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentTestResultsBinding
    private lateinit var storage: Storage
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_test_results, container, false)

        binding = FragmentTestResultsBinding.bind(view)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference

        getButtons()
        return view
    }

    private fun getButtons() {
        val adapter = TestAdaptor(requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = 0
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getItemName(position)

        }.attach()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TestResults.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestResults().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}