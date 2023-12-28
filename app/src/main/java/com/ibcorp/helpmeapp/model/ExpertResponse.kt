package com.ibcorp.helpmeapp.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import android.os.Parcelable
import androidx.annotation.Keep

@SuppressLint("ParcelCreator")
@Parcelize
@Keep
data class ExpertResponse(
    @SerializedName("expert_array")
    var expertArray: List<String> = listOf(),
    @SerializedName("expert_header")
    var expertHeader: String = "",
    @SerializedName("header_text_size")
    var headerTextSize: String = "",
    @SerializedName("subheader_text_size")
    var subheaderTextSize: String = "",
    @SerializedName("update_flash")
    var updateFlash: Boolean = false,
    @SerializedName("video_text")
    var videoText: String = "",
    @SerializedName("video_url")
    var videoUrl: String = "",
    @SerializedName("update_message")
    var update_message: String = ""
) : Parcelable