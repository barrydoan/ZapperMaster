package com.temple.zappermaster

import androidx.test.core.app.ApplicationProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.fail
import org.junit.Test



class ServerTest{
    @Test
    fun testVolleyResponse() {
        // Define the URL to be tested
        val url = "https://88863af5-9e39-42f6-87e8-96aa3770ed4a.mock.pstmn.io"

        // Create a Volley StringRequest object
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Assert that the response is not null
                assertNotNull(response)
            },
            { error ->
                // Fail the test if an error occurred
                fail("Volley error occurred: " + error.message)
            })

        // Add the request to the Volley request queue
        Volley.newRequestQueue(ApplicationProvider.getApplicationContext()).add(stringRequest)
    }
}