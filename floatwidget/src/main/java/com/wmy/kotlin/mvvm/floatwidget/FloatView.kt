package com.wmy.kotlin.mvvm.floatwidget


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi


/**
 * 这是一个悬浮窗
 *@author：wmyasw
 */
class FloatView : View {
    //窗口的配置参数
    private var mLayoutParams: WindowManager.LayoutParams? = null
    //获取屏幕参数
    private var mDisplayMetrics: DisplayMetrics? = null
    //窗口管理
    private var mWindowManager: WindowManager? = null
    val RIGHT_POSTION = 1
    val LEFT_POSTION = 2

    //起始点
    var startX = 0
    var startY = 0
    //是否在移动
    var isMoveing = false
    var startTime: Long = 0
    //最后通过动画将mView的X轴坐标移动到finalMoveX
    var finalMoveX = 0
    var widthPixels = 0
    private var offsetX = 0f
    private var offsetY = 0f
    //记录当前悬浮位置 目前只支持左右位置
    var DOCKING_POSITION = RIGHT_POSTION
//    private var start: PointF? = null
//    private var end: PointF? = null
//    private var control: PointF? = null

    var centerx = 0F
    var centery = 0F
    var wx = 0f
    var wy = 0f
    //定义原型画笔
    var paint: Paint? = null
    //圆柱需要的画笔
    var paintYz: Paint? = null
    var  paintB:Paint?=null
    var list: MutableList<String>? = null

    init {
//        start = PointF(0F, 0F)
//        end = PointF(0F, 0f)
//        control = PointF(0f, 0f)

        list = ArrayList()
          paintB = Paint()
        paintB!!.setColor(Color.GRAY)
        paintB!!.setStrokeWidth(1F)
        paintB!!.setAntiAlias(true)
        paintB!!.setStyle(Paint.Style.STROKE)
        paintB!!.alpha = 200


        paintYz = Paint()
        paintYz!!.setColor(Color.rgb(202,202,202))
        paintYz!!.setStrokeWidth(8F)
        paintYz!!.setStyle(Paint.Style.FILL)
        paintYz!!.alpha = 200
//        paintYz!!.setShadowLayer(10f, 10f, 10f, Color.GRAY)
        // 实例化画笔对象
        paint = Paint()
        // 给画笔设置颜色
        paint!!.setColor(Color.RED)
        paint!!.setAntiAlias(true)
        //设置画笔属性
        paint!!.setStyle(Paint.Style.FILL)//画笔属性是实心圆
        paint!!.setStrokeWidth(8F) //设置画笔粗细

    }

    /**
     * 无参构造方法
     *
     * @param context
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?) : this(context, null, 0)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : this(context, null, 0, 0)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, null, 0, 0) {
        init()
    }

    fun addItem(item: String) {
        list?.add(item)
    }

    private fun init() {

        initWindowManager()
        initLayoutParams()

    }

    /** 判断是否有长按动作发生
     * @param lastX 按下时X坐标
     * @param lastY 按下时Y坐标
     * @param thisX 移动时X坐标
     * @param thisY 移动时Y坐标
     * @param lastDownTime 按下时间
     * @param thisEventTime 移动时间
     * @param longPressTime 判断长按时间的阀值
     */
    fun isLongPressed(lastX: Int, lastY: Int,
                      thisX: Float, thisY: Float,
                      lastDownTime: Long, thisEventTime: Long,
                      longPressTime: Long): Boolean {
        var offsetX = Math.abs(thisX - lastX)
        var offsetY = Math.abs(thisY - lastY)
        var intervalTime = thisEventTime - lastDownTime
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true
        }
        return false
    }


    /**
     * 触摸事件
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x.toInt()
                startY = event.y.toInt()
                startTime = System.currentTimeMillis()
                isMoveing = false

                return false
            }
            MotionEvent.ACTION_MOVE -> {
                //当手指按住并移动时
                isMoveing = true
                mLayoutParams!!.x = (event.rawX - this!!.width / 2).toInt()
                mLayoutParams!!.y = (event.rawY - this!!.height).toInt()
                val curTime = System.currentTimeMillis()
               var isLongClick= isLongPressed(startX,startY,event.rawX,event.rawY,startTime,curTime,300)
                if (isLongClick && (mLayoutParams!!.x - 20 < 0 || mLayoutParams!!.x > widthPixels - width - 20)) {
                    performLongClick()
                    return false

                } else {
                    Log.e("ACTION_MOVE ", "  x  :${mLayoutParams!!.x}")
                    Log.e("ACTION_MOVE ", "  y  :${mLayoutParams!!.y}")

                    updateViewLayout() //更新mView 的位置

                    return true
                }

            }
            MotionEvent.ACTION_UP -> {
                //当手指离开时
                val curTime = System.currentTimeMillis()
//                isMoveing = curTime - startTime > 100

                if (!isMoveing && curTime - startTime < 500 && curTime - startTime > 100) {
                    callOnClick()
                    return false
                }
                //判断mView是在Window中的位置，以中间为界
                finalMoveX = if (mLayoutParams!!.x + this!!.measuredWidth / 2 >= mWindowManager!!.defaultDisplay.width / 2) {
                    mWindowManager!!.defaultDisplay.width - this!!.measuredWidth
                } else {
                    0
                }
                val animator = ValueAnimator.ofInt(mLayoutParams!!.x, finalMoveX).setDuration(Math.abs(mLayoutParams!!.x - finalMoveX).toLong())
                animator.addUpdateListener { animation: ValueAnimator ->
                    mLayoutParams!!.x = animation.animatedValue as Int
                    updateViewLayout()
                }
                animator.start()
                return isMoveing
            }
        }
        return false
    }


    //更新位置
    private fun updateViewLayout() {
        if (null != mLayoutParams) {

            if (mLayoutParams!!.x == 0) {
                Log.e("FloatManager", "显示在最左边 ${mLayoutParams!!.x}")
                DOCKING_POSITION = LEFT_POSTION
                isMoveing = false
            }
            if (mLayoutParams!!.x == widthPixels - width) {
                Log.e("FloatManager", "显示在最右边 ${mLayoutParams!!.x}")
                Log.e("FloatManager", "显示在最右边 ${widthPixels - width}")
                DOCKING_POSITION = RIGHT_POSTION
                isMoveing = false
            }

            invalidate()
            mWindowManager!!.updateViewLayout(this, mLayoutParams)
        }
    }

    /**
     * 初始化窗口管理器
     */
    fun initWindowManager() {
        val dm = context.getResources().getDisplayMetrics()
        widthPixels = dm.widthPixels
        mWindowManager = context.applicationContext
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mDisplayMetrics = DisplayMetrics()
        mWindowManager!!.defaultDisplay.getMetrics(mDisplayMetrics)

    }

    /**
     * 初始化WindowManager.LayoutParams参数
     */
    fun initLayoutParams() {
        if (mWindowManager == null) {
            mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
//        mWindowManager!!.removeView(this)
        mLayoutParams = WindowManager.LayoutParams()

        mLayoutParams!!.flags = (mLayoutParams!!.flags
                or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
        mLayoutParams!!.dimAmount = 0.2f
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mLayoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            mLayoutParams!!.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        mLayoutParams!!.height = 180
        mLayoutParams!!.width = 180//WindowManager.LayoutParams.WRAP_CONTENT
        mLayoutParams!!.gravity = Gravity.START or Gravity.TOP
        mLayoutParams!!.format = PixelFormat.RGBA_8888
        mLayoutParams!!.alpha = 1.0f //  设置整个窗口的透明度
        offsetX = 0F
        offsetY = getStatusBarHeight(context).toFloat()
        mLayoutParams!!.x = ((mDisplayMetrics!!.widthPixels - offsetX).toInt())
        mLayoutParams!!.y = ((mDisplayMetrics!!.heightPixels * 1.0f / 4 - offsetY).toInt())


        mWindowManager!!.addView(this, mLayoutParams)
    }

    /**
     * 获取状态栏的高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas)
    }


    /**
     * 绘制一个圆
     */
    @SuppressLint("NewApi")
    fun drawCircle(canvas: Canvas?) {

        if(isMoveing){
          var  paintYz = Paint()
            paintYz!!.setColor(Color.rgb(202,202,202))
            paintYz!!.setStrokeWidth(8F)
            paintYz!!.setStyle(Paint.Style.FILL)
            paintYz!!.alpha = 200
            paintYz!!.setShadowLayer(10f, 0f, 0f, Color.GRAY)
            canvas!!.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 70F, paintYz!!)
        }
        //判断停靠哪个位置
        if (DOCKING_POSITION == LEFT_POSTION) {
            leftCylinder(canvas)
        } else {
            rightCylinder(canvas)
        }
        canvas!!.save()
        if (list!!.size == 1) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 50F, paint!!)
        } else if (list!!.size == 2) {
            paint!!.setColor(Color.RED)
            canvas.drawCircle(centerx - 20, centery, 50F / 5 * 3, paint!!)
            paint!!.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
            paint!!.setColor(Color.BLUE)
            canvas.drawCircle(centerx + 20, centery, 50F / 5 * 3, paint!!)
        }else
          canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), 50F, paint!!)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerx = w / 2.toFloat()
        centery = h / 2.toFloat()
        wx = 20f
        wy = 20f
//        // 初始化数据点和控制点的位置
//        start!!.x = centerx
//        start!!.y = (centery + 120 / 2)
//        end!!.x = centerx
//        end?.y = centery - 120 / 2
//        control!!.x = centerx - 120
//        control!!.y = centery

    }

    /**
     * 左边圆柱的的样式
     */
    fun leftCylinder(canvas: Canvas?) {

        Log.e("FloatManager", "leftCylinder RectF  $wx    +  $wy")
        val oval = RectF(wx, wy,
                (width - wx), (height - wy))
        val jx = RectF(0f, wy-1,
                (width / 2).toFloat(), (height - wy))

        if (!isMoveing) {
            //绘制半圆
            canvas!!.drawArc(oval, -90F, 180F, true, paintYz!!)
            //绘制矩形
            canvas!!.drawRect(jx, paintYz!!)

        }
    }

    /**
     * 右侧的圆柱体
     */
    fun rightCylinder(canvas: Canvas?) {

        Log.e("FloatManager", "rightCylinder RectF  $wx    +  $wy")
        val oval = RectF(wx, wy,
                ((width - wx).toFloat()), (height - wy))
        val jx = RectF((width / 2).toFloat(), wy-1,
                ((width).toFloat()), (height - wy))

        if (!isMoveing) {
            //绘制半圆
            canvas!!.drawArc(oval, 90F, 180F, true, paintYz!!)
            //绘制矩形
            canvas!!.drawRect(jx, paintYz!!)

        }
    }

    fun doBlur(sentBitmap: Bitmap, radius: Int, canReuseInBitmap: Boolean): Bitmap? {
        val bitmap: Bitmap
        bitmap = if (canReuseInBitmap) {
            sentBitmap
        } else {
            sentBitmap.copy(sentBitmap.config, true)
        }
        if (radius < 1) {
            return null
        }
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) { IntArray(3) }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }


}