package com.ibcorp.helpmeapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ibcorp.helpmeapp.model.UserDetail
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.databinding.ActivityDashboardMainBinding
import com.ibcorp.helpmeapp.databinding.NavHeaderMainBinding
import com.ibcorp.helpmeapp.ui.MainActivity


class DashboardMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding:ActivityDashboardMainBinding
    private var prefManager: PrefManager? = null
    private lateinit var headerBinding:NavHeaderMainBinding
    private lateinit var mGoogleSignInClient:GoogleSignInClient
    private lateinit var username:String
    private lateinit var emaidId:String
    private lateinit var imageUrl:String
    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_dashboard_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        prefManager = PrefManager(this)
        val bundle = intent.getBundleExtra("data")
        var headerView = binding.navView.getHeaderView(0)
        headerBinding = NavHeaderMainBinding.bind(headerView)
        database = Firebase.database.reference
        if(bundle!=null){
            var first_name = bundle!!.getString("first_name")
            var last_name = bundle!!.getString("last_name")
            username = first_name + " " +last_name
            emaidId = bundle!!.getString("email").toString()
            imageUrl = bundle!!.getString("image_url").toString()
            if((!first_name.isNullOrBlank()||!last_name.isNullOrBlank())&&!emaidId.isNullOrBlank()&&!imageUrl.isNullOrBlank()){
                headerBinding.userName.text = username
                headerBinding.userEmailId.text = emaidId
                Glide.with(this).load(imageUrl).into(headerBinding.imageView);
            }
        }else{
            username = prefManager!!.userName
            emaidId =  prefManager!!.emailId
            imageUrl = prefManager!!.imageUrl
            if((!username.isNullOrBlank())&&!emaidId.isNullOrBlank()&&!imageUrl.isNullOrBlank()){
                headerBinding.userName.text = username
                headerBinding.userEmailId.text = emaidId
                Glide.with(this).load(imageUrl).into(headerBinding.imageView);
            }
        }
        headerBinding.appVersion.text = "v"+BuildConfig.VERSION_NAME
        var userDetail =  UserDetail()
        userDetail.username = username
        userDetail.email = emaidId
        var childName = ""
        if(emaidId.equals("guest@helpme.com")){
            userDetail.loginChannel = "Guest"
            childName = "Guest"
            var ref = database.child(childName)
            userDetail.token = ref.push().key!!
        }else{
            userDetail.token = prefManager!!.token
            userDetail.loginChannel = prefManager!!.loginChannel
            childName = "Users"
        }

        Utils.writeNewUser(userDetail,database,binding.root,"",childName,false)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_tuition,R.id.nav_inbox
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener { menuItem ->
//            logoutDialog
            val accessToken = AccessToken.getCurrentAccessToken()
            if (accessToken != null) {
                LoginManager.getInstance().logOut()
            } else if (GoogleSignIn.getLastSignedInAccount(this) != null) {
                mGoogleSignInClient.signOut()
            }
            var isLaunched = prefManager!!.isFirstTimeLaunch
            prefManager!!.clearData()
            prefManager!!.isFirstTimeLaunch = isLaunched
            startActivity(Intent(this,MainActivity::class.java))
            finish()
            true
        }
    }

    override fun onStart() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        super.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        finish()
    }
}