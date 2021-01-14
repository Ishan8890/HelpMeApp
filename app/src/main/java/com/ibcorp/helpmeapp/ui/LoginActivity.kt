package com.ibcorp.helpmeapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.LoginLayoutBinding
import com.ibcorp.helpmeapp.ui.home.AdminActivity

class LoginActivity : AppCompatActivity() {

    lateinit var binding:LoginLayoutBinding
    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.login_layout)
        init()

    }
    fun init(){
        var mfragmentManager = supportFragmentManager
        val adRequest: AdRequest = AdRequest.Builder().build()
        binding.adView!!.loadAd(adRequest)
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }
}