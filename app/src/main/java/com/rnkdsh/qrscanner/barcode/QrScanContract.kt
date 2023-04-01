package com.rnkdsh.qrscanner.barcode

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class QrScanContract : ActivityResultContract<Any, QrData?>() {

    override fun createIntent(context: Context, input: Any): Intent {
        return Intent(context, QrScanActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): QrData {
        return QrData(
            isSuccess = intent?.getBooleanExtra("isSuccess", false) ?: false,
            data = intent?.getStringExtra("data"),
            message = intent?.getStringExtra("message")
        )
    }
}
