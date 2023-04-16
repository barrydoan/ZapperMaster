package com.temple.zappermaster.database

import com.temple.zappermaster.RemoteObj

class RemoteConverter : AbstractConverter<RemoteObj, Remote>() {
    override fun toDao(obj: RemoteObj): Remote {
        val result = Remote(obj.model_number, obj.shared, obj.buttons,  false,
            obj.type, obj.manufacture)
        return result
    }

    override fun toObj(dao: Remote): RemoteObj {
        val result: RemoteObj = RemoteObj(dao.model_number, dao.shared, dao.buttons, dao.type, dao.manufacture)
        return result
    }
}