//package com.ibcorp.helpmeapp.Fragments
//
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageInfo
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.text.InputType
//import android.text.method.HideReturnsTransformationMethod
//import android.text.method.PasswordTransformationMethod
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.Animation
//import android.view.animation.AnimationUtils
//import android.widget.*
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import com.facebook.*
//import com.facebook.login.LoginBehavior
//import com.facebook.login.LoginResult
//import com.facebook.login.widget.LoginButton
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdView
//import com.google.android.gms.ads.MobileAds
//import com.google.android.gms.ads.RequestConfiguration
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.SignInButton
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.ibcorp.helpmeapp.DashboardMainActivity
//import com.ibcorp.helpmeapp.Model.CustomToast
//import com.ibcorp.helpmeapp.PrefManager
//import com.ibcorp.helpmeapp.R
//import com.ibcorp.helpmeapp.ui.home.AdminActivity
//import org.json.JSONException
//import org.json.JSONObject
//import java.security.MessageDigest
//import java.security.NoSuchAlgorithmException
//import java.util.*
//
//
//class Login_Fragment : Fragment(), View.OnClickListener {
//
//    private var emailid: EditText? = null
//    private  var password:EditText? = null
//    private var loginButton: LoginButton?=null
//    lateinit var googleSignIn:SignInButton
//    private var button: Button?=null
//    private var forgotPassword: TextView? = null
//    private  var signUp:TextView? = null
//    private var show_hide_password: CheckBox? = null
//    private var loginLayout: LinearLayout? = null
//    private var shakeAnimation: Animation? = null
//    private var mfragmentManager:FragmentManager?=null
//    private var mAdView: AdView? = null
//    private val EMAIL = "email"
//    private val RC_SIGN_IN = 234
//    var callbackManager:CallbackManager?=null
//    var TAG:String=""
//    private var prefManager: PrefManager? = null
//
//    override
//    fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        mview = inflater.inflate(R.layout.login_layout, container, false)
//        initViews(mview)
//        setListeners()
//        return mview
//    }
//
//    // Initiate Views
//    private fun initViews(view: View) {
//        //the variable it is always not null.
//        prefManager = context?.let { PrefManager(it) }
//        mfragmentManager = activity?.supportFragmentManager
//        mAdView = view.findViewById<View>(R.id.adView) as AdView
//
//        val testDeviceIds = Arrays.asList("63E143D82CC5EE32A453A9DE2638328D")
//        val builder =  RequestConfiguration.Builder()
//        val configuration: RequestConfiguration = builder.setTestDeviceIds(testDeviceIds).build()
//        MobileAds.setRequestConfiguration(configuration)
//
//        val adRequest: AdRequest = AdRequest.Builder().build()
//        mAdView!!.loadAd(adRequest)
//        emailid = view.findViewById<View>(R.id.login_emailid) as EditText
//        password = view.findViewById<View>(R.id.login_password) as EditText
//        forgotPassword = view.findViewById<View>(R.id.forgot_password) as TextView
//        signUp =  view.findViewById<View>(R.id.createAccount) as TextView
//        show_hide_password = view.findViewById<View>(R.id.show_hide_password) as CheckBox
//        loginLayout = view.findViewById<View>(R.id.login_layout) as LinearLayout
//        googleSignIn = view.findViewById(R.id.sign_in_button) as SignInButton
//
//
//        // Load ShakeAnimation
//        shakeAnimation = AnimationUtils.loadAnimation(
//            getActivity(),
//            R.anim.shake
//        )
//        callbackManager = CallbackManager.Factory.create();
//        loginButton =  view.findViewById(R.id.loginButton) as LoginButton
//        button =  view.findViewById(R.id.btn_login) as Button
//        loginButton!!.setFragment(this)
//        loginButton!!.setReadPermissions(Arrays.asList("email", "public_profile"));
//        loginButton!!.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
//
//        // Callback registration
//        loginButton!!.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//            override
//            fun onSuccess(loginResult: LoginResult?) {
//                // App code
//                val loggedIn = AccessToken.getCurrentAccessToken() == null
//                Log.d("API123", "$loggedIn ??")
//                getUserProfile(AccessToken.getCurrentAccessToken())
//            }
//
//            override
//            fun onCancel() {
//                Log.d("API123", "")
//            }
//
//            override
//            fun onError(exception: FacebookException?) {
//                // App code
//                Log.d("API123", "" + exception)
//            }
//        })
//        generateKeyHash()
//        googleSignIn.setOnClickListener(){
//            googleSignin()
//        }
//    }
//private fun googleSignin(){
//    var mAuth = FirebaseAuth.getInstance();
//    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build()
//    var mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
//    signIn(mGoogleSignInClient)
//}
//    private fun generateKeyHash():String{
//        var keyhash =""
//        try {
//            val info: PackageInfo = requireActivity().getPackageManager().getPackageInfo(
//                "com.ibcorp.helpmeapp",
//                PackageManager.GET_SIGNATURES
//            )
//            for (signature in info.signatures) {
//                val md: MessageDigest = MessageDigest.getInstance("SHA")
//                md.update(signature.toByteArray())
//                keyhash = android.util.Base64.encodeToString(
//                    md.digest(),
//                    android.util.Base64.DEFAULT
//                )
//                Log.d(
//                    "KeyHash:", android.util.Base64.encodeToString(
//                        md.digest(),
//                        android.util.Base64.DEFAULT
//                    )
//                )
//            }
//        } catch (e: PackageManager.NameNotFoundException) {
//        } catch (e: NoSuchAlgorithmException) {
//        }
//        return keyhash
//    }
//
//    private fun signIn(googleSignInClient: GoogleSignInClient) {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    private fun getUserProfile(currentAccessToken: AccessToken) {
//        val request: GraphRequest = GraphRequest.newMeRequest(
//            currentAccessToken, object : GraphRequest.GraphJSONObjectCallback {
//                override
//                fun onCompleted(`object`: JSONObject, response: GraphResponse?) {
//                    Log.d("TAG", `object`.toString())
//                    try {
//                        val first_name: String = `object`.getString("first_name")
//                        val last_name: String = `object`.getString("last_name")
//                        val email: String = `object`.getString("email")
//                        val id: String = `object`.getString("id")
//                        val image_url = "https://graph.facebook.com/$id/picture?type=normal"
//
//                        val intent = Intent(activity, DashboardMainActivity::class.java)
//                        intent.putExtra(
//                            "data",
//                            getData(first_name, last_name, email, image_url, id)
//                        )
//                        activity!!.startActivity(intent)
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                }
//            })
//        val parameters = Bundle()
//        parameters.putString("fields", "first_name,last_name,email,id")
//        request.setParameters(parameters)
//        request.executeAsync()
//    }
//
//    private fun getData(
//        first_name: String,
//        last_name: String,
//        email: String,
//        image_url: String,
//        id: String
//    ):Bundle{
//
//        var bundle = Bundle()
//        bundle.putString("first_name", first_name)
//        bundle.putString("last_name", last_name)
//        bundle.putString("email", email)
//        bundle.putString("id", id)
//        bundle.putString("image_url", image_url)
//        prefManager!!.token = id
//        prefManager!!.emailId = email
//        prefManager!!.imageUrl = image_url
//        prefManager!!.userName = first_name + " " +last_name
//
//        return bundle
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        callbackManager!!.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                val intent = Intent(activity, DashboardMainActivity::class.java)
//                intent.putExtra(
//                    "data", getData(
//                        account.displayName!!,
//                        "",
//                        account.email!!,
//                        account.photoUrl.toString(),
//                        account.id!!
//                    )
//                )
//                requireActivity().startActivity(intent)
////                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//                // ...
//            }
//        }
//    }
//    // Set Listeners
//    private fun setListeners() {
//        loginButton?.setOnClickListener(this)
//        forgotPassword?.setOnClickListener(this)
//        signUp!!.setOnClickListener(this)
//        button!!.setOnClickListener(this)
//
//        // Set check listener over checkbox for showing and hiding password
//        show_hide_password?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { button, isChecked ->
//            // If it is checkec then show password else hide
//            // password
//            if (isChecked) {
//                show_hide_password?.setText(R.string.hide_pwd) // change
//                // checkbox
//                // text
//                password?.setInputType(InputType.TYPE_CLASS_TEXT)
//                password?.setTransformationMethod(
//                    HideReturnsTransformationMethod
//                        .getInstance()
//                ) // show password
//            } else {
//                show_hide_password?.setText(R.string.show_pwd) // change
//                // checkbox
//                // text
//                password!!.setInputType(
//                    InputType.TYPE_CLASS_TEXT
//                            or InputType.TYPE_TEXT_VARIATION_PASSWORD
//                )
//                password?.setTransformationMethod(
//                    PasswordTransformationMethod
//                        .getInstance()
//                ) // hide password
//            }
//        })
//    }
//
//    override fun onClick(v: View) {
//        when (v.id) {
//            R.id.btn_login -> checkValidation()
//            R.id.forgot_password ->
//                // Replace forgot password fragment with animation
//                mfragmentManager?.beginTransaction()
//                    ?.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
//                    ?.replace(
//                        R.id.frameContainer,
//                        ForgotPassword_Fragment(),
//                        CustomToast.ForgotPassword_Fragment
//                    )?.commit()
//            R.id.createAccount ->
//                // Replace signup frgament with animation
//                mfragmentManager!!.beginTransaction().setCustomAnimations(
//                    R.anim.right_enter,
//                    R.anim.left_out
//                )
//                    .replace(
//                        R.id.frameContainer, SignUp_Fragment(),
//                        CustomToast.SignUp_Fragment
//                    ).commit()
//        }
//    }
//
//    // Check Validation before login
//    private fun checkValidation() {
////        startActivity(Intent(this@WelcomeActivity, AdminActivity::class.java))
////        mfragmentManager!!.beginTransaction().setCustomAnimations(
////            R.anim.right_enter,
////            R.anim.left_out
////        )
////            .replace(
////                R.id.frameContainer, HomeFragment(),
////                CustomToast.Home_Fragment
////            ).commit()
//        // Get email id and password
////        val getEmailId: String = emailid?.getText().toString()
////        val getPassword: String = password?.getText().toString()
////
////        // Check patter for email id
////        val p: Pattern = Pattern.compile(CustomToast.regEx)
////        val m: Matcher = p.matcher(getEmailId)
////
////        // Check for both field is empty or not
////        if (getEmailId == "" || getEmailId.length == 0 || getPassword == "" || getPassword.length == 0) {
////            loginLayout!!.startAnimation(shakeAnimation)
////            CustomToast.snackbar("Enter both credentials.", mview)
////        } else if (!m.find())
////            CustomToast.snackbar("Your Email Id is Invalid.", mview)
////        else
////            CustomToast.snackbar("Do Login.", mview)
//
////        var intent = Intent(activity, DashboardMainActivity::class.java)
////        startActivity(intent)
//    }
//
//    companion object {
//        lateinit var mview: View
//        private val emailid: EditText? = null
//        private val password: EditText? = null
//        private val loginButton: Button? = null
//        private val forgotPassword: TextView? = null
//        private val signUp: TextView? = null
//        private val show_hide_password: CheckBox? = null
//        private val loginLayout: LinearLayout? = null
//        private val shakeAnimation: Animation? = null
//        private val fragmentManager: FragmentManager? = null
//        lateinit var mGoogleSignInClient: GoogleSignInClient
//        lateinit var mContext:Context
//    }
//}