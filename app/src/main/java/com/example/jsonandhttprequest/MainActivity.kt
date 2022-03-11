package com.example.jsonandhttprequest

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListAdapter
import android.widget.ListView
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val url = "https://mysafeinfo.com/api/data?list=presidents&format=json"

        AsyncTaskHandleJson().execute(url)
    }

    inner class AsyncTaskHandleJson : AsyncTask<String, String, String>() {
        override fun doInBackground(vararg url: String?): String {
            Log.d("debug", "doInBackground")
            lateinit var text: String
            lateinit var connection:HttpsURLConnection
            try {
                connection = URL(url[0]).openConnection() as HttpsURLConnection
                connection.connect()
                text =
                    connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
            } catch (e: Exception) {
                    e.printStackTrace()
            } finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    private fun handleJson(jsonString: String?) {
        val jsonArray = JSONArray(jsonString)
        val list = ArrayList<President>()

        var x = 0
        while (x < jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject((x))

            list.add(
                President(
                    jsonObject.getInt("ID"),
                    jsonObject.getString("FullName"),
                    jsonObject.getString("Party"),
                    jsonObject.getString("Terms")
                )
            )
            x++
        }
        val adapter = ListAdapte(this, list)
        var mListView = findViewById<ListView>(R.id.presidents_list)
        mListView.adapter = adapter
    }
}

