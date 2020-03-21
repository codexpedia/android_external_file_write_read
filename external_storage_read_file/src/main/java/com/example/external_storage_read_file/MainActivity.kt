package com.example.external_storage_read_file

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.charset.Charset
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val TARGET_APP_PACKAGE = "com.example.write_external_file"
        const val TARGET_FILE_NAME = "test.txt"
    }

    private val REQUEST_PERMISSIONS = 100
    private val PERMISSIONS_REQUIRED = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkPermission(PERMISSIONS_REQUIRED)) {
            showFileData()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("MainActivity", "requestCode: $requestCode")
        Log.d("MainActivity", "Permissions:" + permissions.contentToString())
        Log.d("MainActivity", "grantResults: " + grantResults.contentToString())

        if (requestCode == REQUEST_PERMISSIONS) {
            var hasGrantedPermissions = true
            for (i in grantResults.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false
                    break
                }
            }

            if (hasGrantedPermissions) {
                showFileData()
            } else {
                finish()
            }

        } else {
            finish()
        }
    }

    private fun showFileData() {
        val externalDirPath = getExternalFilesDir(null)?.absolutePath ?: ""

        val targetDirPath = if (externalDirPath.isNotEmpty()) {
            externalDirPath.replace(packageName, TARGET_APP_PACKAGE)
        } else {
            ""
        }

        val targetFile = File(targetDirPath, TARGET_FILE_NAME)
        val targetFileContent = if (targetFile.exists()) {
            readFile(targetFile)
        } else {
            ""
        }

        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")
        stringBuilder.append("packageName: $packageName")
        stringBuilder.append("\n")
        stringBuilder.append("getExternalStorageDirectory: $externalDirPath")
        stringBuilder.append("\n")
        stringBuilder.append("target dir path: $targetDirPath")
        stringBuilder.append("\n")
        stringBuilder.append("target file path: ${targetFile.absolutePath}")
        stringBuilder.append("\n")
        stringBuilder.append("target file content: $targetFileContent")

        Log.d("file_debug","file info: $stringBuilder")
        tv_result.text = "file info: $stringBuilder"
    }

    private fun readFile(file: File) : String {
        var resultStr = ""
        try {
            val fileInputStream = FileInputStream(file)
            val size = fileInputStream.available()
            val buffer = ByteArray(size)
            fileInputStream.read(buffer)
            resultStr = String(buffer, Charset.forName("UTF-8"))
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return resultStr
    }

    private fun checkPermission(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}
