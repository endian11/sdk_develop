package com.travelrely.v2.observer;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;

/**
  * @ClassName: ContactObserver
  * @Description: 监听系统通讯录的变化 的内容观察者
  * @author Comsys-john
  * @date 2015-3-27 下午5:56:51
  *
  */
public class ContactObserver extends ContentObserver {

	private Handler mHandler;
	public ContactObserver(Handler handler) {
		super(handler);
		this.mHandler = handler;
	}

	/****************系统通讯录有变化会调用此方法********************/
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Message msg = mHandler.obtainMessage();
		msg.what = 1;
		mHandler.sendMessage(msg);
	}
}
