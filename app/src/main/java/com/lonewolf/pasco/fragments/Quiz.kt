package com.lonewolf.pasco.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RadioGroup.OnCheckedChangeListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.*
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.database.QuizViewModel
import com.lonewolf.pasco.databinding.FragmentQuizBinding
import com.lonewolf.pasco.resources.My_ColorTemplate
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Quiz.newInstance] factory method to
 * create an instance of this fragment.
 */
class Quiz : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentQuizBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
    var cText = 0
    var cHalf = 0
    var cGraph = 0
    var cHint = 0
    var currNum = 0
    private  val arrayList = ArrayList<HashMap<String, String>>()
    private  val arrayListDic = ArrayList<HashMap<String, String>>()
    private  val arrayListAnsSel = ArrayList<HashMap<String, String>>()
    private lateinit var quizViewModel: QuizViewModel
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var aDialog: AlertDialog
    private var cDown : CountDownTimer? = null
    var tPauseVal = 0
    var selected = ""
    var rAns = ""
    var RQues = ""
    var rExtra = 0
    var currentTask = HashMap<String, String>()
    var numIns = 0
    var extraMk = 0
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
        val view =  inflater.inflate(R.layout.fragment_quiz, container, false)
        storage = Storage(requireContext())
        databaseReference = FirebaseDatabase.getInstance().reference
        alertDialog = AlertDialog.Builder(requireActivity())
        binding = FragmentQuizBinding.bind(view)
        quizViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(QuizViewModel::class.java)

      //  quizViewModel.deleteAll()
        if(storage.project.equals(getString(R.string.cat4))){
            println("here")

            quizViewModel.liveData.observe(viewLifecycleOwner){ data ->
                if(data.isNotEmpty()){
                    println("size = ${data.size}")
                    arrayList.clear()
                    getOffline(data)
                }else{
                    getOnlineData()
                }
            }
        }else{
            println(storage.randVal)
            quizViewModel.liveDataSingle.observe(viewLifecycleOwner){ data ->
               // quizViewModel.deleteAll()
                if(data.isNotEmpty()){
                    arrayList.clear()
                    getOffline(data)
                }else{
                    println("running here")
                   // quizViewModel.deleteAll()
                    getOnlineData()
                }
            }
        }


        getButtons()
       // getOnlineData()
        return view
    }

      private fun showKonfet(){
        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            position = Position.Relative(0.5, 0.3),
            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
        )
        binding.konfettiView.start(party )
    }

    private fun getOffline(data: List<com.lonewolf.pasco.database.Quiz>) {
        for (num in data) {
            val hashMapAns = HashMap<String, String>()
            hashMapAns["Question"] = num.question
            hashMapAns["AnswerA"] = num.ansA
            hashMapAns["AnswerB"] = num.ansB
            hashMapAns["AnswerC"] = num.ansC
            hashMapAns["AnswerD"] = num.ansD
            hashMapAns["SelectAns"] = num.ans
            hashMapAns["Question_Number"] = num.qNum
            hashMapAns["Topic"] = "General"
            hashMapAns["Subject"] = num.topic
            hashMapAns["Ans"] = num.ans
            hashMapAns["Type"] = num.type
            arrayList.add(hashMapAns)
        }
        arrayList.shuffle()
        if(numIns==0) {
            numIns = 1
            nextQues()
        }
    }

    private fun getButtons() {

        if(storage.project!!.equals(getString(R.string.cat1))){
            binding.imgHint.visibility = View.GONE
        }else if(arrayListOf(getString(R.string.cat2), getString(R.string.cat3), getString(R.string.cat5)).contains(storage.project!!)){
            binding.imgHalf.visibility = View.GONE
            binding.imgGraph.visibility = View.GONE
        }




    }

    private fun timeUp(type : Int) {

        try {
            if(cDown!=null){
                cDown!!.cancel()
            }
            val alertDialog = AlertDialog.Builder(requireContext())

            val layoutInflater = LayoutInflater.from(requireContext())
            val view =
                layoutInflater.inflate(R.layout.layout_gameover, binding.linMain, false)
            val gOverWrong = view.findViewById<ImageView>(R.id.imgPicWrong)
            val gOver = view.findViewById<ImageView>(R.id.imgPic)
            val topic = view.findViewById<TextView>(R.id.txtTopic)
            val wOk = view.findViewById<Button>(R.id.btnSubmit)
            val ques = view.findViewById<TextView>(R.id.txtQuestion)
            val ans = view.findViewById<TextView>(R.id.txtAnswer)

            ques.text = currentTask["Question"]
            ans.text = currentTask["Ans"]
            if(arrayList[currNum]["Type"].equals("Objectives")){
                if(arrayList[currNum]["SelectAns"]!!.uppercase().equals("A")){
                    ans.text = arrayList[currNum]["AnswerA"]
                }else if(arrayList[currNum]["SelectAns"]!!.uppercase().equals("B")){
                    ans.text = arrayList[currNum]["AnswerB"]
                }else if(arrayList[currNum]["SelectAns"]!!.uppercase().equals("C")){
                    ans.text = arrayList[currNum]["AnswerC"]
                }else if(arrayList[currNum]["SelectAns"]!!.uppercase().equals("D")){
                    ans.text = arrayList[currNum]["AnswerD"]
                }
            }
            if(type==0){

                gOver.visibility = View.INVISIBLE
                gOverWrong.visibility = View.VISIBLE
                topic.text = "Wrong Answer!!!!!"
            }else{

                topic.text = "Time Up!!!!!!!!!"

            }
            alertDialog.setView(view)
            val alert = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.setOnCancelListener {
                try{
                    aDialog.dismiss()
                }catch (e : Exception){
                    e.printStackTrace()
                }
                val score = currNum+extraMk
                (activity as MainBase).score = score.toString()
                (activity as MainBase).navTo(CongratsQuiz(), "Quiz", "Congratulations", 0)
            }

            alert.show()

            val mash = arrayList[currNum]
            mash["ansCecked"] = selected
            arrayListAnsSel.add(mash)

            sendtoDb()
            wOk.setOnClickListener {
                try{
                    aDialog.dismiss()
                }catch (e : Exception){
                    e.printStackTrace()
                }
                alert.dismiss()
                val score = currNum+extraMk
                (activity as MainBase).score = score.toString()
                (activity as MainBase).navTo(CongratsQuiz(), "Quiz", "Congratulations", 0)

            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }

    private fun sendtoDb() {

        val path = (activity as MainBase)
        if(path.auth.currentUser!=null){
            val code = ShortCut_To.timeStamp()+ShortCut_To.randomStringByKotlinRandom(5)
            val score = (currNum + extraMk).toString()
            val hash = HashMap<String, Any>()
            hash["score"] = score
            hash["timestamp"] = ShortCut_To.timeStamp()
            hash["userId"] = storage.uSERID!!
            hash["code"] = code
            hash["datetime"] = Calendar.getInstance().time.toString()
            hash["date"] = ShortCut_To.currentDateFormat2
            hash["time"] = ShortCut_To.getCurrentTime()
            hash["month"] = ShortCut_To.currentMonthYear
            hash["name"] = storage.uSERNAME!!
            hash["answers"] = arrayListAnsSel
            databaseReference.child("Scores").child("Quiz").child(storage.uSERID!!).child(code).setValue(hash)

            if(path.topQuizArray.size<20){
                for (a in path.topQuizArray.indices){
                    val vash = path.topQuizArray[a]
                    if(vash["score"]!!.toInt() > hash["score"]!!.toString().toInt()){
                        databaseReference.child("Global").child("Quiz").child("All Time").child(score).child(code).setValue(hash)

                    }
                }
                if(path.topQuizArray.size==0){
                    databaseReference.child("Global").child("Quiz").child("All Time").child(score).child(code).setValue(hash)
                }

            }else{
                val jash  = path.topQuizArray[path.topQuizArray.size-1]
                if(hash["score"]!!.toString().toInt()> jash["score"]!!.toInt()){
                    databaseReference.child("Global").child("Quiz").child("All Time").child(jash["score"]!!).child(jash["code"]!!).removeValue()
                    databaseReference.child("Global").child("Quiz").child("All Time").child(score).child(code).setValue(hash)
                }
            }

            var cDay = ShortCut_To.currentDateFormat2.replace("/", "I")
            if(path.topQuizDayArray.size<20){
                for (a in path.topQuizDayArray.indices){
                    val vash = path.topQuizDayArray[a]
                    if(vash["score"]!!.toInt() > hash["score"]!!.toString().toInt()){
                        databaseReference.child("Global").child("Quiz").child("Day").child(cDay).child(score).child(code).setValue(hash)

                    }
                }
                if(path.topQuizDayArray.size==0){
                    databaseReference.child("Global").child("Quiz").child("Day").child(cDay).child(score).child(code).setValue(hash)
                }

            }else{
                val jash  = path.topQuizDayArray[path.topQuizDayArray.size-1]
                if(hash["score"]!!.toString().toInt()> jash["score"]!!.toInt()){
                    databaseReference.child("Global").child("Quiz").child("Day").child(cDay).child(jash["score"]!!).child(jash["code"]!!).removeValue()
                    databaseReference.child("Global").child("Quiz").child("Day").child(cDay).child(score).child(code).setValue(hash)
                }
            }

            //handle month
            var cMonth = ShortCut_To.currentMonthYear.replace("/", "I")
            if(path.topQuizMonthArray.size<20){
                for (a in path.topQuizMonthArray.indices){
                    val vash = path.topQuizMonthArray[a]
                    if(vash["score"]!!.toInt() > hash["score"]!!.toString().toInt()){
                        databaseReference.child("Global").child("Quiz").child("Month").child(cMonth).child(score).child(code).setValue(hash)

                    }
                }
                if(path.topQuizMonthArray.size==0){
                    databaseReference.child("Global").child("Quiz").child("Month").child(cMonth).child(score).child(code).setValue(hash)
                }

            }else{
                val jash  = path.topQuizMonthArray[path.topQuizMonthArray.size-1]
                if(hash["score"]!!.toString().toInt()> jash["score"]!!.toInt()){
                    databaseReference.child("Global").child("Quiz").child("Month").child(cMonth).child(jash["score"]!!).child(jash["code"]!!).removeValue()
                    databaseReference.child("Global").child("Quiz").child("Month").child(cMonth).child(score).child(code).setValue(hash)
                }
            }


        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getOnlineData() {
        GlobalScope.launch(Dispatchers.IO) {
            databaseReference.child("Questions").child("History").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {


                        try {
                            for(grand in p0.children){
                                for(father in grand.children){
                                    val hashMapAns = HashMap<String, String>()


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
                                    hashMapAns["Type"] = "Objectives"

                                    arrayList.add(hashMapAns)
                                }

                                arrayList.shuffle()
                            }
                            println("aloha = ${arrayList}")
                            getDicOnline()
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                }
            )

        }

    }

    private fun getDicOnline() {
        GlobalScope.launch(Dispatchers.IO) {
            println("hhhh")
            databaseReference.child("Dictionary").child("History").orderByChild("Word")
                .addListenerForSingleValueEvent(
                    object : ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            println("000 -- $p0")
                            for(father in p0.children){
                                val hashMapAns = HashMap<String, String>()

                                hashMapAns["Question"]= father.child("Word").value.toString()
                                hashMapAns["AnswerA"]= father.child("Meaning").value.toString()
                                hashMapAns["AnswerB"]= ""
                                hashMapAns["AnswerC"]= ""
                                hashMapAns["AnswerD"]= ""
                                hashMapAns["SelectAns"]= father.child("Meaning").value.toString()
                                hashMapAns["Question_Number"]= ""
                                hashMapAns["Topic"]= "General"
                                hashMapAns["Subject"]= "History"
                                hashMapAns["Ans"]= ""
                                hashMapAns["Type"] = "Dictionary"
                                arrayList.add(hashMapAns)
                            }
                            println("array - - ${arrayList}")
                            if(arrayList.size>0){
                                arrayList.shuffle()
                                insertData()
                               // nextQues()
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                            println("looop -- $p0" )

                        }
                    }
                )

        }
    }

    private fun nextQues() {
        binding.btnSubmit.setOnClickListener {
            println("$rAns /// $selected")

            //Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
            if(currNum+1<arrayList.size){
                if(rAns == selected){
                    extraMk += if ((tPauseVal/1000)<10) 2 else if((tPauseVal/1000)<20) 1 else 0
                    showKonfet()
                    currNum++
                    val score = "Score : ${currNum + extraMk}pts"
                    binding.txtPage.text = score
                    nextQues()
                }else{
                    try {
                        timeUp(0)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                }


            }
        }
        binding.progressBar.visibility = View.GONE

        binding.radioMain.clearCheck()
        tPauseVal=0
        if(cDown!=null){
            cDown!!.cancel()
        }
        getTimer()
        val hash = arrayList[currNum]
        ShortCut_To.hideKeyboard(requireActivity())



        if(hash["Type"].equals("Dictionary")){
            binding.edtAnswer.setText("")
            binding.radioMain.visibility = View.GONE
            binding.txtInp.visibility = View.VISIBLE

            if(storage.project.equals("Word Scramble")) {
            rAns = hash["Question"]!!.uppercase()

            binding.linMissing.visibility = View.GONE
            binding.txtQuestion.textSize = 24F
            val jumb = hash["Question"]!!.uppercase().toCharArray()
            jumb.shuffle()
            if(jumb.toString()==hash["Question"]){
                jumb.shuffle()
            }

            binding.txtQuestion.text = String(jumb)
            currentTask.put("Question", String(jumb))
            currentTask.put("Ans", rAns)



            binding.edtAnswer.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    selected = p0.toString().uppercase()
                }

            })

            binding.imgHint.setOnClickListener {
                if(cHint==0){
                    getHelp(2, hash)
                }
            }
            }else if(storage.project.equals(getString(R.string.cat5))){
                binding.txtQuestion.visibility = View.GONE
                binding.edtAnswer.visibility = View.GONE
                binding.linMissing.removeAllViews()
                isMissing()
            }else if(storage.project.equals(getString(R.string.cat3))){
                binding.edtAnswer.setText("")
                binding.radioMain.visibility = View.GONE
                binding.txtInp.visibility = View.VISIBLE

                rAns = hash["Question"]!!.uppercase()

                binding.linMissing.visibility = View.GONE
                binding.txtQuestion.textSize = 24F
                val jumb = hash["SelectAns"]!!.uppercase().toCharArray()


                binding.txtQuestion.text = String(jumb)
                currentTask.put("Question", String(jumb))
                currentTask.put("Ans", rAns)



                binding.edtAnswer.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        selected = p0.toString().uppercase()
                    }

                })

                binding.imgHint.setOnClickListener {
                    if(cHint==0){
                        getHelp(2, hash)
                    }
                }

            }



        }else{


            binding.radioMain.visibility = View.VISIBLE
            binding.txtInp.visibility = View.GONE
            binding.txtQuestion.text = hash["Question"]
            rAns = hash["SelectAns"]!!
            binding.radioA.text = "a. ${hash["AnswerA"]}"
            binding.radioB.text = "b. ${hash["AnswerB"]}"
            binding.radioC.text = "c. ${hash["AnswerC"]}"
            binding.radioD.text = "d. ${hash["AnswerD"]}"

            binding.radioA.visibility = View.VISIBLE
            binding.radioB.visibility = View.VISIBLE
            binding.radioC.visibility = View.VISIBLE
            binding.radioD.visibility = View.VISIBLE

            binding.imgHalf.setOnClickListener{
                if(cHalf==0){
                    getHelp(0, hash)
                }
            }
            binding.imgGraph.setOnClickListener {
                if(cGraph==0){
                    getHelp(1, hash)
                }
            }



            currentTask.put("Question", hash["Question"]!!)
            currentTask.put("Ans", rAns)


            binding.radioMain.setOnCheckedChangeListener(object : OnCheckedChangeListener{
                override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                    if(p1==R.id.radioA){
                        selected = "A"
                    }else if(p1==R.id.radioB){
                        selected = "B"
                    }else if(p1==R.id.radioC){
                        selected = "C"
                    }else if(p1==R.id.radioD){
                        selected = "D"
                    }
                }

            })
        }

        binding.imgWhatsapp.setOnClickListener {
            if(cText==0){
                getHelp(3, hash)
            }
        }
        binding.txtNum.text = ("#${currNum+1}")
    }

    private fun isMissing() {
        val hash = arrayList[currNum]
        rAns = hash["Question"]!!.uppercase()
        var ques = ""

        val myAns = ArrayList<String>()
        val layoutInflater = LayoutInflater.from(requireContext())

        for(a in 0 until rAns.length){
            val view = layoutInflater.inflate(R.layout.layout_missing, binding.linMissing, false)
            val text = view.findViewById<TextView>(R.id.txtText)
            val edit = view.findViewById<EditText>(R.id.edtText)

            if(a%2==0){
                text.text = rAns[a].toString()
                edit.visibility = View.GONE
                edit.setText("")
                ques += rAns[a].toString()
                myAns.add(rAns[a].toString())
            }else{
                text.text = ""
                text.visibility = View.GONE
                edit.setText("")
                ques+="_"
                myAns.add("_")
                edit.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        myAns[a] = p0.toString()
                        var nAns = ""
                        for(b in 0 until rAns.length){
                            nAns += rAns[b]
                        }
                        selected = nAns
                    }

                })
            }


            binding.linMissing.addView(view)

        }

        currentTask.put("Question", ques)
        currentTask.put("Ans", rAns)
        selected = ques
    }

    private fun getHelp(i: Int, hash : HashMap<String, String>) {
        val random1 = (0..2).shuffled().last()

        if(i==0){


            alertDialog.setTitle("Alert")
            alertDialog.setIcon(R.drawable.ic_fraction_one_half_black_36dp)

            alertDialog.setMessage("Do you want to user a 50 50 option?")
            alertDialog.setPositiveButton("Yes"){
                dialog, unit->
                if(hash["SelectAns"]!!.uppercase().equals("A")){
                    binding.radioA.visibility = View.VISIBLE
                    binding.radioB.visibility = View.GONE
                    binding.radioC.visibility = View.GONE
                    binding.radioD.visibility = View.GONE
                    if(random1==0){
                       binding.radioB.visibility = View.VISIBLE
                   }else if(random1==1){
                        binding.radioC.visibility = View.VISIBLE
                    }else if(random1==2){
                        binding.radioD.visibility = View.VISIBLE
                    }
                }else if(hash["SelectAns"]!!.uppercase().equals("B")){
                    binding.radioB.visibility = View.VISIBLE
                    binding.radioA.visibility = View.GONE
                    binding.radioC.visibility = View.GONE
                    binding.radioD.visibility = View.GONE
                    if(random1==0){
                        binding.radioA.visibility = View.VISIBLE
                    }else if(random1==1){
                        binding.radioC.visibility = View.VISIBLE
                    }else if(random1==2){
                        binding.radioD.visibility = View.VISIBLE
                    }
                }else if(hash["SelectAns"]!!.equals("C")){
                    binding.radioC.visibility = View.VISIBLE
                    binding.radioB.visibility = View.GONE
                    binding.radioA.visibility = View.GONE
                    binding.radioD.visibility = View.GONE
                    if(random1==0){
                        binding.radioB.visibility = View.VISIBLE
                    }else if(random1==1){
                        binding.radioA.visibility = View.VISIBLE
                    }else if(random1==2){
                        binding.radioD.visibility = View.VISIBLE
                    }
                }else if(hash["SelectAns"]!!.equals("D")){
                    binding.radioD.visibility = View.VISIBLE
                    binding.radioB.visibility = View.GONE
                    binding.radioC.visibility = View.GONE
                    binding.radioA.visibility = View.GONE
                    if(random1==0){
                        binding.radioB.visibility = View.VISIBLE
                    }else if(random1==1){
                        binding.radioC.visibility = View.VISIBLE
                    }else if(random1==2){
                        binding.radioA.visibility = View.VISIBLE
                    }
                }
                cHalf = 1
                val color = ContextCompat.getColor(requireContext(), R.color.grey)

                binding.imgHalf.setColorFilter(color, PorterDuff.Mode.SRC_IN)


            }
            alertDialog.setNegativeButton("No"){
                dialog, unit->
                dialog.dismiss()
            }
            alertDialog.create().show()
        }else if(i==1){
            alertDialog.setTitle("Alert")
            alertDialog.setIcon(R.drawable.ic_chart_bar_black_36dp)

            alertDialog.setMessage("Do you want to use the Popularity option?")
            alertDialog.setPositiveButton("Yes") { dialog, unit ->
//                databaseReference.child("Graph_History").child("History")
//                    .child(hash["SelectAns"]!!).addValueEventListener(object : ValueEventListener{
//                        override fun onDataChange(p0: DataSnapshot) {
//                            println("goooooooo")
//                            if(p0.hasChildren()){
//                                showBar(10, 20, 40, 60)
//                            }else{
//                                showBar(1, 2, 3, 4)
//                            }
//                        }
//
//                        override fun onCancelled(p0: DatabaseError) {
//                            println("wassop")
//                        }
//
//                    })
                val ran = n_random()
                val color = ContextCompat.getColor(requireContext(), R.color.grey)

                binding.imgGraph.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                showBar(ran[0], ran[1], ran[2], ran[3])

            }
            cGraph = 1


            alertDialog.setNegativeButton("No"){
                    dialog, unit->
                dialog.dismiss()
            }
            alertDialog.create().show()


        }else if(i==2){
            alertDialog.setTitle("Alert")
            alertDialog.setIcon(R.drawable.ic_account_question_black_36dp)

            alertDialog.setMessage("Do you want to use the Hint option?")
            alertDialog.setPositiveButton("Yes") { dialog, unit ->
                val alert = AlertDialog.Builder(requireActivity())
                val layoutInflater = LayoutInflater.from(requireActivity())
                val view = layoutInflater.inflate(R.layout.layout_dictionary_dialogue, binding.linMain, false)
                val topic = view.findViewById<TextView>(R.id.txtWord)
                val hint = view.findViewById<TextView>(R.id.txtMeaning)

                topic.text = getString(R.string.hint)
                var hHint = hash["SelectAns"]
                if(storage.project.equals(getString(R.string.defineQuiz))){
                    hHint= hash["Question"]!!.uppercase()
                    var nHint = ""
                    for(x in 0 until hHint.length){
                        if(x%2==0){
                            nHint=nHint+hHint[x]
                        }else{
                            nHint=nHint+"_"
                        }
                    }
                    hHint = nHint
                }
                hint.text = hHint

                alert.setView(view)
                alert.create().show()

                cHint =1
//                binding.imgHint.setColorFilter(ContextCompat.getColor(requireContext(), R.color.light_grey), android.graphics.PorterDuff.Mode.MULTIPLY)
//                Picasso.get().load(R.drawable.ic_account_question_grey600_36dp).into(binding.imgHint)
                val color = ContextCompat.getColor(requireContext(), R.color.grey)
                binding.imgHint.setColorFilter(color, PorterDuff.Mode.SRC_IN)

            }

            alertDialog.setNegativeButton("No"){
                    dialog, unit->
                dialog.dismiss()
            }
            alertDialog.create().show()
        }else if (i==3){
                cText = 1
            var mess = ""
            if(storage.project.equals(getString(R.string.cat1))){
                     mess = "Hello, Can you help me solve this question?\n\n" +
                            "${arrayList[currNum]["Question"]} \n" +
                            "*Possible Answers*\n" +
                            "a. ${arrayList[currNum]["AnswerA"]} \n" +
                            "b. ${arrayList[currNum]["AnswerB"]} \n" +
                            "c. ${arrayList[currNum]["AnswerC"]} \n" +
                            "d. ${arrayList[currNum]["AnswerD"]} \n"
                }else if(storage.project.equals(getString(R.string.cat2))){
                    mess = "Hello, Can you help me unscramble this word?\n\n" +
                            binding.txtQuestion.text.toString()
                }else if(storage.project.equals(getString(R.string.cat5))){
                    mess = "Hello, Can you help me fill in the missing letters?\n\n" +
                            selected

                }else if(storage.project.equals(getString(R.string.cat3))){
                    mess = "Hello, can you help me find the word which defines this sentence?\n\n"+
                            selected

                }else if(storage.project.equals(getString(R.string.cat4))){

                }

            sendWhatsapp(mess)
        }
    }

    private fun showBar(i: Int, i1: Int, i2: Int, i3: Int) {
        val alert = AlertDialog.Builder(requireActivity())
        val layoutInflater = LayoutInflater.from(requireActivity())
        val view = layoutInflater.inflate(R.layout.layout_graph, binding.linMain, false)
        val barChart = view.findViewById<BarChart>(R.id.barMain)
        val ok = view.findViewById<Button>(R.id.btnSubmit)
        val arrayBar = java.util.ArrayList<BarEntry>()
        val listNum = arrayOf(i, i1, i2, i3 )

        for (y in listNum.indices) {

            arrayBar.add(y, BarEntry(y.toFloat(), listNum.get(y).toFloat()))

        }

        val barData = BarDataSet(arrayBar, "")

        val barData1 = BarData(barData)
        barData.setColors(My_ColorTemplate.JOYFUL_COLORS, 250)
        //barData1.setBarWidth(0.9f);

        //barData1.setBarWidth(0.9f);
        barChart.data = barData1
        barChart.description.text = ""
        barChart.animateXY(1000, 1000)
        barChart.axisRight.isEnabled = false
        barChart.legend.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = DayAxisValueFormatter()
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.isEnabled = true
        xAxis.textColor = Color.BLACK

        alert.setView(view)

        aDialog = alert.create()
        aDialog.show()

        ok.setOnClickListener {
            aDialog.dismiss()
        }

    }

    class DayAxisValueFormatter() : ValueFormatter() {
        private val mValues = arrayListOf("A", "B", "C", "D")
        override fun getFormattedValue(value: Float): String {
            return mValues[value.toInt()]
        }
    }


    private  fun insertData() {
        for(a in 0 until arrayList.size){
            val hash = arrayList[a]
            val qInfo = com.lonewolf.pasco.database.Quiz(0, hash["Question"]!!,
            hash["SelectAns"]!!, hash["AnswerA"]!!, hash["AnswerB"]!!, hash["AnswerC"]!!,
                hash["AnswerD"]!!, hash["Question_Number"]!!, storage.selectedCategory!!, hash["Type"]!!)

        quizViewModel.insert(qInfo)
        }
    }

    private fun getTimer(){

        val time = 45000+rExtra
        rExtra=0
        tPauseVal = 0
        val selTime = time.toLong()
        cDown= object : CountDownTimer(selTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Used for formatting digit to be in 2 digits only
                val f: NumberFormat = DecimalFormat("00")
                val hour = millisUntilFinished / 3600000 % 24
                val min = millisUntilFinished / 60000 % 60
                val sec = millisUntilFinished / 1000 % 60
                tPauseVal += 1000

                binding.txtTime.setText(
                    f.format(hour).toString() + ":" + f.format(min) + ":" + f.format(
                        sec
                    )
                )
            }

            // When the task is over it will print 00:00:00 there
            override fun onFinish() {
                binding.txtTime.setText("00:00:00")

                lifecycleScope.launch {
                    timeUp(1)
                }
            }
        }
        cDown!!.start()

    }
    private fun pauseTimer(){
        cDown!!.cancel()
        rExtra = 120000-tPauseVal
        getTimer()

    }

    override fun onDestroy() {
        if(cDown!=null){
            cDown!!.cancel()
        }
        super.onDestroy()
    }

    fun n_random() : ArrayList<Int> {
        val r = Random()
        val load = ArrayList<Int>()
        var temp = 0
        var sum = 0
        for (i in 0..4) {
            if (i != 4) {
                temp = r.nextInt(100 - sum)

                load.add(temp)
                sum += temp
            } else {
                val last = 100 - sum
                load.add(last)
                sum += last
            }
        }
        return load
    }

    private fun sendWhatsapp(message : String){
        try {
            val  sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, message)
            sendIntent.setPackage("com.whatsapp")
            sendIntent.setType("text/plain")
            startActivity(sendIntent)
            pauseTimer()
        }catch (e:Exception){
            Toast.makeText(requireActivity(), "You do not have whatsapp installed", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Quiz.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Quiz().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}