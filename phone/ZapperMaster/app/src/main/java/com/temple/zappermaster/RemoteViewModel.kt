package com.temple.zappermaster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RemoteViewModel : ViewModel() {
    private val lastIrCode by lazy {
        MutableLiveData<String>()
    }

    fun setLastIrCode(value: String) {
        lastIrCode.postValue(value)
    }

    fun getLastIrCode(): LiveData<String> {
        return lastIrCode
    }
}