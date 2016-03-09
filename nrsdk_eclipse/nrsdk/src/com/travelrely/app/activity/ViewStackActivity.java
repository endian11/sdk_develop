package com.travelrely.app.activity;

import java.util.AbstractMap;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public abstract class ViewStackActivity extends Activity
{
    AbstractMap<Integer, View> views = new HashMap<Integer, View>();

    /**
     * @see android.app.Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //this.loadViews(this.getViewIds());
    }

    void loadViews(int[] ids)
    {
        LayoutInflater flater = this.getLayoutInflater();
        for (int i = 0; i < ids.length; ++i)
        {
            View view = flater.inflate(ids[i], null);
            this.OnViewCreated(ids[i], view);
            views.put(new Integer(ids[i]), view);
        }
    }

    public abstract int[] getViewIds();

    public abstract void OnViewCreated(int id, View view);

    View loadView(int id)
    {
        View view = this.getLayoutInflater().inflate(id, null);
        this.OnViewCreated(id, view);
        views.put(new Integer(id), view);
        
        return view;
    }

    public void setActiveView(int id)
    {
        View view = views.get(new Integer(id));
        if (view == null)
        {
            view = loadView(id);
        }
        this.setContentView(view);
    }
}
