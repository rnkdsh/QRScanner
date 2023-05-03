package com.rnkdsh.qrscanner.common.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import java.io.File
import java.io.FileDescriptor
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow

fun String.isValidIndianMobileNumber(): Boolean {
    var string = this
    if (string.startsWith("+91"))
        string = string.substringAfter("+91")
    return Pattern.matches("^[6-9]\\d{9}\$", string)
}

fun String.formatIndianMobileNumber(): String {
    return if (startsWith("+91")) {
        removePrefix("+91")
    } else {
        this
    }.replace(" ", "")
}

fun Uri.getBitmap(context: Context): Bitmap? {
    val parcelFileDescriptor: ParcelFileDescriptor? =
        context.contentResolver.openFileDescriptor(this, "r")
    val fileDescriptor: FileDescriptor? = parcelFileDescriptor?.fileDescriptor
    val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): Uri {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    //val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val storageDir: File? = cacheDir
    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(this, "$packageName.provider", file)
}

fun Long.beautify(): String {
    if (this < 1000) return "" + this
    val exp = (ln(this.toDouble()) / ln(1000.0)).toInt()
    return String.format("%.1f %c", this / 1000.0.pow(exp.toDouble()), "KMGTPE"[exp - 1])
}

fun Long.beautifyFileSize(): String {
    if (this <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(this / 1024.0.pow(digitGroups.toDouble()))
        .toString() + " " + units[digitGroups]
}

fun Context.showSettingsScreen() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    startActivity(intent)
}

fun Intent.canBeHandled(packageManager: PackageManager): Boolean {
    return resolveActivity(packageManager) != null
}
