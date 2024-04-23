package org.feup.carlosverissimo3001.theaterbite

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.util.Base64
import androidx.annotation.RequiresApi
import kotlinx.coroutines.withContext
import org.feup.carlosverissimo3001.theaterbite.api.submitOrder
import org.feup.carlosverissimo3001.theaterbite.models.ConfirmedOrder
import org.feup.carlosverissimo3001.theaterbite.models.Order
import org.feup.carlosverissimo3001.theaterbite.models.Parser.parseProduct
import org.feup.carlosverissimo3001.theaterbite.models.Parser.parseVoucher
import org.feup.carlosverissimo3001.theaterbite.models.Product
import org.feup.carlosverissimo3001.theaterbite.models.Voucher
import org.json.JSONObject
import java.nio.ByteBuffer
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Signature
import java.security.spec.X509EncodedKeySpec
import java.util.Locale
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/***** UTILS.KT *****/
// Description: Helper functions for many purposes
/***** UTILS.KT *****/

/**
 * Define extension function to get a parcelable extra from an Intent
 * @param key key of the parcelable extra
 * @return parcelable extra
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    // Use the new getParcelableExtra method that accepts a class parameter
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)

    // else use the deprecated getParcelableExtra method
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

/**
 * Convert a ByteArray to a hexadecimal string
 * @param ba ByteArray to convert
 * @return hexadecimal string
 */
fun byteArrayToHex(ba: ByteArray): String {
    val sb = StringBuilder(ba.size * 2)
    for (b in ba) sb.append(String.format("%02x", b))
    return sb.toString()
}

/**
 * Convert a hexadecimal string to a ByteArray
 * @param s hexadecimal string to convert
 * @return ByteArray
 */
fun hexStringToByteArray(s: String): ByteArray {
    val data = ByteArray(s.length/2)
    for (k in 0 until s.length/2)
        data[k] = ((Character.digit(s[2*k], 16) shl 4) + Character.digit(s[2*k+1], 16)).toByte()
    return data
}

/**
 * Decode a Base64 encoded public key into a PublicKey object
 * @param base64PublicKey Base64 encoded public key
 * @return PublicKey object

 */
fun decodePublicKey(base64PublicKey: String): PublicKey {
    // Decode the Base64 string into a byte array
    val publicKeyBytes = Base64.decode(base64PublicKey, Base64.DEFAULT)

    // Create an X509EncodedKeySpec using the byte array
    val keySpec = X509EncodedKeySpec(publicKeyBytes)

    // Initialize a KeyFactory for RSA
    val keyFactory = KeyFactory.getInstance("RSA")

    // Generate the public key from the X509EncodedKeySpec
    return keyFactory.generatePublic(keySpec)
}

/**
 * Format a price into a string with 2 decimal places and the currency symbol
 * @param price price to format
 * @return formatted price
 */
fun formatPrice (price: Double) : String {
    // NOTE: Locale.US is used to ensure the decimal separator is a dot
    return String.format(Locale.US, "%.2f", price) + "€"
}

/**
 * Given a JSON response, parse it into a ConfirmedOrder object
 * @param jsonResponse JSON response to parse
 * @return ConfirmedOrder object
 */
fun parseJSONResponse(jsonResponse: JSONObject): ConfirmedOrder {
    val orderObject = jsonResponse.getJSONObject("order")

    val orderNumber = orderObject.getInt("order_number")
    val total = orderObject.getDouble("total")
    val items = orderObject.getJSONArray("items")
    val vouchersUsed = orderObject.getJSONArray("vouchers_used")
    val vouchersGenerated = orderObject.getJSONArray("vouchers_generated")

    // Order items
    val products = mutableListOf<Product>()
    for (i in 0 until items.length()) {
        products.add(parseProduct(items.getJSONObject(i)))
    }

    // Vouchers used
    val vouchersUsedList = mutableListOf<Voucher>()
    for (i in 0 until vouchersUsed.length()) {
        vouchersUsedList.add(parseVoucher(vouchersUsed.getJSONObject(i)))
    }

    // Vouchers generated
    val vouchersGeneratedList = mutableListOf<Voucher>()
    for (i in 0 until vouchersGenerated.length()) {
        vouchersGeneratedList.add(parseVoucher(vouchersGenerated.getJSONObject(i)))
    }

    return ConfirmedOrder(orderNumber, products, total, vouchersUsedList, vouchersGeneratedList)
}

/**
 * Verify the signature of a message
 * @param content message to verify
 * @param publicKey public key to use for verification
 * @return true if the signature is valid, false otherwise
 */
fun verifySignature(content: ByteArray, publicKey: PublicKey) : Boolean {
    val bb = ByteBuffer.wrap(content)
    val sign = ByteArray(Constants.KEY_SIZE / 8)
    val mess = ByteArray(content.size - sign.size)

    // get the message and the signature
    bb.get(mess, 0, mess.size)
    bb.get(sign, 0, Constants.KEY_SIZE/8)
    // the signature bytes are the last 64 bytes of the content

    return try {
        Signature.getInstance(Constants.SIGN_ALGO).run {
            initVerify(publicKey)
            update(mess)
            verify(sign)
        }
    } catch (ex: Exception) {
        println("Error verifying signature: ${ex.message}")
        false
    }
}

/**
* Call the API to submit an order
* @param userid user id
* @param order order to submit
* @return ConfirmedOrder object
*/
fun sendOrder(userid: String, order: Order) : ConfirmedOrder {
    var isOrderSubmitted = false

    var confirmedOrder = ConfirmedOrder(
        orderNo = 0,
        products = emptyList(),
        total = 0.0,
        vouchersUsed = emptyList(),
        vouchersGenerated = emptyList()
    )

    submitOrder(
        userid,
        order,
        onError = {
            println("Error submitting order")
            return@submitOrder
        },
        onSuccess = {
            confirmedOrder = it
            isOrderSubmitted = true
        })

    while (!isOrderSubmitted){
        Thread.sleep(10)
    }

    return confirmedOrder
}

/**
 * Parse a voucher type into a human-readable string
 * @param type voucher type
 * @return human-readable string
 */
fun parseVoucherType (type: String) : String {
    return when (type) {
        "FREE_COFFEE" -> "Free Coffee"
        "FREE_POPCORN" -> "Free Popcorn"
        else -> "5% Discount"
    }
}

var itemEmojis = mapOf(
    "Popcorn" to "\uD83C\uDF7F",
    "Soda" to "\uD83E\uDD64",
    "Coffee" to "☕",
    "Sandwich" to "\uD83C\uDF54",
    "Free Popcorn" to "\uD83C\uDF7F",
    "Free Coffee" to "☕",
)