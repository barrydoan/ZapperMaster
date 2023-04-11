package com.temple.zappermaster.database

import androidx.room.*

@Dao
interface RemoteDao {
    @Query("SELECT * FROM remote where remote.is_deleted == 0")
    fun getAll(): List<Remote>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(books: List<Remote>)
    @Query("UPDATE remote set is_deleted = 1")
    fun deleteAll()
    @Query("SELECT * FROM remote WHERE model_number = :id")
    fun loadAllByModel(name: String): Remote
}