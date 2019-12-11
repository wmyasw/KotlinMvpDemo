package com.wmy.kotlin.demo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *
 *@author：wmyasw
 */
class BezierView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var mPaint: Paint
    var centerX: Int = 0
    var centerY: Int = 0

    var start: PointF
    var end: PointF
    var control: PointF

    init {
        mPaint = Paint()
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8F);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(60F);

        start = PointF(0F, 0F);
        end = PointF(0F, 0F);
        control = PointF(0F, 0F);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;

        // 初始化数据点和控制点的位置
        start.x = (centerX - 200).toFloat();
        start.y = centerY.toFloat();
        end.x = (centerX + 200).toFloat();
        end.y = centerY.toFloat();
        control.x = centerX.toFloat()
        control.y = (centerY - 100).toFloat()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        control.x = event!!.getX()
        control.y = event.getY()
        invalidate()
        return true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            // 绘制数据点和控制点
            mPaint.setColor(Color.GRAY);
            mPaint.setStrokeWidth(20F);
            canvas.drawPoint(start.x, start.y, mPaint);
            canvas.drawPoint(end.x, end.y, mPaint);
            canvas.drawPoint(control.x, control.y, mPaint);

            // 绘制辅助线
            mPaint.setStrokeWidth(4F)
            canvas.drawLine(start.x, start.y, control.x, control.y, mPaint);
            canvas.drawLine(end.x, end.y, control.x, control.y, mPaint);

            // 绘制贝塞尔曲线
            mPaint.setColor(Color.RED)
            mPaint.setStrokeWidth(8F)
            mPaint.setStyle(Paint.Style.FILL)
            var path = Path()
            path.fillType=Path.FillType.EVEN_ODD
            path.moveTo(start.x, start.y)
            path.quadTo(control.x, control.y, end.x, end.y)

            canvas.drawPath(path, mPaint);
            canvas.drawLine( start.x, start.y, end.x, end.y, mPaint);
        }
    }


}