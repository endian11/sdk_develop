package com.travelrely.app.view;

import java.io.File;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.travelrely.app.activity.ChatMsgListAct;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.app.view.SysListAlert.OnListAlertClickListener;
import com.travelrely.core.Constant;
import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.sdk.Api;
import com.travelrely.sdk.R;
import com.travelrely.v2.util.FileUtil;
import com.travelrely.v2.util.NetUtil;
import com.travelrely.v2.util.Utils;

@SuppressWarnings("deprecation")
public class CallLayout extends LinearLayout implements OnClickListener,
		OnLongClickListener, OnListAlertClickListener
{
	/**
	 * 显示隐藏初始数据
	 */
	public static final String initValueSwitch = "#*#*12321*#*#";

	public static final String voiceSwitch = "#*#*112233*#*#";

	public static final String DELETE_HEAD_IMAGE_CACHEIMGDIR = "#*#*333222111*#*#";

	public static final String INSERT_CONTACTS = "#*#*+++*#*#";

	CallNumberItem num_1, num_2, num_3, num_4, num_5, num_6, num_7, num_8,
			num_9, num_0, num_jing, num_xing;

	TextView call_num;

	String currentNum = "";

	View delete, call, new_person;
	
	private SysAlertDialog mAppDialog;
	
	public CallLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/***************************定义一个借口 添加电话到已有联系人*****************************************/
	private  OnAddHaveContactListener mOnL;
	  public interface OnAddHaveContactListener
	    {
	        public void OnAddHaveContactk(String callNum);
	    }
    
	public OnAddHaveContactListener getmOnL() {
		return mOnL;
	}

	public void setmOnL(final OnAddHaveContactListener mOnL) {
		this.mOnL = mOnL;
	}

	/********************************************************************/
	@Override
	protected void onFinishInflate()
	{
		super.onFinishInflate();
		num_1 = (CallNumberItem) findViewById(R.id.num_1);
		num_2 = (CallNumberItem) findViewById(R.id.num_2);
		num_3 = (CallNumberItem) findViewById(R.id.num_3);
		num_4 = (CallNumberItem) findViewById(R.id.num_4);
		num_5 = (CallNumberItem) findViewById(R.id.num_5);
		num_6 = (CallNumberItem) findViewById(R.id.num_6);
		num_7 = (CallNumberItem) findViewById(R.id.num_7);
		num_8 = (CallNumberItem) findViewById(R.id.num_8);
		num_9 = (CallNumberItem) findViewById(R.id.num_9);
		num_0 = (CallNumberItem) findViewById(R.id.num_0);
		num_xing = (CallNumberItem) findViewById(R.id.num_xing);
		num_jing = (CallNumberItem) findViewById(R.id.num_jing);

		call_num = (TextView) findViewById(R.id.call_num);
		call_num.setText("");

		delete = findViewById(R.id.delete);
		call = findViewById(R.id.call);
		new_person = findViewById(R.id.new_person);

		init();
	}

	private void init()
	{
		num_1.setNum("1");
		num_2.setNum("2");
		num_3.setNum("3");
		num_4.setNum("4");
		num_5.setNum("5");
		num_6.setNum("6");
		num_7.setNum("7");
		num_8.setNum("8");
		num_9.setNum("9");
		num_0.setNum("0");
		num_xing.setNum("*");
		num_jing.setNum("#");

		num_1.setZiMu("");
		num_2.setZiMu("ABC");
		num_3.setZiMu("DEF");
		num_4.setZiMu("GHI");
		num_5.setZiMu("JKL");
		num_6.setZiMu("MNO");
		num_7.setZiMu("PQRS");
		num_8.setZiMu("TUV");
		num_9.setZiMu("WXYZ");
		num_0.setZiMu("+");

		num_xing.setZiMu("");
		num_jing.setZiMu("");

		num_1.setOnClickListener(this);
		num_0.setOnLongClickListener(this);
		num_2.setOnClickListener(this);
		num_3.setOnClickListener(this);
		num_4.setOnClickListener(this);
		num_5.setOnClickListener(this);
		num_6.setOnClickListener(this);
		num_7.setOnClickListener(this);
		num_8.setOnClickListener(this);
		num_9.setOnClickListener(this);
		num_0.setOnClickListener(this);
		num_xing.setOnClickListener(this);
		num_jing.setOnClickListener(this);

		delete.setOnClickListener(this);
		call.setOnClickListener(this);
		new_person.setOnClickListener(this);
		delete.setOnLongClickListener(this);
		num_xing.setOnLongClickListener(this);
		num_jing.setOnLongClickListener(this);
	}

	private void addChar(String num)
	{
		currentNum = currentNum + num;
		call_num.setText(currentNum);
		if (initValueSwitch.equals(currentNum))
		{
			ReleaseConfig.initValue = !ReleaseConfig.initValue;
			if (ReleaseConfig.initValue)
			{
				Utils.showToast((Activity) getContext(), getResources()
						.getString(R.string.showInit));

			}
			else
			{
				Utils.showToast((Activity) getContext(), getResources()
						.getString(R.string.hideInit));

			}
			currentNum = "";
			call_num.setText(currentNum);
		}

		if (voiceSwitch.equals(currentNum))
		{
			Constant.showRecord = !Constant.showRecord;
			if (Constant.showRecord)
			{
				Utils.showToast((Activity) getContext(), "语音模式");
			}
			else
			{
				Utils.showToast((Activity) getContext(), "非语音模式");

			}
			currentNum = "";
			call_num.setText(currentNum);

		}

		if (DELETE_HEAD_IMAGE_CACHEIMGDIR.equals(currentNum))
		{
			String path = FileUtil.getImagePath("head_img");
			File file = new File(path);
			if (file.exists())
			{
				File[] childs = file.listFiles();

				int count = 0;
				for (int i = 0; i < childs.length; i++)
				{
					File child = childs[i];
					if (child.delete())
					{
						count++;
					}

				}
				Utils.showToast((Activity) getContext(), " 要删除的图片："
						+ childs.length + "删除了" + count + "张");

			}
			else
			{
				Utils.showToast((Activity) getContext(), "头像缓存文件夹不存在！");

			}
			currentNum = "";
			call_num.setText(currentNum);
		}
		else if (INSERT_CONTACTS.equals(currentNum))
		{
			addContacts();
			Utils.showToast((Activity) getContext(), "添加1000条");

			currentNum = "";
			call_num.setText(currentNum);
		}
	}

	/**
	 * 批量插入
	 * 
	 * @throws Throwable
	 */
	private void addContacts()
	{
		for (int i = 0; i < 1000; i++)
		{
			insertContact(getContext(), "张三" + i, "1369919" + i);
		}
	}

	private Uri insertContact(Context context, String name, String phone)
	{
		ContentValues values = new ContentValues();
		values.put(People.NAME, name);
		Uri uri = getContext().getContentResolver().insert(People.CONTENT_URI,
				values);
		Uri numberUri = Uri.withAppendedPath(uri,
				People.Phones.CONTENT_DIRECTORY);
		values.clear();

		values.put(Contacts.Phones.TYPE, People.Phones.TYPE_MOBILE);
		values.put(People.NUMBER, phone);
		getContext().getContentResolver().insert(numberUri, values);

		return uri;
	}

	private void delete()
	{
		if (currentNum.length() > 0)
		{
			currentNum = currentNum.substring(0, currentNum.length() - 1);
			call_num.setText(currentNum);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return true;
	}

	@Override
	public void onClick(View v)
	{
		int id = v.getId();
		if (id == R.id.num_1) {
			addChar("1");
		} else if (id == R.id.num_2) {
			addChar("2");
		} else if (id == R.id.num_3) {
			addChar("3");
		} else if (id == R.id.num_4) {
			addChar("4");
		} else if (id == R.id.num_5) {
			addChar("5");
		} else if (id == R.id.num_6) {
			addChar("6");
		} else if (id == R.id.num_7) {
			addChar("7");
		} else if (id == R.id.num_8) {
			addChar("8");
		} else if (id == R.id.num_9) {
			addChar("9");
		} else if (id == R.id.num_0) {
			addChar("0");
		} else if (id == R.id.num_xing) {
			addChar("*");
		} else if (id == R.id.num_jing) {
			addChar("#");
		} else if (id == R.id.delete) {
			delete();
		} else if (id == R.id.new_person) {
			showSelectAddContactDialog();
		} else if (id == R.id.call) {
			showSelectCallDialog();
		}
	}

	@Override
	public boolean onLongClick(View v)
	{
		int id = v.getId();
		if (id == R.id.delete) {
			call_num.setText("");
			currentNum = "";
		} else if (id == R.id.num_0) {
			addChar("+");
		} else if (id == R.id.num_jing) {
			addChar(";");
		} else if (id == R.id.num_xing) {
			addChar(",");
		}
		return true;
	}
	
	private void showSelectAddContactDialog()
	{
		if (currentNum == null || currentNum.equals(""))
		{
			return;
		}
		
		new SysListAlert(getContext(), 0, new String[] {
			getContext().getString(R.string.add_contact),
			getContext().getString(R.string.add_contact_zoo)}, null, "", this).show();
	}

	private void showSelectCallDialog()
	{
		if (currentNum == null || currentNum.equals(""))
		{
			return;
		}
		Engine.getInstance().showCallDialog(getContext(), listener1, 1);
	}
    
    private OnListAlertClickListener listener1 = new OnListAlertClickListener()
    {
        @Override
        public void onListAlertClick(int id, int which)
        {
            final String num = call_num.getText().toString();
            
            if (which == 0)
            {
                Engine.getInstance().getOnListAlertClick(getContext(), id, which,
                        Engine.getInstance().setCallRecord(num, 1, 0, 
                        CallLog.Calls.INCOMING_TYPE));
            }
            else if (which == 1)
            {
                if (NetUtil.getNetType(getContext()) == 255)
                {
                    Engine.getInstance().showSysDialogAct(getContext(), "提示",
                            "没有可使用的网络连接", "", "", 0, "");
                    return;
                }
                
                if (!Engine.getInstance().isNRRegisted)
                {
                    Engine.getInstance().showSysDialogAct(getContext(), "提示",
                            "请先在\"我\"中发起旅信电话注册", "", "", 0, "");
                    return;
                }
                
                if (NetUtil.getNetType(getContext()) == 1)
                {
                    if (mAppDialog == null)
                    {
                        mAppDialog = new SysAlertDialog(getContext());
                    }
                    
                    mAppDialog.setTitle("提示");
                    mAppDialog.setMessage("网络质量较差，是否继续通话？");
                    mAppDialog.setLeft("取消");
                    mAppDialog.setRight("继续");
                    mAppDialog.setOnClickListener(new OnSysAlertClickListener()
                    {
                        @Override
                        public void onRightClick(SysAlertDialog dialog)
                        {
                            // Utils.callNoRoaming(getContext(), num);
                            Api.getInstance().call(getContext(), num);
                            currentNum = "";
                            call_num.setText(currentNum);
                        }
                        
                        @Override
                        public void onOkClick(SysAlertDialog dialog)
                        {
                            // TODO Auto-generated method stub
                        }
                        
                        @Override
                        public void onLeftClick(SysAlertDialog dialog)
                        {
                            // TODO Auto-generated method stub
                        }
                    });
                    mAppDialog.show();
                    return;
                }

                // Utils.callNoRoaming(getContext(), num);
                Api.getInstance().call(getContext(), num);
                currentNum = "";
                call_num.setText(currentNum);
            }
        }
    };

	@Override
	public void onListAlertClick(int id, int which)
	{
		if (id == 0)
		{
			if (which == 0)//添加联系人
			{
//				Intent intent = new Intent(getContext(),
//						UpdateContactActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putString(UpdateContactActivity.CONTACT_NUM, currentNum);
//				intent.putExtras(bundle);
//				getContext().startActivity(intent);
				
				
				//开启系统通讯录界面
	        	 Intent intent = new Intent(Intent.ACTION_INSERT);
	        	 intent.putExtra(Contacts.Intents.Insert.PHONE, currentNum);
	             intent.setType("vnd.android.cursor.dir/person");
	             intent.setType("vnd.android.cursor.dir/contact");
	             intent.setType("vnd.android.cursor.dir/raw_contact");
	             getContext().startActivity(intent);
			}
			else if (which == 1)//添加到现有联系人
			{
//				Intent intent = new Intent(getContext(),
//						SelectContactsToSendActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putInt("type", ContactsFragment.SELECT_CONTACT_TO_ADD);
//				bundle.putString(UpdateContactActivity.CONTACT_NUM, currentNum);
//				intent.putExtras(bundle);
//				getContext().startActivity(intent);
				if (this.mOnL != null){
					
					this.mOnL.OnAddHaveContactk(currentNum);
				}
			}
		}
		
		
	}
	
}
