package uz.futuresoft.applockdemo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toBitmap

fun String.getAppIcon(context: Context): Bitmap {
    return context.packageManager.getApplicationIcon(this).toBitmap()
}

fun Drawable.toBitmap(): Bitmap {
    val bitmap = createBitmap(this.intrinsicWidth, this.intrinsicHeight)
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}