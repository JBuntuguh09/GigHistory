package com.lonewolf.pasco.adaptors

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.lonewolf.pasco.MainActivity
import com.lonewolf.pasco.MainBase
import com.lonewolf.pasco.R
import com.lonewolf.pasco.databinding.ActivitySplashBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.zip.Inflater

class Splash : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_splash)
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        }catch (e:Exception){
            e.printStackTrace()
        }
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navTo()
    }

    private fun navTo() {
        GlobalScope.launch {
            delay(3000)
            startActivity(Intent(this@Splash, MainBase::class.java))
            finish()
        }
    }
}