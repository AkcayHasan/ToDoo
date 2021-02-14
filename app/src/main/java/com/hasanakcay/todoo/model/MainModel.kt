package com.hasanakcay.todoo.model

import com.google.gson.annotations.SerializedName

data class MainModel (
    @SerializedName("temp")
    val temp : Double ?= null
    )