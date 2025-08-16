package com.goodplayer.keugeugeuk.ui.scratch

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.goodplayer.keugeugeuk.R

class ScratchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    interface OnScratchCompleteListener {
        fun onScratchComplete()
    }

    private var scratchCompleteListener: OnScratchCompleteListener? = null
    private var overlayBitmap: Bitmap
    private var overlayCanvas: Canvas
    private val overlayPaint: Paint
    private val scratchPath = Path()
    private val scratchPaint: Paint

    // 스크래치 완료 체크용 플래그
    private var isCompleted = false
    var scratchThreshold = 70f // % 기준 (기본값 70%)

    init {
        overlayBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        overlayCanvas = Canvas(overlayBitmap)

        overlayPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        scratchPaint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 120f
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        overlayBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        overlayCanvas = Canvas(overlayBitmap)
        drawOverlay()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(overlayBitmap, 0f, 0f, overlayPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                scratchPath.moveTo(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                scratchPath.lineTo(event.x, event.y)
                overlayCanvas.drawPath(scratchPath, scratchPaint)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                scratchPath.reset()
                checkScratchCompletion()
            }
        }
        return true
    }

    fun resetScratch() {
        isCompleted = false
        overlayCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        drawOverlay()
        invalidate()
    }

    private fun drawOverlay() {
        val overlayDrawable = ContextCompat.getDrawable(context, R.drawable.scratch_background)
        overlayDrawable?.setBounds(0, 0, width, height)
        overlayDrawable?.draw(overlayCanvas)
    }

    /**
     * 현재 긁힌 비율(%) 계산
     */
    fun getScratchedPercentage(): Float {
        if (overlayBitmap.width == 0 || overlayBitmap.height == 0) return 0f

        val totalPixels = overlayBitmap.width * overlayBitmap.height
        val pixels = IntArray(totalPixels)
        overlayBitmap.getPixels(pixels, 0, overlayBitmap.width, 0, 0, overlayBitmap.width, overlayBitmap.height)

        var transparentPixels = 0
        for (pixel in pixels) {
            if (Color.alpha(pixel) == 0) {
                transparentPixels++
            }
        }

        return (transparentPixels.toFloat() / totalPixels) * 100f
    }

    /**
     * 스크래치 완료 체크 후 리스너 호출
     */
    private fun checkScratchCompletion() {
        if (!isCompleted) {
            val scratchedPercent = getScratchedPercentage()
            if (scratchedPercent >= scratchThreshold) {
                isCompleted = true
                scratchCompleteListener?.onScratchComplete()
            }
        }
    }

    fun setOnScratchCompleteListener(listener: OnScratchCompleteListener) {
        this.scratchCompleteListener = listener
    }
}