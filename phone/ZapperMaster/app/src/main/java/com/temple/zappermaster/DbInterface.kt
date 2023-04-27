package com.temple.zappermaster

interface DbInterface {
    fun saveRemote(name: String, type: String, manufacture: String, shared: Boolean ,buttonExtendedList: MutableList<ButtonExtended>)
}