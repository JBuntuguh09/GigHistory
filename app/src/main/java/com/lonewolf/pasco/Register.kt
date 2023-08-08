package com.lonewolf.pasco

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.pasco.databinding.ActivityRegisterBinding
import com.lonewolf.pasco.notification.Send
import com.lonewolf.pasco.resources.Constant
import com.lonewolf.pasco.resources.ShortCut_To
import com.lonewolf.pasco.resources.Storage

class Register : AppCompatActivity() {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var confirm : EditText
    private lateinit var fname : EditText
    private lateinit var phone : EditText
    private lateinit var register : Button
    private lateinit var spinGender : Spinner
    private lateinit var spinSchool : AutoCompleteTextView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: Storage
    private lateinit var progressBar: ProgressBar
    private lateinit var spinGrade : Spinner
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fname = findViewById(R.id.edtFName)
        phone = findViewById(R.id.edtPhone)
        email = findViewById(R.id.edtEmail)
        password = findViewById(R.id.edtPassword)
        confirm = findViewById(R.id.edtConfirm)
        register = findViewById(R.id.btnSignUp)
        progressBar = findViewById(R.id.progressBar)
        spinGender = findViewById(R.id.spinGender)
        spinSchool = findViewById(R.id.spinSchool)
        spinGrade = findViewById(R.id.spinGrade)

        storage = Storage(this)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        getButtons()

    }

    private fun getButtons() {

        fname.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0 != null) {
                    if (p0.toString().isNotEmpty()) {
                        binding.txtSymbol.text = p0.toString().substring(0..0).uppercase()
                    }else{
                        binding.txtSymbol.text = ""
                    }
                }
            }

        })
        register.setOnClickListener {
            if(fname.text.toString().isEmpty()){
                fname.error = getString(R.string.enter_f_name)
                Toast.makeText(this, getString(R.string.enter_f_name), Toast.LENGTH_SHORT).show()
            }else if(phone.text.toString().isEmpty()){
                phone.error = getString(R.string.enter_phone)
                Toast.makeText(this, getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show()
            }else if(email.text.toString().isEmpty()){
                email.error = getString(R.string.enter_email)
                Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show()
            }else if(!email.text.toString().contains("@")){
                email.error = getString(R.string.enter_valid_email)
                Toast.makeText(this, getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show()
            }else if(spinGender.selectedItemPosition==0){
                spinGender.prompt = getString(R.string.selectgender)
                Toast.makeText(this, getString(R.string.selectgender), Toast.LENGTH_SHORT).show()
            }else if(spinSchool.text.toString().isEmpty()){
                spinSchool.error = getString(R.string.selectSchool)
                Toast.makeText(this, getString(R.string.selectSchool), Toast.LENGTH_SHORT).show()
            }else if(spinGrade.selectedItemPosition==0){
                spinGrade.prompt = getString(R.string.selectGrade)
                Toast.makeText(this, getString(R.string.selectGrade), Toast.LENGTH_SHORT).show()
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

        val genderAdapt = ArrayAdapter(this, R.layout.spinner_item, ShortCut_To.gender)
        genderAdapt.setDropDownViewResource(R.layout.drop_down_layout)
        spinGender.adapter = genderAdapt

        val gradeAdapt = ArrayAdapter(this, R.layout.spinner_item, ShortCut_To.grade)
        gradeAdapt.setDropDownViewResource(R.layout.drop_down_layout)
        spinGrade.adapter = gradeAdapt


        val adapter = ArrayAdapter(
            this,
            R.layout.drop_down_layout,
            ShortCut_To.getSchools()
        )
        spinSchool.setAdapter(adapter)
    }

    private fun registerTo() {
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnSuccessListener {
            storage.uSERID = auth.currentUser?.uid
            auth.signInWithEmailAndPassword(Constant.username, Constant.password).addOnSuccessListener {
                val hashMap = HashMap<String, String>()
                hashMap["Name"] = fname.text.toString()
                hashMap["Phone"] = phone.text.toString()
                hashMap["Email"] = email.text.toString()
                hashMap["Password"] = password.text.toString()
                hashMap["Gender"] = spinGender.selectedItem.toString()
                hashMap["School"] = spinSchool.text.toString()
                hashMap["Created_Datetime"] = ShortCut_To.currentDatewithTime
                hashMap["Version"] = "Android"
                hashMap["UserId"] = storage.uSERID!!
                hashMap["Grade"] = spinGrade.selectedItem.toString()

                databaseReference.child("Users").child(storage.uSERID!!).setValue(hashMap).addOnSuccessListener {
                    Toast.makeText(this, "Successfully signed up", Toast.LENGTH_SHORT).show()
                    Send.notification(storage.uSERID!!, "Sign Up", "Welcome to GIG History", storage.tokenId!!, this, 0)
                    progressBar.visibility = View.GONE
                    storage.justLoggedIn = true
//                    val intent = Intent(this, MainBase::class.java)
//                    startActivity(intent)
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