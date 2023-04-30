package com.temple.zappermaster

data class TypeObj(
    var id: Int,
    var name: String,
    var description: String
) {

    override fun toString(): String {
        return name
    }

}
