package com.travelrely.app.view;

import java.util.List;

import za.co.immedia.pinnedheaderlistview.PinnedHeaderListView;
import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.model.PhoneSection;
import com.travelrely.sdk.R;

public class ContactSectionSelectBar extends LinearLayout
{
    PinnedHeaderListView pinnedHeaderListView;

    public ContactSectionSelectBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    String[] nameBegins = { "#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        init();
    }

    private void init()
    {
        this.setOrientation(LinearLayout.VERTICAL);
        removeAllViews();

        for (String str : nameBegins)
        {
            TextView textView = new TextView(getContext());

            textView.setTextColor(getResources().getColor(R.color.sky_blue));
            textView.setText(str);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            params.weight = 1;
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            float size = getResources().getDimension(R.dimen.text_size_0);
            textView.getPaint().setTextSize(size);
            textView.getPaint().setFakeBoldText(true);
            textView.setGravity(Gravity.CENTER);
            addView(textView);
        }
    }

    List<PhoneSection> srcList;

    public void init(List<PhoneSection> travelrelyList,
            PinnedHeaderListView pinnedHeaderListView)
    {

        setList(travelrelyList);
        setListView(pinnedHeaderListView);
    }

    public void setList(List<PhoneSection> travelrelyList)
    {
        srcList = travelrelyList;
    }

    public void setListView(PinnedHeaderListView pinnedHeaderListView)
    {
        this.pinnedHeaderListView = pinnedHeaderListView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        //float x = event.getX();
        float y = event.getY();
        StateListDrawable stateListDrawable = (StateListDrawable) getBackground();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                int pressed = android.R.attr.state_pressed;

                stateListDrawable.setState(new int[] { pressed });

                // int position = getSelectedPosition(v, y);
                //
                // // LogManager.d("position" + position);
                //
                // listView.setSelection(position);
                break;

            case MotionEvent.ACTION_MOVE:
            {
                int position1 = getSectionPosition(this, y);
                int selectionPosiion = getSelectedPosition(position1);
                if (onSelectListener != null)
                {
                    onSelectListener
                    .onSelectPosition(srcList, selectionPosiion);
                }

                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            {
                int enabled = android.R.attr.state_enabled;
                stateListDrawable.setState(new int[] { enabled });

                break;
            }

            default:
                break;
        }
        return true;
    }

    int getSelectedPosition(int sectionPosition)
    {
        int j = 0;
        if (srcList == null){
    		return 0;
    	}
        if (sectionPosition >= srcList.size())
        {
            sectionPosition = srcList.size() - 1;

        }

        for (int i = 0; i < sectionPosition; i++)
        {

            PhoneSection phoneSection = srcList.get(i);
            int size = phoneSection.getList().size();
            j = j + size;

        }

        return j + sectionPosition;
    }

    int getSectionPosition(View v, float y)
    {
        String str = getSelectedStr(v, y);
        return getSelectedPositionByStr(str);
    }

    String getSelectedStr(View v, float y)
    {
        ViewGroup viewGroup = (ViewGroup) v;

        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            TextView child = (TextView) viewGroup.getChildAt(i);
            int top = child.getBottom();
            if (top > y)
            {

                if (child.getText().toString().equals("#"))
                {
                    return " ";
                }
                else
                {
                    return child.getText().toString();
                }
            }
        }

        return "Z";
    }

    int getSelectedPositionByStr(String str)
    {
    	if (srcList == null){
    		return 0;
    	}
        for (int i = 0; i < srcList.size(); i++)
        {
            PhoneSection phoneSection = srcList.get(i);
            if (phoneSection.getBegin() >= str.charAt(0))
            {

                if (onSelectListener != null)
                {
                    onSelectListener.onSelectSection(srcList, str);
                }
                return i;
            }
        }

        return srcList.size() - 1;
    }

    OnSelectListener onSelectListener;

    public OnSelectListener getOnSelectListener()
    {
        return onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener)
    {
        this.onSelectListener = onSelectListener;
    }

    public interface OnSelectListener
    {
        public void onSelectPosition(List<PhoneSection> list, int position);

        public void onSelectSection(List<PhoneSection> list, String str);

    }
}
