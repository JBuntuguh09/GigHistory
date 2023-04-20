package com.lonewolf.pasco.adaptors

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.lonewolf.pasco.resources.Storage
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.databinding.LayoutOptionsBinding
import com.lonewolf.pasco.dialog.show_me
import com.lonewolf.pasco.fragments.*

class RecyclerViewList(context: Context, arrayList : ArrayList<HashMap<String, String>>, type : Int)
    : RecyclerView.Adapter<RecyclerViewList.MyHolder>() {


    private var arrayList = ArrayList<HashMap<String, String>>()
    private var context : Context
    private var storage : Storage
    private var type : Int

    init {
        this.arrayList =arrayList
        this.context = context
        this.storage = Storage(context)
        this.type = type
    }

    inner class MyHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var button : Button

        init {
            button = itemview.findViewById(R.id.btnList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewList.MyHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_button,parent, false)

        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewList.MyHolder, position: Int) {
        val hash = arrayList[position]
        holder.button.setText(hash["val"])
        holder.button.setOnClickListener {
            if(type==0){
                if(storage.selectedCategory.equals("Objectives")){
                    storage.project = hash["val"]
                    //(context as MainBase).navTo(Objectives(), hash["val"]!!, "List",1)
                    showBottom(hash)

                }else if(storage.selectedCategory.equals("Section B")){
                    storage.project = hash["val"]
                    (context as MainBase).navTo(SectionB(), hash["val"]!!, "List",1)
                    // showBottom(hash)

                }else if(storage.selectedCategory.equals("Dictionary")){
                    storage.project = hash["val"]
                    (context as MainBase).navTo(Dictionary(), hash["val"]!!, "List",1)

                }else if(storage.selectedCategory.equals("WASSCE Textbook")){
                    storage.fragVal = hash["val"]
                    storage.project = hash["val"]
                    (context as MainBase).navTo(ListSub(), hash["val"]!!, "List", 1)
                }else if(storage.selectedCategory.equals("Quiz")){
                    storage.project = hash["val"]
                    if(hash["val"]!!.lowercase().equals("objective quiz")){
                        storage.randVal = "Objectives"
                    }else{
                        storage.randVal = "Dictionary"
                    }
                    if(hash["val"]!!.lowercase().equals("objective quiz") && storage.first1==0){
                        show_me.answerB(context as Activity, (context as MainBase).mainBaseBinding.linMain, 0)
                    }else if(hash["val"]!!.equals("Word Scramble") && storage.first2==0){
                        show_me.answerB(context as Activity, (context as MainBase).mainBaseBinding.linMain, 1)

                    }else if(hash["val"]!!.equals("Define Quiz") && storage.first3==0){
                        show_me.answerB(context as Activity, (context as MainBase).mainBaseBinding.linMain, 2)

                    }else if(hash["val"]!!.equals("Horde") && storage.first4==0){
                        show_me.answerB(context as Activity, (context as MainBase).mainBaseBinding.linMain, 3)

                    }else if(hash["val"]!!.equals("Missing Letters") && storage.first5==0){
                        show_me.answerB(context as Activity, (context as MainBase).mainBaseBinding.linMain, 4)

                    }else{
                        (context as MainBase).navTo(Quiz(), hash["val"]!!, "List", 1)
                    }

                }
            } else{
                storage.project = hash["val"]
                storage.randVal = hash["Note_Id"]
                (context as MainBase).navTo(WassceTextbook(), hash["val"]!!, "List Sub",1)

            }
        }
    }

    override fun getItemCount(): Int {
        return  arrayList.size
    }

    private fun showBottom(hash : HashMap<String, String>) {
        val dialogue = Dialog(context)
        dialogue.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = LayoutOptionsBinding.inflate(LayoutInflater.from(context))
        dialogue.setContentView(binding.root)
        binding.txtTopic.text = hash["val"]

        if(storage.selectedCategory.equals("Section B")){
            binding.radio1.text="1"
            binding.radio2.text="3"
            binding.radio3.text="6"
            binding.edtCustomNum.setText("1")

        }

        binding.radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
//                when(p1){
//                    R.id.radio4-> {
//                        binding.textInputNum.visibility = View.VISIBLE
//                        binding.edtCustomNum.setText("")
//                    }
//                    else->{
//                        binding.textInputNum.visibility = View.GONE
//                        if (p0 != null) {
//                            binding.edtCustomNum.setText(p0.findViewById<RadioButton?>(p1).text.toString())
//                        }
//                    }
//                }
                if (p0 != null) {
                    binding.edtCustomNum.setText(p0.findViewById<RadioButton?>(p1).text.toString())
                }



            }
        })

        binding.radioGroupTime.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener{
            override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                when(p1){
                    R.id.radioT4-> {
                        binding.textInputMin.visibility = View.VISIBLE
                        binding.textInputTime.visibility = View.VISIBLE
                        binding.edtCustomTime.setText("")
                    }
                    else->{
                        binding.textInputMin.visibility = View.GONE
                        binding.textInputTime.visibility = View.GONE

                        if (p0 != null) {
                            binding.edtCustomHr.setText("")
                            binding.edtCustomTime.setText(p0.findViewById<RadioButton?>(p1).text.toString().replace("min", ""))
                        }
                    }
                }



            }
        })



        binding.btnSubmit.setOnClickListener {
            if(binding.edtCustomNum.text.toString().isEmpty()){
                Toast.makeText(context, "Select the number of questions you want", Toast.LENGTH_SHORT).show()
            }else if(binding.edtCustomHr.text.toString().isEmpty() && binding.edtCustomTime.text.toString().isEmpty()){
                Toast.makeText(context, "Set your time", Toast.LENGTH_SHORT).show()
            }else {
                (context as MainBase).randomQuesTotal = binding.edtCustomNum.text.toString().toInt()

                var hr = 0
                var min = 0
                if(!binding.edtCustomHr.text.toString().isEmpty()){
                    hr = binding.edtCustomHr.text.toString().toInt() * 3600000
                }

                if(!binding.edtCustomTime.text.toString().isEmpty()){
                    min = binding.edtCustomTime.text.toString().toInt() * 60000
                }
                val time = hr+min
                (context as MainBase).quesTime = time
                dialogue.dismiss()

                if(storage.selectedCategory.equals("Section B")){
                    (context as MainBase).navTo(SectionB(), hash["val"]!!, "List", 1)
                } else {
                    (context as MainBase).navTo(Objectives(), hash["val"]!!, "List", 1)
                }
            }
        }

        dialogue.show()
        dialogue.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogue.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogue.window!!.attributes.windowAnimations = R.style.bottomAnim
        dialogue.window!!.setGravity(Gravity.BOTTOM)
    }


}