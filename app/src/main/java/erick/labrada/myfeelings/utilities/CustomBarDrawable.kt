package erick.labrada.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import erick.labrada.myfeelings.R

class CustomBarDrawable(
    private val context: Context,
    private val emocion: Emociones
) : Drawable() {

    override fun draw(canvas: Canvas) {
        val fondo = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.gray)
        }

        val ancho = (canvas.width - 10).toFloat()
        val alto = (canvas.height - 10).toFloat()

        val rectFondo = RectF(0f, 0f, ancho, alto)
        canvas.drawRect(rectFondo, fondo)

        val porcentaje = emocion.porcentaje * (canvas.width - 10) / 100
        val rectEmocion = RectF(0f, 0f, porcentaje, alto)

        val seccion = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            color = ContextCompat.getColor(context, emocion.color)
        }

        canvas.drawRect(rectEmocion, seccion)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int = PixelFormat.OPAQUE
}
