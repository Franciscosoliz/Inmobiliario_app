package com.ute.app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PostDTO(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
) : Parcelable