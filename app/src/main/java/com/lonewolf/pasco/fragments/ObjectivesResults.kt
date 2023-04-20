package com.lonewolf.pasco.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.database.Answers
import com.lonewolf.pasco.database.answersViewModel
import com.lonewolf.pasco.databinding.FragmentObjectivesResultsBinding
import com.lonewolf.pasco.dialog.show_me
import com.lonewolf.pasco.resources.Storage
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ObjectivesResults.newInstance] factory method to
 * create an instance of this fragment.
 */
class ObjectivesResults : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage : Storage
    private lateinit var binding : FragmentObjectivesResultsBinding
    var cVal = 0
    private lateinit var ansViewModel: answersViewModel

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
        val view = inflater.inflate(R.layout.fragment_objectives_results, container, false)

        storage = Storage(requireContext())

        databaseReference = FirebaseDatabase.getInstance().reference

        binding = FragmentObjectivesResultsBinding.bind(view)

        ansViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(answersViewModel::class.java)

        ansViewModel.liveData.observe(viewLifecycleOwner) { data ->
            getResultsOffline(data)
        }

        return view
    }

    private fun getResultsOffline(data: List<Answers>) {
        val arrayList = ArrayList<HashMap<String, String>>()
        for(element in data) {
            val hashMap = HashMap<String, String>()
            hashMap["SelectAns"] = element.ansSelected
            hashMap["Question_Number"] = element.qNum
            hashMap["Ans"] = element.ans
            hashMap["Topic"] = element.topic
            hashMap["Question"] = element.question
            hashMap["AnswerA"] = element.ansA
            hashMap["AnswerB"] = element.ansB
            hashMap["AnswerC"] = element.ansC
            hashMap["AnswerD"] = element.ansD

            arrayList.add(hashMap)
        }
        if(arrayList.size>0){
            //setResults(arrayList)
            setQuestions(arrayList)
        }
    }

    private fun getResults() {
        val nId = (activity as MainBase).objCompletedId
        databaseReference.child("Answers").child(storage.uSERID!!).child("History").child(storage.project!!).child(nId).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    println(p0.toString())
                    binding.linMain.removeAllViews()
                    val arrayList = ArrayList<HashMap<String, String>>()
                    for(father in p0.children){
                        val hashMap = HashMap<String, String>()
                        hashMap["SelectAns"] = father.child("SelectAns").value.toString()
                        hashMap["Question_Number"] = father.child("Question_Number").value.toString()
                        hashMap["Ans"] = father.child("Ans").value.toString()
                        hashMap["Topic"] = father.child("Topic").value.toString()
                        hashMap["Question"]= father.child("Question").value.toString()
                        hashMap["AnswerA"]= father.child("AnswerA").value.toString()
                        hashMap["AnswerB"]= father.child("AnswerB").value.toString()
                        hashMap["AnswerC"]= father.child("AnswerC").value.toString()
                        hashMap["AnswerD"]= father.child("AnswerD").value.toString()

                        arrayList.add(hashMap)
                    }
                    if(arrayList.size>0){
                        //setResults(arrayList)
                        setQuestions(arrayList)
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            }
        )
    }

    private fun setResults(arrayList: ArrayList<HashMap<String, String>>) {

        val layoutInflater = LayoutInflater.from(requireContext())
        for(a in 0 until arrayList.size){
            val view = layoutInflater.inflate(R.layout.layout_obj_res, binding.linMain, false)
            val hash = arrayList[a]
            val selected = view.findViewById<TextView>(R.id.txtSelected)
            val ans = view.findViewById<TextView>(R.id.txtCorrect)
            val num = view.findViewById<TextView>(R.id.txtNum)
            val pic = view.findViewById<ImageView>(R.id.imgPic)

            selected.text = hash["SelectAns"]
            ans.text = hash["Ans"]
            num.text = hash["Question_Number"]

            if(hash["SelectAns"].equals(hash["Ans"])){
                Picasso.get().load(R.drawable.ic_check_bold_black_24dp).into(pic)
                setTint(R.color.green, pic)
            }else{

                Picasso.get().load(R.drawable.ic_close_thick_black_24dp).into(pic)
                setTint(R.color.red, pic)
            }

            view.setOnClickListener {

                show_me.question(requireActivity(), binding.linMain, hash["Question_Number"]!!, hash["Ans"]!!)
            }

           binding.linMain.addView(view)

        }
    }

    private fun setTint(@ColorRes colorRes: Int, img: ImageView) {
        ImageViewCompat.setImageTintList(
            img,
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorRes))
        )

    }

    private fun setQuestions(arrayList: ArrayList<HashMap<String, String>>) {
        val layoutInflater = LayoutInflater.from(requireContext())
        var correct = 0
        var count = 1
        for(a in 0 until arrayList.size) {

            val viewz = layoutInflater.inflate(R.layout.layout_objectives, binding.linMain, false)
            val hash = arrayList[a]
            val questn: TextView = viewz.findViewById(R.id.txtQuestion)
            val ansA: TextView = viewz.findViewById(R.id.radioA)
            val ansB: TextView = viewz.findViewById(R.id.radioB)
            val ansC: TextView = viewz.findViewById(R.id.radioC)
            val ansD: TextView = viewz.findViewById(R.id.radioD)
            val imgA: ImageView = viewz.findViewById(R.id.imgA)



            val num: TextView = viewz.findViewById(R.id.txtNum)

            questn.text = hash["Question"]
            ansA.text = "a. ${hash["AnswerA"]}"
            ansB.text = "b. ${hash["AnswerB"]}"
            ansC.text = "c. ${hash["AnswerC"]}"
            ansD.text = "d. ${hash["AnswerD"]}"
            num.text = "${count}. "

            if (hash["SelectAns"].equals(hash["Ans"])){
                correct++
                Picasso.get().load(R.drawable.ic_check_bold_black_24dp).into(imgA)
                setTint(R.color.green, imgA)
            }else{
                Picasso.get().load(R.drawable.ic_close_thick_black_24dp).into(imgA)
                setTint(R.color.red, imgA)
            }
            if (hash["SelectAns"] == "A") {
                ansA.setBackgroundResource(R.drawable.bg_green)

            } else if (hash["SelectAns"] == "B") {
                ansB.setBackgroundResource(R.drawable.bg_green)

            } else if (hash["SelectAns"] == "C") {
                ansC.setBackgroundResource(R.drawable.bg_green)

            } else if (hash["SelectAns"] == "D") {
                ansD.setBackgroundResource(R.drawable.bg_green)

            }

            if (hash["SelectAns"] != "A"
                && hash["Ans"] == "A"
            ) {
                ansA.setBackgroundResource(R.drawable.bg_red)
                Picasso.get().load(R.drawable.ic_close_thick_black_24dp).into(imgA)
                setTint(R.color.red, imgA)
            } else if (hash["SelectAns"] != "B"
                && hash["Ans"] == "B"
            ) {
                ansB.setBackgroundResource(R.drawable.bg_red)
                Picasso.get().load(R.drawable.ic_close_thick_black_24dp).into(imgA)
                setTint(R.color.red, imgA)
            } else if (hash["SelectAns"] != "C"
                && hash["Ans"] == "C"
            ) {
                ansC.setBackgroundResource(R.drawable.bg_red)
                Picasso.get().load(R.drawable.ic_close_thick_black_24dp).into(imgA)
                setTint(R.color.red, imgA)
            } else if (hash["SelectAns"] != "D"
                && hash["Ans"] == "D"
            ) {
                ansD.setBackgroundResource(R.drawable.bg_red)
                Picasso.get().load(R.drawable.ic_close_thick_black_24dp).into(imgA)
                setTint(R.color.red, imgA)
            }

            count += 1
            binding.linMain.addView(viewz)

        }
        val mScore = "Score : $correct/${arrayList.size}"
        val mPercent = (correct*100)/arrayList.size
        binding.txtScore.text = mScore
        binding.txtPercent.text = "$mPercent%"
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ObjectivesResults.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ObjectivesResults().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
       // (activity as MainBase).navTo(ObjectivesResults(), "", "", 3)
        super.onDestroy()
    }


}