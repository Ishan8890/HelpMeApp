package com.ibcorp.helpmeapp.ui.home

import Model
import User
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.ibcorp.helpmeapp.Model.CustomToast
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentHomeBinding
import java.io.File
import java.io.IOException


open class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var mStorageRef: StorageReference
    lateinit var topic:EditText
    lateinit var description:EditText
    lateinit var url:EditText
    lateinit var submit:Button
    lateinit var firestore: FirebaseFirestore
    lateinit var binding: FragmentHomeBinding
    var TAG:String=""
    var selectedDomain:String = ""
    lateinit var dataModel:Model
    val PICK_PDF_CODE = 2342

    //the firebase objects for storage and database
    var mStorageReference: StorageReference? = null

    //Image request code
    private val PICK_IMAGE_REQUEST = 1

    //storage permission code
    private val STORAGE_PERMISSION_CODE = 123
    lateinit var storage: FirebaseStorage
    //Bitmap to get image from gallery
    private var bitmap: Bitmap? = null

    //Uri to store the image uri
    private var filePath: Uri? = null
    val STORAGE_PATH_UPLOADS = "uploads/"
    val DATABASE_PATH_UPLOADS = "uploads"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )
        val view: View = binding.getRoot()

        if(CustomToast.isInternetAvailable(requireContext())){
            binding.adminParent.visibility = View.VISIBLE
            binding.noInternet.visibility = View.GONE
            init()
        }else{
           binding.adminParent.visibility = View.GONE
            binding.noInternet.visibility = View.VISIBLE
        }
        return view
    }
    fun init(){
        firestore = FirebaseFirestore.getInstance()
        val domainList = resources.getStringArray(R.array.domain)
        mStorageRef = FirebaseStorage.getInstance().getReference();
        storage = mStorageRef.storage
        requestStoragePermission()
        if (binding.domainName != null) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, domainList
            )
            binding.domainName.adapter = adapter
            binding.domainName.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    var oldDomainSelection = selectedDomain
                     selectedDomain = domainList[position]
                    if(!selectedDomain.equals(oldDomainSelection)){
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
        binding.uploadPDF.setOnClickListener() {
            mStorageReference = FirebaseStorage.getInstance().getReference();
            getPDF()
        }
        binding.uploadData.setOnClickListener(){
            var topic = binding.topic.text.toString()
            var desc = binding.description.text.toString()
            var url = binding.url.text.toString()
            var user = User()
            if (!topic.isBlank() && !desc.isBlank() && !url.isBlank() && !selectedDomain.isBlank()) {
                user.description = desc
                user.topic = topic
                user.url = url
                uploadData(user)
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

    private fun getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + requireActivity().getPackageName())
            )
            startActivity(intent)
            return
        }

        //creating an intent for file chooser
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE)
    }


    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it
    private  fun uploadFile(data: Uri) {
//        progressBar.setVisibility(View.VISIBLE)
        val sRef: StorageReference = mStorageReference!!.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf")
        try{
            sRef.putFile(data).addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                Log.d("", "Success")
//                var docUrl = taskSnapshot.uploadSessionUri.toString()


                sRef.downloadUrl.addOnSuccessListener {
                    var uri = it.toString()
                    binding.url.setText(uri, TextView.BufferType.EDITABLE)
                    binding.url.isEnabled = false
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
                        Toast.makeText(activity, exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }catch (ex:Exception){
            Log.d("",""+ex.toString())
        }

//            .addOnProgressListener{
//                fun onProgress(taskSnapshot: UploadTask.TaskSnapshot) {
//                    val progress: Double =
//                        100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()
////                    textViewStatus.setText(progress as Int.toString() + "% Uploading...")
//                }
//            }
    }

    fun uploadData(user: User){
        firestore.collection(selectedDomain).add(user)
            .addOnSuccessListener {
                CustomToast.snackbar("Upload successfully!", binding.adminParent)
            }
            .addOnCanceledListener {
                CustomToast.snackbar("Error writing document!", binding.adminParent)
            }
    }


    private fun showFileChooser() {
        val intent = Intent()
        intent.type = "application/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_PDF_CODE && resultCode === RESULT_OK && data != null && data.getData() != null) {
            var filePath = data.getData()
            uploadFile(filePath!!)
            var test = File(filePath!!.path)
            try {
//                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
//                imageView.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

   /* fun getPath(uri: Uri?): String? {
        var cursor: Cursor = getContentResolver().query(uri, null, null, null, null)
        cursor.moveToFirst()
        var document_id: String = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = getContentResolver().query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null
        )
        cursor.moveToFirst()
        val path: String = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }*/

    //Requesting permission
    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) return
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            requireActivity(),
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
                    activity,
                    "Permission granted now you can read the storage",
                    Toast.LENGTH_LONG
                ).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(activity, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


}
class Constant{
    companion object{
        var dataList:ArrayList<User> = ArrayList()
        var i:Int=0
    }
}