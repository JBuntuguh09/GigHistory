package com.lonewolf.pasco.dialog

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.*

import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.fragments.Quiz
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage

object show_me {
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var alert: AlertDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage

    fun question(activity: Activity, linearLayout: LinearLayout, num : String, myAns : String){
        storage = Storage(activity)
        databaseReference = FirebaseDatabase.getInstance().reference
        alertDialog = AlertDialog.Builder(activity)
        alert = alertDialog.create()

        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(R.layout.layout_obj_sel, linearLayout, false)

        val question = view.findViewById<TextView>(R.id.txtQuestion)
        val ansA = view.findViewById<TextView>(R.id.radioA)
        val ansB = view.findViewById<TextView>(R.id.radioB)
        val ansC = view.findViewById<TextView>(R.id.radioC)
        val ansD = view.findViewById<TextView>(R.id.radioD)
        val txtNum = view.findViewById<TextView>(R.id.txtNum)


        databaseReference.child("Questions").child("History")
            .child(storage.project!!).child(num)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(p0: DataSnapshot) {
                    question.text = p0.child("Question").value.toString()
                    ansA.text = "A. ${p0.child("AnswerA").value.toString()}"
                    ansB.text = "B. ${p0.child("AnswerB").value.toString()}"
                    ansC.text = "C. ${p0.child("AnswerC").value.toString()}"
                    ansD.text = "D. ${p0.child("AnswerD").value.toString()}"
                    txtNum.text = "${num}. "


                    if(p0.child("SelectAns").value.toString() == "A"){
                        ansA.setTextColor(activity.resources.getColor(R.color.green))
                    }else if(p0.child("SelectAns").value.toString() == "B"){
                        ansB.setTextColor(activity.resources.getColor(R.color.green))
                    }else if(p0.child("SelectAns").value.toString() == "C"){
                        ansC.setTextColor(activity.resources.getColor(R.color.green))
                    }else if(p0.child("SelectAns").value.toString() == "D"){
                        ansD.setTextColor(activity.resources.getColor(R.color.green))
                    }

                    if(p0.child("SelectAns").value.toString() != "A"
                        && myAns == "A"
                    ){
                        ansA.setTextColor(activity.resources.getColor(R.color.red))
                    }else if(p0.child("SelectAns").value.toString() != "B"
                        && myAns == "B"){
                        ansB.setTextColor(activity.resources.getColor(R.color.red))
                    }else if(p0.child("SelectAns").value.toString() != "C"
                        && myAns == "C"){
                        ansC.setTextColor(activity.resources.getColor(R.color.red))
                    }else if(p0.child("SelectAns").value.toString() != "D"
                        && myAns == "D"){
                        ansD.setTextColor(activity.resources.getColor(R.color.red))
                    }




                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        alert.setView(view)
        alert.show()
    }

    fun wordDic(activity: Activity, linearLayout: LinearLayout,
                hashMap: HashMap<String, String>){
        storage = Storage(activity)
        alertDialog = AlertDialog.Builder(activity)
        alert = alertDialog.create()

        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(R.layout.layout_dictionary_dialogue, linearLayout, false)

        val word = view.findViewById<TextView>(R.id.txtWord)
        val meaning = view.findViewById<TextView>(R.id.txtMeaning)

        word.text = hashMap["Word"]
        meaning.text = hashMap["Meaning"]

        alert.setView(view)
        alert.show()
    }

    fun answerB(activity: Activity, linearLayout : LinearLayout, num : Int){
        storage = Storage(activity)
        alertDialog = AlertDialog.Builder(activity)
        alert = alertDialog.create()

        val layoutInflater = LayoutInflater.from(activity)
        val view = layoutInflater.inflate(R.layout.layout_answerb, linearLayout, false)

        val topic = view.findViewById<TextView>(R.id.txtTopic)
        val meaning = view.findViewById<TextView>(R.id.txtAnswer)
        val ok = view.findViewById<Button>(R.id.btnSubmit)

        topic.text = "Instructions"
        meaning.text = ShortCut_To.getInstructions(num)

        ok.setOnClickListener {
            alert.dismiss()
            (activity as MainBase).navTo(Quiz(), "Quiz", "List", 1)
            if(num==0){
                storage.first1 = 1
            }else if(num==1){
                storage.first2 = 1
            }else if(num==2){
                storage.first3 = 1
            }else if(num==3){
                storage.first4 = 1
            }else if(num==4){
                storage.first5 = 1
            }
        }

        ok.visibility = View.VISIBLE


        alert.setView(view)
        alert.show()
    }
}