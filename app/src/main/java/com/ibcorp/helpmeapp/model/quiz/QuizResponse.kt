package com.ibcorp.helpmeapp.model.quiz


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class QuizResponse(
    @SerializedName("response_code")
    var responseCode: Int = 0,
    var results: List<Result> = listOf()
) : Parcelable