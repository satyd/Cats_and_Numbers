package com.levp.catsandmath.data.remote.dto

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class CatImageUrl(
    @SerializedName("file")
    val file:String
)
