package org.feup.carlosverissimo3001.theaterpal.file

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream


fun saveImageToCache(imageBase64: String, filename: String, context: Context, isSuccess: (Boolean) -> (Unit)) {
    val cacheDir = context.cacheDir
    val imagesCacheDir = File(cacheDir, "images")

    if (!imagesCacheDir.exists()) {
        imagesCacheDir.mkdirs()
    }

    val imageFile = File(imagesCacheDir, filename)
    val bitmap = decodeBase64ToBitmap(imageBase64)

    try {
        val stream = FileOutputStream(imageFile)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
        isSuccess(true)
    }
    catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

fun saveShowsToCache(shows: JSONArray, context: Context, isSuccess: (Boolean) -> (Unit)) {
    val cacheDir = context.cacheDir
    val showsCacheDir = File(cacheDir, "shows")

    if (!showsCacheDir.exists()) {
        showsCacheDir.mkdirs()
    }

    val showsFile = File(showsCacheDir, "shows.json")

    try {
        val stream = FileOutputStream(showsFile)
        stream.write(shows.toString().toByteArray())
        stream.close()
        isSuccess(true)
    }
    catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

fun loadShowsFromCache(context: Context, callback: (JSONArray) -> Unit) {
    val cacheDir = context.cacheDir
    val showsCacheDir = File(cacheDir, "shows")

    if (!showsCacheDir.exists()) {
        callback(JSONArray())
    }

    val showsFile = File(showsCacheDir, "shows.json")
    val showsString = showsFile.readText()

    callback(JSONArray(showsString))
}

fun areImagesStoreInCache(context: Context): Boolean {
    val cacheDir = context.cacheDir
    val imagesCacheDir = File(cacheDir, "images")

    return imagesCacheDir.exists()
}

fun areShowsStoreInCache(context: Context): Boolean {
    val cacheDir = context.cacheDir
    val showsCacheDir = File(cacheDir, "shows")

    return showsCacheDir.exists()
}


fun loadImageFromCache(filename: String, context: Context): Bitmap? {
    // println("Loading image $filename from cache")

    val cacheDir = context.cacheDir
    val imagesCacheDir = File(cacheDir, "images")

    if (!imagesCacheDir.exists()) {
        return null
    }

    val imageFile = File(imagesCacheDir, filename)
    return BitmapFactory.decodeFile(imageFile.absolutePath)
}

fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}