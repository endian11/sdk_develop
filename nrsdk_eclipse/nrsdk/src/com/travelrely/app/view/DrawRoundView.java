package com.travelrely.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class DrawRoundView extends View
{
    private Context mContext;
    
    private int circleNum = 0;

    private int[] circleColor = null;

    public DrawRoundView(Context context)
    {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int cx = getWidth() / 2;
        int cy = getWidth() *25 / 64;
        
        // 内圆半径
        int[] innerRadius = {getWidth()*3/32, getWidth()*5/32,
                getWidth()*31/128, getWidth()*203/640};
        
        // 圆环半径
        int[] ringRadius = {getWidth()*1/32, getWidth()*11/256,
                getWidth()*3/80, getWidth()*47/1280};

        Paint paint = new Paint();
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Style.STROKE);

        int[] color = {Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE};
        int[] alpha = {255/4, 255*3/20, 255*12/100, 255*8/100};
        
        if (circleColor != null)
        {
            // 绘制外圆
            for (int j = 0; j < circleColor.length; j++)
            {
                paint.setColor(circleColor[j]);
                paint.setStrokeWidth(ringRadius[j]*2);
                paint.setAlpha(alpha[j]);
                canvas.drawCircle(cx, cy, innerRadius[j] + ringRadius[j],
                        paint);
            }
            
            return;
        }
        
        if (circleNum > 0)
        {
            // 绘制外圆
            for (int j = 0; j < circleNum; j++)
            {
                paint.setColor(color[j]);
                paint.setStrokeWidth(ringRadius[j]*2);
                paint.setAlpha(alpha[j]);
                canvas.drawCircle(cx, cy, innerRadius[j] + ringRadius[j],
                        paint);
            }
            
            return;
        }
    }

    /* 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
    public static int dip2px(Context context, float dpValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    public void setCircleColor(int circleColor[])
    {
        if (circleColor == null)
        {
            return;
        }
        this.circleColor = new int[circleColor.length];
        System.arraycopy(circleColor, 0, this.circleColor, 0, circleColor.length);
    }
    
    public void setCircleNum(int num)
    {
        this.circleNum = num;
    }
}
