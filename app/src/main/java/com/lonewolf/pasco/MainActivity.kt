package com.lonewolf.pasco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.lonewolf.pasco.resources.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.lonewolf.pasco.resources.Constant
const val TOPIC = "/topics/myTopic"
class MainActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var submit: Button
    private lateinit var register: TextView
    private lateinit var forgot: TextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : Storage
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        storage = Storage(this)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        submit = findViewById(R.id.btnLogin)
        email = findViewById(R.id.edtEmail)
        password = findViewById(R.id.edtPassword)
        register = findViewById(R.id.txtRegister)
        forgot = findViewById(R.id.txtForgot)
        progressBar = findViewById(R.id.progressBar)


        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            storage.tokenId = it.toString()

        }

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)


        getButtons()
    }

    private fun getButtons() {
        submit.setOnClickListener {
            if(email.text.toString().isEmpty()){
                email.error = "Enter your email"
                Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            }else if (!email.text.toString().contains("@")){
                email.error = getString(R.string.enter_valid_email)
                Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show()

            } else if (password.text.toString().isEmpty()){
              password.error = getString(R.string.enter_password)
                Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()

            }else{
                progressBar.visibility = View.VISIBLE
                loginTo()
            }

        }

        register.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun loginTo() {
        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnSuccessListener {
            storage.uSERID = auth.currentUser?.uid
            auth.signInWithEmailAndPassword(Constant.username, Constant.password).addOnSuccessListener {
                val intent = Intent(this, MainBase::class.java)
                startActivity(intent)
                finish()
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Successfully logged in", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            progressBar.visibility = View.GONE
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }
}