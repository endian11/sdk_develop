package com.travelrely.core.util;

import android.os.AsyncTask;

import com.travelrely.core.nrs.Engine;
import com.travelrely.model.ContactModel;

public class FetchTokenOneTask extends AsyncTask<ContactModel, String, String>
{
    ContactModel contact;
    OnFetchTokenListener fetchTokenListener;

    public OnFetchTokenListener getFetchTokenListener()
    {
        return fetchTokenListener;
    }

    public void setFetchTokenListener(OnFetchTokenListener fetchTokenListener)
    {
        this.fetchTokenListener = fetchTokenListener;
    }

    @Override
    protected String doInBackground(ContactModel... params)
    {
        contact = params[0];
        Utils.getFetchToken(contact);
        return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        if (fetchTokenListener != null)
        {
            fetchTokenListener.onSucess();
        }
        else
        {
            new DLoadOneHeadImgTask().execute(contact);
            Engine.getInstance().syncContactThread();
        }
    }

    public static interface OnFetchTokenListener
    {
        public void onSucess();

        public void onFail();
    }
}
