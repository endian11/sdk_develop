package com.travelrely.app.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author wangqijun
 * @version 2014年8月13日
 */

public class AutoScaleTextView extends TextView
{
    private static float MIN_SIZE = 15;
    private static float MAX_SIZE = 20;
    
    private Paint paint;
    private float minSize, maxSize;

    private float density = getResources().getDisplayMetrics().density;
    
    public AutoScaleTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init();
    }
    
    private void init()
    {
        paint = new Paint();
        paint.set(this.getPaint());
        
        maxSize = this.getTextSize();
        if (maxSize <= MIN_SIZE)
        {
            maxSize = MAX_SIZE;
        }
        
        minSize = MIN_SIZE;
    }
    
    private void refitText(String text, int textWidth)
    {
        if (textWidth <= 0)
        {
            return;
        }
        
        //LOGManager.i(text+"["+textWidth+"]");
        
        int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
        //LOGManager.i("availableWidth"+"["+availableWidth+"]");
        
        float trySize = maxSize;
        paint.setTextSize(trySize);
        float dp = paint.measureText(text + " ");
        float px = dp * density + 0.5f;
        //LOGManager.i("["+trySize+", "+px+"]");
        while(trySize > minSize && px >= availableWidth)
        {
            trySize -= 1;
            if (trySize <= minSize)
            {
                trySize = minSize;
                paint.setTextSize(trySize);
                break;
            }
            
            paint.setTextSize(trySize);
            dp = paint.measureText(text + " ");
            px = dp * density + 0.5f;
            //LOGManager.i("["+trySize+", "+px+"]");
        }
        
        this.setTextSize(trySize);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before,
            int after)
    {
        refitText(text.toString(), this.getWidth());
    }
}
