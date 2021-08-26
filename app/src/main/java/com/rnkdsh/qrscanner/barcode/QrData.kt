package com.rnkdsh.qrscanner.barcode

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QrData(
    val isSuccess: Boolean,
    val data: String?,
    val message: String?
) : Parcelable