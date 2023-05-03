package com.temple.zappermaster.databaseTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.temple.zappermaster.RemoteList
import com.temple.zappermaster.RemoteObj
import com.temple.zappermaster.database.AppDatabase
import com.temple.zappermaster.database.Remote
import com.temple.zappermaster.database.RemoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AbstractConverterTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var appDatabase: AppDatabase
    private lateinit var dao: RemoteDao

    @Before
    fun setup (){
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = appDatabase.remoteDao()


    }

    @After
    fun teardown(){
        appDatabase.close()
    }

    @Test
    fun ConverterFromDaoToObjListTest(){
        val remoteObj1= RemoteObj("test1",false,"string button Example","hello","hello")
        val remoteObj2= RemoteObj("test2",false,"string button Example","hello","hello")

        val remoteDao = Remote("test1",false,"string button Example",false,"hello","hello")

        val remoteList= RemoteList(true)
        remoteList.add(remoteObj1)
        remoteList.add(remoteObj2)
        val abstractConverter = AbstractConverterTest()

    }
}