package com.ibcorp.helpmeapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.ibcorp.helpmeapp.DashboardMainActivity
import com.ibcorp.helpmeapp.Fragments.Login_Fragment
import com.ibcorp.helpmeapp.Model.CustomToast
import com.ibcorp.helpmeapp.PrefManager
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    private var mAdView: AdView? = null
    private val EMAIL = "email"
    private val RC_SIGN_IN = 234
    var callbackManager: CallbackManager?=null
    var TAG:String=""
    private var prefManager: PrefManager? = null


    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        init()

        mfragmentManager = supportFragmentManager
//        checkAppStatus()
        // If savedinstnacestate is null then replace login fragment
        binding.admin.setOnClickListener {
            binding.btnParent.visibility = View.GONE
            if (savedInstanceState == null) {
                mfragmentManager?.beginTransaction()?.replace(
                    R.id.frameContainer, Login_Fragment(),
                    CustomToast.Login_Fragment
                )?.commit()
            }
        }
    }


    fun init(){
        prefManager = PrefManager(this)
        callbackManager = CallbackManager.Factory.create()
        binding.loginButton.setReadPermissions(Arrays.asList("email", "public_profile"))
        binding.loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        binding.loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override
            fun onSuccess(loginResult: LoginResult?) {
                // App code
                val loggedIn = AccessToken.getCurrentAccessToken() == null
                Log.d("API123", "$loggedIn ??")
                getUserProfile(AccessToken.getCurrentAccessToken())
            }

            override
            fun onCancel() {
                Log.d("API123", "")
            }

            override
            fun onError(exception: FacebookException?) {
                // App code
                Log.d("API123", "" + exception)
            }
        })

        binding.signInButton.setOnClickListener(){
            googleSignin()
        }

    }

    private fun googleSignin(){
        var mAuth = FirebaseAuth.getInstance();
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn(mGoogleSignInClient)
    }

    private fun signIn(googleSignInClient: GoogleSignInClient) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun getUserProfile(currentAccessToken: AccessToken) {
        val request: GraphRequest = GraphRequest.newMeRequest(
            currentAccessToken, object : GraphRequest.GraphJSONObjectCallback {
                override
                fun onCompleted(`object`: JSONObject, response: GraphResponse?) {
                    Log.d("TAG", `object`.toString())
                    try {
                        val first_name: String = `object`.getString("first_name")
                        val last_name: String = `object`.getString("last_name")
                        val email: String = `object`.getString("email")
                        val id: String = `object`.getString("id")
                        val image_url = "https://graph.facebook.com/$id/picture?type=normal"

                        val intent = Intent(this@MainActivity, DashboardMainActivity::class.java)
                        intent.putExtra("data", getData(first_name,last_name,email,image_url,id))
                        startActivity(intent)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        val parameters = Bundle()
        parameters.putString("fields", "first_name,last_name,email,id")
        request.setParameters(parameters)
        request.executeAsync()
    }

    private fun getData(first_name:String,last_name:String,email:String,image_url:String,id:String):Bundle{

        var bundle = Bundle()
        bundle.putString("first_name", first_name)
        bundle.putString("last_name", last_name)
        bundle.putString("email", email)
        bundle.putString("id", id)
        bundle.putString("image_url", image_url)
        prefManager!!.token = id
        prefManager!!.emailId = email
        prefManager!!.imageUrl = image_url
        prefManager!!.userName = first_name + " " +last_name

        return bundle
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val intent = Intent(this, DashboardMainActivity::class.java)
                intent.putExtra("data", getData(account.displayName!!,"",account.email!!,account.photoUrl.toString(),account.id!!))
                startActivity(intent)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    /*fun checkAppStatus(){
        var mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    val bannerData = mFirebaseRemoteConfig.getString("ad_codes")
                    Log.d("TAG", "Config params updated: $updated")
                } else {
                   CustomToast.snackbar("Fetch failed", button)
                }
//                displayWelcomeMessage()
            }
    }*/

    // Replace Login Fragment with animation
     fun replaceLoginFragment() {
        mfragmentManager?.beginTransaction()
            ?.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
            ?.replace(
                R.id.frameContainer, Login_Fragment(),
                CustomToast.Login_Fragment
            )?.commit()
    }
    override
    fun onBackPressed() {
        super.onBackPressed()
        // Find the tag of signup and forgot password fragment
//        val SignUp_Fragment: Fragment = mfragmentManager?.findFragmentByTag(Utils.SignUp_Fragment)!!
//        val ForgotPassword_Fragment: Fragment = mfragmentManager?.findFragmentByTag(Utils.ForgotPassword_Fragment)!!
//
//        // Check if both are null or not
//        // If both are not null then replace login fragment else do backpressed
//        // task
//        if (SignUp_Fragment != null) replaceLoginFragment() else if (ForgotPassword_Fragment != null) replaceLoginFragment() else super.onBackPressed()
    }

    companion object {
        private var mfragmentManager: FragmentManager? = null
    }
}
