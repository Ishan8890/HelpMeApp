package com.ibcorp.helpmeapp.ui

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.facebook.*
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginResult
import com.google.android.gms.ads.AdView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.ibcorp.helpmeapp.DashboardMainActivity
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.PrefManager
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.ActivityMainBinding
import com.ibcorp.helpmeapp.model.CustomToast
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        changePassword()

//        Intent(this, HelloService::class.java).also { intent ->
//
//            if (!isMyServiceRunning(HelloService::class.java)) {
//                startService(intent);
//            }
////            startService(intent)
//        }


        init()

        mfragmentManager = supportFragmentManager
//        checkAppStatus()
        // If savedinstnacestate is null then replace login fragment
        binding.admin.setOnClickListener {
            Utils.captureFirebaseEvents("Admin","Admin","Admin",this)
            startActivity(Intent(this, LoginActivity::class.java))
//            binding.btnParent.visibility = View.GONE
//            if (savedInstanceState == null) {
//                mfragmentManager?.beginTransaction()?.replace(
//                    R.id.frameContainer, Login_Fragment(),
//                    CustomToast.Login_Fragment
//                )?.commit()
//            }
        }
    }

    private fun changePassword() {
         FirebaseAuth.getInstance().sendPasswordResetEmail("ishanb30@gmail.com")
//            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(Activity.this, "Password Reset Email Sent!"), Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(Activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
              .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
//                            val user = auth.currentUser
//                            updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
//                            updateUI(null)
                        }
                    }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.getClassName()) {
                Log.i("Service status", "Running")
                return true
            }
        }
        Log.i("Service status", "Not running")
        return false
    }



    fun init(){
        prefManager = PrefManager(this)
        callbackManager = CallbackManager.Factory.create()
        binding.loginButton.setReadPermissions(Arrays.asList("email", "public_profile","user_friends"))
        binding.loginButton.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        binding.guest.setOnClickListener {
            if(binding.guest.text.equals("Guest")){
                binding.etGuest.visibility = View.VISIBLE
                binding.btnParent.visibility = View.GONE
                binding.guest.text = "Login"
            }else if(binding.guest.text.equals("Login")){
                var userName = binding.etGuest.text.toString()
                if(!userName.isNullOrBlank()){
//                    binding.guest.text = "Guest"
                    val intent = Intent(this, DashboardMainActivity::class.java)
                    intent.putExtra(
                        "data", getData(
                            userName,
                            "",
                            "guest@helpme.com",
                            "",
                            "Guest",false
                        )
                    )
                    Utils.captureFirebaseEvents("Guest User","Guest","Login",this)
                    startActivity(intent)
                    finish()
                }else{
                    CustomToast.snackbar("Field is Empty",binding.btnParent)
                }

            }
        }
        binding.loginButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
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
        getToken()
    }

    @SuppressLint("StringFormatInvalid")
    fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            prefManager!!.firebaseToken = token
            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun googleSignin(){
//        var mAuth = FirebaseAuth.getInstance();
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn(mGoogleSignInClient)
    }

    private fun signIn(googleSignInClient: GoogleSignInClient) {
        binding.llProgressBar.visibility =  View.VISIBLE
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
                        Utils.captureFirebaseEvents(first_name+""+last_name,"Facebook","Login",applicationContext)
                        val intent = Intent(this@MainActivity, DashboardMainActivity::class.java)
                        intent.putExtra(
                            "data",
                            getData(first_name, last_name, email, image_url, id,true)
                        )
                        startActivity(intent)
                        finish()
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

    private fun getData(
        first_name: String,
        last_name: String,
        email: String,
        image_url: String,
        id: String,isFacebook:Boolean
    ):Bundle{

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
        if(isFacebook){
            prefManager!!.loginChannel = "Facebook"
        }else{
            prefManager!!.loginChannel = "Google"
        }

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
                var userName = account.displayName
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                val intent = Intent(this, DashboardMainActivity::class.java)
                intent.putExtra(
                    "data", getData(
                        account.displayName!!,
                        "",
                        account.email!!,
                        account.photoUrl.toString(),
                        account.id!!,false
                    )
                )
                Utils.captureFirebaseEvents(userName!!,"Google","Login",this)
                startActivity(intent)
                binding.llProgressBar.visibility =  View.GONE
                finish()
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

    override fun onBackPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        private var mfragmentManager: FragmentManager? = null
    }
}
