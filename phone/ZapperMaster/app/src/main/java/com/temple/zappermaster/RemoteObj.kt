package com.temple.zappermaster

import com.beust.klaxon.Json

data class RemoteObj(
    @Json(name = "model_number")
    val model_number:String,
    @Json(name = "shared")
    val shared: Boolean,
    @Json(name = "buttons")
    val buttons: String,
    @Json(name = "type")
    val type: String,
    @Json(name = "manufacture")
    val manufacture: String
)