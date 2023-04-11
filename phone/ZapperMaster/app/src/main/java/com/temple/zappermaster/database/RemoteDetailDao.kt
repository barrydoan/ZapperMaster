package com.temple.zappermaster.database

import androidx.room.*

@Dao
interface RemoteDetailDao {
    @Query("SELECT * FROM playingDetail")
    fun getAll(): List<Remote>

    @Query("SELECT * FROM playingDetail WHERE id = :id")
    fun loadAllById(id: Int): Remote

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(playingDetail: Remote)

    @Delete
    fun delete(playingDetail: Remote)

    @Query("SELECT EXISTS(SELECT * FROM playingDetail WHERE id = :id)")
    fun isRowIsExist(name : String) : Boolean
}