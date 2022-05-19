package com.example.networksample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.networksample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var urlText = binding.editTextTextPersonName.text.toString()
                    if(!urlText.startsWith("https")){
                        urlText = "https://${urlText}"
                    }
                    val url = URL(urlText)
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"
                    if(urlConnection.responseCode == HttpURLConnection.HTTP_OK){
                        val streamReader = InputStreamReader(urlConnection.inputStream)

                        val buffered = BufferedReader(streamReader)

                        val content = StringBuilder()
                        while (true){
                            val line = buffered.readLine()?:break
                            content.append(line)
                        }
                        buffered.close()
                        urlConnection.disconnect()
                        launch(Dispatchers.Main) {
                            binding.textView.text = content.toString()
                        }
                    }
                } catch (e : IOException){
                    e.printStackTrace()
                }
            }
        }
    }
}