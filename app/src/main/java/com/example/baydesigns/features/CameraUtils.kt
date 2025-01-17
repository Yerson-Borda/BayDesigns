package com.example.baydesigns.features

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import java.io.File

object CameraUtils {
    fun saveImageToGallery(context: Context, bitmap: Bitmap): Boolean {
        val fileName = "AR_Design_${System.currentTimeMillis()}.jpg"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        return try {
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }

            // Add the image to the system gallery
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                fileName,
                "AR Design"
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}