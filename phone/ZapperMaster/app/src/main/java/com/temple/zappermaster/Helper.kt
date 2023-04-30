package com.temple.zappermaster

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/**
 * A helper class to store all functions relating to:
 * API Control
 * User Management
 */
class Helper {

    object api {

        const val ENDPOINT_REMOTES = "remotes/"

        const val API_BASE = "http://10.0.2.2:8000/"

        interface Response {
            fun processResponse(response: JSONObject)
        }

        fun getRemoteList(context: Context, response: Response?){
            val params = mutableMapOf(
                Pair("action", "REGISTER")
            )
            makeGetRequest(context, ENDPOINT_REMOTES, params, response)
        }

        fun shareRemote(context: Context, remoteObj: RemoteObj, response: Response?) {
            val params = mutableMapOf(
                Pair("model_number", remoteObj.model_number),
                Pair("shared", "true"),
                Pair("buttons", remoteObj.buttons),
                // need to add the id from the server for the field
                Pair("created", "1"),
                Pair("type", "1"),
                Pair("manufacture", "1")

            )
            makePostRequest(context, ENDPOINT_REMOTES, params, response)
        }




        private fun makeGetRequest(context: Context, endPoint: String, params: MutableMap<String, String>, responseCallback: Response?) {
            Volley.newRequestQueue(context)
                .add(object: StringRequest(Request.Method.GET, API_BASE + endPoint, {
                    Log.d("AAA", "Request: $params")
                    Log.d("AAA", "Response: $it")
                    responseCallback?.processResponse(JSONObject(it))
                }, {
                    it.printStackTrace()
                }){
                    override fun getParams(): MutableMap<String, String> {
                            return params;
                    }

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

        private fun makePostRequest(context: Context, endPoint: String, params: MutableMap<String, String>, responseCallback: Response?) {
            Volley.newRequestQueue(context)
                .add(object: JsonObjectRequest(Request.Method.POST, API_BASE + endPoint, null, {
                    Log.d("AAA", "Request: $params")
                    Log.d("AAA", "Response: $it")
                    responseCallback?.processResponse(it)
                }, {
                    it.printStackTrace()
                }){
                    override fun getParams(): MutableMap<String, String> {
                        return params;
                    }

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