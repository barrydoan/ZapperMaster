package com.temple.zappermaster

class RemoteList {
    private var remotes: ArrayList<RemoteObj> = ArrayList()

    fun addAll(books_: ArrayList<RemoteObj>) {
        remotes = books_
    }

    fun add(book: RemoteObj) {
        remotes.add(book)
    }

    fun remove(book: RemoteObj) {
        remotes.remove(book)
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
}