package com.temple.zappermaster.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteDetail(
    @PrimaryKey
    val model_number:String,
    @ColumnInfo(name = "shared")
    val shared: Boolean,
    @ColumnInfo(name = "buttons")
    val buttons: String
)
