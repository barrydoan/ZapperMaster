package com.temple.zappermaster.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Remote(
    @PrimaryKey
    val model_number: String,
    @ColumnInfo(name = "shared")
    val shared: Boolean,
    @ColumnInfo(name = "buttons")
    val buttons: String,
    @ColumnInfo(name = "is_deleted")
    val isDeleted: Boolean,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "manufacture")
    var manufacture: String

)
