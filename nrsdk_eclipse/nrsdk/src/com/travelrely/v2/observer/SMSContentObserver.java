package com.travelrely.v2.observer;

import com.travelrely.core.util.LOGManager;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

/** 
 * 
 * @author zhangyao
 * @version 2014年7月11日下午12:05:01
 */

//用来观察系统里短消息的数据库变化  ”表“内容观察者,只要信息数据库发生变化，都会触发该ContentObserver 派生类
public class SMSContentObserver extends ContentObserver {
  
  public static final int MSG_OUTBOXCONTENT = 2 ;
  
  private Context mContext  ;
  private Handler mHandler ;   //更新UI线程
  
  public SMSContentObserver(Context context,Handler handler) {
      super(handler);
      mContext = context ;
      mHandler = handler ;
  }
  /**
   * 当所监听的Uri发生改变时，就会回调此方法
   * 
   * @param selfChange  此值意义不大 一般情况下该回调值false
   */
  @Override
  public void onChange(boolean selfChange){
      LOGManager.d("the sms table has changed");
      
      //查询发件箱里的内容     
//      Uri outSMSUri = Uri.parse("content://sms/sent") ;
      
    //查询收件箱里的内容     
      Uri outSMSUri = Uri.parse("content://sms/inbox") ;
      
      Cursor c = mContext.getContentResolver().query(outSMSUri, null, null, null,"date desc");
      if(c != null){
          
          LOGManager.d("the number of send is"+c.getCount()) ;
          
          StringBuilder sb = new StringBuilder() ;
          //循环遍历
          while(c.moveToNext()){
              try {
                  String temp = c.getString(c.getColumnIndex("body"));
                  if(temp.contains("欢迎您使用旅信服务")){
                      sb.append(temp.substring(6, 12));
                      break;
                  }
            } catch (Exception e) {
                // TODO: handle exception
                LOGManager.e("解析短信错误");
            }
          }
          c.close(); 
          if(sb != null){
              mHandler.obtainMessage(MSG_OUTBOXCONTENT, sb.toString()).sendToTarget();
          }
      }
  }
  
}
