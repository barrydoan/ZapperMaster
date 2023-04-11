package com.temple.zappermaster.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Remote::class, RemoteDetail::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun remoteDao(): RemoteDao
    abstract fun remoteDetailDao(): RemoteDetailDao
}