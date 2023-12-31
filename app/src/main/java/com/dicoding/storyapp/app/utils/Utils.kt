package com.dicoding.storyapp.app.utils

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Environment
import android.view.View
import com.dicoding.storyapp.BuildConfig
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


object Utils {
    const val BASE_URL = BuildConfig.BASE_URL
    const val IS_DEBUG = BuildConfig.BUILD_TYPE == "debug"

    const val GET_EMAIL_KEY = "GET_EMAIL_KEY"
    const val GET_PASSWORD_KEY = "GET_PASSWORD_KEY"
    const val GET_REQ_EMAIL = "GET_REQ_KEY"
    const val GET_REQ_PASSWORD = "GET_PASSWORD_KEY"

    private const val FILENAME_FORMAT = "yyyy-MM-dd:HH-mm-ss-SSS"
    private const val MAXIMAL_SIZE = 1000000

    fun showLoading(view: View, isLoading: Boolean) =
        if (isLoading) view.visibility = View.VISIBLE
        else view.visibility = View.INVISIBLE

    fun createFile(application: Application): File {
        val timeStamp: String = SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.US
        ).format(System.currentTimeMillis())


        val outputDirectory = application.cacheDir.absoluteFile

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun rotateFile(file: File, isBackCamera: Boolean = false) {
        val matrix = Matrix()
        val bitmap = BitmapFactory.decodeFile(file.path)
        val rotation = if (isBackCamera) 90f else -90f
        matrix.postRotate(rotation)
        if (!isBackCamera) {
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
        val result = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        result.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(file))
    }

    fun getFileFromUri(uri: Uri, context: Context): File {
        val timeStamp: String = SimpleDateFormat(
            FILENAME_FORMAT,
            Locale.US
        ).format(System.currentTimeMillis())
        val contentResolver: ContentResolver = context.contentResolver

        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val photoFile: File = File.createTempFile(timeStamp, ".jpg", storageDir)

        val inputStream = contentResolver.openInputStream(uri) as InputStream
        val outputStream: OutputStream = FileOutputStream(photoFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)

        outputStream.close()
        inputStream.close()

        return photoFile
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val conMan = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return conMan.activeNetworkInfo != null && conMan.activeNetworkInfo!!.isConnected
    }
}