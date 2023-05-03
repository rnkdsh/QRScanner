package com.rnkdsh.qrscanner.common.extensions

import android.app.Activity
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.ShareCompat

fun Context.call(phone: String?) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phone")
    startActivity(intent)
}

fun Context.mail(email: String?, subject: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
    //intent.data = Uri.parse("mailto:$email")
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, deviceDetails())
    startActivity(Intent.createChooser(intent, "Send Email"))
}

fun Context.sendWhatsApp(phone: String? = null, message: String? = null) {
    val url = Uri.Builder().scheme("https")
        .authority("api.whatsapp.com")
        .path("send")
    if (phone != null) {
        var number = phone.replace("+", "")
        if (number.length == 10) {
            number = "91$number"
        }
        url.appendQueryParameter("phone", number)
    }
    if (message != null) {
        url.appendQueryParameter("text", message)
    }
    //Toast.makeText(this, url.build().toString(), Toast.LENGTH_LONG).show()
    startActivity(Intent(Intent.ACTION_VIEW, url.build()))
}

fun Context.deviceDetails(): String {
    return buildString {
        append("\n\n")
        append("--------------------")
        append("\n")
        append("Device Info:")
        append("\n\n")
        append("OS Version: " + System.getProperty("os.version") + "(" + Build.VERSION.INCREMENTAL + ")")
        append("\n")
        append("OS API Level: " + Build.VERSION.SDK_INT)
        append("\n")
        append("Device: " + Build.DEVICE)
        append("\n")
        append("Model (Product): " + Build.MODEL + " (" + Build.PRODUCT + ")")
    }
}

fun Context.openBrowser(url: String?) {
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW)
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            intent.data = Uri.parse("http://$url")
        } else {
            intent.data = Uri.parse(url)
        }
        startActivity(intent)
    }
}

fun Context.shareText(
    text: String,
    title: String? = null,
    previewImage: Uri? = null,
    pendingIntent: PendingIntent? = null
) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)

        title?.let {
            // (Optional) Here we're setting the title of the content
            putExtra(Intent.EXTRA_TITLE, it)
        }

        previewImage?.let {
            // (Optional) Here we're passing a content URI to an image to be displayed
            data = it
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        startActivity(Intent.createChooser(intent, "Share", pendingIntent?.intentSender))
    } else {
        startActivity(Intent.createChooser(intent, "Share"))
    }
}

fun Activity.shareImage(uri: Uri, pendingIntent: PendingIntent? = null) {
    val sharingIntent = ShareCompat.IntentBuilder(this)
        .setStream(uri)
        .setType("text/html")
        .intent
        .setAction(Intent.ACTION_SEND)
        .setDataAndType(uri, "image/*")
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        startActivity(Intent.createChooser(sharingIntent, "Share", pendingIntent?.intentSender))
    } else {
        startActivity(Intent.createChooser(sharingIntent, "Share"))
    }
}

fun Context.openPlayStore() {
    try {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}
