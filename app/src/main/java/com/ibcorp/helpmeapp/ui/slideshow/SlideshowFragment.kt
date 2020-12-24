package com.ibcorp.helpmeapp.ui.slideshow

import android.R.attr
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ibcorp.helpmeapp.Model.CustomToast
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.databinding.FragmentSlideshowBinding


class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    private lateinit var binding:FragmentSlideshowBinding
    var email: String? = null
    var subject: String? = null
    var message: String? = null
    var attachmentFile: String? = null
    var URI: Uri? = null
    private val PICK_FROM_GALLERY = 101
    var columnIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(SlideshowViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_slideshow, container, false)
        val view: View = binding.getRoot()
        init()
        return view
    }
    fun init(){
        binding.btSend.setOnClickListener {
            sendEmail()
        }
        binding.btAttachment.setOnClickListener {
            openFolder()
        }
    }

    fun sendEmail() {
        try {
            email = "ibcorphelpme@gmail.com"
            subject = binding.etSubject.getText().toString()
            message = binding.etMessage.getText().toString()
            if(!email.isNullOrBlank()&&!subject.isNullOrBlank()&&!message.isNullOrBlank()){
                val emailIntent = Intent(Intent.ACTION_SEND)
//                emailIntent.type = "plain/text"
                emailIntent.type = "message/rfc822"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>(email!!))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
                if (URI != null) {
                    emailIntent.putExtra(Intent.EXTRA_STREAM, URI)
                }
                emailIntent.putExtra(Intent.EXTRA_TEXT, message)
                this.startActivity(Intent.createChooser(emailIntent, "Sending email..."))
                CustomToast.snackbar("Mail Sent.", binding.etSubject)
            }else{
                CustomToast.snackbar("Fields is Empty", binding.etSubject)
            }
        } catch (t: Throwable) {
            CustomToast.snackbar("Request failed try again: $t", binding.etSubject)
        }
    }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === PICK_FROM_GALLERY && resultCode === RESULT_OK) {
            URI = data!!.getData();
        }
    }
}