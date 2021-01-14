package com.ibcorp.helpmeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ibcorp.helpmeapp.Model.CustomToast
import com.ibcorp.helpmeapp.databinding.ActivityFragmentReplacementBinding
import com.ibcorp.helpmeapp.ui.LoginActivity
import com.ibcorp.helpmeapp.ui.MainActivity

class FragmentReplacementActivity : AppCompatActivity() {

    lateinit var binding:ActivityFragmentReplacementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_fragment_replacement)

//        var mfragmentManager = supportFragmentManager
//        // If savedinstnacestate is null then replace login fragment
//        if (savedInstanceState == null) {
//            mfragmentManager?.beginTransaction()?.replace(
//                R.id.frameContainer, HomeFragment(),
//                CustomToast.Home_Fragment
//            )?.commit()
//
//        }
    }
}