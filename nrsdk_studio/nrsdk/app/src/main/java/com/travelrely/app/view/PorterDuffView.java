package com.travelrely.app.view;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.travelrely.sdk.R;

/**
 * 自定义组件实现图片异步加载。<br/>
 */
public class PorterDuffView extends ImageView
{
    /** 下载进度条的颜色。 */
    public static final int TEXT_COLOR = 0xff000000;

    /** 进度百分比字体大小。 */
    public static final int FONT_SIZE = 30;

    private Paint paint;

    /** 标识当前进度。 */
    private float progress;

    /** 标识进度图片的宽度与高度。 */
    private int width, height;

    /** 格式化输出百分比。 */
    private DecimalFormat decFormat;

    /** 进度百分比文本的锚定Y中心坐标值。 */
    private float txtBaseY;

    /** 标识是否使用PorterDuff模式重组界面。 */
    public boolean porterduffMode;

    /** 标识是否正在下载图片。 */
    private boolean loading;

    public PorterDuffView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs)
    {
        if (attrs != null)
        {
            TypedArray typedArr = context.obtainStyledAttributes(attrs,
                    R.styleable.PorterDuffView);
            porterduffMode = typedArr.getBoolean(
                    R.styleable.PorterDuffView_porterduffMode, false);
        }
        if (!porterduffMode)
        {
            porterduffMode = false;
        }

        paint = new Paint();
        paint.setFilterBitmap(false);
        paint.setAntiAlias(true);
        paint.setTextSize(FONT_SIZE);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        // 在此处直接计算出来，避免了在onDraw()处的重复计算
        txtBaseY = (height - fontMetrics.bottom - fontMetrics.top) / 2;
        decFormat = new DecimalFormat("0.0%");
    }

    public void onDraw(Canvas canvas)
    {
        if (porterduffMode)
        {
            int tmpW = (getWidth() - width) / 2, tmpH = (getHeight() - height) / 2;
            // 设置PorterDuff模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN));
            // 立即取消xfermode
            paint.setXfermode(null);
            int oriColor = paint.getColor();
            paint.setColor(TEXT_COLOR);
            paint.setTextSize(FONT_SIZE);
            String tmp = decFormat.format(progress);
            float tmpWidth = paint.measureText(tmp);
            canvas.drawText(decFormat.format(progress), tmpW
                    + (width - tmpWidth) / 2, tmpH + txtBaseY, paint);
            // 恢复为初始值时的颜色
            paint.setColor(oriColor);
        }
        else
        {
            // LOGManager.i("onDraw super");
            super.onDraw(canvas);
        }
    }

    public void setProgress(float progress)
    {
        if (porterduffMode)
        {
            this.progress = progress;
            // 刷新自身。
            invalidate();
        }
    }

    public void setBitmap(Bitmap bg)
    {
        if (porterduffMode)
        {
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            txtBaseY = (height - fontMetrics.bottom - fontMetrics.top) / 2;

            setImageBitmap(bg);
        }
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public void setPorterDuffMode(boolean bool)
    {
        porterduffMode = bool;
    }
}
