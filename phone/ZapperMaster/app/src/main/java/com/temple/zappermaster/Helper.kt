package com.temple.zappermaster

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

/**
 * A helper class to store all functions relating to:
 * API Control
 * User Management
 */
class Helper {

    object api {

        const val ENDPOINT_REMOTES = "remotes/?format=json"
        const val ENDPOINT_TYPES = "types/"
        const val ENDPOINT_MANUFACTURES = "manufactures/"

        const val API_BASE = "http://10.0.2.2:8000/"

        interface Response {
            fun processResponse(response: JSONObject)
        }

        fun getRemoteList(context: Context, response: Response?) {
            makeGetRequest(context, ENDPOINT_REMOTES, response)
        }

        fun getTypeList(context: Context, response: Response?) {
            makeGetRequest(context, ENDPOINT_TYPES, response)
        }

        fun getManufactureList(context: Context, response: Response?) {
            makeGetRequest(context, ENDPOINT_MANUFACTURES, response)
        }

        fun shareRemote(context: Context, remoteObj: RemoteObj, response: Response?) {
            val jsonObject = JSONObject()
            jsonObject.put(Constants.REMOTE_MODEL_NUMBER, remoteObj.model_number)
            jsonObject.put(Constants.REMOTE_SHARED, true)
            jsonObject.put(Constants.REMOTE_BUTTONS, JSONArray(remoteObj.buttons))
            jsonObject.put(Constants.REMOTE_CREATED, 1)
            jsonObject.put(Constants.REMOTE_TYPE, 1)
            jsonObject.put(Constants.REMOTE_MANUFACTURE, 1)
            makePostRequest(context, ENDPOINT_REMOTES, jsonObject, response)
        }


        fun makeGetRequest(
            context: Context,
            endPoint: String,
            responseCallback: Response?
        ) {
            Volley.newRequestQueue(context)
                .add(object : StringRequest(Request.Method.GET, API_BASE + endPoint, {
                    Log.d("AAA", "Response: $it")
                    responseCallback?.processResponse(JSONObject(it))
                }, {
                    it.printStackTrace()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        val creds = String.format("%s:%s", "admin", "password123")
                        val auth =
                            "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                        params["Authorization"] = auth
                        return params
                    }
                })
        }

        fun makePostRequest(
            context: Context,
            endPoint: String,
            jsonObject: JSONObject,
            responseCallback: Response?
        ) {
            Volley.newRequestQueue(context)
                .add(object :
                    JsonObjectRequest(Request.Method.POST, API_BASE + endPoint, jsonObject, {
                        Log.d("AAA", "Response: $it")
                        responseCallback?.processResponse(it)
                    }, {
                        it.printStackTrace()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val params = HashMap<String, String>()
                        val creds = String.format("%s:%s", "admin", "password123")
                        val auth =
                            "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.DEFAULT)
                        params["Authorization"] = auth
                        return params
                    }
                })
        }

        fun isSuccess(response: JSONObject): Boolean {
            return response.getString("status").equals("SUCCESS")
        }

        fun getErrorMessage(response: JSONObject): String {
            return response.getString("message")
        }
    }
}