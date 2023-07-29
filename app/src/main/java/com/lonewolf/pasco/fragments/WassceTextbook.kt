package com.lonewolf.pasco.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.database.NotesViewModel
import com.lonewolf.pasco.databinding.FragmentWassceTextbookBinding
import com.lonewolf.pasco.resources.ShortCut_To

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WassceTextbook.newInstance] factory method to
 * create an instance of this fragment.
 */
class WassceTextbook : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var binding: FragmentWassceTextbookBinding

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

        val view = inflater.inflate(R.layout.fragment_wassce_textbook, container, false)

        binding = FragmentWassceTextbookBinding.bind(view)
        notesViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(NotesViewModel::class.java)
        getButtons()
        return view
    }

    private fun getButtons() {
        val notes = (activity as MainBase).notes
        binding.txtTopic.text = notes["Sub_Topic"]

        ShortCut_To.boldText(notes["Content"]!!, binding.txtContent)
        binding.txtContent.setTextIsSelectable(true)

        binding.txtContent.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && binding.txtContent.hasSelection()) {

                true
            } else {
                val selectedText = binding.txtContent.text.subSequence(binding.txtContent.selectionStart, binding.txtContent.selectionEnd)
                println("lllll"+selectedText)
                false
            }
        }

//        binding.txtContent.setOnLongClickListener {
//            if (binding.txtContent.hasSelection()) {
//                val selectedText = binding.txtContent.text.subSequence(binding.txtContent.selectionStart, binding.txtContent.selectionEnd)
//                println(selectedText)
//                true
//            } else {
//                val selectedText = binding.txtContent.text.subSequence(binding.txtContent.selectionStart, binding.txtContent.selectionEnd)
//                println(selectedText)
//                false
//            }
//        }


        //binding.txtContent.text = bolden
//        notesViewModel.liveSingle.observe(viewLifecycleOwner){data->
//            if(data.isNotEmpty()){
//                val hash = data[0]
//                binding.txtTopic.text = hash.Sub_Topic
//                binding.txtContent.text = hash.Content
//            }else{
//                binding.txtTopic.text = getString(R.string.noNotes)
//                binding.txtContent.text = ""
//            }
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WassceTextbook.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WassceTextbook().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}