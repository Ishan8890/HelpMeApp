package com.ibcorp.helpmeapp.Fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.ibcorp.helpmeapp.Model.CustomToast
import com.ibcorp.helpmeapp.R
import com.ibcorp.helpmeapp.ui.MainActivity
import java.util.regex.Pattern


class SignUp_Fragment : Fragment(), View.OnClickListener {
    private var mAdView: AdView? = null
    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.signup_layout, container, false)
        initViews(view)
        setListeners()
        return view
    }

    // Initialize all views
    private fun initViews(view: View) {

        mAdView = view.findViewById<View>(R.id.adView) as AdView
        val adRequest: AdRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)
        fullName = view.findViewById<View>(R.id.fullName) as EditText
        emailId = view.findViewById<View>(R.id.userEmailId) as EditText
        mobileNumber = view.findViewById<View>(R.id.mobileNumber) as EditText
        location = view.findViewById<View>(R.id.location) as EditText
        password = view.findViewById<View>(R.id.password) as EditText
        confirmPassword = view.findViewById<View>(R.id.confirmPassword) as EditText
        signUpButton = view.findViewById<View>(R.id.signUpBtn) as Button
        login = view.findViewById<View>(R.id.already_user) as TextView
        terms_conditions = view.findViewById<View>(R.id.terms_conditions) as CheckBox

        // Setting text selector over textviews
//        val xrp: XmlResourceParser = getResources().getXml(R.drawable.text_selector)
//        try {
//            val csl = ColorStateList.createFromXml(
//                getResources(),
//                xrp
//            )
//            login!!.setTextColor(csl)
//            terms_conditions!!.setTextColor(csl)
//        } catch (e: Exception) {
//        }
    }

    // Set Listeners
    private fun setListeners() {
        signUpButton!!.setOnClickListener(this)
        login!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.signUpBtn ->
                // Call checkValidation method
                checkValidation()
            R.id.already_user ->
                // Replace login fragment
                MainActivity().replaceLoginFragment()
        }
    }

    // Check Validation Method
    private fun checkValidation() {

        // Get all edittext texts
        val getFullName = fullName!!.text.toString()
        val getEmailId = emailId!!.text.toString()
        val getMobileNumber = mobileNumber!!.text.toString()
        val getLocation = location!!.text.toString()
        val getPassword = password!!.text.toString()
        val getConfirmPassword = confirmPassword!!.text.toString()

        // Pattern match for email id
        val p = Pattern.compile(CustomToast.regEx)
        val m = p.matcher(getEmailId)

//        // Check if all strings are null or not
//        if (getFullName == "" || getFullName.length == 0 || getEmailId == "" || getEmailId.length == 0 || getMobileNumber == "" || getMobileNumber.length == 0 || getLocation == "" || getLocation.length == 0 || getPassword == "" || getPassword.length == 0 || getConfirmPassword == "" || getConfirmPassword.length == 0) CustomToast().Show_Toast(
//            getActivity(),
//            view!!,
//            "All fields are required."
//        ) else if (!m.find()) CustomToast().Show_Toast(
//            getActivity(),
//            view!!,
//            "Your Email Id is Invalid."
//        ) else if (getConfirmPassword != getPassword) CustomToast().Show_Toast(
//            getActivity(),
//            view!!,
//            "Both password doesn't match."
//        ) else if (!terms_conditions!!.isChecked) CustomToast().Show_Toast(
//            getActivity(),
//            view!!,
//            "Please select Terms and Conditions."
//        ) else Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
//            .show()
    }

    companion object {
        private var view: View? = null
        private var fullName: EditText? = null
        private var emailId: EditText? = null
        private var mobileNumber: EditText? = null
        private var location: EditText? = null
        private var password: EditText? = null
        private var confirmPassword: EditText? = null
        private var login: TextView? = null
        private var signUpButton: Button? = null
        private var terms_conditions: CheckBox? = null
    }
}