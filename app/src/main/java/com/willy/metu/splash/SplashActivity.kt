package com.willy.metu.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.willy.metu.MainActivity
import com.willy.metu.R
import com.willy.metu.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val duration = 2500L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        Handler().postDelayed({

            startActivity(Intent(this, MainActivity::class.java))

            leave(this)

        }, duration)
    }


    fun leave(view: SplashActivity) {
        finish()
    }

}