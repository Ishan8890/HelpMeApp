package com.ibcorp.helpmeapp.ui.slideshow

import com.ibcorp.helpmeapp.model.source.User
import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ibcorp.helpmeapp.model.CustomToast
import com.ibcorp.helpmeapp.model.Utils
import com.ibcorp.helpmeapp.PrefManager
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentSlideshowBinding
import java.io.File
import java.io.IOException


class TutionFragment : Fragment() {

//    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var binding:FragmentSlideshowBinding
    var email: String? = null

    var attachmentFile: String? = null
    var URI: Uri? = null
    private val PICK_FROM_GALLERY = 101
    var columnIndex = 0
    private var prefManager: PrefManager? = null
    lateinit var firestore: FirebaseFirestore
    val PICK_PDF_CODE = 2343
    val STORAGE_PATH_UPLOADS = "uploads/"
    var mStorageReference: StorageReference? = null
    var doc_url:String?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        slideshowViewModel =
//                ViewModelProvider(this).get(SlideshowViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false)
        val view: View = binding.getRoot()

        init()
        return view
    }
    fun init(){
        prefManager = PrefManager(requireContext())
        firestore = FirebaseFirestore.getInstance()
        var username = prefManager!!.userName
        var email =  prefManager!!.emailId
        var userToken = prefManager!!.firebaseToken
        binding.btSend.setOnClickListener {
//            sendEmail()
            var topic = binding.etSubject.text.toString()
            var desc = binding.etMessage.text.toString()
            var user = User()
            val ref = firestore.collection("Tuition").document()
            val id = ref.id
            if(!email.isNullOrBlank()&&!username.isNullOrBlank()&&!topic.isNullOrBlank()&&!desc.isNullOrBlank()){
                user.userName = username
                user.emailId = email
                user.topic = topic
                user.description = desc
                user.notificationToken = userToken
                user.docId = id
                if(doc_url.isNullOrBlank()){
                    user.url = " "
                    user.isAttached = false
                }else{
                    user.isAttached = true
                    user.url = doc_url as String
                }
                uploadData(user)
            }else{
                CustomToast.snackbar("Fields is Empty", binding.etSubject)
            }

        }
        binding.btAttachment.setOnClickListener {
//            openFolder()
            mStorageReference = FirebaseStorage.getInstance().getReference();
        getPDF()
        }
    }

    fun uploadData(user: User){

        firestore.collection("Tuition").document(user.docId).set(user)
            .addOnSuccessListener {
                Utils.captureUserEvents(user.topic,"Tuition",user.topic,requireContext())
                binding.etSubject.text.clear()
                binding.etMessage.text.clear()
                CustomToast.snackbar("Mail Sent..", binding.btAttachment)
            }
            .addOnCanceledListener {
                CustomToast.snackbar("Something went wrong..", binding.btAttachment)
            }
    }

//    fun sendEmail() {
//        try {
//            email = "ibcorphelpme@gmail.com"
//            subject = binding.etSubject.getText().toString()
//            message = binding.etMessage.getText().toString()
//            if(!email.isNullOrBlank()&&!subject.isNullOrBlank()&&!message.isNullOrBlank()){
//                val emailIntent = Intent(Intent.ACTION_SEND)
////                emailIntent.type = "plain/text"
//                emailIntent.type = "message/rfc822"
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(email!!))
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
//                if (URI != null) {
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI)
//                }
//                emailIntent.putExtra(Intent.EXTRA_TEXT, message)
//                this.startActivity(Intent.createChooser(emailIntent, "Sending email..."))
//                CustomToast.snackbar("Mail Sent.", binding.etSubject)
//            }else{
//                CustomToast.snackbar("Fields is Empty", binding.etSubject)
//            }
//        } catch (t: Throwable) {
//            CustomToast.snackbar("Request failed try again: $t", binding.etSubject)
//        }
//    }

    fun openFolder() {
        val intent = Intent()
        intent.type = "application/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(
            Intent.createChooser(intent, "Complete action using"),
            PICK_FROM_GALLERY
        )
    }

    private  fun uploadFile(data: Uri) {
//        progressBar.setVisibility(View.VISIBLE)
        val sRef: StorageReference = mStorageReference!!.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".pdf")
        try{
            sRef.putFile(data).addOnSuccessListener { taskSnapshot -> // Get a URL to the uploaded content
                Log.d("", "Success")
//                var docUrl = taskSnapshot.uploadSessionUri.toString()


                sRef.downloadUrl.addOnSuccessListener {
                    binding.progressBarDialog.visibility = View.GONE
                    doc_url = it.toString()
//                    binding.url.setText(uri, TextView.BufferType.EDITABLE)
//                    binding.url.isEnabled = false
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
                        Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
        }catch (ex:Exception){
            Log.d("",""+ex.toString())
        }
    }

    private fun getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                requireContext(),
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode === PICK_FROM_GALLERY && resultCode === RESULT_OK) {
//            URI = data!!.getData();
//        }
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === PICK_PDF_CODE && resultCode === RESULT_OK && data != null && data.getData() != null) {
            var filePath = data.getData()
            binding.progressBarDialog.visibility = View.VISIBLE
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
}