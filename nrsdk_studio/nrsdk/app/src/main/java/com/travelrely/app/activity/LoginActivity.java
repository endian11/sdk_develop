/*
 * Copyright (C) 2013 TravelRely Technology Limited
 * 
 * 文件功能:
 *         实现用户界面登录的功能,提供用户名和密码的输入框以及登录按钮
 *         提供密码找回的的功能
 *         
 */

package com.travelrely.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.travelrely.app.view.FormsArrawsBtn;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialogChk;
import com.travelrely.app.view.NavigationBar.OnNavigationBarClick;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.app.view.SysAlertDialogChk.OnSysAlertChkClickListener;
import com.travelrely.core.glms.ProgressOverlay;
import com.travelrely.core.glms.ProgressOverlay.OnProgressEvent;
import com.travelrely.core.glms.request.VerifyReq;
import com.travelrely.core.glms.response.VerifyRsp;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.nrs.Res;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.SysUtil;
import com.travelrely.core.util.UrlUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.model.CountrySelectModel;
import com.travelrely.sdk.R;
import com.travelrely.app.db.GroupDBHelper;
import com.travelrely.v2.net_interface.GetCommStatusRsp;
import com.travelrely.v2.net_interface.LoginRsp;
import com.travelrely.v2.net_interface.TransferClientIdReq;
import com.travelrely.v2.response.GetGroupList;
import com.travelrely.v2.response.GroupList;

/**
  * @ClassName: LoginActivity
  * @Description: 登陆界面
  * @author Comsys-john
  * @date 2015-3-27 下午6:13:29
  *
  */
public class LoginActivity extends NavigationActivity implements
        OnClickListener, OnNavigationBarClick {
    private FormsArrawsBtn bt_rraws;
    private EditText etPhoneNum;
    private View div;
    private EditText etPassword;

    private String strPreActivity = null;
    
    CheckBox mCheckBox;
    TextView tvService;
    
    Button btnLogin, btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        init();

        // 判断归属地还是漫游地,并设置homeLogin全局变量
//        Engine.getInstance().judgeHomeOrRoam();

        // 从meal界面跳到登录界面会传递一个字符串标识
        Intent intent = getIntent();
        strPreActivity = intent.getStringExtra("PRE_ACTIVITY");
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	if (SpUtil.getfirstInstallToLogin()== 0){
    		//友好提示
    		friendlyPrompt();
    	}
    }
    private void init() {
        bt_rraws = (FormsArrawsBtn) findViewById(R.id.bt_rraws);
        bt_rraws.setRightImgDrawable(R.drawable.arrow_below);
        bt_rraws.setTextLeft("+86");
        bt_rraws.setTextCentre(R.string.china_1);
        bt_rraws.setOnClickListener(this);

        // 用户名输入框
        etPhoneNum = (EditText) findViewById(R.id.et_up);

        div = findViewById(R.id.div);

        // 密码输入框
        etPassword = (EditText) findViewById(R.id.et_down);

        // 如果存在长密码,则只要保留用户名输入框,隐藏密码输入框
        if (Engine.getInstance().hasLongPswd()) {
            div.setVisibility(View.GONE);
            etPassword.setVisibility(View.GONE);
        }
        
        // 服务条款复选框
        mCheckBox = (CheckBox)findViewById(R.id.check_box);
        mCheckBox.setChecked(true);

        // 服务条款查看链接
        tvService = (TextView)findViewById(R.id.tvService);
        tvService.setOnClickListener(this);
        
        btnLogin = (Button) findViewById(R.id.bt_login);
        btnReg = (Button) findViewById(R.id.bt_reg);
        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    protected void initNavigationBar() {
        setTitle(R.string.login);
        getNavigationBar().hideRight();
        setLeftText(R.string.Cancel);
        getNavigationBar().hideLeftText();
    }

    @Override
    public void onLeftClick() {
        if (!Engine.getInstance().isLogIn) {
            Intent intent = new Intent(this, HomePageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onLeftClick();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
    	int viewId = v.getId();
    	if (viewId == R.id.tvService) {
    		final Bundle bundle = new Bundle();
            bundle.putInt("ITEM", 1);
            // openActivity(SetPageActivity.class, bundle);
    	} else if (viewId == R.id.bt_login) {
    		gotoLogin();
    	} else if (viewId == R.id.bt_reg) {
    		gotoRegist();
    	} else if (viewId == R.id.bt_rraws) {
//          Intent intent = new Intent(this, SelectCountryAct.class);
//          startActivityForResult(intent, SelectCountryAct.REQUEST_CODE);
//          overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
//          break;
      	// TODO cwj
    	} else {
    		
    	}
    }
    
    
    
    /**
	 * @return true -标识用户同意，可以继续操作 。false标识用户任何操作都要弹出这个提示框
	 */
	private void friendlyPrompt() {
		if (SpUtil.getfirstInstallToLogin() == 0){//第一次安装 登陆
        	
        	//提示用户 是否开通了 免漫游 ，用户必须勾选，并点击同意
        	showAppAlertchk("资费说明",getResources().getString(R.string.friendly_prompt), "同意", new OnSysAlertChkClickListener() {
				
				@Override
				public void onRightClick(SysAlertDialogChk dialog) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onOkClick(SysAlertDialogChk dialog) {
					dialog.dismiss();
					SpUtil.setfirstInstallToLogin(1);
				}
				
				@Override
				public void onLeftClick(SysAlertDialogChk dialog) {
					// TODO Auto-generated method stub
					
				}
			});
        } else {
			SpUtil.setfirstInstallToLogin(0);
		}
	}
    

    private void gotoRegist() {
        if (mCheckBox.isChecked() == false) {
            Utils.showToast(this,
                    getResources().getString(R.string.selServiceItem));
            return;
        }
        
        String cc = bt_rraws.getTextLeft();
        String phone = etPhoneNum.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showShortToast(R.string.enterPhoneNum1);
            return;
        }
        
        final String strUsrName = cc + phone;
        Engine.getInstance().setUserName(strUsrName);
        Engine.getInstance().setCC(cc);

        ProgressOverlay mOverlay = new ProgressOverlay(LoginActivity.this);
        mOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                Bundle bundle = new Bundle();
                bundle.putString("USER_NAME", strUsrName);

                // 判断用户是否已注册
                VerifyRsp rsp = VerifyReq.verify(
                        LoginActivity.this, strUsrName, "",
                        VerifyReq.VERIFY_BY_REGIST);
                if (!rsp.getBaseRsp().isSuccess())
                {
                    Res.toastErrCode(LoginActivity.this, rsp.getBaseRsp().getMsg());
                    return;
                }
                if (rsp.getData().isUserExist())
                {
                    showShortToast("该用户已经注册");
                    return;
                }
                
                SpUtil.setNRFemtoIp(rsp.getData().getFemtoInfos().get(0).getIp());

                if (SysUtil.getSysSdkCode() < 18)
                {
                    // openActivity(VerifyByPhoneAct.class, bundle);
                    return;
                }

                openActivity(VerifyByBoxAct.class, bundle);
            }
        });
    }
    
    private void gotoVerify() {
        String cc = bt_rraws.getTextLeft();
        String phone = etPhoneNum.getText().toString();
        if (TextUtils.isEmpty(phone))
        {
            showShortToast(R.string.enterPhoneNum1);
            return;
        }

        final String strUsrName = cc + phone;
        Engine.getInstance().setUserName(strUsrName);
        
        ProgressOverlay mOverlay = new ProgressOverlay(LoginActivity.this);
        mOverlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
                Bundle bundle = new Bundle();
                bundle.putString("USER_NAME", strUsrName);

                // 判断用户是否已注册
                VerifyRsp rsp = VerifyReq.verify(
                        LoginActivity.this, strUsrName, "",
                        VerifyReq.VERIFY_BY_REGIST);
                if (!rsp.getBaseRsp().isSuccess()) {
                	Res.toastErrCode(LoginActivity.this, rsp.getBaseRsp().getMsg());
                    return;
                }
                
                SpUtil.setNRFemtoIp(rsp.getData().getFemtoInfos().get(0).getIp());

                if (SysUtil.getSysSdkCode() < 18) {
                    // openActivity(VerifyByPhoneAct.class, bundle);
                    return;
                }

                openActivity(VerifyByBoxAct.class, bundle);
            }
        });
    }

    private void gotoLogin() {
        if (mCheckBox.isChecked() == false) {
            Utils.showToast(this,
                    getResources().getString(R.string.selServiceItem));
            return;
        }

        String cc = bt_rraws.getTextLeft();
        String phone = etPhoneNum.getText().toString();

        // 检查输入的用户名是否合法
        boolean bRslt = checkInputUsrName(phone);
        if (bRslt == false) {
            return;
        }
        
        final String url = ReleaseConfig.getUrl(cc);

        final String strUsrName = cc + phone;
        Engine.getInstance().setUserName(strUsrName);
        Engine.getInstance().setCC(cc);

        // 登录流程处理
        ProgressOverlay mOverlay = new ProgressOverlay(LoginActivity.this);
        mOverlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
                boolean bRslt = loginProcess(url, strUsrName);
                // 登录失败
                if (bRslt == false) {
                    // 清长密码
                    Engine.getInstance().setLongPswd("");
                    return;
                }

                // 登录成功
                Utils.hideInputMethod(LoginActivity.this);

                if (strPreActivity != null
                        && strPreActivity.equals("SingleCardActivity")) {
                    LOGManager.d("from SingleCardActivity");
                    finish();
                    return;
                }

                openActivity(HomePageActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
                
                Engine.getInstance().tryToStartNR(LoginActivity.this);
                
                finish();
                return;
            }
        });
    }

    private boolean checkInputUsrName(String strInputUsrName) {
        if (strInputUsrName == null || strInputUsrName.equals("")) {
            showShortToast(R.string.enterPhoneNum1);
            return false;
        }

        return true;
    }

    private boolean loginProcess(String url, String strUsrName) {
        String pswd = SpUtil.getLongPswd(LoginActivity.this);
        // 登录处理
        final LoginRsp login = Engine.getInstance().loginRequest(
                url, strUsrName, pswd, LoginActivity.this);
        if (login == null) {
            Utils.showToast(LoginActivity.this,
                    getResources().getString(R.string.errorNetwork2));
            return false;
        }

        // 登陆失败
        if (!login.getBaseRsp().isSuccess()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAppAlert("提示", login.getBaseRsp().getMsg(), "取消", "验证",
                        new OnSysAlertClickListener() {
                            @Override
                            public void onRightClick(SysAlertDialog dialog) {
                                gotoVerify();
                            }
                            
                            @Override
                            public void onOkClick(SysAlertDialog dialog) {
                            }
                            
                            @Override
                            public void onLeftClick(SysAlertDialog dialog) {
                                
                            }
                        });
                }
            });

            Engine.getInstance().isLogIn = false;
            //SpUtil.setLastLogin(false);
            LOGManager.d("login失败");
            
            return false;
        }

        // 登录成功的后续处理
        Engine.getInstance().isLogIn = true;
        
        // 直接登录成功的后续处理
    	
    	TransferClientIdReq.transferclientidReq(LoginActivity.this,
    			PushManager.getInstance().getClientid(getApplicationContext())+"",strUsrName);
        
        
        // 启动同步通讯录
        Engine.getInstance().syncContactThread();
        
        // 获取用户信息
        Engine.getInstance().getUserInfoRequest(
                Engine.getInstance().getUserName(), LoginActivity.this);
        
        //获取群列表
        getGroupList();
        
        // 获取公共状态
        GetCommStatusRsp rsp = Engine.getInstance().getCommStatusRequest(this, url);
        Engine.getInstance().syncProfilesThread(rsp);

        LOGManager.d("login成功");

        // 启动消息线程
//        Intent intent = new Intent(LoginActivity.this, TravelService.class);
//        startService(intent);
        
//        Engine.getInstance().judgeHomeOrRoam();

        // 根据homeLogin判断是否获取归属地配置信息还是漫游地配置信息
        if (Engine.getInstance().homeLogin == true)
        {
            // get home profile
            Engine.getInstance().getHomeProfileFromDB(
                    Engine.getInstance().getSimMcc(),
                    Engine.getInstance().getSimMnc());
        }
        else
        {
            // get roam profile
            Engine.getInstance().getRoamProfileFromDB(
                    Engine.getInstance().getSimMcc(),
                    Engine.getInstance().getSimMnc());

            String strRoamGlmsIp = Engine.getInstance().getRoamGlmsLoc();
            if (null == strRoamGlmsIp || strRoamGlmsIp.equals(""))
            {
                LOGManager.e("没有获取到漫游地配置信息");
                return false;
            }

            Engine.getInstance().loginRequest(UrlUtil.makeUrl(strRoamGlmsIp),
                    Engine.getInstance().userName, pswd, null);
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == 67 && Engine.getInstance().hasLongPswd())
        {
            if (etPhoneNum.hasFocus())
            {
                div.setVisibility(View.GONE);
                etPassword.setVisibility(View.GONE);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    
    //获取群信息
    private void getGroupList()
    {
        GroupDBHelper gHelper = GroupDBHelper.getInstance();
        if(!gHelper.isGroup(Engine.getInstance().getUserName()))
        {
            GetGroupList getGroupList = Engine.getInstance().getGroupList(this);
            if (getGroupList == null)
            {
                return;
            }
            for(GroupList gList : getGroupList.getDate().getGrouplist()){
                gHelper.insertGroupList(gList);
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
//                if(resultCode == SelectCountryAct.REQUEST_CODE){
//                    CountrySelectModel cModel = (CountrySelectModel) bundle.getSerializable(SelectCountryAct.COUNTRY_INFO);
//                    refresh(cModel);
//                }
            	// TODO cwj
            }
        }
    }
    
    private void refresh(final CountrySelectModel cModel){
        
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                // TODO Auto-generated method stub
                bt_rraws.setTextLeft(cModel.getCountryNum());
                bt_rraws.setTextCentre(cModel.getCountryName());
            }
        });
    }
}
