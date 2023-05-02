package com.temple.zappermaster

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.*
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class RemoteViewModelTest {
    private lateinit var viewModel: RemoteViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        viewModel = RemoteViewModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun setAndGetSelectedRemoteisNullTest(){
        val selectedRemote = RemoteObj("Test1",false,"TestButtonString","tv","Sony")
        viewModel.setSelectedRemote(selectedRemote)

        val value = viewModel.getSelectedRemote()
        assertThat(value).isNotNull()
    }
    @Test
    fun selectedRemoteEmptyTest(){
        val value = viewModel.getSelectedRemote()
        assertThat(value.value?.model_number).isNull()
        assertThat(value.value?.type).isNull()
        assertThat(value.value?.shared).isNull()
        assertThat(value.value?.manufacture).isNull()
        assertThat(value.value?.buttons).isNull()
    }
    @Test
    fun setAndGetSelectedRemoteValidElementTest(){
        val selectedRemote = RemoteObj("Test1",false,"hello","tv","Sony")

        viewModel.setSelectedRemote(selectedRemote)

        val value = viewModel.getSelectedRemote()
        assertThat(value.value?.model_number).isEqualTo("Test1")
        assertThat(value.value?.buttons).isEqualTo("hello")
        assertThat(value.value?.type).isEqualTo("tv")
        assertThat(value.value?.manufacture).isEqualTo("Sony")
        assertThat(value.value?.shared).isFalse()

    }
    @Test
    fun selectedRemoteObserveChangeTest(){
        val selectedRemote = RemoteObj("Test1",false,"hello","tv","Sony")
        val selectedRemote2 = RemoteObj("Test2",true,"hello","tv","Sony")
        viewModel.setSelectedRemote(selectedRemote)
        viewModel.getSelectedRemote().hasObservers().apply {
            assertThat(this).isNotEqualTo(selectedRemote2)
        }
        viewModel.getSelectedRemote().hasActiveObservers().apply {
            assertThat(this).isNotEqualTo(selectedRemote2)
        }
    }

    @Test
    fun getAndSetRemoteListIsEmptyTest(){
        val remoteList = RemoteList(true)

        assertThat(remoteList.size()).isEqualTo(0)
    }
    @Test
    fun getAndSetRemoteListOneItemTest(){
        val selectedRemote = RemoteObj("Test1",false,"hello","tv","Sony")
        val remoteList = RemoteList(true)
        remoteList.add(selectedRemote)

        assertThat(remoteList[0]).isSameInstanceAs(selectedRemote)
    }
    @Test
    fun getAndSetRemoteListMultipleItemTest(){
        val Remote1 = RemoteObj("Test1",false,"hello1","tv","Sony")
        val Remote2 = RemoteObj("Test2",false,"hello2","tv","Sony")
        val Remote3 = RemoteObj("Test3",false,"hello3","tv","Sony")
        val Remote4 = RemoteObj("Test4",false,"hello4","tv","Sony")

        val remoteList = RemoteList(true)
        remoteList.add(Remote1)
        remoteList.add(Remote2)
        remoteList.add(Remote3)
        remoteList.add(Remote4)

        remoteList.let {
            for (i in 0 until remoteList.size()){
                var currentobj = remoteList[i]
                assertThat(currentobj).isAnyOf(Remote1,Remote2,Remote3,Remote4)
            }
        }
    }
    @Test
    fun remoteListObserverTest(){
        val Remote1 = RemoteObj("Test1",false,"hello","tv","Sony")
        val Remote2 = RemoteObj("Test2",true,"hello","tv","Sony")
        val remoteList = RemoteList(true)
        remoteList.add(Remote1)
        assertThat(remoteList[0]).isSameInstanceAs(Remote1)
        remoteList.add(Remote2)
        remoteList.let {
            for (i in 0 until remoteList.size()){
                var currentobj = remoteList[i]
                assertThat(currentobj).isAnyOf(Remote1,Remote2)
            }
        }
    }
    @Test
    fun getAndSetLastIrCodeIsNullTest(){
        val remoteViewModel = RemoteViewModel()
        val ir = remoteViewModel.getLastIrCode()
        assertThat(ir.value).isNull()
    }
    @Test
    fun getAndSetLastIrCodeIsValid() {
        val remoteViewModel = RemoteViewModel()
        remoteViewModel.setLastIrCode("This is the ir code")
        val getIr = remoteViewModel.getLastIrCode()
        assertThat(getIr.value).isEqualTo("This is the ir code")
    }
}