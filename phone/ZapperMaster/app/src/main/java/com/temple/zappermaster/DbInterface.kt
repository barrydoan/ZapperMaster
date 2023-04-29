package com.temple.zappermaster

import android.content.Context

interface DbInterface {
    fun saveRemote(name: String, type: String, manufacture: String, shared: Boolean ,buttonExtendedList: MutableList<ButtonExtended>)
    fun shareRemote(remoteObj: RemoteObj)
}