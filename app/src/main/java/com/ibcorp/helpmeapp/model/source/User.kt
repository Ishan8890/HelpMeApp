package com.ibcorp.helpmeapp.model.source
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class User{
    @SerializedName("Description")
    var description: String = ""
    @SerializedName("Topic")
    var topic: String= ""
    @SerializedName("url")
    var url: String= ""
    @SerializedName("emailId")
    var emailId: String= ""
    @SerializedName("userName")
    var userName: String= ""
    @SerializedName("docId")
    var docId: String= ""
    @SerializedName("isAnswered")
    var isAnswered: Boolean= false
    @SerializedName("isAttached")
    var isAttached: Boolean= false
    @SerializedName("notificationToken")
    var notificationToken: String= ""
    //Add this
    constructor()

}

