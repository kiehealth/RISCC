package com.cronelab.riscc.support.common.extension.customViews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView


class CircleImage : AppCompatImageView {
    private var path: Path? = null
    private var rect: RectF? = null
    private fun init() {
        path = Path()
        rect = RectF()
    }

    override fun onDraw(canvas: Canvas) {
        rect!![5f, 5f, this.measuredWidth - 5.toFloat()] = this.measuredHeight - 5.toFloat()
        val radius = 200.0f
        path!!.addRoundRect(rect!!, radius, radius, Path.Direction.CW)
        canvas.clipPath(path!!)
        super.onDraw(canvas)
    }

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init()
    }
}