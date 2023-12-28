package com.ibcorp.helpmeapp.model

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.DatabaseReference
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings




class Utils {


    companion object{

        var isUpdate = false
        private lateinit var mFirebaseAnalytics: FirebaseAnalytics

        fun captureFirebaseEvents(
            userName: String,
            userLoginType: String,
            classType: String,
            context: Context
        ){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, userName)
            bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, userLoginType)
            bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, classType)
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
        }
        fun captureFirebaseActionEvents(userType: String, screenType: String, context: Context){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("ACTION_TYPE", userType)
            bundle.putString("SCREEN_TYPE", screenType)
            mFirebaseAnalytics.logEvent("CLICKED_EVENT", bundle)
        }
        fun captureFirebaseReplyDataEvents(
            topic: String,
            screenName: String,
            domain: String,
            context: Context
        ){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("TOPIC_TYPE", topic)
            bundle.putString("DOMAIN_TYPE", topic)
            bundle.putString("SCREEN_NAME", screenName)
            mFirebaseAnalytics.logEvent("REPLY_EVENT", bundle)
        }
        fun captureUploadEvents(topic: String, screenName: String, domain: String, context: Context){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("TOPIC_TYPE", topic)
            bundle.putString("DOMAIN_TYPE", domain)
            bundle.putString("SCREEN_NAME", screenName)
            mFirebaseAnalytics.logEvent("UPLOAD_EVENT", bundle)
        }
        fun captureUserEvents(topic: String, screenName: String, domain: String, context: Context){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("TOPIC_TYPE", topic)
            bundle.putString("DOMAIN_TYPE", domain)
            bundle.putString("SCREEN_NAME", screenName)
            mFirebaseAnalytics.logEvent("USER_EVENT", bundle)
        }
        fun captureUserTypeEvents(screenName: String, userCount: String, context: Context){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("USER_COUNT", userCount)
            bundle.putString("SCREEN_NAME", screenName)
            mFirebaseAnalytics.logEvent("USER_EVENT", bundle)
        }
        fun captureUserLectureEvents(
            screenName: String,
            userCount: String,
            domain: String,
            context: Context
        ){
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context)
            val bundle = Bundle()
            bundle.putString("USER_COUNT", userCount)
            bundle.putString("DOMAIN_TYPE", domain)
            bundle.putString("SCREEN_NAME", screenName)
            mFirebaseAnalytics.logEvent("USER_EVENT", bundle)
        }

        fun hideKeyboard(v: View, activity: Activity) {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager!!.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
        }

        fun writeNewUser(
            userDetail: UserDetail,
            database: DatabaseReference,
            view: View,
            message: String,
            dbChildName: String,
            isRateUs: Boolean
        ) {
            database.child(dbChildName).child(userDetail.token).setValue(userDetail)
                .addOnSuccessListener {
                    if(isRateUs)
                        CustomToast.snackbar(message, view)
                }
                .addOnFailureListener {
                    CustomToast.snackbar(it.toString(), view)
                }
        }
    }
}