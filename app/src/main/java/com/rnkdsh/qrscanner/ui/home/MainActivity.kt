package com.rnkdsh.qrscanner.ui.home

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.rnkdsh.qrscanner.barcode.QrScanContract
import com.rnkdsh.qrscanner.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val qrDataResult = registerForActivityResult(QrScanContract()) {
        it?.let {
            if (it.isSuccess) {
//                binding.qrCodeResult.text = it.data
            } else {
                Toast.makeText(this@MainActivity, it.message.orEmpty(), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .build()
        val scanner = GmsBarcodeScanning.getClient(this@MainActivity, options)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(
                            onClick = {
                                qrDataResult.launch(Any())
                            }
                        ) {
                            Text(text = "Scan Camera")
                        }
                        Button(
                            onClick = {
                                scanner.startScan()
                                    .addOnSuccessListener { barcode ->
                                        // Task completed successfully
                                        val rawValue: String? = barcode.rawValue
                                        Toast.makeText(
                                            this@MainActivity,
                                            rawValue,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    .addOnCanceledListener {
                                        // Task canceled
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Cancelled",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    .addOnFailureListener { e ->
                                        // Task failed with an exception
                                        Toast.makeText(
                                            this@MainActivity,
                                            e.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                        ) {
                            Text(text = "Scan Google Services")
                        }
                    }
                }
            }
        }
    }
}

fun setSplashExitAnimation(splashScreen: SplashScreen) {
    // Add a callback that's called when the splash screen is animating to the
    // app content.
    splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
        // Get the duration of the animated vector drawable.
        val animationDuration = splashScreenViewProvider.iconAnimationDurationMillis
        // Get the start time of the animation.
        val animationStart = splashScreenViewProvider.iconAnimationStartMillis
        // Calculate the remaining duration of the animation.
        val remainingDuration =
            (animationDuration - animationStart - System.currentTimeMillis()).coerceAtLeast(500L)

        // Create your custom animation.
        val slideUp = ObjectAnimator.ofFloat(
            splashScreenViewProvider.view,
            View.TRANSLATION_Y,
            0f,
            -splashScreenViewProvider.view.height.toFloat()
        )
        slideUp.interpolator = AnticipateInterpolator()
        slideUp.duration = remainingDuration

        // Call SplashScreenView.remove at the end of your custom animation.
        slideUp.doOnEnd { splashScreenViewProvider.remove() }

        // Run your animation.
        slideUp.start()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
