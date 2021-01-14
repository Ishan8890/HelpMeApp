package com.ibcorp.helpmeapp.Model

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.ibcorp.helpmeapp.R


class CustomToast {
    // Custom Toast Method
    fun Show_Toast(context: Context, view: View, error: String?) {

        // Layout Inflater for inflating custom view
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // inflate the layout over view
        val layout: View = inflater.inflate(
            R.layout.custom_toast,
            view.findViewById(R.id.toast_root) as ViewGroup
        )

        // Get TextView id and set error
        val text = layout.findViewById(R.id.toast_error) as TextView
        text.text = error
        val toast = Toast(context) // Get Toast Context
        toast.setGravity(Gravity.TOP or Gravity.FILL_HORIZONTAL, 0, 0) // Set
        // Toast
        // gravity
        // and
        // Fill
        // Horizoontal
        toast.duration = Toast.LENGTH_SHORT // Set Duration
        toast.view = layout // Set Custom View over toast
        toast.show() // Finally show toast
    }
    companion object{
        const val regEx = "\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+/.[A-Za-z]{2,4}\b"

        //Fragments Tags
        const val Login_Fragment = "Login_Fragment"
        const val SignUp_Fragment = "SignUp_Fragment"
        const val Home_Fragment = "Home_Fragment"
        const val ForgotPassword_Fragment = "ForgotPassword_Fragment"
         fun snackbar(str:String,mview:View){
            val snackBar = Snackbar.make(
                mview, str,
                Snackbar.LENGTH_LONG
            ).setAction("Action", null)
            snackBar.setActionTextColor(Color.WHITE)
            val snackBarView = snackBar.view
            snackBarView.setBackgroundColor(Color.BLUE)
            val textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            snackBar.show()
        }

        fun getProgressBar(context: Context):ProgressBar{
            val progressBar = ProgressBar(context)
            progressBar.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            return progressBar
        }
        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }
    }
}