package com.travelrely.core.glms;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.travelrely.app.activity.BaseActivity;
import com.travelrely.app.view.CustomProgressDialog;

/**
 * @author seekting.x.zhang
 */
public class ProgressOverlay
{
    private final Context context;

    // private Dialog dialog = null;

    private CustomProgressDialog dialog = null;

    public interface OnProgressEvent
    {
        void onProgress();
    }

    public ProgressOverlay(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    /**
     * @param title
     * @param event
     */
    public void show(String title, final OnProgressEvent event)
    {
        if (title == null || title.equals(""))
        {
            title = "waiting";
        }

        try
        {
            if (dialog == null)
            {
                dialog = CustomProgressDialog.createDialog(context);
            }

            dialog.setCancelable(false);
            dialog.show();
        }
        catch (Exception e)
        {
        	
        }
        	
        	BackgrounThread thread = new BackgrounThread(dialog, event);
        	thread.start();

    }

    public static class BackgrounThread extends Thread
    {
        private CustomProgressDialog mdialog;

        OnProgressEvent event;

        public BackgrounThread(CustomProgressDialog mdialog,
                OnProgressEvent onProgressEvent)
        {
            this.mdialog = mdialog;
            this.event = onProgressEvent;
        }

        @Override
        public void run()
        {
            try
            {
                event.onProgress();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                if (mdialog.getContext() instanceof Activity)
                {
                    Activity activity = (Activity) mdialog
                            .getContext();
                    // baseActivity.showShortToast(e.getClass().toString());
                    showShortToast(activity, e.getClass().toString());
                }
            }
            finally
            {
                if (mdialog != null)
                {
                    mdialog.dismiss();
                    mdialog = null;
                }
            }
        }
    }
    
    public static void showShortToast(final Activity activity, final String pMsg)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(activity, pMsg, Toast.LENGTH_SHORT)
                .show();
            }
        });
    }
}
