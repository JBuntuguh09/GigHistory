package com.lonewolf.pasco.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.database.Answers
import com.lonewolf.pasco.database.Highscore
import com.lonewolf.pasco.database.HighscoreViewModel
import com.lonewolf.pasco.database.QuesViewModel
import com.lonewolf.pasco.database.Question
import com.lonewolf.pasco.database.answersViewModel
import com.lonewolf.pasco.databinding.FragmentObjectivesBinding
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Objectives.newInstance] factory method to
 * create an instance of this fragment.
 */
class Objectives : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    private  var listAns : ArrayList<HashMap<String, String>> = ArrayList()
    private lateinit var binding: FragmentObjectivesBinding
    var currNum = 0
    //val arrayList = ArrayList<HashMap<String, String>>()
    var numz = 0
    var cDown: CountDownTimer? = null
    //val selVal = ArrayList<Int>()
    private val ansList = ArrayList<String>()

    private lateinit var quesViewModel: QuesViewModel
    private lateinit var ansViewModel: answersViewModel
    private lateinit var highscoreViewModel: HighscoreViewModel
    var shouldInterceptBackPress = true
    private var arrayListOff = ArrayList<HashMap<String, String>>()


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
        val view = inflater.inflate(R.layout.fragment_objectives, container, false)
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(requireContext())

        binding = FragmentObjectivesBinding.bind(view)
        numz = (activity as MainBase).randomQuesTotal
        storage.isComplete = 0
        quesViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(QuesViewModel::class.java)
        ansViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(answersViewModel::class.java)
        highscoreViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(HighscoreViewModel::class.java)

        quesViewModel.liveData.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                getOffLine(data)
            } else {
                getQuestions()
            }
        }

        getButtons()

        backPressed()
        return view
    }



    private fun getQuestions() {
        binding.progressBar.visibility = View.VISIBLE
        databaseReference.child("Questions").child("History").child(storage.project!!).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        listAns.clear()
                        binding.linMain.removeAllViews()

                        currNum = 0
                        for(father in p0.children){
                            val hashMap = HashMap<String, String>()
                            val hashMapAns = HashMap<String, String>()
                            hashMap["Question"]= father.child("Question").value.toString()
                            hashMap["AnswerA"]= father.child("AnswerA").value.toString()
                            hashMap["AnswerB"]= father.child("AnswerB").value.toString()
                            hashMap["AnswerC"]= father.child("AnswerC").value.toString()
                            hashMap["AnswerD"]= father.child("AnswerD").value.toString()
                            hashMap["SelectAns"]= father.child("SelectAns").value.toString()
                            hashMap["Question_Number"]= father.child("Question_Number").value.toString()


                            hashMapAns["Question"]= father.child("Question").value.toString()
                            hashMapAns["AnswerA"]= father.child("AnswerA").value.toString()
                            hashMapAns["AnswerB"]= father.child("AnswerB").value.toString()
                            hashMapAns["AnswerC"]= father.child("AnswerC").value.toString()
                            hashMapAns["AnswerD"]= father.child("AnswerD").value.toString()
                            hashMapAns["SelectAns"]= father.child("SelectAns").value.toString()
                            hashMapAns["Question_Number"]= father.child("Question_Number").value.toString()
                            hashMapAns["Topic"]= storage.project!!
                            hashMapAns["Subject"]= "History"
                            hashMapAns["Ans"]= ""
                            hashMapAns["Check"] = "0"


                            arrayListOff.add(hashMap)
                            listAns.add(hashMapAns)
                            ansList.add("")

                        }
                        binding.progressBar.visibility = View.GONE
                        if(listAns.size>0){
                            //setQuestions(arrayList)
                            listAns.shuffle()
                            deleteAllOffline()
                            insertData()

                            if(listAns.size<= numz){
                                numz = listAns.size
                            }
                            val s: MutableSet<Int> = mutableSetOf()
                            while (s.size < numz) { s.add((0 until listAns.size).random()) }
                            //  myRandomValues = s.toList()

                            nextQues(0)
                            binding.btnSubmit.setOnClickListener {
                                cleanAnswer(0)
                            }
                            getTimer()
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            }
        )
    }

    private fun getOffLine(data : List<Question>){
        for(element in data) {
            val hasd = element
            val hashMap = HashMap<String, String>()
            val hashMapAns = HashMap<String, String>()
            hashMap["Question"] = hasd.question
            hashMap["AnswerA"] = hasd.ansA
            hashMap["AnswerB"] = hasd.ansB
            hashMap["AnswerC"] = hasd.ansC
            hashMap["AnswerD"] = hasd.ansD
            hashMap["SelectAns"] = hasd.ans
            hashMap["Question_Number"] = hasd.qNum


            hashMapAns["Question"] = hasd.question
            hashMapAns["AnswerA"] = hasd.ansA
            hashMapAns["AnswerB"] = hasd.ansB
            hashMapAns["AnswerC"] = hasd.ansC
            hashMapAns["AnswerD"] = hasd.ansD
            hashMapAns["SelectAns"] = hasd.ans
            hashMapAns["Question_Number"] = hasd.qNum
            hashMapAns["Topic"] = storage.project!!
            hashMapAns["Subject"] = "History"
            hashMapAns["Ans"] = ""
            hashMapAns["Check"] = "0"



            listAns.add(hashMapAns)
            ansList.add("")

        }
            binding.progressBar.visibility = View.GONE

            if(listAns.size>0){
                listAns.shuffle()
                if(listAns.size<= numz){
                    numz = listAns.size
                }

                val s: MutableSet<Int> = mutableSetOf()
                while (s.size < numz) { s.add((0 until listAns.size).random()) }
               // myRandomValues = s.toList()

                nextQues(0)
                binding.btnSubmit.setOnClickListener {
                    cleanAnswer(0)
                }
                getTimer()
            }

    }

    private fun submitTest() {
        try {
            binding.progressBar.visibility = View.VISIBLE
        }catch (e:Exception){
            e.printStackTrace()
        }

        insertAns()
    }

    private fun nextQues(ind: Int) {

        println("vind : $ind")
        if(ind==1){
            binding.radioMain.clearCheck()
            println("vind2 : $ind")
        }
        //


        var sAns = ansList[currNum]

        val hash = listAns[currNum]


        binding.txtQuestion.text = hash["Question"]
        "a. ${hash["AnswerA"]}".also { binding.radioA.text = it }
        "b. ${hash["AnswerB"]}".also { binding.radioB.text = it }
        "c. ${hash["AnswerC"]}".also { binding.radioC.text = it }
        "d. ${hash["AnswerD"]}".also { binding.radioD.text = it }
        "#${currNum+1}. ".also { binding.txtNum.text = it }

        if(ind==0){
            if (sAns.equals("A")) {
                binding.radioA.isChecked = true
            } else if (sAns.equals("B")) {
                binding.radioB.isChecked = true
            } else if (sAns.equals("C")) {
                binding.radioC.isChecked = true
            } else if (sAns.equals("D")) {
                binding.radioD.isChecked = true
            }
        }



        binding.radioMain.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            sAns = ""
            if (checkedId == R.id.radioA) {
                sAns = "A"
            } else if (checkedId == R.id.radioB) {
                sAns = "B"
            } else if (checkedId == R.id.radioC) {
                sAns= "C"
            } else if (checkedId == R.id.radioD) {
                sAns = "D"
            }

                listAns[currNum]["Check"] = "1"
                listAns[currNum]["Question_Number"] = "${currNum + 1}"
                listAns[currNum]["Ans"] = sAns
                ansList[currNum] = sAns


        })


        if (currNum+1==numz){
            binding.btnSubmit.text = getString(R.string.submit)
            binding.btnSubmit.visibility = View.VISIBLE
        }
       // selVal.add(currNum, currNum)
       // println("nubm = "+listAns.toString())
        "${currNum+1}/${numz}".also { binding.txtPage.text = it }
        //getTimer()

    }


    private fun getButtons() {
        binding.btnNext.setOnClickListener {
            if(currNum+1>=numz){
                cleanAnswer(0)
            }else {
                currNum++
                if(ansList[currNum].equals("")){
                    nextQues(1)
                }else{
                    nextQues(0)
                }

            }
        }

        binding.btnPrev.setOnClickListener {
            if(currNum>0) {
                currNum--
                if(ansList[currNum].equals("")){
                    nextQues(1)
                }else{
                    nextQues(0)
                }

            }
        }

        binding.btnGoTo.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())

            val layoutInflater = LayoutInflater.from(requireContext())
            val view = layoutInflater.inflate(R.layout.layout_goto, binding.linMain, false)

            val numQues = view.findViewById<EditText>(R.id.edtNum)
            val send = view.findViewById<Button>(R.id.btnSubmit)


            alertDialog.setView(view)
            val alert  = alertDialog.create()

            send.setOnClickListener {
                if(numQues.text.toString().isEmpty()){
                    Toast.makeText(requireContext(), "Enter a question number", Toast.LENGTH_SHORT).show()
                }else if(numQues.text.toString().toInt()>numz){
                    Toast.makeText(requireContext(), "There is no question number ${numQues.text}", Toast.LENGTH_SHORT).show()
                }else{
                    currNum = numQues.text.toString().toInt()-1
                    // nextQues(currNum)
                    if(ansList[currNum].equals("")){
                        nextQues( 1)
                    }else{
                        nextQues( 0)
                    }
                    alert.dismiss()

                }
            }

            alert.show()
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Objectives.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Objectives().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getTimer(){
        val selTime = (activity as MainBase).quesTime.toLong()
         cDown= object : CountDownTimer(selTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                binding.txtTime.setText(
                    f.format(hour).toString() + ":" + f.format(min) + ":" + f.format(
                        sec
                    )
                )
            }

            // When the task is over it will print 00:00:00 there
            override fun onFinish() {
                binding.txtTime.setText("00:00:00")
                cleanAnswer(1)
            }
        }
        cDown!!.start()

    }
    private fun cleanAnswer(v : Int){
        if(v==0){
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Alert")
            alertDialog.setMessage("Do you want to submit your test?")
            alertDialog.setPositiveButton("Yes"){
                    dialog, which->
                var a=0
                val arrNew = ArrayList<HashMap<String, String>>()
                while (a<numz){
                    val hash = listAns[a]
                    arrNew.add(hash)
                    a++
                }

                listAns = arrNew
                submitTest()
            }

            alertDialog.setNegativeButton("No"){
                    dialog, which->
                dialog.dismiss()
            }
            alertDialog.create().show()
        }else{
            var a=0
            val arrNew = ArrayList<HashMap<String, String>>()
            while (a<numz){
                val hash = listAns[a]
                arrNew.add(hash)
                a++
            }

            listAns = arrNew
            submitTest()
        }



    }


    override fun onDestroy() {
        cDown?.cancel()
        super.onDestroy()
    }

    private fun insertAns(){
        println("jammmm : ${listAns}")
        var r = 0
        var w = 0
        var t = ""
        binding.progressBar.visibility = View.GONE
        val rId = (1000..10000).shuffled().last()
        val aId = "${ShortCut_To.currentDatewithTime.split(".")[0].replace(" T ", " ")}$rId"
        for(a in 0 until listAns.size){
            val hash = listAns.get(a)
            val ans = Answers(0, hash.get("Question")!!,hash.get("Ans")!!, hash.get("SelectAns")!!, hash.get("AnswerA")!!,
                hash.get("AnswerB")!!,hash.get("AnswerC")!!,hash.get("AnswerD")!!,(a+1).toString(),
                storage.project!!, storage.selectedCategory!!, aId, ShortCut_To.currentDatewithTime)
            ansViewModel.insert(ans)
            if (hash.get("Ans")!! == hash.get("SelectAns")!!){
                r+=1
            }else{
                w+=1
            }
        }


        val score = "$r/${r+w}"
        var h1 = Highscore(0, aId, ShortCut_To.currentDateFormat2, score, storage.project!!,
            "Objectives", storage.uSERID!!)
        highscoreViewModel.insert(h1)
        cDown?.cancel()
        binding.progressBar.visibility = View.GONE
        storage.ansTitle = aId
        (activity as MainBase).objCompletedId = aId
        (activity as MainBase).navTo(
            Congrats(),
            "${storage.project} results",
            "Objectives",
            0
        )
    }
    private  fun insertData(){
        for(a in 0 until listAns.size){
            val hash = listAns.get(a)
            val ques = Question(0, hash.get("Question")!!,hash.get("SelectAns")!!,hash.get("AnswerA")!!,
                hash.get("AnswerB")!!,hash.get("AnswerC")!!,hash.get("AnswerD")!!,hash.get("Question_Number")!!,
                storage.project!!, storage.selectedCategory!!)
            quesViewModel.insert(ques)

        }

    }

    private fun deleteAllOffline(){
        quesViewModel.deleteAll(storage.project!!, storage.selectedCategory!!)
    }

    private fun deleteTopic(question: Question){
        quesViewModel.deleteQuestion(question)
    }

    private fun backPressed(){
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(shouldInterceptBackPress){
                    val alertDialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    alertDialog.setTitle("Alert")
                    alertDialog.setMessage(getString(R.string.endWithoutSubmitting))
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

    fun syncOffline(){
        val arrayListNew = ArrayList<HashMap<String, String>>()
        databaseReference.child("Questions").child("History").child(storage.project!!).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {

                    for(father in p0.children){
                        val hashMap = HashMap<String, String>()
                        val hashMapAns = HashMap<String, String>()
                        hashMap["Question"]= father.child("Question").value.toString()
                        hashMap["AnswerA"]= father.child("AnswerA").value.toString()
                        hashMap["AnswerB"]= father.child("AnswerB").value.toString()
                        hashMap["AnswerC"]= father.child("AnswerC").value.toString()
                        hashMap["AnswerD"]= father.child("AnswerD").value.toString()
                        hashMap["SelectAns"]= father.child("SelectAns").value.toString()
                        hashMap["Question_Number"]= father.child("Question_Number").value.toString()




                        listAns.add(hashMapAns)


                    }

                    ShortCut_To.sortData(arrayListOff, "Question_Number")

                    if(arrayListOff!=arrayListNew){
                        quesViewModel.deleteAll(storage.project!!, storage.selectedCategory!!)

                        insertData()
                    }

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            }
        )
    }


}