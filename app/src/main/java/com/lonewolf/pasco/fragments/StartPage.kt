package com.lonewolf.pasco.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.lonewolf.pasco.resources.Storage
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.databinding.ActivityMainBaseBinding
import com.lonewolf.pasco.databinding.FragmentStartPageBinding
import com.lonewolf.pasco.resources.Constant

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StartPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class StartPage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var storage: Storage
    private  var mainBaseBinding: ActivityMainBaseBinding? = null
    private lateinit var startPageBinding: FragmentStartPageBinding
    private val binding get() =  mainBaseBinding!!
    private lateinit var auth: FirebaseAuth
    var shouldInterceptBackPress = true


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
        val view = inflater.inflate(R.layout.fragment_start_page, container, false)
        storage = Storage(requireContext())
        auth = FirebaseAuth.getInstance()
        startPageBinding = FragmentStartPageBinding.bind(view)

        getButtons()

        backPressed()
        return view
    }

    private fun getButtons() {

        moveTo(ListMain(), "Objectives", startPageBinding.constQA)
        moveTo(ListMain(), "Section B", startPageBinding.constEssay)
        moveTo(Dictionary(), "History Dictionary", startPageBinding.constDict)
        moveTo(ListMain(), "WASSCE Textbook", startPageBinding.constNotes)



    }

    private fun moveTo(fragment: Fragment, page:String, button : ConstraintLayout){
        button.setOnClickListener {
            (activity as MainBase).mainBaseBinding.txtQuiz.visibility = View.GONE
            (activity as MainBase).bMenu = 0
            storage.selectedCategory = page
            storage.fragValPrev = (activity as MainBase).mainBaseBinding.txtTopic.text.toString()
            (activity as MainBase).navTo(fragment, page, "Welcome",2)
        }
    }

    override fun onResume() {
        (activity as MainBase).bMenu = 0
        val welcome = (activity as MainBase).welcome
        (activity as MainBase).mainBaseBinding.txtTopic.text = welcome
        (activity as MainBase).mainBaseBinding.bottomBar.visibility = View.VISIBLE
        super.onResume()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StartPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun backPressed(){
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(shouldInterceptBackPress){
                    val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    alertDialog.setTitle("Alert")
                    alertDialog.setMessage("${getString(R.string.exitApp)} ${getString(R.string.app_name)}?" )
                    alertDialog.setPositiveButton("Yes"){
                            dialog, which->
                        shouldInterceptBackPress = false
                        activity?.onBackPressed()
                    }

                    alertDialog.setNegativeButton("No"){
                            dialog, which->
                        dialog.dismiss()
                    }
                    alertDialog.create().show()
                    // in here you can do logic when backPress is clicked
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }
}