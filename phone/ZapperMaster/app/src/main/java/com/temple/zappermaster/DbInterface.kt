package com.temple.zappermaster

interface DbInterface {
    fun saveRemote(name: String, type: String, manufacture: String, buttonExtendedList: MutableList<ButtonExtended>)
}