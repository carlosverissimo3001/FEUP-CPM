package org.feup.carlosverissimo3001.theaterpal.file

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseShow
import org.feup.carlosverissimo3001.theaterpal.models.Parser.showsToJson
import org.feup.carlosverissimo3001.theaterpal.models.show.Show
import org.json.JSONArray
import org.json.JSONObject
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

fun saveShowsToCache(shows: List<Show>, context: Context, isSuccess: (Boolean) -> (Unit)) {
    val cacheDir = context.cacheDir
    val showsCacheDir = File(cacheDir, "shows")
    val showsString = showsToJson(shows)

    if (!showsCacheDir.exists()) {
        showsCacheDir.mkdirs()
    }

    val showsFile = File(showsCacheDir, "shows.json")

    try {
        val stream = FileOutputStream(showsFile)
        stream.write(showsString.toByteArray())
        stream.close()
        isSuccess(true)
    }
    catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

fun loadShowsFromCache(context: Context, callback: (List<Show>) -> Unit) {
    val cacheDir = context.cacheDir
    val showsCacheDir = File(cacheDir, "shows")
    val shows = mutableListOf<Show>()

    if (!showsCacheDir.exists()) {
        callback(emptyList())
    }

    val showsFile = File(showsCacheDir, "shows.json")
    val showsString = showsFile.readText()

    if (showsString == "") {
        callback(emptyList())
    }

    // Parse the string to a list of shows
    val showsJsonArray = JSONObject(showsString).getJSONArray("shows")
    for (i in 0 until showsJsonArray.length()){
        if (showsJsonArray.isNull(i)) continue
        val showJson = showsJsonArray.getJSONObject(i)
        shows.add(parseShow(showJson))
    }

    callback(shows)
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