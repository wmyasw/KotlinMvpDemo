package com.wmy.kotlin.demo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wmy.kotlin.demo.utils.LogUtils
import java.util.*


/**
 *
 *@author：wmyasw
 */
class ScatterFlowersView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var mPaint: Paint
    var centerX: Int = 0
    var centerY: Int = 0

    var start: PointF
    var end: PointF
    lateinit var temp: RectF
    var vx: Float = 0F
    var vy: Float = 0F
    var growth: Float = 0F
    private var mWidth = 0

    init {
        mPaint = Paint()
        this.vx = (Math.random() * 6 - 3).toFloat()
        this.vy = (Math.random() * 4 - 2).toFloat()
        this.growth = ((Math.abs(this.vx) + Math.abs(this.vy)) * 0.007).toFloat()
//        mPaint.setStrokeWidth(8F);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setTextSize(60F);

        start = PointF(0F, 0F);
        end = PointF(0F, 0F);
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    fun measureSize(measureSpec: Int): Int {
        var size = MeasureSpec.getSize(measureSpec);
        mWidth = Math.min(measuredWidth, size)
        return mWidth
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2;
        centerY = h / 2;


        // 初始化数据点和控制点的位置
    }

    fun getRandomColor(): String {
        val random = Random()
        var r = Integer.toHexString(random.nextInt(256)).toUpperCase()
        var g = Integer.toHexString(random.nextInt(256)).toUpperCase()
        var b = Integer.toHexString(random.nextInt(256)).toUpperCase()

        r = if (r.length === 1) "0$r" else r
        g = if (g.length === 1) "0$g" else g
        b = if (b.length === 1) "0$b" else b
        var color="#" + r + g + b
        LogUtils.e(color)
        return color
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        control.x = event!!.getX()
//        control.y = event.getY()
//        invalidate()
//        return true
//    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            // 绘制数据点和控制点
            for (i in 0..100) {
                mPaint.setColor(Color.parseColor(getRandomColor()))
//                mPaint.setStrokeWidth(20F)
                this.vx = (Math.random() * width - 3).toFloat()
                this.vy = (Math.random() *height - 2).toFloat()
                mPaint.setStrokeWidth(2F)
                mPaint.setStyle(Paint.Style.FILL)
                var path = Path()
                path.fillType = Path.FillType.EVEN_ODD
                temp= RectF()
                temp.left=vx
                temp.top=vy
                temp.right= (vx+(Math.random() * 30 )).toFloat()
                temp.bottom= (vy+(Math.random() * 30 )).toFloat()
                canvas.drawRect(temp, mPaint)
//                path.moveTo(start.x, start.y)
                val path1 = Path()
                path1.moveTo(vx, vy)
                for (i in 1..4) {

                    path1.lineTo(((vx + (Math.random() *5*i)).toFloat()), ((vy + (Math.random() *5*i)).toFloat()))
                }
                path1.close() //封闭

                canvas.drawPath(path1, mPaint)
//            path.quadTo(control.x, control.y, end.x, end.y)
//                path.addRect(temp,Path.Direction.CCW);

//                canvas.drawPath(path, mPaint);
//            canvas.drawLine(start.x, start.y, end.x, end.y, mPaint);
            }
        }
    }


}