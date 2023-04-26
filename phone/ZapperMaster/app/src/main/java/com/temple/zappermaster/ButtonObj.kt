package com.temple.zappermaster

import com.beust.klaxon.Json

data class ButtonObj(
    @Json(name = "background_color")
    var background_color : String,
    @Json(name = "size")
    var size: String,
    @Json(name = "top_position_percent")
    var top_position_percent: Double,
    @Json(name = "left_position_percent")
    var left_position_percent: Double,
    @Json(name = "display_name")
    var display_name: String,
    @Json(name = "code")
    var code: String
)
