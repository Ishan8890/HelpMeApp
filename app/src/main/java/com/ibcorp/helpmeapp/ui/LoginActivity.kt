package com.ibcorp.helpmeapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.Utils
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
            val email = binding.loginEmailid.text
            val pwd = binding.loginPassword.text
            if(!email.isNullOrBlank()&& !pwd.isNullOrBlank()){
                if(email.contains("ibcorphelpme@gmail.com") && pwd.contains("1234")){
                    Utils.captureFirebaseEvents(email.toString(),"Admin Email","Admin Login",this)
                    startActivity(Intent(this, AdminActivity::class.java))
                }else{
                    CustomToast.snackbar("Incorrect email or pwd",binding.adView)
                }
            }else{
                CustomToast.snackbar("Fields is Empty",binding.adView)
            }

        }
    }
}