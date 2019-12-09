package com.wmy.kotlin.demo.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import com.wmy.kotlin.demo.R
import com.wmy.kotlin.demo.utils.LogUtils
import com.wmy.kotlin.demo.utils.Utils
import java.util.*


/**
 *
 *@author：wmyasw
 */
class WatchBoard @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var mRadius: Float = 0.0f // 圆形半径
    var mPadding: Float = 0.0f  // 边距
    var mTextSize: Float = 0.0f  // 文字大小
    var mHourPointWidth: Float = 0.0f // 时针宽度
    var mMinutePointWidth: Float = 0.0f // 分针宽度
    var mSecondPointWidth: Float = 0.0f  // 秒针宽度
    var mPointRadius: Float = 0.0f    // 指针圆角
    var mPointEndLength: Float = 0.0f // 指针末尾长度

    var mHourPointColor: Int = 0  // 时针的颜色
    var mMinutePointColor: Int = 0  // 分针的颜色
    var mSecondPointColor: Int = 0   // 秒针的颜色
    var mColorLong: Int = 0    // 长线的颜色
    var mColorShort: Int = 0  // 短线的颜色

    var mPaint: Paint? = null // 画笔
    var mDrawFilter: PaintFlagsDrawFilter? = null // 为画布设置抗锯齿
    private var mWidth = 0
    private var mHeight = 0
    private var widthPixels: Int
    private var heightPixels: Int

    //绘制各部件时用的Paint
    private var mPaintScaleLong: Paint? = null
    private var mPaintScaleShort: Paint? = null
    private var mPaintOutline: Paint? = null
    private var mPaintNum: Paint? = null
    private var mPaintTickHour: Paint? = null
    private var mPaintTickMinute: Paint? = null
    private var mPaintTickSecond: Paint? = null


    init {
        // 为画布实现抗锯齿
        mDrawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
//        //测量手机的宽度
        widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        heightPixels = context.getResources().getDisplayMetrics().heightPixels;
//        // 默认和屏幕的宽高最小值相等
        mWidth = Math.min(widthPixels, heightPixels);
        mHeight = mWidth
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.isDither = true

        if (attrs != null) {
            obtainStyledAttrs(attrs)
        }
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


    fun obtainStyledAttrs(attrs: AttributeSet) {
        var typedArray: TypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WatchBoard);
        mPadding = typedArray.getDimension(R.styleable.WatchBoard_wb_padding, Utils.dp2px(10));
        mTextSize = typedArray.getDimension(R.styleable.WatchBoard_wb_text_size, Utils.sp2px(16));
        mHourPointWidth = typedArray.getDimension(R.styleable.WatchBoard_wb_hour_pointer_width, Utils.dp2px(5));
        mMinutePointWidth = typedArray.getDimension(R.styleable.WatchBoard_wb_minute_pointer_width, Utils.dp2px(3));
        mSecondPointWidth = typedArray.getDimension(R.styleable.WatchBoard_wb_second_pointer_width, Utils.dp2px(2));
        mPointRadius = typedArray.getDimension(R.styleable.WatchBoard_wb_pointer_corner_radius, Utils.dp2px(10));
        mPointEndLength = typedArray.getDimension(R.styleable.WatchBoard_wb_pointer_end_length, Utils.dp2px(10));

        mHourPointColor = typedArray.getColor(R.styleable.WatchBoard_wb_hour_pointer_color, Color.BLACK);
        mMinutePointColor = typedArray.getColor(R.styleable.WatchBoard_wb_minute_pointer_color, Color.BLACK);
        mSecondPointColor = typedArray.getColor(R.styleable.WatchBoard_wb_second_pointer_color, Color.RED);
        mColorLong = typedArray.getColor(R.styleable.WatchBoard_wb_scale_long_color, Color.argb(225, 0, 0, 0));
        mColorShort = typedArray.getColor(R.styleable.WatchBoard_wb_scale_short_color, Color.argb(125, 0, 0, 0));

        // 一定要回收
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 为画布设置抗锯齿
        canvas.setDrawFilter(mDrawFilter);
//         绘制半径圆
        drawCircle(canvas)
        drawScale(canvas)
        drawText(canvas)
        drawPointer(canvas)
        postInvalidateDelayed(1000)
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

    fun drawText(canvas: Canvas) {
        canvas.save()
        var lineWidth: Int = 40

        for (i in 0..60) {
            if (i % 5 == 0) {
                mPaint!!.setStrokeWidth(Utils.dp2px(2))
                mPaint!!.setColor(mColorLong);

                // 这里是字体的绘制
                mPaint!!.setTextSize(mTextSize);
                val text = (if (i / 5 === 0) 12 else i / 5).toString() + ""
                var textBound = Rect()
                mPaint!!.setColor(Color.BLACK);
                mPaint!!.getTextBounds(text, 0, text.length, textBound)
                val textHeight: Int = textBound.bottom - textBound.top
                canvas.save()
                //这个时候我们使用Canvas的Save方法保存了刚才的状态，这个时候我们不移动X轴，只移动Y轴，将坐标向上移动-mRidus + textHeight距离，因为对于手机来说，向上为Y的负轴，所以为-mRidus + textHeight，下面我一会用图来说明
                canvas.translate(0f, -mRadius + textHeight + lineWidth + mPadding / 2)
                //由于我们没旋转一次，会使用Canvas的rotate方法顺时针旋转6度，所以文字也会跟着旋转，但是我们要让文字不旋转的话，就需要逆时针旋转相应的度数
                canvas.rotate(-(6 * i).toFloat())
                canvas.drawText(text,
                        (mWidth - mPadding) / 2.toFloat()-Utils.dp2px(2),
                        (mWidth + mPadding) / 2.toFloat(), mPaint!!)
                canvas.restore()
            }
            //旋转6度
            canvas.rotate(6F)
        }
        canvas.restore()
    }


    //绘制表盘
    fun drawScale(canvas: Canvas) {
        canvas.save()
        mPaint?.setStrokeWidth(Utils.dp2px(1));
        var lineWidth: Int

        for (i in 0..60) {
            if (i % 5 == 0) {
                lineWidth = 40;
                mPaint!!.setStrokeWidth(Utils.dp2px(2))
                mPaint!!.setColor(mColorLong);

            } else {
                lineWidth = 30;
                mPaint!!.setColor(mColorShort);
                mPaint!!.setStrokeWidth(Utils.dp2px(1))
            }
//            canvas.save()
            canvas.drawLine((mWidth!! / 2).toFloat(), mPadding, (mWidth!! / 2).toFloat(), mPadding + lineWidth, mPaint!!);
            canvas.rotate(6F, (mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat());
        }
        canvas.restore()

    }


    fun drawPointer(canvas: Canvas) {
        var calendar = Calendar.getInstance()

        var hour = calendar.get(Calendar.HOUR);// 时
        var minute = calendar.get(Calendar.MINUTE);// 分
        var second = calendar.get(Calendar.SECOND);// 秒
        // 转过的角度 //设置时针每5分钟转动一下角度
        var angleHour: Float = (hour * 360 + minute / 5 * 30) / 12.toFloat()
        var angleMinute = (minute + (second / 60)) * 360 / 60
        var angleSecond: Float = (second * 360 / 60).toFloat()

        LogUtils.e("时针旋转角度：$angleHour" + "分针旋转角度：$angleMinute")
        // 绘制时针
        canvas.save()
        canvas.rotate(angleHour.toFloat(), (mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat()) // 旋转到时针的角度
        var rectHour = RectF(mWidth!! / 2 - mHourPointWidth / 2, mWidth!! / 2 - mRadius * 3 / 5, mWidth!! / 2 + mHourPointWidth / 2, mWidth!! / 2 + mPointEndLength);
        mPaint!!.setColor(mHourPointColor);
        mPaint!!.setStyle(Paint.Style.STROKE);
        mPaint!!.setStrokeWidth(mHourPointWidth);
        canvas.drawRoundRect(rectHour, mPointRadius, mPointRadius, mPaint!!);
        canvas.restore();
        // 绘制分针
        canvas.save()
        canvas.rotate(angleMinute.toFloat(), (mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat()); // 旋转到分针的角度
        var rectMinute = RectF(mWidth!! / 2 - mMinutePointWidth / 2, mWidth!! / 2 - mRadius * 3.5f / 5, mWidth!! / 2 + mMinutePointWidth / 2, width!! / 2 + mPointEndLength);
        mPaint!!.setColor(mMinutePointColor);
        mPaint!!.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectMinute, mPointRadius, mPointRadius, mPaint!!);
        canvas.restore()
        // 绘制分针
        canvas.save()
        canvas.rotate(angleSecond, (mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat()); // 旋转到分针的角度
        var rectSecond = RectF(mWidth!! / 2 - mSecondPointWidth / 2, mWidth!! / 2 - mRadius + Utils.dp2px(10), width!! / 2 + mSecondPointWidth / 2, width!! / 2 + mPointEndLength)
        mPaint!!.setStrokeWidth(mSecondPointWidth)
        mPaint!!.setColor(mSecondPointColor)
        canvas.drawRoundRect(rectSecond, mPointRadius, mPointRadius, mPaint!!)
        canvas.restore();

        // 绘制原点
        mPaint!!.setStyle(Paint.Style.FILL);
        canvas.drawCircle((mWidth!! / 2).toFloat(), (mWidth!! / 2).toFloat(), mSecondPointWidth * 4, mPaint!!);
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
}