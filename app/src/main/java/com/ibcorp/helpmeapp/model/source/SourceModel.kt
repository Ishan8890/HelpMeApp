package com.ibcorp.helpmeapp.model.source


import androidx.annotation.Keep

@Keep
data class SourceModel(
    var sources: List<Source>,
    var status: String
)