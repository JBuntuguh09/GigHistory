package com.lonewolf.pasco.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.database.QuesViewModel
import com.lonewolf.pasco.database.Question
import com.lonewolf.pasco.database.answersViewModel
import com.lonewolf.pasco.databinding.FragmentSectionBBinding
import com.lonewolf.pasco.databinding.FragmentSectionBResultBinding
import com.lonewolf.pasco.resources.Storage
import java.text.DecimalFormat
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SectionBResult.newInstance] factory method to
 * create an instance of this fragment.
 */
class SectionBResult : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSectionBResultBinding
    private lateinit var storage: Storage
    private  var arrayList = ArrayList<HashMap<String, String>>()
    private lateinit var databaseReference : DatabaseReference
    private lateinit var ansViewModel: answersViewModel
    var numz = 0
    var currNum = 0
    var showAns = false
    var chkDiag = false


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
        val view = inflater.inflate(R.layout.fragment_section_b_result, container, false)
        binding = FragmentSectionBResultBinding.bind(view)

        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(requireContext())
        arrayList = (activity as MainBase).sectBAns
        ansViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(answersViewModel::class.java)

        numz = (activity as MainBase).randomQuesTotal


        setQues()
        return view
    }

    private fun getButtons() {

//        ansViewModel.liveData.observe(viewLifecycleOwner){data->
//            if(data.isNotEmpty()){
//                println("howdy : "+data)
//                for(ans in data){
//                    val hash = HashMap<String, String>()
//                    hash["Question"] = ans.question
//                    hash["Question_Number"] = ans.qNum
//                    hash["Answer"] = ans.ans
//                    hash["Selected_Answer"] = ans.ansSelected
//                    hash["Topic"] = ans.topic
//
//                    arrayList.add(hash)
//                }
//                if(arrayList.size>0){
//                    setQues()
//                }
//            }
//        }


    }

    private fun setQues() {
        numz = arrayList.size
        nextQues(currNum)

        binding.bottomBar.setOnItemSelectedListener {
            when (it.itemId){
                R.id.menuNext ->{

                    if(currNum+1>=numz){
                        Toast.makeText(requireContext(), "End of list", Toast.LENGTH_SHORT).show()
                    }else {
                        currNum++
                        nextQues(currNum)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.menuPrev ->{

                    if(currNum>0){
                        currNum--
                        nextQues(currNum)
                    }

                    return@setOnItemSelectedListener true
                }

                R.id.menuGoTo ->{

                    val alertDialog = android.app.AlertDialog.Builder(requireContext())

                    val layoutInflater = LayoutInflater.from(requireContext())
                    val view = layoutInflater.inflate(R.layout.layout_goto, binding.linTop, false)

                    val numQues = view.findViewById<EditText>(R.id.edtNum)
                    val send = view.findViewById<Button>(R.id.btnSubmit)


                    alertDialog.setView(view)
                    val alert  = alertDialog.create()

                    send.setOnClickListener {
                        if(numQues.text.toString().isEmpty()){
                            Toast.makeText(requireContext(), "Enter a question number", Toast.LENGTH_SHORT).show()
                        }else if(numQues.text.toString().toInt()>numz){
                            Toast.makeText(requireContext(), "There is no question number ${numQues.text}", Toast.LENGTH_SHORT).show()
                        } else{
                            currNum = numQues.text.toString().toInt()-1
                            nextQues(currNum)

                            alert.dismiss()

                        }
                    }

                    alert.show()
                    return@setOnItemSelectedListener true
                }


            }
            false
        }
    }

    private fun nextQues(num: Int){

        val hash = arrayList[num]

        binding.txtNum.text= "${currNum+1}."
        binding.txtQuestion.text = hash["Question"]
        binding.txtSuggested.text = hash["Answer"]
        binding.txtYours.text = hash["Selected_Answer"]

        if(hash["Selected_Answer"]!!.isEmpty()){
            binding.linTopYour.visibility = View.GONE
            binding.txtYours.visibility = View.GONE
        }else{
            binding.linTopYour.visibility = View.VISIBLE
            binding.txtYours.visibility = View.VISIBLE
        }


        "${currNum+1}/${numz}".also { binding.txtPage.text = it }
    }





    override fun onResume() {
        (activity as MainBase).mainBaseBinding.txtTopic.text = storage.project
        super.onResume()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SectionBResult.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SectionBResult().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}