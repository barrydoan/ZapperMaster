package com.temple.zappermaster.database

import androidx.room.*

@Dao
interface RemoteDao {
    @Query("SELECT * FROM remote where remote.is_deleted == 0")
    fun getAll(): List<Remote>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remotes: List<Remote>)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(remote: Remote)
    @Query("UPDATE remote set is_deleted = 1")
    fun deleteAll()
    @Query("DELETE FROM remote WHERE model_number = :model_number")
    fun delete(model_number: String)
    @Query("SELECT * FROM remote WHERE model_number = :model_number and is_deleted = 0")
    fun loadAllByModel(model_number: String): Remote?
}