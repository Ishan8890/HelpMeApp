package com.ibcorp.helpmeapp

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class PrefManager(var _context: Context) {
    var pref: SharedPreferences
    var editor: Editor

    // shared pref mode
    var PRIVATE_MODE = 0
    var isFirstTimeLaunch: Boolean
        get() = pref.getBoolean(IS_FIRST_TIME_LAUNCH, false)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
            editor.commit()
        }

    var token:String
    get() = pref.getString(LOGIN_ID,"").toString()
    set(getToken) {
        editor.putString(LOGIN_ID, getToken)
        editor.commit()
    }

    var userName:String
    get() = pref.getString(USER_NAME,"").toString()
    set(userName) {
        editor.putString(USER_NAME, userName)
        editor.commit()
    }

    var loginChannel:String
        get() = pref.getString(LOGIN_CHANNEL,"").toString()
        set(loginChannel) {
            editor.putString(LOGIN_CHANNEL, loginChannel)
            editor.commit()
        }

    var firebaseToken:String
        get() = pref.getString(FIREBASE_TOKEN,"").toString()
        set(firebaseToken) {
            editor.putString(FIREBASE_TOKEN, firebaseToken)
            editor.commit()
        }

    var emailId:String
    get() = pref.getString(USER_EMAIL_ID,"").toString()
    set(emailId) {
        editor.putString(USER_EMAIL_ID, emailId)
        editor.commit()
    }

    var imageUrl:String
    get() = pref.getString(USER_IMAGE_URL,"").toString()
    set(imageUrl) {
        editor.putString(USER_IMAGE_URL, imageUrl)
        editor.commit()
    }

    fun clearData(){
        editor.clear()
        editor.commit()
    }

    companion object {
        // Shared preferences file name
        private const val PREF_NAME = "help_me_preference"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
        private const val LOGIN_ID = "loginId"
        private const val USER_NAME = "user_name"
        private const val LOGIN_CHANNEL = "login_channel"
        private const val USER_EMAIL_ID = "email_id"
        private const val USER_IMAGE_URL = "image_url"
        private const val FIREBASE_TOKEN = "token"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}