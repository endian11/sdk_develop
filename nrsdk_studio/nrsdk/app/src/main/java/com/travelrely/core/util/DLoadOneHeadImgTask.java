package com.travelrely.core.util;

import java.io.File;

import android.os.AsyncTask;

import com.travelrely.core.nrs.Engine;
import com.travelrely.model.ContactModel;
import com.travelrely.app.db.ContactDBHelper;

/**
 * 启动任务下载头像
 * 
 * @author seekting
 */
public class DLoadOneHeadImgTask extends AsyncTask<ContactModel, Void, Void>
{
    @Override
    protected Void doInBackground(ContactModel... params)
    {
        ContactModel contactModel = null;
        if (params == null || params.length == 0 || params[0] == null)
        {
            return null;
        }
        else
        {
            contactModel = params[0];
        }

        String headImgName = contactModel.getHeadPortrait();
        String headImgPath = contactModel.getLocalHeadImgPath();
        if (headImgName == null || headImgName.equals(""))
        {
            LOGManager.d(contactModel.getName() + "没有头像");
            return null;
        }

        if (headImgPath != null && headImgPath.contains(headImgName))
        {
            // 说明已经有图片不用下载
            File file = new File(headImgPath);
            LOGManager.d(contactModel.getName() + "已经有头像！");
            if (!file.exists())
            {
                LOGManager.d(contactModel.getName() + "本地头像无法找到！启用重新下载！");
                download(contactModel, headImgName);
            }
        }
        else
        {
            download(contactModel, headImgName);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);

        if (mDownloadListener != null)
        {
            mDownloadListener.onHeadImgDownload();
        }
        else
        {
            Engine.getInstance().syncContactThread();
        }
    }

    private void download(ContactModel contactModel, String head)
    {
        String localUrl = Engine.getInstance().downloadHeadImg(
                Engine.getInstance().getContext(), head, "_s");
        if (localUrl != null)
        {
            contactModel.setLocalHeadImgPath(localUrl);
            ContactDBHelper.getInstance().update(contactModel);
            LOGManager.d("下载一个头像成功!" + contactModel.getName());
        }
        else
        {
            contactModel.setLocalHeadImgPath(null);
            ContactDBHelper.getInstance().update(contactModel);
            LOGManager.e("下载一个头像失败!" + contactModel.getName());
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
