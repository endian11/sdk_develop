package com.travelrely.core.util;

import java.util.List;

import android.os.AsyncTask;

import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.Res;
import com.travelrely.model.ContactModel;
import com.travelrely.app.db.ContactDBHelper;

public class FetchTokensTask extends
        AsyncTask<List<ContactModel>, String, String>
{
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
    protected String doInBackground(List<ContactModel>... params)
    {
        ContactDBHelper helper = ContactDBHelper.getInstance();
        
        if (params == null || params.length == 0 || params[0] == null)
        {
            List<ContactModel> list = helper.getAllContacts();
            boolean rslt = Utils.fetch_token(list);
            if (rslt)
            {
                SpUtil.setContactStatus(Res.CONTACT_SYNC);
            }

            return null;
        }
        else
        {
            boolean rslt = Utils.fetch_token(params[0]);
            if (rslt)
            {
                SpUtil.setContactStatus(Res.CONTACT_SYNC);
            }

            return null;
        }
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
            new DLoadAllHeadImgTask().execute();
            Engine.getInstance().syncContactThread();
        }
    }

    public static interface OnFetchTokenListener
    {
        public void onSucess();

        public void onFail();
    }
}
