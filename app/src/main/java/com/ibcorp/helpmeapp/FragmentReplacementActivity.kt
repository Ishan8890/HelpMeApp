package com.ibcorp.helpmeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.ibcorp.helpmeapp.databinding.ActivityFragmentReplacementBinding

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