package com.syuheifujita.android_weather_app

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private lateinit var customProgressDialog: Dialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private inner class CallAPILoginAsyncTask(): AsyncTask<Any, Void, String>() {
        override fun doInBackground(vararg params: Any?): String {
            TODO("Not yet implemented")
        }
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(this@MainActivity)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)

        customProgressDialog.show()
    }

    private fun cancelProgressDialog() {

    }
}
