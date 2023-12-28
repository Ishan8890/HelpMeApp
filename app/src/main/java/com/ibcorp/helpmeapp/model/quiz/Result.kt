package com.ibcorp.helpmeapp.model.quiz


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class Result(
    var category: String = "",
    @SerializedName("correct_answer")
    var correctAnswer: String = "",
    var difficulty: String = "",
    @SerializedName("incorrect_answers")
    var incorrectAnswers: List<String> = listOf(),
    var question: String = "",
    var type: String = ""
) : Parcelable