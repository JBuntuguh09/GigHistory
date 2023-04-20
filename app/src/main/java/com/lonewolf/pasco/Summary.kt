package com.lonewolf.pasco

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lonewolf.pasco.databinding.FragmentSummaryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Summary.newInstance] factory method to
 * create an instance of this fragment.
 */
class Summary : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSummaryBinding

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
        val view = inflater.inflate(R.layout.fragment_summary, container, false)
        binding = FragmentSummaryBinding.bind(view)
        getButtons()
        return view
    }

    private fun getButtons() {
        (activity as MainBase).mainBaseBinding.txtTopic.text = getString(R.string.summary)
        binding.txtGrade.text = (activity as MainBase).grade
        binding.txtScore.text = (activity as MainBase).score
        binding.txtAttempted.text = (activity as MainBase).attempted
        binding.txtPercentage.text = "${(activity as MainBase).percentage}%"
        binding.txtSkipped.text = (activity as MainBase).skipped
        binding.txtTotal.text = (activity as MainBase).randomQuesTotal.toString()


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Summary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Summary().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}