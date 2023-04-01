package com.rnkdsh.qrscanner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rnkdsh.qrscanner.barcode.QrScanContract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val qrDataResult = registerForActivityResult(QrScanContract()) {
        it?.let {
            if (it.isSuccess) {
//                binding.qrCodeResult.text = it.data
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContentView(binding.root)
        binding.scanQRCode.setOnClickListener {
            qrDataResult.launch(Any())
        }*/
    }
}
