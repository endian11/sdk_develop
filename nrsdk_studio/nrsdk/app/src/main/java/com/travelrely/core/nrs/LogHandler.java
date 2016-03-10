package com.travelrely.core.nrs;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.travelrely.core.util.FileManager;
import com.travelrely.core.util.NetUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;

public class LogHandler extends Handler
{
    //private final static String TAG = LogHandler.class.getSimpleName();

    private Context mContext;
    private FileManager fm;
    private File f;

    
    public LogHandler(Looper looper, Context c)
    {
        super(looper);
        mContext = c;
        fm = new FileManager(mContext);
    }

    public synchronized void procMsg(int iMsgId, String msgContent)
    {
        Message msg = obtainMessage();
        msg.what = iMsgId;
        msg.obj = msgContent;
        sendMessage(msg);
    }

    @Override
    public void handleMessage(Message msg)
    {
        switch (msg.what)
        {
            case 1:
                String user = Engine.getInstance().getUserName();
                //以日期来做文件名
                Date date = new Date();
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(date);
                fm.saveLog(dateStr, (String)msg.obj);
                
                //如果用户当前log前两天的log没上传 ，就把前两天之前的前两天的log删除掉
                String[] str1 = new String[2];
                
                for (int i=0;i<str1.length;i++){
              	  try {
						str1[i] = Utils.getStatetime(i-3);
						 f = fm.getLog(str1[i]);
			                if (f == null){
			                	continue ;
			                }else{
			                	f.delete();
			                }
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                
                break;

            case 2:
                  
                  //上传前两天的log
                  
                  String[] str = new String[2];
                  
                  for (int i=0;i<str.length;i++){
                	  try {
						str[i] = Utils.getStatetime(i-1);
						 f = fm.getLog(str[i]);
			                if (f == null){
			                	continue ;
			                }else{
					                    	
										            boolean uploadLog = Engine.getInstance().uploadLog(f) ;
							                    	if (uploadLog){
							                    		f.delete();
							                    	}
							                    	Thread.currentThread().sleep(2000);//延时2s 否则服务器会覆盖前一个文件
											
			                }
			               
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
                  }
                  
                  
            	
            	
            	
                break;
                
            default:
                break;
        }
    }
}
