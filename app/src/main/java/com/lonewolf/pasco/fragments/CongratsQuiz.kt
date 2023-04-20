package com.lonewolf.pasco.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.databinding.FragmentCongratsQuizBinding
import com.lonewolf.pasco.resources.Storage
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CongratsQuiz.newInstance] factory method to
 * create an instance of this fragment.
 */
class CongratsQuiz : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentCongratsQuizBinding
    private lateinit var storage: Storage

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
        val view = inflater.inflate(R.layout.fragment_congrats_quiz, container, false)
        binding = FragmentCongratsQuizBinding.bind(view)
        storage = Storage(requireContext())

        getButtons()

        return view
    }

    private fun getButtons() {

        val score = (activity as MainBase).score.toInt()

        if(score>=20){
            binding.imageView.setImageResource(R.drawable.high)
        }else if(score>=3){
            binding.imageView.setImageResource(R.drawable.medium)
        }else{
            binding.imageView.setImageResource(R.drawable.small)
        }

        if(storage.project.equals(getString(R.string.cat1))){
            if(score>storage.highscore1!!.toInt()) {
                storage.highscore1 = score.toString()

                confeti()
            }
            binding.txtLocalHS.text = "HI-SCORE \n${storage.highscore1}"
        }else if(storage.project.equals(getString(R.string.cat2))){
            if(score>storage.highscore2!!.toInt()) {
                storage.highscore2 = score.toString()

                confeti()
            }
            binding.txtLocalHS.text = "HI-SCORE \n${storage.highscore2}"
        }else if(storage.project.equals(getString(R.string.cat3))){
            if(score>storage.highscore3!!.toInt()) {
                storage.highscore3 = score.toString()

                confeti()
            }
            binding.txtLocalHS.text = "HI-SCORE \n${storage.highscore3}"
        }else if(storage.project.equals(getString(R.string.cat4))){
            if(score>storage.highscore4!!.toInt()) {
                storage.highscore4 = score.toString()

                confeti()
            }
            binding.txtLocalHS.text = "HI-SCORE \n${storage.highscore4}"
        }else if(storage.project.equals("Missing Letters")){
            if(score>storage.highscore5!!.toInt()) {
                storage.highscore5 = score.toString()

                confeti()
            }
            binding.txtLocalHS.text = "HI-SCORE \n${storage.highscore5}"
        }
        binding.txtGrade.text = "Score\n${score}"



    }

    private fun confeti(){
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

        binding.txtMessage.blink()
    }

    fun View.blink(
        times: Int = Animation.INFINITE,
        duration: Long = 1000L,
        offset: Long = 20L,
        minAlpha: Float = 0.0f,
        maxAlpha: Float = 1.0f,
        repeatMode: Int = Animation.REVERSE
    ) {
        startAnimation(AlphaAnimation(minAlpha, maxAlpha).also {
            it.duration = duration
            it.startOffset = offset
            it.repeatMode = repeatMode
            it.repeatCount = times
        })

        binding.txtMessage.visibility = View.VISIBLE


    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CongratsQuiz.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CongratsQuiz().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}