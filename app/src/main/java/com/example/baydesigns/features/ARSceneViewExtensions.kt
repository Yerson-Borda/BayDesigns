package com.example.baydesigns.features

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.SurfaceView
import io.github.sceneview.SceneView

fun SceneView.captureBitmap(callback: (Bitmap?) -> Unit) {
    try {
        if (this is SurfaceView) {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            PixelCopy.request(
                this, bitmap,
                { result ->
                    if (result == PixelCopy.SUCCESS) {
                        callback(bitmap)
                    } else {
                        callback(null)
                    }
                },
                Handler(Looper.getMainLooper())
            )
        } else {
            callback(null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        callback(null)
    }
}
