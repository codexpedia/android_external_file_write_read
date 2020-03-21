package com.example.write_external_file

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_write.setOnClickListener {
            val randomString = generateRandomStr()

            val filename = "test.txt"
            val folder = getExternalFilesDir(null)

//            if(!File("/storage/emulated/0/Android/data/test").exists()) {
//                File("/storage/emulated/0/Android/data/test").mkdir()
//                File("/storage/emulated/0/Android/data/test/files").mkdir()
//            }
            val myFile = File(folder, filename)

            writeToFile(myFile, randomString)

            // /storage/emulated/0
            // /storage/emulated/0/Android/data/com.example.write_external_file/files/text.txt
            // /storage/emulated/0/Android/data/com.example.write_external_file/files/test.txt
            //     Content: 7oywMdG2viRK8NxtHQr24ebdFqzHA3EewqzUr8vVqxye4Ne5AphU
            val textToDisplay = "File path: ${myFile.absolutePath}\n Content: $randomString"
            tv_text.text = textToDisplay
            Log.d("Main", textToDisplay)
        }

    }

    private fun writeToFile(file: File, dataStr: String) {
        try {
            val fstream = FileOutputStream(file)
            fstream.write(dataStr.toByteArray())
            fstream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun generateRandomStr(): String {
        val stringSize = Random.nextInt(30, 100)
        return (1..stringSize)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

}
