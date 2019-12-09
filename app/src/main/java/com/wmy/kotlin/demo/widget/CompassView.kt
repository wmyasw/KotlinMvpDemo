package com.wmy.kotlin.demo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.wmy.kotlin.demo.utils.Utils
import okhttp3.internal.Util


/**
 * 指南针
 *@author：wmyasw
 */
class CompassView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var mRadius: Float = 0.0f // 圆形半径
    //指南针圆心点坐标
    private val mCenterX = 0
    private val mCenterY = 0
    //外圆半径
    private val mOutSideRadius = 0
    //外接圆半径
    private val mCircumRadius = 0

    var mPadding: Float = 0.0f  // 边距
    var mTextSize: Float = 0.0f  // 文字大小
    var mSecondPointWidth: Float = 0.0f  // 秒针宽度
    var mPointRadius: Float = 0.0f    // 指针圆角
    var mPointEndLength: Float = 0.0f // 指针末尾长度

    var pointColor: Int = 0  // 指针颜色
    var mColorLong: Int = 0    // 长线的颜色
    var mColorShort: Int = 0  // 短线的颜色

    var mPaint: Paint? = null // 画笔
    var mDrawFilter: PaintFlagsDrawFilter? = null // 为画布设置抗锯齿
    private var mWidth = 0
    private var mHeight = 0
    private var widthPixels: Int
    private var heightPixels: Int

    init {
        // 为画布实现抗锯齿
        mDrawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
//        //测量手机的宽度
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        heightPixels = context.getResources().getDisplayMetrics().heightPixels;
//        // 默认和屏幕的宽高最小值相等
        mWidth = Math.min(widthPixels, heightPixels);
        mPadding = Utils.dp2px(10)
        mHeight = mWidth
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = (Math.min(w, h) - mPadding) / 2
        //判断当前view 设置宽度大于屏幕宽度的话 使用屏幕宽度
        if (w > widthPixels)
            mRadius = (Math.min(widthPixels, h) - mPadding) / 2
        if (h > heightPixels)
            mRadius = (Math.min(w, heightPixels) - mPadding) / 2
        mPointEndLength = mRadius / 6; // 设置成半径的六分之一
    }

    var rotate :Float=0F
    open fun setValues(values: FloatArray)
    {
        rotate = values[0]
        postInvalidate()
    }

    fun measureSize(measureSpec: Int): Int {
        var size = MeasureSpec.getSize(measureSpec);
        mWidth = Math.min(measuredWidth, size)
        if (mWidth > widthPixels)
            mWidth = widthPixels
        if (mWidth > heightPixels)
            mWidth = heightPixels
        mHeight = mWidth
        return mWidth
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            canvas.rotate(rotate,(mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat())
            drawCircle(canvas)
            drawScale(canvas)
        }
    }

    // 绘制半径圆
    fun drawCircle(canvas: Canvas) {
        //保存原始状态
        canvas.save()
        mPaint!!.setColor(Color.WHITE);
        mPaint!!.setStyle(Paint.Style.FILL);
        canvas.drawCircle((mWidth / 2).toFloat(), (mWidth / 2).toFloat(), mRadius, mPaint!!);
        canvas.restore()

    }

    //绘制刻度
    fun drawScale(canvas: Canvas) {
        canvas.save()
        var lineWidth: Int
        canvas.drawText("北", (mWidth!! / 2).toFloat(), 0f, mPaint!!)
        mPaint!!.setStyle(Paint.Style.FILL);
        for (i in 0..12) {
            if (i % 3 == 0) {
                lineWidth = 40
                mPaint!!.setStrokeWidth(Utils.dp2px(2))
//                mPaint!!.setColor(mColorLong)
                mPaint!!.setColor(Color.RED)
                canvas.save()
                mPaint!!.setTextSize(Utils.sp2px(14));
//                canvas.rotate(-90F * i, (mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat());

                canvas.restore()
            } else {
                lineWidth = 30
//                mPaint!!.setColor(mColorShort)
                mPaint!!.setColor(Color.BLUE);
                mPaint!!.setStrokeWidth(Utils.dp2px(1))
            }
//            canvas.save()

            canvas.drawLine((mWidth!! / 2).toFloat(), 0f, (mWidth!! / 2).toFloat(), lineWidth.toFloat(), mPaint!!);
            canvas.rotate(90F, (mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat());
        }
        canvas.restore()

    }
}