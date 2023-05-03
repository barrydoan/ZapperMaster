package com.temple.zappermaster.databaseTest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.temple.zappermaster.RemoteObj
import com.temple.zappermaster.database.Remote
import com.temple.zappermaster.database.RemoteConverter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)

class RemoteConverterTest{
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun convertFromObjToDaoIsCorrectTest(){
        val remoteObj= RemoteObj("test1",false,"string button Example","hello","hello")

        val remoteItem1 = Remote("test1",false,"string button Example",false,"hello","hello")
        val remoteConverter = RemoteConverter()
        val remoteToDao = remoteConverter.toDao(remoteObj)
        assertThat(remoteToDao).isEqualTo(remoteItem1)

    }
    @Test
    fun convertFromObjToDaoIsErrorTest() {
        val remoteObj= RemoteObj("test2",false,"string button Example","hello","hello")

        val remoteItem1 = Remote("test3",false,"string button Example",false,"hello","hello")
        val remoteConverter = RemoteConverter()
        val remoteToDao = remoteConverter.toDao(remoteObj)
        assertThat(remoteToDao).isNotEqualTo(remoteItem1)

    }

    @Test
    fun covertFromDaoToObjCorrectTest(){
        val remoteObj= RemoteObj("test4",false,"string button Example","hello","hello")
        val remoteItem1 = Remote("test4",false,"string button Example",false,"hello","hello")
        val remoteConverter = RemoteConverter()

        val remoteToObj = remoteConverter.toObj(remoteItem1)
        assertThat(remoteToObj).isEqualTo(remoteObj)

    }

    @Test
    fun covertFromDaoToObjErrorTest(){
        val remoteObj= RemoteObj("test5",false,"string button Example","hello","hello")
        val remoteItem1 = Remote("test6",false,"string button Example",false,"hello","hello")
        val remoteConverter = RemoteConverter()
        val remoteToObj = remoteConverter.toObj(remoteItem1)
        assertThat(remoteToObj).isNotEqualTo(remoteObj)

    }

}