package com.temple.zappermaster.database

abstract class AbstractConverter<O, D> {
    abstract fun toDao(obj: O): D
    abstract fun toObj(dao: D): O

    fun toDaoList(objList: List<O>): List<D> {
        val resultList: ArrayList<D> = ArrayList<D>()
        for (obj:O in objList) {
            resultList.add(toDao(obj))
        }
        return resultList
    }

    fun toObjList(daoList: List<D>): ArrayList<O> {
        val resultList: ArrayList<O> = ArrayList<O>()
        for (dao:D in daoList) {
            resultList.add(toObj(dao))
        }
        return resultList
    }


}