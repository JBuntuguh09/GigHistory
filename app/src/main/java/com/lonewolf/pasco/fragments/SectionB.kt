package com.lonewolf.pasco.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.database.Answers
import com.lonewolf.pasco.database.QuesViewModel
import com.lonewolf.pasco.database.Question
import com.lonewolf.pasco.database.answersViewModel
import com.lonewolf.pasco.databinding.FragmentSectionBBinding
import com.lonewolf.pasco.dialog.show_me
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage
import java.text.DecimalFormat
import java.text.NumberFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SectionB.newInstance] factory method to
 * create an instance of this fragment.
 */
class SectionB : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSectionBBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    private lateinit var linearLayout: LinearLayout

    //private  var listAns : ArrayList<HashMap<String, String>> = ArrayList()
    private lateinit var progressBar: ProgressBar
    val arrayList = ArrayList<HashMap<String, String>>()
    var listAnswers = ArrayList<HashMap<String, String>>()

    var numz = 0
    var currNum = 0
    var showAns = false
    var chkDiag = false
    var cDown: CountDownTimer? = null

    private lateinit var quesViewModel: QuesViewModel
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
        val view =  inflater.inflate(R.layout.fragment_section_b, container, false)

        binding = FragmentSectionBBinding.bind(view)
        databaseReference = FirebaseDatabase.getInstance().reference
        storage = Storage(requireContext())
        progressBar = (activity as MainBase).mainBaseBinding.progressBar
        quesViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(QuesViewModel::class.java)
        ansViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(answersViewModel::class.java)
        numz = (activity as MainBase).randomQuesTotal

        quesViewModel.liveData.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                getOffLine(data)
            } else {
                getQuestions()

            }

        }
        getButtons()
        hideKeyboard()
        return view
    }

    private fun getButtons() {

        binding.btnNext.setOnClickListener {
            if(currNum+1>=numz){
                cleanData()
            }else {
                currNum++
                nextQues(currNum)
            }
            hideKeyboard()
        }

        binding.btnPrev.setOnClickListener {
            if(currNum>0){
                currNum--
                nextQues(currNum)
            }
            hideKeyboard()
        }

    }

    private fun getQuestions() {
        progressBar.visibility = View.VISIBLE
        databaseReference.child("SectionB").child("History").child(storage.project!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                arrayList.clear()
                currNum =0

                for(father in p0.children){
                    val hashMapAns = HashMap<String, String>()

                    hashMapAns["Question"]= father.child("Question").value.toString()
                    hashMapAns["Answer"]= father.child("Answer").value.toString()
                    hashMapAns["Question_Number"]= father.child("Question_Num").value.toString()
                    hashMapAns["Topic"]= storage.project!!
                    hashMapAns["Subject"]= "History"
                    hashMapAns["Selected_Answer"]=""
                    hashMapAns["Check"] = "0"

                    arrayList.add(hashMapAns)
                    listAnswers.add(hashMapAns)

                }
                progressBar.visibility = View.GONE
                if(arrayList.size>0){

                    deleteAllOffline()
                    insertData()

                    numz = arrayList.size
                    binding.btnSubmit.setOnClickListener {
                        cleanData()

                    }
                    nextQues(currNum)
                    //getTimer()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getOffLine(data: List<Question>) {
        for(a in 0 until data.size) {
            val hasd = data[a]

            val hashMapAns = HashMap<String, String>()

            hashMapAns["Question"] = hasd.question
            hashMapAns["Answer"] = hasd.ans
            hashMapAns["Question_Number"] = hasd.qNum
            hashMapAns["Topic"] = storage.project!!
            hashMapAns["Subject"] = "History"

            hashMapAns["Check"] = "0"
            hashMapAns["Selected_Answer"]=""
            arrayList.add(hashMapAns)

            listAnswers.add(hashMapAns)

        }

        if(arrayList.size>0){
            ShortCut_To.sortData(arrayList, "Question_Number")
            ShortCut_To.sortData(listAnswers, "Question_Number")
            numz = arrayList.size

            binding.btnSubmit.setOnClickListener {
                (activity as MainBase).sectBAns = arrayList
                (activity as MainBase).navTo(
                    Congrats(),
                    "${storage.project} results",
                    "Section B",
                    0
                )
            }
            nextQues(currNum)
           // getTimer()
        }
    }

    private fun nextQues(num: Int){
        var nNow = listAnswers[currNum]["Selected_Answer"]
        val hash = arrayList[currNum]



        binding.txtNum.text= "${currNum+1}."
        binding.txtQuestion.text = hash.get("Question")
        binding.txtAnswer.text = hash.get("Answer")



        //  println("mmm : ${listAnswers}")

        binding.floatShow.setOnClickListener{
                if(showAns){
                    showAns = !showAns
                    binding.txtAnswer.visibility = View.GONE
                    binding.txtAnsTopic.visibility = View.GONE
                    binding.floatShow.setImageResource(R.drawable.ic_eye_white_36dp)
                }else{
                    if(chkDiag){
                        binding.scrollMain.scrollTo(0, binding.scrollMain.bottom)
                        binding.txtAnswer.visibility = View.VISIBLE
                        binding.txtAnsTopic.visibility = View.VISIBLE
                        binding.floatShow.setImageResource(R.drawable.ic_eye_off_white_36dp)
                        showAns = !showAns
                    }else {
                        binding.scrollMain.scrollTo(0, binding.scrollMain.bottom)
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setMessage("Alert")
                        alertDialog.setMessage("Are you sure you want to view the answer?")
                        alertDialog.setPositiveButton("Yes I want to view answers") { dialog, which ->
                            binding.scrollMain.scrollTo(0, binding.scrollMain.bottom)
                            binding.txtAnswer.visibility = View.VISIBLE
                            binding.txtAnsTopic.visibility = View.VISIBLE
                            chkDiag = !chkDiag
                            showAns = !showAns
                            binding.floatShow.setImageResource(R.drawable.ic_eye_off_white_36dp)
                        }
                        alertDialog.setNegativeButton("No I do not want to view answers") { dialog, which ->


                        }

                        alertDialog.create().show()
                    }
                }


        }
//        if (currNum+1==numz){
//            binding.btnSubmit.text = getString(R.string.submit)
//            binding.btnSubmit.visibility = View.VISIBLE
//        }
        "${currNum+1}/${numz}".also { binding.txtPage.text = it }
    }

    override fun onResume() {
        (activity as MainBase).mainBaseBinding.txtTopic.text = storage.project
        super.onResume()
    }


    //insert Question
    private  fun insertData(){
        for(a in 0 until arrayList.size){
            val hash = arrayList.get(a)
            val ques = Question(0, hash.get("Question")!!,hash.get("Answer")!!,"",
                "","","",hash.get("Question_Number")!!,
                storage.project!!, storage.selectedCategory!!)
            quesViewModel.insert(ques)

        }

    }

    //Insert Answers
    private fun insertAns(){
        val rId = (1000..10000).shuffled().last()
        val aId = "${ShortCut_To.currentDatewithTime.split(".")[0].replace(" T ", " ")}$rId"
        storage.ansTitle = aId
        (activity as MainBase).objCompletedId = aId
        for(a in 0 until arrayList.size){
            val hash = arrayList.get(a)
            val ans = Answers(0, hash.get("Question")!!,hash.get("Answer")!!,hash.get("Selected_Answer")!!,
                "","","","",hash.get("Question_Number")!!,
                storage.project!!, storage.selectedCategory!!, aId, ShortCut_To.currentDatewithTime)
            ansViewModel.insert(ans)

        }
    }

    //clean and sort data
    private fun cleanData(){
        if(listAnswers.size>0){
            var nArr = ArrayList<HashMap<String, String>>()
            for (a in 0 until numz) {
                listAnswers[a]["Question_Number"] = (a + 1).toString()
                nArr.add(listAnswers[a])
            }

            listAnswers.clear()
            listAnswers = nArr
           // insertAns()

            (activity as MainBase).sectBAns = arrayList
            (activity as MainBase).navTo(
                Congrats(),
                "${storage.project} results",
                "Section B",
                0
            )
        }

    }

    private fun deleteAllOffline(){

        quesViewModel.deleteAll(storage.project!!, storage.selectedCategory!!)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SectionB.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SectionB().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}