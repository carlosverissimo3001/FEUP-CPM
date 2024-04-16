package org.feup.carlosverissimo3001.theaterpal.nfc

import android.content.Context
import android.nfc.NfcAdapter
import android.util.Log
import org.feup.carlosverissimo3001.theaterpal.Constants
import org.feup.carlosverissimo3001.theaterpal.auth.Authentication
import org.feup.carlosverissimo3001.theaterpal.models.order.Order
import org.feup.carlosverissimo3001.theaterpal.models.Ticket
import java.nio.ByteBuffer
import java.security.KeyStore
import java.security.Signature

const val logTag = "TheaterPalNFC"

fun isNfcavailable(context: Context): Boolean {
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    return nfcAdapter != null
}

fun isNfcEnabled(context: Context): Boolean {
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    return nfcAdapter?.isEnabled ?: false
}

fun buildOrderMessage(order: Order, ctx: Context) : ByteArray{
    val products = order.barOrder.items

    // get the user_id
    val user_id = Authentication(ctx).getUserID()

    // vouchers
    val vouchersUsed = order.vouchersUsed.size

    // how many bytes to be allocated for the vouchers
    val voucherBytesLength = vouchersUsed * Constants.UUID_SIZE

    // elements of the order
    var productBytes = 0

    products.entries.forEach {(item, qnt) ->
        // 1 byte for the length of the product name
        // n bytes for the product name
        // 1 byte for the quantity
        // 1 byte for the length of the product price
        // n bytes for the product price
        productBytes += 1 + item.name.length + 1 + item.price.toString().length + 1
    }

    // byte @index 0 is the number of products
    // byte @index 1 is the length of the user_id
    val messageSize = 1 + (1 + user_id.length) + (1 + voucherBytesLength) + productBytes + 1 + order.barOrder.total.toString().length + (Constants.KEY_SIZE / 8)

    val messageBuilder = ByteBuffer.allocate(messageSize)
    messageBuilder.put(products.size.toByte())

    // put the user_id
    val user_idBytes = user_id.toByteArray()
    val user_idLength = user_idBytes.size.toByte()
    messageBuilder.put(user_idLength)
    messageBuilder.put(user_idBytes)

    // put the vouchers
    messageBuilder.put(vouchersUsed.toByte())
    order.vouchersUsed.forEach { voucher ->
        val voucherBytes = voucher.voucherid.toByteArray()
        messageBuilder.put(voucherBytes)
    }

    products.forEach{ (item, qnt) ->
        // PRODUCT NAME
        val nameBytes = item.name.toByteArray()
        val nameLength = nameBytes.size.toByte()
        messageBuilder.put(nameLength)
        messageBuilder.put(nameBytes)

        // PRODUCT PRICE
        val priceBytes = item.price.toString().toByteArray()
        val priceLength = priceBytes.size.toByte()
        messageBuilder.put(priceLength)
        messageBuilder.put(priceBytes)

        // QUANTITY
        messageBuilder.put(qnt.toByte())
    }

    //total
    val totalBytes = order.barOrder.total.toString().toByteArray()
    val totalLength = totalBytes.size.toByte()
    messageBuilder.put(totalLength)
    messageBuilder.put(totalBytes)

    val message = messageBuilder.array()

    // Delimits where the signature should be placed
    val messageOffset = message.size - Constants.KEY_SIZE / 8

    try {
        val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
            load(null)
            getEntry(Constants.KEY_NAME, null)
        }
        val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
        val signature = Signature.getInstance(Constants.SIGN_ALGO).run {
            initSign(privateKey)
            update(message, 0, messageOffset)
            sign(message, messageOffset, Constants.KEY_SIZE / 8)
        }
        Log.d(logTag, "Signature size = $signature bytes.")
        println("Signature size = $signature bytes.")
        println("Total size = ${message.size} bytes.")
    } catch (ex: Exception) {
        Log.d(logTag, ex.toString())
    }

    return message
}

fun buildTicketMessage(tickets: List<Ticket>, ctx: Context): ByteArray {
    // get the user_id
    val user_id = Authentication(ctx).getUserID()

    // byte @index 0 is the number of tickets
    // byte @index 1 is the length of the user_id
    val totalSize = 1 + 1 + user_id.length + tickets.sumOf {
        1 + it.ticketid.length + 1 + it.userid.length + 1 + it.showName.length + 1 +
                it.seat.length + 1 + 1 + it.date.length
    } + Constants.KEY_SIZE / 8
    // include the size of the signature and the size of the user_id

    val messageBuilder = ByteBuffer.allocate(totalSize)

    messageBuilder.put(tickets.size.toByte())

    // put the user_id
    val user_idBytes = user_id.toByteArray()
    val user_idLength = user_idBytes.size.toByte()
    messageBuilder.put(user_idLength)
    messageBuilder.put(user_idBytes)

    tickets.forEach { ticket ->
        // TICKETID
        val ticketidBytes = ticket.ticketid.toByteArray()
        val tickedidLength = ticketidBytes.size.toByte()
        messageBuilder.put(tickedidLength)
        messageBuilder.put(ticketidBytes)

        // USERID
        val useridBytes = ticket.userid.toByteArray()
        val useridLength = useridBytes.size.toByte()
        messageBuilder.put(useridLength)
        messageBuilder.put(useridBytes)


        // SHOWNAME
        val showNameBytes = ticket.showName.toByteArray()
        val showNameLength = showNameBytes.size.toByte()
        messageBuilder.put(showNameLength)
        messageBuilder.put(showNameBytes)

        // SEAT
        val seatBytes = ticket.seat.toByteArray()
        val seatLength = seatBytes.size.toByte()
        messageBuilder.put(seatLength)
        messageBuilder.put(seatBytes)

        // IS USED
        val isUsedByte = if (ticket.isUsed) 1.toByte() else 0.toByte()
        messageBuilder.put(isUsedByte)

        // DATE
        val dateBytes = ticket.date.toByteArray()
        val dateLength = dateBytes.size.toByte()
        messageBuilder.put(dateLength)
        messageBuilder.put(dateBytes)
    }

    val message = messageBuilder.array()

    // Delimits where the signature should be placed
    val messageOffset = message.size - Constants.KEY_SIZE / 8

    try {
        val entry = KeyStore.getInstance(Constants.ANDROID_KEYSTORE).run {
            load(null)
            getEntry(Constants.KEY_NAME, null)
        }
        val privateKey = (entry as KeyStore.PrivateKeyEntry).privateKey
        val signature = Signature.getInstance(Constants.SIGN_ALGO).run {
            initSign(privateKey)
            update(message, 0, messageOffset)
            sign(message, messageOffset, Constants.KEY_SIZE / 8)
        }
        Log.d(logTag, "Signature size = $signature bytes.")
        println("Signature size = $signature bytes.")
        println("Total size = ${message.size} bytes.")
    } catch (ex: Exception) {
        Log.d(logTag, ex.toString())
    }

    return message
}