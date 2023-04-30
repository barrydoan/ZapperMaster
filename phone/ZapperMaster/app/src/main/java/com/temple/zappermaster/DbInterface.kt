package com.temple.zappermaster

interface DbInterface {
    fun saveRemote(name: String, type: String, manufacture: String, shared: Boolean ,buttonExtendedList: MutableList<ButtonExtended>)
    fun shareRemote(remoteObj: RemoteObj)
    fun deleteRemote(remoteObj: RemoteObj)
    fun updateRemote(remoteObj: RemoteObj)
    fun checkExistedOnLocal(modelNumber: String): Boolean
    fun saveRemote(remoteObj: RemoteObj)
    fun loadAllTypes(): MutableList<TypeObj>
    // test
    fun loadAllManufacture(): MutableList<ManufactureObj>
}