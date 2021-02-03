package com.hasanakcay.todoo.model

data class Weather(
    val id : Int ?= null,
    val main : String ?= null,
    val description : String ?= null,
    val icon : String ?= null,
    val temp : Double ?= null
) {
}