package com.syuheifujita.android_weather_app

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallAPILoginAsyncTask().execute()
    }

    private inner class CallAPILoginAsyncTask(): AsyncTask<Any, Void, String>() {
        private lateinit var customProgressDialog: Dialog

        override fun onPreExecute() {
            super.onPreExecute()

            showProgressDialog()
        }

        override fun doInBackground(vararg params: Any?): String {
            var result: String

            var connection: HttpURLConnection? = null

//            インターネットに接続できないたびにアプリがクラッシュするのを防ぐため
            try {
                val url = URL("http://www.mocky.io/v2/5ed737bf3200007f5a27458e")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true

                val httpResult: Int = connection.responseCode
                if(httpResult == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream

                    val reader = BufferedReader(
                        InputStreamReader(inputStream)
                    )

                    val stringBuilder = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {

                        }
                    }
                    result = stringBuilder.toString()
                } else {
                    result = connection.responseMessage
                }
            } catch (e: SocketTimeoutException) {
                result = "Connection Timeout"
            } catch (e: Exception) {
                result = "Erroe  :" + e.message
            } finally {
                connection?.disconnect()
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            cancelProgressDialog()

            Log.i("JSON RESPONS RESULT", result)

            val jsonObject = JSONObject(result)
            val message = jsonObject.optString("message")
            Log.i("Message", message)
            val userId = jsonObject.optInt("user_id")
            Log.i("User Id", "$userId")
            val name = jsonObject.optString("name")
            Log.i("Name", "$message")

            val profileDetailObject = jsonObject.optJSONObject("profile_datail")
            val isProfileCompleted = profileDetailObject.optBoolean("is_profile_completed")
            Log.i("Is Profile Completed", "$isProfileCompleted")

            val dataListArray = jsonObject.optJSONArray("data_list")
            Log.i("Data List Size", "${dataListArray.length()}")

            for (item in 0 until dataListArray.length()) {
                Log.i("Data List Size", "${dataListArray[item]}")

                val dataItemObject: JSONObject = dataListArray[item] as JSONObject

                val id = dataItemObject.optInt("id")
                Log.i("ID", "$id")

                val value = dataItemObject.optString("values")
                Log.i("Value", "$value")
            }
        }

        private fun showProgressDialog() {
            customProgressDialog = Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.dialog_custom_progress)
            customProgressDialog.show()
        }

        private fun cancelProgressDialog() {
            customProgressDialog.dismiss()
        }
    }
}
