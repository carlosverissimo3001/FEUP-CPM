package org.feup.carlosverissimo3001.theaterpal.file

import android.R.attr.bitmap
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseShow
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseTicket
import org.feup.carlosverissimo3001.theaterpal.models.Parser.parseVoucher
import org.feup.carlosverissimo3001.theaterpal.models.Parser.showsToJson
import org.feup.carlosverissimo3001.theaterpal.models.Parser.ticketsToJson
import org.feup.carlosverissimo3001.theaterpal.models.Parser.vouchersToJson
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import org.feup.carlosverissimo3001.theaterpal.models.Voucher
import org.feup.carlosverissimo3001.theaterpal.models.show.Show
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
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

fun areShowsStoredInCache(context: Context): Boolean {
    val cacheDir = context.cacheDir
    val showsCacheDir = File(cacheDir, "shows")

    return showsCacheDir.exists()
}



/**** TICKETS ****/

/**
 * When the user purchases tickets, the server will return a list of tickets.
 * We want to append these tickets to the cache
 * @param tickets List of tickets to append to the cache
 * @param context Context of the application
 * @param isSuccess Callback function to be called when the operation is done
 * @see saveTicketsToCache
 */
fun appendTicketsToCache(tickets: JSONArray, context: Context, isSuccess: (Boolean) -> Unit){
    val cacheDir = context.cacheDir
    val ticketsCacheDir = File(cacheDir, "tickets")
    val ticketsFile = File(ticketsCacheDir, "tickets.json")

    // First check if the file exists
    if (!ticketsFile.exists()) {
        val ticketList = mutableListOf<Ticket>()
        for (i in 0 until tickets.length()) {
            ticketList.add(parseTicket(tickets.getJSONObject(i)))
        }

        saveTicketsToCache(ticketList, context, isSuccess)
        return
    }

    try {
        // Read existing JSON content from file
        val existingJsonArray = if (ticketsFile.exists()) {
            val inputStream = FileInputStream(ticketsFile)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            JSONArray(String(buffer))
        } else {
            JSONArray()
        }

        // Append new tickets to existing JSON array
        for (i in 0 until tickets.length()) {
            existingJsonArray.put(tickets.getJSONObject(i))
        }

        // Write updated JSON content back to file
        val outputStream = FileOutputStream(ticketsFile)
        outputStream.write(existingJsonArray.toString().toByteArray())
        outputStream.close()
        isSuccess(true)

    } catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

/**
 * Save a list of tickets to the cache
 * @param tickets List of tickets to save to the cache
 * @param context Context of the application
 * @param isSuccess Callback function to be called when the operation is done
 * @see appendTicketsToCache
 * @see Ticket
 */
fun saveTicketsToCache(tickets: List<Ticket>, context: Context, isSuccess: (Boolean) -> Unit){
    val cacheDir = context.cacheDir
    val ticketsCacheDir = File(cacheDir, "tickets")
    val ticketsString = ticketsToJson(tickets)

    if(!ticketsCacheDir.exists()){
        ticketsCacheDir.mkdirs()
    }

    val ticketsFile = File(ticketsCacheDir, "tickets.json")

    // append to the file
    try {
        val stream = FileOutputStream(ticketsFile)
        stream.write(ticketsString.toByteArray())
        stream.close()
        isSuccess(true)
    }
    catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

/**
 * Check if the tickets are stored in the cache
 * @param context Context of the application
 * @return True if the tickets are stored in the cache, false otherwise
 */
fun areTicketsStoredInCache(context: Context): Boolean {
    val cacheDir = context.cacheDir
    val ticketsCacheDir = File(cacheDir, "tickets")

    return ticketsCacheDir.exists()
}

/**
 * Load the tickets from the cache
 * @param context Context of the application
 * @param callback Callback function to be called when the tickets are loaded
 * @see Ticket
 */
fun loadTicketsFromCache(context: Context, callback: (List<Ticket>) -> Unit) {
    val cacheDir = context.cacheDir
    val ticketsCacheDir = File(cacheDir, "tickets")
    val tickets = mutableListOf<Ticket>()

    if (!ticketsCacheDir.exists()) {
        callback(emptyList())
    }

    val ticketsFile = File(ticketsCacheDir, "tickets.json")
    val ticketsString = ticketsFile.readText()

    if (ticketsString == "") {
        callback(emptyList())
    }

    // Parse the string to a list of tickets
    val ticketsJsonArray = JSONArray(ticketsString)

    for (i in 0 until ticketsJsonArray.length()){
        if (ticketsJsonArray.isNull(i)) continue
        val ticketJson = ticketsJsonArray.getJSONObject(i)
        tickets.add(parseTicket(ticketJson))
    }

    callback(tickets)
}

fun setTicketsAsUsed(ticketsUsed: List<Ticket>, context: Context, callback: (Boolean) -> Unit){
    // first need to load the tickets from the cache
    loadTicketsFromCache(context) { tickets ->
        // then set the tickets as used
        val updatedTickets = tickets.map { ticket ->
            if (ticketsUsed.contains(ticket)) {
                ticket.isUsed = true
            }
            ticket
        }

        saveTicketsToCache(updatedTickets, context) { isSuccess ->
            callback(isSuccess)
        }
    }
}

/**** TICKETS ****/

/**** VOUCHERS ****/

/**
 * Save a list of vouchers to the cache
 * @param vouchers List of vouchers to save to the cache
 * @param context Context of the application
 * @param isSuccess Callback function to be called when the operation is done
 * @see Voucher
 */
fun saveVouchersToCache(vouchers: List<Voucher>, context: Context, isSuccess: (Boolean) -> Unit){
    val cacheDir = context.cacheDir
    val vouchersCacheDir = File(cacheDir, "vouchers")
    val vouchersString = vouchersToJson(vouchers)

    if(!vouchersCacheDir.exists()){
        vouchersCacheDir.mkdirs()
    }

    val vouchersFile = File(vouchersCacheDir, "vouchers.json")

    try {
        val stream = FileOutputStream(vouchersFile)
        stream.write(vouchersString.toByteArray())
        stream.close()
        isSuccess(true)
    }
    catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

/**
 * When the user purchases tickets, the server will return a list of vouchers that were generated.
 * We want to append these vouchers to the cache
 * @param vouchers List of vouchers to append to the cache
 * @param context Context of the application
 * @param isSuccess Callback function to be called when the operation is done
 * @see saveVouchersToCache
 */
fun appendVouchersToCache(vouchers: JSONArray, context: Context, isSuccess: (Boolean) -> Unit){
    val cacheDir = context.cacheDir
    val vouchersCacheDir = File(cacheDir, "vouchers")
    val vouchersFile = File(vouchersCacheDir, "vouchers.json")

    // First check if the file exists
    if (!vouchersFile.exists()) {
        val voucherList = mutableListOf<Voucher>()
        for (i in 0 until vouchers.length()) {
            voucherList.add(parseVoucher(vouchers.getJSONObject(i)))
        }

        saveVouchersToCache(voucherList, context, isSuccess)
        return
    }

    try {
        // Read existing JSON content from file
        val existingJsonArray = if (vouchersFile.exists()) {
            val inputStream = FileInputStream(vouchersFile)
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            inputStream.close()
            JSONArray(String(buffer))
        } else {
            JSONArray()
        }

        // Append new vouchers to existing JSON array
        for (i in 0 until vouchers.length()) {
            existingJsonArray.put(vouchers.getJSONObject(i))
        }

        // Write updated JSON content back to file
        val outputStream = FileOutputStream(vouchersFile)
        outputStream.write(existingJsonArray.toString().toByteArray())
        outputStream.close()
        isSuccess(true)

    } catch (e: Exception) {
        e.printStackTrace()
        isSuccess(false)
    }
}

/**
 * Load the vouchers from the cache
 * @param context Context of the application
 * @param callback Callback function to be called when the vouchers are loaded
 * @see Voucher
 */
fun loadVouchersFromCache(context: Context, callback: (List<Voucher>) -> Unit) {
    val cacheDir = context.cacheDir
    val vouchersCacheDir = File(cacheDir, "vouchers")
    val vouchers = mutableListOf<Voucher>()

    if (!vouchersCacheDir.exists()) {
        callback(emptyList())
    }

    val vouchersFile = File(vouchersCacheDir, "vouchers.json")
    val vouchersString = vouchersFile.readText()

    if (vouchersString == "") {
        callback(emptyList())
    }

    // Parse the string to a list of vouchers
    val vouchersJsonArray = JSONArray(vouchersString)
    for (i in 0 until vouchersJsonArray.length()){
        if (vouchersJsonArray.isNull(i)) continue
        val voucherJson = vouchersJsonArray.getJSONObject(i)
        vouchers.add(parseVoucher(voucherJson))
    }

    callback(vouchers)
}

/**
 * Check if the vouchers are stored in the cache
 * @param context Context of the application
 * @return True if the vouchers are stored in the cache, false otherwise
 */
fun areVouchersStoredInCache(context: Context): Boolean {
    val cacheDir = context.cacheDir
    val vouchersCacheDir = File(cacheDir, "vouchers")

    return vouchersCacheDir.exists()
}

fun setVouchersAsUsed(vouchersUsed: List<Voucher>, context: Context, callback: (Boolean) -> Unit){
    // first need to load the vouchers from the cache
    loadVouchersFromCache(context) { vouchers ->
        // then set the vouchers as used
        val updatedVouchers = vouchers.map { voucher ->
            if (vouchersUsed.contains(voucher)) {
                voucher.isUsed = true
            }
            voucher
        }

        saveVouchersToCache(updatedVouchers, context) { isSuccess ->
            callback(isSuccess)
        }
    }
}

/**** VOUCHERS ****/


/**** OTHER ****/

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