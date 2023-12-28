package com.ibcorp.helpmeapp.ui.home

import com.ibcorp.helpmeapp.model.source.User
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ibcorp.helpmeapp.Adapters.CommonReadAdapter
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentHomeBinding
import com.ibcorp.helpmeapp.ui.home.Constant.Companion.binding
import com.ibcorp.helpmeapp.ui.home.Constant.Companion.firestore
import java.io.File
import java.io.IOException


class AdminActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    lateinit var mStorageRef: StorageReference
    lateinit var topic:EditText
    lateinit var description:EditText
    lateinit var url:EditText
    lateinit var submit:Button


    var TAG:String=""
    //Image request code
    private val PICK_IMAGE_REQUEST = 1

    //storage permission code
    private val STORAGE_PERMISSION_CODE = 123
    lateinit var storage: FirebaseStorage
    //Bitmap to get image from gallery
    private var bitmap: Bitmap? = null

    //Uri to store the image uri
    private var filePath: Uri? = null

    val DATABASE_PATH_UPLOADS = "uploads"


    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.fragment_home)
        val view: View = binding.getRoot()
        Utils.captureFirebaseEvents("Admin Panel","Admin Mode","Admin Activity",this)
        if(CustomToast.isInternetAvailable(this)){
            binding.adminParent.visibility = View.VISIBLE
            binding.noInternet.visibility = View.GONE
            init()
        }else{
           binding.adminParent.visibility = View.GONE
            binding.noInternet.visibility = View.VISIBLE
        }
    }
    fun init(){
        Constant.context = this
        Constant.activity = this
        firestore = FirebaseFirestore.getInstance()
        Constant.mStorageReference = FirebaseStorage.getInstance().getReference();
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val domainList = resources.getStringArray(R.array.domain)
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storage = mStorageRef.storage
        requestStoragePermission()
        if (binding.domainName != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, domainList
            )
            adapter.setDropDownViewResource(R.layout.spinner_textview_align)
            binding.domainName.adapter = adapter
            binding.domainName.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    var oldDomainSelection = Constant.selectedDomain
                    Constant.selectedDomain = domainList[position]
                    if(!Constant.selectedDomain.equals(oldDomainSelection)){
                        Constant.dataList.clear()
                        binding.recordCount.text = "Record count: 0"
                    }
//                    for(data in domainList){
//                        if(!data.equals(oldDomainSelection)){
//                            Constant.dataList.clear()
//                        }
//                    }
//                    Toast.makeText(activity, "" + domainList[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        binding.docs.setOnClickListener {
            Utils.captureFirebaseActionEvents("Document Upload","Admin",this)
            binding.rootDocumentUpload.visibility = View.VISIBLE
//            binding.queryId.root.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
        }
        binding.ids.setOnClickListener {
            Utils.captureFirebaseActionEvents("Query Reply","Admin",this)
            binding.rootDocumentUpload.visibility = View.GONE
              binding.recyclerView.visibility = View.VISIBLE
            Constant.readDataFromFirestore()
//            binding.queryId.root.visibility = View.VISIBLE
        }
        binding.uploadPDF.setOnClickListener() {
            Constant.getPDF(this,this,false)
        }
        binding.uploadData.setOnClickListener(){
            var topic = binding.topic.text.toString()
            var desc = binding.description.text.toString()
            var url = binding.url.text.toString()
            var user = User()
            if (!topic.isBlank() && !desc.isBlank() && !url.isBlank() && !Constant.selectedDomain.isBlank()) {
                user.description = desc
                user.topic = topic
                user.url = Constant.url
                Constant.uploadData(user,Constant.selectedDomain)
//                Constant.dataList.add(d)
                binding.topic.text.clear()
                binding.description.text.clear()
                binding.url.text.clear()
                binding.recordCount.text = "Record Count :"+Constant.dataList.size
            }else{
                CustomToast.snackbar("Data is Empty", binding.adminParent)
            }


            }


    }










    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "application/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === Constant.PICK_PDF_CODE && resultCode === RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData()
            Constant.uploadFile(filePath!!,this,this)
            var test = File(filePath!!.path)
            try {
//                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
//                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    //Requesting permission
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(
                    this,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
class Constant{
    companion object{
        lateinit var context: Context
        lateinit var activity: Activity
        var dataList:ArrayList<User> = ArrayList()
        var mStorageReference: StorageReference? = null
        val STORAGE_PATH_UPLOADS = "uploads/"
        val PICK_PDF_CODE = 2342
        lateinit var binding: FragmentHomeBinding
        lateinit var filePath:Uri
        lateinit var firestore: FirebaseFirestore
        var selectedDomain:String = ""
        var url:String = ""
        var isComeFromQuery:Boolean = false

         fun getPDF(context:Context,activity:Activity,isQuerySolved:Boolean) {
             isComeFromQuery =  isQuerySolved
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + context.getPackageName())
                )
                context.startActivity(intent)
                return
            }

            //creating an intent for file chooser
            val intent = Intent()
            intent.type = "application/pdf"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(activity,Intent.createChooser(intent, "Select Picture"), Constant.PICK_PDF_CODE,null)
        }


          fun uploadFile(data: Uri,activity: Activity,context: Context) {
              if(isComeFromQuery){
                  CommonReadAdapter.showProgressBar()
              }else{
                  binding.progressBar.visibility = View.VISIBLE
              }

            val sRef: StorageReference = mStorageReference!!.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf")
            try{
                sRef.putFile(data).addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                    Log.d("", "Success")
//                var docUrl = taskSnapshot.uploadSessionUri.toString()


                    sRef.downloadUrl.addOnSuccessListener {
                        if(isComeFromQuery){
                            CommonReadAdapter.hideProgressBar()
                        }else{
                            binding.progressBar.visibility = View.GONE
                        }
                        Constant.url = it.toString()
                        binding.url.setText(Constant.url, TextView.BufferType.EDITABLE)
//                        binding.url.isEnabled = false
                    }
//                    val downloadUrl: Uri = taskSnapshot.getDownloadUrl()
                }
                    .addOnFailureListener {
                        Log.d("", "Failure" + it)
                        // Handle unsuccessful uploads
                        // ...
                    }
                    .addOnFailureListener {
                        fun onFailure(@NonNull exception: Exception) {
                            Toast.makeText(context, exception.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
            }catch (ex:Exception){
                Log.d("",""+ex.toString())
            }
        }

        fun uploadData(user: User, selectedDomain:String){
            firestore.collection(selectedDomain).add(user)
                .addOnSuccessListener {
                    if(user.isAnswered){
                        Utils.captureFirebaseReplyDataEvents(user.topic,"Queries", selectedDomain,activity)
                    }else{
                        Utils.captureUploadEvents(user.topic,"Document Upload",selectedDomain, activity)
                    }

                    CustomToast.snackbar("Upload successfully!", binding.adminParent)
                    if(user.isAnswered)
                    deleteUserQuery(user)
                }
                .addOnCanceledListener {
                    CustomToast.snackbar("Error writing document!", binding.adminParent)
                }
        }

        fun deleteUserQuery(user: User){
            firestore.collection("Tuition").document(user.docId).delete()
                .addOnSuccessListener {
//                    CustomToast.snackbar("Delete successfully!", binding.adminParent)
                    readDataFromFirestore()
                }
                .addOnCanceledListener {
                    CustomToast.snackbar("Error writing document!", binding.adminParent)
                }
        }


         fun readDataFromFirestore(){
            var mFirestore = FirebaseFirestore.getInstance()
            binding.progressBar.visibility = View.VISIBLE
//        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

            mFirestore.collection("Tuition").get()
                .addOnSuccessListener {
                    var userList:List<User> = it.toObjects(User::class.java)
                    if(!userList.isNullOrEmpty()){
                        binding.progressBar.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        Utils.captureUserTypeEvents("Admin Queries",userList.size.toString(), activity)
                        val adapter = CommonReadAdapter(userList, Constant.context,true,Constant.activity,object : RefreshView {
                            override fun getRefreshData() {
//                                CustomToast.snackbar("Refresh view", binding.adminParent)
                            }
                        })
                        binding.recyclerView.adapter = adapter
                    }else{
                        Toast.makeText(
                            Constant.context,
                            "No Query Available.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

    }
}
interface RefreshView {
     fun getRefreshData()
}