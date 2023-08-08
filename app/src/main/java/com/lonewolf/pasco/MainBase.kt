package com.lonewolf.pasco

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lonewolf.pasco.databinding.ActivityMainBaseBinding
import com.lonewolf.pasco.fragments.Leaderboard
import com.lonewolf.pasco.fragments.ListMain
import com.lonewolf.pasco.fragments.Quiz
import com.lonewolf.pasco.fragments.StartPage
import com.lonewolf.pasco.resources.Constant
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainBase : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storage: Storage
     lateinit var mainBaseBinding: ActivityMainBaseBinding
     lateinit var auth: FirebaseAuth
     var randomQuesTotal = 0
    var randomQuesArr = ArrayList<Int>()
    var quesTime = 0
     var objCompletedId = ""
    var welcome = "Welcome"
    var sectBAns = ArrayList<HashMap<String, String>>()
    var topQuizArray = ArrayList<HashMap<String, String>>()
    var topQuizInc = 0

    var bMenu = 0


    //Performance
    var grade =""
    var percentage = ""
    var score = ""
    var attempted =""
    var skipped =""
    var isQuiz = false

    var notes = HashMap<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_base)

        mainBaseBinding = ActivityMainBaseBinding.inflate(layoutInflater)
        setContentView(mainBaseBinding.root)
        //
        auth = FirebaseAuth.getInstance()
        storage = Storage(this)
        databaseReference = FirebaseDatabase.getInstance().reference


        getUser()
        navTo(StartPage(), "Home", "Login",0)
        getButtons()
        getTop()
    }

    private fun getButtons() {
        if(auth.currentUser==null){
            auth.signInWithEmailAndPassword(Constant.username, Constant.password).addOnSuccessListener {
                //getUser()
            }
        }



        mainBaseBinding.bottomBar.setOnItemSelectedListener {
            when (it.itemId){
                R.id.menuHome ->{
                    if (bMenu!=0){

                        if(arrayOf("Quiz", "Objectives").contains( storage.selectedCategory)){
                            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
                            alertDialog.setTitle("Alert")
                            alertDialog.setMessage(getString(R.string.endWithoutSubmitting))
                            alertDialog.setPositiveButton("Yes"){
                                    dialog, which->
                                startActivity(Intent(this, MainBase::class.java))
                                finish()
                            }

                            alertDialog.setNegativeButton("No"){
                                    dialog, which->
                                dialog.dismiss()
                            }
                            alertDialog.create().show()
                        }else {
                            startActivity(Intent(this, MainBase::class.java))
                            finish()
                        }

                    }
                    bMenu = 0
                    return@setOnItemSelectedListener true
                }

                R.id.menuShare ->{
                    Toast.makeText(this@MainBase, "Share", Toast.LENGTH_SHORT).show()
                    bMenu = 1
                   return@setOnItemSelectedListener true
                }

                R.id.menuMore ->{
                    if (bMenu!=2){
                        if(isQuiz){

                            mainBaseBinding.txtQuiz.visibility = View.GONE
                        }else{
                            mainBaseBinding.txtQuiz.visibility = View.VISIBLE
                            mainBaseBinding.txtQuiz.setOnClickListener {
                                storage.selectedCategory = "Quiz"
                                storage.fragValPrev = mainBaseBinding.txtTopic.text.toString()
                                navTo(ListMain(), "More", "Start", 1)
                                mainBaseBinding.txtQuiz.visibility = View.GONE
                                isQuiz = !isQuiz
                            }
                        }
                        isQuiz = !isQuiz
                        bMenu = 2
                    }
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        mainBaseBinding.imgLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        mainBaseBinding.frameMain.setOnClickListener {
            mainBaseBinding.txtQuiz.visibility = View.GONE
        }

        mainBaseBinding.txtLogin.setOnClickListener {
            if(auth.currentUser!=null){
                val alert = AlertDialog.Builder(this)
                alert.setTitle("Alert")
                alert.setMessage("Are you sure you want to log out?")
                alert.setPositiveButton("Yes") { dial, all ->
                    auth.signOut()
                }
                alert.setNegativeButton("No") { dial, all ->

                }

                alert.show()
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


        }

        setButtons(mainBaseBinding.txtHome, StartPage())
        setButtons(mainBaseBinding.txtQuizLeader, Leaderboard())
        setButtons(mainBaseBinding.txtDash, StartPage())
        setButtons(mainBaseBinding.txtTestScores, StartPage())
        setButtons(mainBaseBinding.txtNotification, StartPage())

    }

    fun navTo(frag : Fragment, page:String, prev: String,returnable : Int) {
        storage.currPage = page
        storage.fragValPrev = prev
        val fragmentManager = supportFragmentManager
         val fragmentTransaction = fragmentManager.beginTransaction()
        if(returnable==1){
            fragmentTransaction.replace(R.id.frameMain, frag, page).addToBackStack(page)
        }else if(returnable==2){
            fragmentTransaction.replace(R.id.frameMain, frag, page).addToBackStack(prev)
        }else if(returnable==3){
            fragmentTransaction.replace(R.id.frameMain, frag).addToBackStack("prev")
        }else{
            fragmentTransaction.replace(R.id.frameMain, frag, page)
        }

        fragmentTransaction.commit()
        mainBaseBinding.txtTopic.text = page



    }

    private fun getUser(){
        databaseReference.child("Users").child(storage.uSERID!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(father: DataSnapshot) {
                if(father.exists()){
                    if(father.child("Name").value.toString()!="null") {
                        welcome =
                            "Welcome ${father.child("Name").value.toString()}"
                    storage.uSERNAME = father.child("Name").value.toString()
                        mainBaseBinding.txtTopic.setText(welcome)
                    }
                }else{

                    Toast.makeText(this@MainBase, "Your account was not properly setup", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                val list = ArrayList<String>()
            }
        })

        databaseReference.child("Version").child("NewContentVersion").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    fun setButtons(txt : TextView, frag : Fragment){
        txt.setOnClickListener {
            if(auth.currentUser !=null) {
                if (txt.text.toString() != storage.currPage) {
                    navTo(frag, txt.text.toString(), "Start", 1)
                }
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            mainBaseBinding.drawLay.closeDrawer(GravityCompat.START)
        }
    }
    override fun onResume() {

        if(storage.justLoggedIn){
            storage.justLoggedIn = false
            getUser()
            mainBaseBinding.txtLogin.text = "Log out"

            mainBaseBinding.txtHome.isEnabled  = true
            mainBaseBinding.txtNotification.isEnabled  = true
            mainBaseBinding.txtDash.isEnabled  = true
            mainBaseBinding.txtProfile.isEnabled  = true
            mainBaseBinding.txtTestScores.isEnabled  = true

            //navTo()

        }
        super.onResume()
    }

    fun getTop(){
        databaseReference.child("Global").child("Quiz").child("All Time").limitToFirst(20).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                topQuizArray.clear()
                for(grand in p0.children){
                    for(father in grand.children){
                        val hashMap = HashMap<String, String>()
                        hashMap["name"] = father.child("name").value.toString()
                        hashMap["score"] = father.child("score").value.toString()
                        hashMap["userId"] = father.child("userId").value.toString()
                        hashMap["name"] = father.child("name").value.toString()
                        //hashMap["name"] = father.child("name").value.toString()

                        topQuizArray.add(hashMap)
                    }
                }
                ShortCut_To.sortNumericallyReverse(topQuizArray, "score")
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }


}