/**
 * 该文件是用户设置的界面
 */
package com.travelrely.app.activity;

import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.travelrely.app.view.CustomProgressDialog;
import com.travelrely.app.view.FormsArrawsRightButton;
import com.travelrely.app.view.FormsArrawsRightCentreBt;
import com.travelrely.app.view.FormsArrawsRightDownBt;
import com.travelrely.app.view.FormsArrawsRightUpBt;
import com.travelrely.app.view.FormsFinishButton;
import com.travelrely.core.Constant;
import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.core.Res;
import com.travelrely.core.nr.util.ActivityCollector;
import com.travelrely.model.ContactModel;
import com.travelrely.net.HttpConnector;
import com.travelrely.net.ProgressOverlay;
import com.travelrely.net.ProgressOverlay.OnProgressEvent;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.GroupDBHelper;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.net_interface.LogoutReq;
import com.travelrely.v2.net_interface.LogoutRsp;
import com.travelrely.v2.response.GetNoroamingStatus;
import com.travelrely.v2.response.Response;
import com.travelrely.v2.util.FetchTokensTask;
import com.travelrely.v2.util.LOGManager;
import com.travelrely.v2.util.NetUtil;
import com.travelrely.v2.util.PreferencesUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.Utils;

public class SetActivity extends NavigationActivity implements OnClickListener
{
    FormsArrawsRightButton btnNRSet; // 消息提醒
    
    private FormsArrawsRightButton btnBleMatch;

    private FormsArrawsRightUpBt btnSyncContacts;
    private FormsArrawsRightCentreBt btnI18N;
    private FormsArrawsRightDownBt btnChangePswd;

    FormsFinishButton btnLogOut;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_set);

        init();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        int iLangType;
        String strLang = "en";

        iLangType = SpUtil.getLangType();
        switch (iLangType)
        {
            case 1:
                btnI18N.setRightText(R.string.lang_zh_cn);
                break;

            case 2:
                btnI18N.setRightText(R.string.lang_zh_tw);
                break;

            case 3:
                btnI18N.setRightText(R.string.lang_en);
                break;

            default:
                strLang = Locale.getDefault().getLanguage();
                if (strLang.equals("zh"))
                {
                    if (Locale.getDefault().toString().equals("zh_CN"))
                    {
                        btnI18N.setRightText(R.string.lang_zh_cn);
                    }

                    if (Locale.getDefault().toString().equals("zh_TW"))
                    {
                        btnI18N.setRightText(R.string.lang_zh_tw);
                    }
                }
                else
                {
                    btnI18N.setRightText(R.string.lang_en);
                }
                break;
        }
    
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
//        this.stopService(new Intent(this, TravelService.class));
    }

    @SuppressLint("ResourceAsColor")
    private void init()
    {
        btnNRSet = (FormsArrawsRightButton) findViewById(R.id.btnNRSet);
        btnNRSet.setLeftText(R.string.NRSet);
        btnNRSet.setOnClickListener(this);
        
        btnBleMatch = (FormsArrawsRightButton) findViewById(R.id.btnBleMatch);
        btnBleMatch.setOnClickListener(this);
        btnBleMatch.setLeftText("蓝牙盒子配对");
        
        btnSyncContacts = (FormsArrawsRightUpBt) findViewById(R.id.btnSyncContacts);
        btnSyncContacts.setOnClickListener(this);
        btnSyncContacts.setLeftText(R.string.sync_contact);

        // 国际化多语言支持
        btnI18N = (FormsArrawsRightCentreBt) findViewById(R.id.i18n_btn);
        btnI18N.setLeftText(R.string.languageSupport);
        btnI18N.showRightText();
        btnI18N.setRightTextColor(R.color.gray);
        btnI18N.setOnClickListener(this);
        
        btnChangePswd = (FormsArrawsRightDownBt) findViewById(R.id.btnChangePswd);
        btnChangePswd.setLeftText(R.string.tv_xgmm);
        btnChangePswd.setOnClickListener(this);
        
        btnLogOut = (FormsFinishButton) findViewById(R.id.btnLogOut);
        btnLogOut.setText(R.string.tv_exit);
        btnLogOut.setOnClickListener(this);
    }

    @Override
    protected void initNavigationBar()
    {
        getNavigationBar().hideRight();
        setTitle(R.string.sysSetting);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.btnSyncContacts) {
			try
			{
			    sync();
			}
			catch (Exception e)
			{
			    showShortToast(R.string.contact_permissions_dialog);
			}
		} else if (id == R.id.i18n_btn) {
		} else if (id == R.id.btnChangePswd) {
			openActivity(NRCallAct.class);
		} else if (id == R.id.btnNRSet) {
			gotoNRSetAct();
		} else if (id == R.id.btnBleMatch) {
			// openActivity(BleAct.class);
		} else if (id == R.id.btnLogOut) {
			logOut();
		} else {
		}
    }

    private void sync()
    {
        ProgressOverlay overlay = new ProgressOverlay(this);
        overlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                Utils.hebingContacts();
                FetchTokensTask fechTokenTask = new FetchTokensTask();
                List<ContactModel> list = null;
                fechTokenTask.execute(list);
                Utils.showToast(SetActivity.this, getResources().getString(
                                R.string.SyncSucc));
            }
        });
    }
    
    private void gotoNRSetAct()
    {
        String type = PreferencesUtil.getSharedPreferencesStr(this,
                PreferencesUtil.PUBLIC_PRERENCES,
                ConstantValue.layoutTypeKey);
        if (type.equals("0"))
        {
            openActivity(NRSetAct.class);
        }
        else
        {
            if (Utils
                    .isHasFile("/data/data/com.travelrely.v2/shared_prefs/"
                            + PreferencesUtil.PUBLIC_PRERENCES + ".xml"))
            {
                openActivity(NRSetAct.class);
            }
            else
            {
                getNoamingState();
            }
        }
    }
    
    private void getNoamingState()
    {
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                String cc = Engine.getInstance().getCC();
                String host = ReleaseConfig.getUrl(cc);
                String postData = Request.getNoamingState();
                String urls = host + "api/user/get_noroaming_status";
                String httpResult = null;
                HttpConnector httpConnector = new HttpConnector();
                httpResult = httpConnector.requestByHttpPut(urls, postData,
                        SetActivity.this, false);
                if (httpResult == null || httpResult.equals(""))
                {
                    return;
                }
                GetNoroamingStatus getStatus = Response
                        .getNoroamingStatus(httpResult);
                if (getStatus != null)
                {
                    if (getStatus.getResponseInfo().isSuccess())
                    {
                        if (getStatus.getData().getType().equals("1")
                                || getStatus.getData().getType().equals("0"))
                        {
                            PreferencesUtil.setSharedPreferences(
                                    SetActivity.this,
                                    PreferencesUtil.PUBLIC_PRERENCES,
                                    ConstantValue.layoutTypeKey, getStatus
                                            .getData().getType());
                        }
                        else if (getStatus.getData().getType().equals("2"))
                        {

                            String startTime = getStatus.getData()
                                    .getStarttime();
                            String endTime = getStatus.getData().getEndtime();
                            PreferencesUtil.setSharedPreferences(
                                    SetActivity.this,
                                    PreferencesUtil.PUBLIC_PRERENCES,
                                    ConstantValue.layoutTypeKey, getStatus
                                            .getData().getType());
                            PreferencesUtil.setSharedPreferences(
                                    SetActivity.this,
                                    PreferencesUtil.PUBLIC_PRERENCES,
                                    ConstantValue.startTimeKey, startTime
                                            .substring(0,
                                                    startTime.length() - 3));
                            PreferencesUtil.setSharedPreferences(
                                    SetActivity.this,
                                    PreferencesUtil.PUBLIC_PRERENCES,
                                    ConstantValue.endTimeKey, endTime
                                            .substring(0,
                                                    startTime.length() - 3));
                        }
                        openActivity(NRSetAct.class);
                    }
                }
                else
                {
                    Utils.showToast(SetActivity.this, getStatus
                            .getResponseInfo().getMsg());
                }
            }
        });
    }
    
    // 退出登录
    private void logOut()
    {
        Engine.getInstance().stopNR(this);
        
      	new AsyncTask<Void, Void, Void>(){
    		
    		private CustomProgressDialog dialog;
			protected void onPreExecute() {
    			if (NetUtil.isNetworkAvailable(getApplicationContext())){
    				dialog = CustomProgressDialog.createDialog(SetActivity.this);
    				dialog.show();
    			}else{
    				 mainTabMsgCount();

                     Engine.getInstance().isLogIn = false;
                     Engine.getInstance().setUserName("");
                     SpUtil.setUserName("");
                     Engine.getInstance().isHeadImg = false;
                     SpUtil.setHomeProfileVer(0);
                     SpUtil.setRoamProfileVer(0);
                     SpUtil.setExpPriceVer(0);
                     SpUtil.setAdvVer(0);
                     SpUtil.setCountryVer(0);
                     SpUtil.setPkgVer(0);
//                     Engine.getInstance().homePageAct.finish();
                     
                     ActivityCollector.finishAll();
                     GroupDBHelper.getInstance().delAllData();
//                     Intent intent = new Intent(SetActivity.this, TravelService.class);
//                     stopService(intent);
                     openActivity(HomePageActivity.class);
    			}
    		};

			@Override
			protected Void doInBackground(Void... params) {
				  LogoutRsp rsp;
				try {
					rsp = LogoutReq.logout(SetActivity.this);
					 if (rsp == null)
		                {
		                }else{
		                	  mainTabMsgCount();

		                      Engine.getInstance().isLogIn = false;
		                      Engine.getInstance().setUserName("");
		                      SpUtil.setUserName("");
		                      Engine.getInstance().isHeadImg = false;
		                      SpUtil.setHomeProfileVer(0);
		                      SpUtil.setRoamProfileVer(0);
		                      SpUtil.setExpPriceVer(0);
		                      SpUtil.setAdvVer(0);
		                      SpUtil.setCountryVer(0);
		                      SpUtil.setPkgVer(0);
//		                      Engine.getInstance().homePageAct.finish();
		                      
		                      ActivityCollector.finishAll();
		                      GroupDBHelper.getInstance().delAllData();
//		                      Intent intent = new Intent(SetActivity.this, TravelService.class);
//		                      stopService(intent);
		                      openActivity(HomePageActivity.class);
		                }
		                if (rsp.getBaseRsp().isSuccess())
		                {
		                    LOGManager.d("退出成功");
		                    mainTabMsgCount();

		                    Engine.getInstance().isLogIn = false;
		                    Engine.getInstance().setUserName("");
		                    SpUtil.setUserName("");
		                    Engine.getInstance().isHeadImg = false;
		                    SpUtil.setHomeProfileVer(0);
		                    SpUtil.setRoamProfileVer(0);
		                    SpUtil.setExpPriceVer(0);
		                    SpUtil.setAdvVer(0);
		                    SpUtil.setCountryVer(0);
		                    SpUtil.setPkgVer(0);
//		                    Engine.getInstance().homePageAct.finish();
		                    
		                    ActivityCollector.finishAll();
		                    GroupDBHelper.getInstance().delAllData();
//		                    Intent intent = new Intent(SetActivity.this, TravelService.class);
//		                    stopService(intent);
		                    openActivity(HomePageActivity.class);
		                    finish();
		                }
		                else
		                {
		                    Res.toastErrCode(SetActivity.this, rsp.getBaseRsp().getMsg());
		                  
		                }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					 mainTabMsgCount();

                     Engine.getInstance().isLogIn = false;
                     Engine.getInstance().setUserName("");
                     SpUtil.setUserName("");
                     Engine.getInstance().isHeadImg = false;
                     SpUtil.setHomeProfileVer(0);
                     SpUtil.setRoamProfileVer(0);
                     SpUtil.setExpPriceVer(0);
                     SpUtil.setAdvVer(0);
                     SpUtil.setCountryVer(0);
                     SpUtil.setPkgVer(0);
//                     Engine.getInstance().homePageAct.finish();
                     
                     ActivityCollector.finishAll();
                     GroupDBHelper.getInstance().delAllData();
//                     Intent intent = new Intent(SetActivity.this, TravelService.class);
//                     stopService(intent);
                     openActivity(HomePageActivity.class);
				}
	               
					return null;
			}
			protected void onPostExecute(Void result) {
				if (dialog != null && dialog.isShowing()){
					dialog.dismiss();
					dialog=null;
				}
			};
    		
    	}.execute();
    	
    }
    
    private void mainTabMsgCount()
    {
    	Constant.isMsgCount = false;
    	Constant.isSmsCount = false;
        Intent intent = new Intent();
        intent.setAction(IAction.MSG_COUNT_ACTION);
        sendBroadcast(intent);
    }
}
