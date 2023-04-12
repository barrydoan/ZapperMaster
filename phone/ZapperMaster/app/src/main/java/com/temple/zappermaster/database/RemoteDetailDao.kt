package com.temple.zappermaster.database

import androidx.room.*

@Dao
interface RemoteDetailDao {
    @Query("SELECT * FROM RemoteDetail")
    fun getAll(): List<RemoteDetail>

    @Query("SELECT * FROM RemoteDetail WHERE model_number = :model_number")
    fun loadAllByModel(model_number: String): RemoteDetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(RemoteDetail: RemoteDetail)

    @Delete
    fun delete(RemoteDetail: RemoteDetail)

    @Query("SELECT EXISTS(SELECT * FROM RemoteDetail WHERE model_number = :model_number)")
    fun isRowIsExist(model_number : String) : Boolean
}