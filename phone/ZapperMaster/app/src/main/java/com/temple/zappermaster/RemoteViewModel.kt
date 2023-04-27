package com.temple.zappermaster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemoteViewModel : ViewModel() {
    private val lastIrCode by lazy {
        MutableLiveData<String>()
    }

    private val remoteList : MutableLiveData<RemoteList> by lazy {
        MutableLiveData<RemoteList>()
    }

    private val selectedRemote : MutableLiveData<RemoteObj?> by lazy {
        MutableLiveData<RemoteObj?>()
    }
    fun setRemoteList(_remoteList: RemoteList){
        remoteList.value = _remoteList
    }

    fun getRemoteList(): LiveData<RemoteList>{
        return remoteList
    }

    fun setSelectedRemote(remote: RemoteObj?){
        selectedRemote.value = remote
    }

    fun getSelectedRemote():LiveData<RemoteObj?>{
        return selectedRemote
    }

    fun setLastIrCode(value: String) {
        lastIrCode.postValue(value)
    }

    fun getLastIrCode(): LiveData<String> {
        return lastIrCode
    }
}