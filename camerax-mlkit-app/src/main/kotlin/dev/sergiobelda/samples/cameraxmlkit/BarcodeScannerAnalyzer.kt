package dev.sergiobelda.samples.cameraxmlkit

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeScannerAnalyzer : ImageAnalysis.Analyzer {

    var listener: Listener? = null

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanBarcodes(image)
        }
        imageProxy.close()
    }

    private fun scanBarcodes(image: InputImage) {
        val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        ).build()
        // val scanner = BarcodeScanning.getClient(options)
        val scanner = BarcodeScanning.getClient()
        val result =
            scanner.process(image).addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints
                    val rawValue = barcode.rawValue
                    /*
                    when (barcode.valueType) {
                        Barcode.TYPE_WIFI -> {
                            val ssid = barcode.wifi?.ssid
                            val password = barcode.wifi?.password
                            val type = barcode.wifi?.encryptionType
                        }
                        Barcode.TYPE_URL -> {
                            val title = barcode.url?.title
                            val url = barcode.url?.url
                        }

                        else -> {
                            listener?.onBarcodeQRScanned(barcode?.displayValue ?: "")
                            Log.d("QR Code", barcode.displayValue ?: "")
                        }
                    }
                    */
                    listener?.onBarcodeQRScanned(barcode?.displayValue ?: "")
                    Log.d("QR Code", barcode.displayValue ?: "")
                }
            }.addOnFailureListener {

            }
    }

    interface Listener {
        fun onBarcodeQRScanned(value: String)
    }
}
