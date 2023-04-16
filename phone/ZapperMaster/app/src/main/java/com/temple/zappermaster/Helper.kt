package com.temple.zappermaster

import android.content.Context
import android.util.Base64
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
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

        const val ENDPOINT_REMOTES = "remotes"

        const val API_BASE = "http://10.0.2.2:8000/"

        interface Response {
            fun processResponse(response: JSONObject)
        }

        fun getRemoteList(context: Context, password: String, response: Response?){
            val params = mutableMapOf(
                Pair("action", "REGISTER")
            )
            makeRequest(context, ENDPOINT_REMOTES, params, response)
        }




        private fun makeRequest(context: Context, endPoint: String, params: MutableMap<String, String>, responseCallback: Response?) {
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





        fun isSuccess(response: JSONObject): Boolean {
            return response.getString("status").equals("SUCCESS")
        }

        fun getErrorMessage(response: JSONObject): String {
            return response.getString("message")
        }

    }
}