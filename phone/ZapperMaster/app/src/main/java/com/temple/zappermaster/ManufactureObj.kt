package com.temple.zappermaster

data class ManufactureObj(
    var id: Int,
    var name: String,
    var description: String
) {

    override fun toString(): String {
        return name
    }

}
