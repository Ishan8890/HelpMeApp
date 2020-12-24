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
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.navigation.NavigationView
import com.ibcorp.helpmeapp.databinding.ActivityDashboardMainBinding
import com.ibcorp.helpmeapp.databinding.NavHeaderMainBinding
import com.ibcorp.helpmeapp.ui.MainActivity


class DashboardMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding:ActivityDashboardMainBinding
    private var prefManager: PrefManager? = null
    private lateinit var headerBinding:NavHeaderMainBinding
    private lateinit var mGoogleSignInClient:GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_dashboard_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        prefManager = PrefManager(this)
        val bundle = intent.getBundleExtra("data")
        var headerView = binding.navView.getHeaderView(0)
        headerBinding = NavHeaderMainBinding.bind(headerView)
        if(bundle!=null){
            var first_name = bundle!!.getString("first_name")
            var last_name = bundle!!.getString("last_name")
            var email = bundle!!.getString("email")
            var id = bundle!!.getString("id")
            var image_url = bundle!!.getString("image_url")
            if((!first_name.isNullOrBlank()||!last_name.isNullOrBlank())&&!email.isNullOrBlank()&&!image_url.isNullOrBlank()){
                headerBinding.userName.text = first_name + " " +last_name
                headerBinding.userEmailId.text = email
                Glide.with(this).load(image_url).into(headerBinding.imageView);
            }
        }else{
            var username = prefManager!!.userName
            var email =  prefManager!!.emailId
            var imageurl = prefManager!!.imageUrl
            if((!username.isNullOrBlank())&&!email.isNullOrBlank()&&!imageurl.isNullOrBlank()){
                headerBinding.userName.text = username
                headerBinding.userEmailId.text = email
                Glide.with(this).load(imageurl).into(headerBinding.imageView);
            }
        }




        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_tuition
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