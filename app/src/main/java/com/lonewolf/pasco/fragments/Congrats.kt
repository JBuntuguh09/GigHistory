package com.lonewolf.pasco.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.Summary
import com.lonewolf.pasco.database.Answers
import com.lonewolf.pasco.database.answersViewModel
import com.lonewolf.pasco.databinding.FragmentCongratsBinding
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage
import com.squareup.picasso.Picasso
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Congrats.newInstance] factory method to
 * create an instance of this fragment.
 */
class Congrats : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCongratsBinding
    private lateinit var storage: Storage
    private lateinit var ansViewModel: answersViewModel
    private  var liveData: MutableLiveData<String> = MutableLiveData()

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
        val view = inflater.inflate(R.layout.fragment_congrats, container, false)
        binding = FragmentCongratsBinding.bind(view)
        storage = Storage(requireContext())



        getButtons()

        return view
    }

    private fun getButtons() {
        ansViewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(answersViewModel::class.java)

        ansViewModel.liveData.observe(viewLifecycleOwner) { data ->
            getResultsOffline(data)
        }
        binding.btnViewQues.setOnClickListener {
            if(storage.selectedCategory.equals("Objectives")){
                (activity as MainBase).navTo(
                    ObjectivesResults(),
                    "${storage.project} results",
                    "Congrats",
                    1
                )
            }else{
                (activity as MainBase).navTo(
                    SectionBResult(),
                    "${storage.project} results",
                    "Congrats",
                    1
                )
            }


        }
        binding.btnSummary.setOnClickListener {
            (activity as MainBase).navTo(
            Summary(),
            "${storage.project} results",
            "Summary",
            1
            )
        }

        if(storage.selectedCategory.equals("Objectives")){

        }else{
            binding.btnSummary.visibility = View.GONE
        }
    }

    private fun getResultsOffline(data: List<Answers>?) {
        val arrayList = ArrayList<HashMap<String, String>>()
        if (data != null) {
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
        }
        if(arrayList.size>0){
            var correct = 0
            var count = 1
            var attempt = 0
            var skip=0
            for(a in 0 until arrayList.size) {
                val hash = arrayList[a]
                if (hash["SelectAns"].equals(hash["Ans"])){
                    correct++
                }
                if (hash["Ans"].equals("")){
                    skip++
                }else{
                    attempt++
                }
                count += 1


            }
            val mScore = "Score : $correct/${arrayList.size}"
            var mPercent = (correct*100)/arrayList.size


            (activity as MainBase).grade = ShortCut_To.getGrade(mPercent)
            (activity as MainBase).score = "${correct}/${arrayList.size}"
            (activity as MainBase).attempted = attempt.toString()
            (activity as MainBase).skipped = skip.toString()
            (activity as MainBase).percentage = mPercent.toString()

            if(storage.selectedCategory.equals("Objectives")){
                binding.txtTotal.text = mScore
                binding.txtPercent.text = "$mPercent%"
                binding.txtGrade.text = ShortCut_To.getGrade(mPercent)
                binding.txtGrade.setTextColor(
                    ContextCompat.getColor(requireContext(), ShortCut_To.getColorGrade(mPercent))
                )
                Picasso.get().load(ShortCut_To.getBackground(mPercent))
                    .into(binding.imageView)
            }else{
                Picasso.get().load(ShortCut_To.getBackground(100))
                    .into(binding.imageView)
                mPercent =100
            }


            if(mPercent>=80){
                val party =Party(
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

            if(mPercent>=80){
               binding.txtMessage.text =  "Keep it up. Keep reading your history Notes"
            }else if(mPercent>=55){
                binding.txtMessage.text = "Read more on ${storage.project} in your history notes and" +
                        "bet at your best"
            }else{
                binding.txtMessage.text = "Read more on ${storage.project} in your history notes. " +
                        "We know you can do better"
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Congrats.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Congrats().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}