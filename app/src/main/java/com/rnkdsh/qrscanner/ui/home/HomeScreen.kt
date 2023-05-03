package com.rnkdsh.qrscanner.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.rnkdsh.qrscanner.barcode.QrScanContract
import com.rnkdsh.qrscanner.common.DevicePreviews
import com.rnkdsh.qrscanner.ui.theme.MyApplicationTheme

@Composable
internal fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()
    val scanner = GmsBarcodeScanning.getClient(context, options)
    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        onClickCamera = {
            //qrDataResult.launch(Any())
        },
        onClickGoogle = {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    // Task completed successfully
                    val rawValue: String? = barcode.rawValue
                    Toast.makeText(
                        context,
                        rawValue,
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnCanceledListener {
                    // Task canceled
                    Toast.makeText(
                        context,
                        "Cancelled",
                        Toast.LENGTH_LONG
                    ).show()
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                    Toast.makeText(
                        context,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
        },
    )
}

@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    onClickCamera: () -> Unit,
    onClickGoogle: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = onClickCamera
        ) {
            Text(text = "Scan Camera")
        }
        Button(
            onClick = onClickGoogle
        ) {
            Text(text = "Scan Google Services")
        }
    }
}

/*private val qrDataResult = registerForActivityResult(QrScanContract()) {
    it?.let {
        if (it.isSuccess) {
//                binding.qrCodeResult.text = it.data
        } else {
            Toast.makeText(this@MainActivity, it.message.orEmpty(), Toast.LENGTH_LONG).show()
        }
    }
}*/

@DevicePreviews
@Composable
fun HomeScreenPreview() {
    BoxWithConstraints {
        MyApplicationTheme {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                onClickCamera = {},
                onClickGoogle = {},
            )
        }
    }
}
