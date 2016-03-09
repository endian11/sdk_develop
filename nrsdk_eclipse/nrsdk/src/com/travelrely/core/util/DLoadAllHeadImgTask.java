package com.travelrely.core.util;

import java.util.List;

import android.os.AsyncTask;

import com.travelrely.core.nrs.Engine;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.v2.db.ContactDBHelper;

/**
 * 启动任务下载头像
 * 
 * @author seekting
 */
public class DLoadAllHeadImgTask extends AsyncTask<Void, Void, Void>
{
    @Override
    protected Void doInBackground(Void... params)
    {
        List<TagNumber> tags = null;
        
        // 去数据库里找旅信用户
        tags = ContactDBHelper.getInstance().getTravelrelyTagNum();

        ImgDloadUtil.dloadAllHead(tags);

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        
        Engine.getInstance().syncContactThread();

        if (mDownloadListener != null)
        {
            mDownloadListener.onHeadImgDownload();
        }
    }

    OnImgDownloadListener mDownloadListener;

    public OnImgDownloadListener getmDownloadListener()
    {
        return mDownloadListener;
    }

    public void setmDownloadListener(OnImgDownloadListener mDownloadListener)
    {
        this.mDownloadListener = mDownloadListener;
    }

    public static interface OnImgDownloadListener
    {
        public void onHeadImgDownload();
    }
}
