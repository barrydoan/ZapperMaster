package com.temple.zappermaster.databaseTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.*
import com.temple.zappermaster.database.AppDatabase
import com.temple.zappermaster.database.Remote
import com.temple.zappermaster.database.RemoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RemoteDaoTest {
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
    fun insertAndLoadSingleItemTest() = runBlocking {
        val remoteItem = Remote("test1",false,"nothing",false,"tv","sony")
        dao.insert(remoteItem)

        val allRemoteItem = dao.getAll()
        assertThat(allRemoteItem[0]).isEqualTo(dao.loadAllByModel("test1"))
    }
    @Test
    fun insertAndLoadMultipleItemTest() = runBlocking {
        val remoteItem1 = Remote("test1",false,"nothing",false,"tv","sony")
        val remoteItem2 = Remote("test2",false,"nothing",false,"tv","sony")
        dao.insert(remoteItem1)
        dao.insert(remoteItem2)

        val allRemoteItem = dao.getAll()
        var name = 0

        for (i in allRemoteItem){
            name +=1
            assertThat(i).isEqualTo(dao.loadAllByModel("test$name"))
        }
    }


    @Test
    fun validObjectTest() = runBlocking {
        val jArray = JSONArray()
        val jObject = JSONObject()
        jObject.put("background_color", "white")
        jObject.put("size", "normal")
        jObject.put("top_position_percent", 0.25)
        jObject.put("left_position_percent", 0.25)
        jObject.put("display_name", "Add")
        jObject.put("code", "<23|64|6F|15>")
        jArray.put(jObject)


        val remoteItem1 = Remote("test1",false,jArray.toString(),false,"tv","sony")
        dao.insert(remoteItem1)
        val selectedRemoteItem = dao.loadAllByModel("test1")
        val correctRemoteItem= Remote("test1",
            false,
            jArray.toString(),
            false,
            "tv",
            "sony")
        assertThat(selectedRemoteItem?.model_number).isEqualTo(correctRemoteItem.model_number)
        assertThat(selectedRemoteItem?.shared).isEqualTo(correctRemoteItem.shared)
        assertThat(selectedRemoteItem?.type).isEqualTo(correctRemoteItem.type)
        assertThat(selectedRemoteItem?.manufacture).isEqualTo(correctRemoteItem.manufacture)
        assertThat(selectedRemoteItem?.isDeleted).isEqualTo(correctRemoteItem.isDeleted)
        assertThat(selectedRemoteItem?.buttons).isEqualTo(correctRemoteItem.buttons)
    }

    @Test
    fun deletedSelectedItemTest(){
        val jArray = JSONArray()
        val jObject = JSONObject()
        jObject.put("background_color", "white")
        jObject.put("size", "normal")
        jObject.put("top_position_percent", 0.25)
        jObject.put("left_position_percent", 0.25)
        jObject.put("display_name", "Add")
        jObject.put("code", "<23|64|6F|15>")
        jArray.put(jObject)

        val remoteItem1= Remote("test1",
            false,
            jArray.toString(),
            false,
            "tv",
            "sony")

        dao.insert(remoteItem1)
        dao.delete(remoteItem1.model_number)
        assertThat(dao.loadAllByModel("test1")).isNull()
    }
    @Test
    fun deletedAllItemTest(){
        val remoteItem1 = Remote("test1",false,"nothing",false,"tv","sony")
        val remoteItem2 = Remote("test2",false,"nothing",false,"tv","sony")
        val remoteItem3 = Remote("test3",false,"nothing",false,"tv","sony")
        val remoteItem4 = Remote("test4",false,"nothing",false,"tv","sony")
        dao.insert(remoteItem1)
        dao.insert(remoteItem2)
        dao.insert(remoteItem3)
        dao.insert(remoteItem4)
        dao.deleteAll()
        assertThat(dao.getAll()).isEmpty()
    }
}