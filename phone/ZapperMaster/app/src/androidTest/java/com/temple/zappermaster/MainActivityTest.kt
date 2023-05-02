package com.temple.zappermaster

import android.app.Application
import com.google.common.truth.Truth.assertThat
import com.temple.zappermaster.Helper.api.ENDPOINT_REMOTES
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

const val TEST_SERVER_API ="https://88863af5-9e39-42f6-87e8-96aa3770ed4a.mock.pstmn.io"

class MainActivityTest: Application(){

    @Before
    fun setup(){


    }
    @Test
    fun testPostResponse(){

        var jsonString = FileReader.readStringFromFile("device3.json")

        var jsonObject = JSONObject(jsonString)
        var model = jsonObject.getString(Constants.REMOTE_MODEL_NUMBER)
        var buttons = jsonObject.getString(Constants.REMOTE_BUTTONS)
        var shared = jsonObject.getBoolean(Constants.REMOTE_SHARED)
        var type = jsonObject.getString(Constants.REMOTE_TYPE)
        var manufacture = jsonObject.getString(Constants.REMOTE_MANUFACTURE)
         var remoteObj = RemoteObj(model, shared, buttons, type, manufacture)

        Helper.api.shareRemote(this,remoteObj,object : Helper.api.Response{
            override fun processResponse(response: JSONObject) {
                assertThat(response).isNotNull()
            }

        })

        Helper.api.makeGetRequest(this, TEST_SERVER_API + ENDPOINT_REMOTES,object : Helper.api.Response{
            override fun processResponse(response: JSONObject) {
                assertThat(response).isEqualTo(remoteObj)
            }
        })
    }
}