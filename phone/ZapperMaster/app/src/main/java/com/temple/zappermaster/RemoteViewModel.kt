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
    private val lastTopPosition by lazy{
        MutableLiveData<Double>()
    }
    private val lastLeftPosition by lazy {
        MutableLiveData<Double>()
    }
    fun setTopPosition(double: Double){
        lastTopPosition.value = double
    }
    fun setLeftPosition(double: Double){
        lastLeftPosition.value = double
    }
    fun getTopPosition(): LiveData<Double>{
        return lastTopPosition
    }
    fun getLeftPostion(): LiveData<Double>{
        return lastLeftPosition
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