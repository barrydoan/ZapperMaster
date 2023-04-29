package com.temple.zappermaster

class RemoteList {
    private var remotes: ArrayList<RemoteObj> = ArrayList()
    private var isLocal: Boolean = true

    constructor(isLocal: Boolean) {
        this.isLocal = isLocal
    }


    fun addAll(remotes_: ArrayList<RemoteObj>) {
        remotes = remotes_
    }

    fun add(remote: RemoteObj) {
        remotes.add(remote)
    }

    fun remove(remote: RemoteObj) {
        remotes.remove(remote)
    }

    fun removeAll() {
        remotes = ArrayList()
    }

    operator fun get(index: Int): RemoteObj {
        return remotes[index];
    }

    fun size(): Int {
        return remotes.size
    }

    fun setLocalFlag(flag: Boolean) {
        this.isLocal = flag
    }

    fun getLocalFlag(): Boolean {
        return this.isLocal
    }
}