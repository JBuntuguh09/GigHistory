package com.lonewolf.pasco

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.lonewolf.pasco.resources.Storage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.pasco.resources.Constant
import com.lonewolf.pasco.resources.ShortCut_To

class Register : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirm : EditText
    private lateinit var fname : EditText
    private lateinit var lname : EditText
    private lateinit var register : Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: Storage
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        fname = findViewById(R.id.edtFName)
        lname = findViewById(R.id.edtLName)
        email = findViewById(R.id.edtEmail)
        password = findViewById(R.id.edtPassword)
        confirm = findViewById(R.id.edtConfirm)
        register = findViewById(R.id.btnSignUp)
        progressBar = findViewById(R.id.progressBar)

        storage = Storage(this)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        getButtons()

    }

    private fun getButtons() {
        register.setOnClickListener {
            if(fname.text.toString().isEmpty()){
                fname.error = getString(R.string.enter_f_name)
                Toast.makeText(this, getString(R.string.enter_f_name), Toast.LENGTH_SHORT).show()
            }else if(lname.text.toString().isEmpty()){
                lname.error = getString(R.string.enter_last_name)
                Toast.makeText(this, getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show()
            }else if(email.text.toString().isEmpty()){
                email.error = getString(R.string.enter_email)
                Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            }else if(!email.text.toString().contains("@")){
                email.error = getString(R.string.enter_valid_email)
                Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show()
            }else if(password.text.toString().isEmpty()){
                password.error = getString(R.string.enter_password)
                Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show()
            }else if(!password.text.toString().equals(confirm.text.toString())){
                confirm.error = getString(R.string.confirm_pass)
                Toast.makeText(this, getString(R.string.confirm_pass), Toast.LENGTH_SHORT).show()
            }else{
                progressBar.visibility = View.VISIBLE
                registerTo()
            }
        }
    }

    private fun registerTo() {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnSuccessListener {
            storage.uSERID = auth.currentUser?.uid
            auth.signInWithEmailAndPassword(Constant.username, Constant.password).addOnSuccessListener {
                val hashMap = HashMap<String, String>()
                hashMap.put("First_Name", fname.text.toString())
                hashMap.put("Last_Name", lname.text.toString())
                hashMap.put("Email", email.text.toString())
                hashMap.put("Password", password.text.toString())
                hashMap.put("Created_Datetime", ShortCut_To.currentDatewithTime)
                hashMap.put("Version", "Android")
                hashMap.put("UserId", storage.uSERID!!)

                databaseReference.child("Users").child(storage.uSERID!!).setValue(hashMap).addOnSuccessListener {
                    Toast.makeText(this, "Successfully signed up", Toast.LENGTH_SHORT).show()
                    com.lonewolf.pasco.notification.Send.notification(storage.uSERID!!, "Sign Up", "Welcome to Pasco", storage.tokenId!!, this, 0)
                    progressBar.visibility = View.GONE
                    val intent = Intent(this, MainBase::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Log.d("num1", it.message.toString())
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }

            }.addOnFailureListener {
                Log.d("num2", it.message.toString())
                progressBar.visibility = View.GONE
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Log.d("num3", it.message.toString())
            progressBar.visibility = View.GONE
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        //super.onBackPressed()
    }
}