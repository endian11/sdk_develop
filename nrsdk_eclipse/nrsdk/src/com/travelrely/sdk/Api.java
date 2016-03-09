package com.travelrely.sdk;

import com.igexin.sdk.PushManager;
import com.travelrely.core.glms.request.GetVerifyCodeReq;
import com.travelrely.core.glms.request.VerifyReq;
import com.travelrely.core.glms.response.GetVerifyCodeRsp;
import com.travelrely.core.glms.response.VerifyRsp;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.nrs.Res;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.UrlUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.GroupDBHelper;
import com.travelrely.v2.net_interface.GetCommStatusRsp;
import com.travelrely.v2.net_interface.LoginRsp;
import com.travelrely.v2.net_interface.LogoutReq;
import com.travelrely.v2.net_interface.LogoutRsp;
import com.travelrely.v2.net_interface.TransferClientIdReq;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class Api {
	private String TAG = Api.class.getName();
	
	private static Api mApi;
	
	private Api() {
		/** TODO init **/
		
	}
	
	public static Api getInstance() {
		if (mApi == null) {
			mApi = new Api();
		}
		return mApi;
	}
	
	public void login() {
		
	}
	
	/**
	 * 生成验证码 登录流程中,点击获取验证码时,向服务发起请求
	 * @param context
	 * @param userName
	 * @param listener
	 */
	public void generateVerifyCode(final Context context, final String userName, 
			final RequestResultListener listener) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				GetVerifyCodeRsp rsp = GetVerifyCodeReq.getVerifyCode(
                        context, userName);
                if (rsp != null && rsp.getResponseInfo().isSuccess()) {
                	listener.success(rsp.getResponseInfo());
                } else {
                	listener.fail(rsp.getResponseInfo());
                }
			}
		}).start();
	}
	
	/**
	 * 通过验证码验证 登录流程中,用户输入验证码后,向服务发起验证
	 * @param context
	 * @param userName
	 * @param authNum
	 * @param listener
	 */
	public void verify(final Activity context, final String userName, final String authNum, 
			final RequestResultListener listener) {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				VerifyRsp rsp = VerifyReq.verify(context,
		                userName, authNum, VerifyReq.VERIFY_BY_PHONE);
		        if (rsp != null && rsp.getBaseRsp().isSuccess()) {
		            Engine.getInstance().setUserName(rsp.getData().getUserName());
		            Engine.getInstance().setLongPswd(rsp.getData().getPassword());

		            SpUtil.setLongPswd(rsp.getData().getPassword());
		            SpUtil.setUserName(rsp.getData().getUserName());
		            
		            if (loginProcess(context, Engine.getInstance().getUserName())) {
		                Engine.getInstance().tryToStartNR(context);
		                listener.success(rsp.getBaseRsp());
		            }
		            
		        } else {
		        	listener.fail(rsp.getBaseRsp());
		        }
			}
		}).start();
    }
	
	/**
	 * 用户登陆接口
	 * @param strUsrName
	 * @param cc
	 * @param listener
	 */
	public void login(final Activity activity, final String strUsrName, final String cc, 
			final RequestResultListener listener) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Engine.getInstance().setCC(cc);
				boolean isSuccess = loginProcess(activity, cc+strUsrName);
				if (isSuccess) {
					listener.success(null);
				} else {
					listener.fail(null);
				}
			} 
		}).start();
	}

	public boolean loginProcess(final Activity context, String userName) {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        
        String pswd = SpUtil.getLongPswd(context);
        // 登录处理
        final LoginRsp login = Engine.getInstance().loginRequest(host, userName, pswd, context);
        if (login == null) {
            Utils.showToast(context, context.getResources().getString(R.string.errorNetwork2));
            LogUtil.i("loginProcess in VerifyByBoxAct", "login == null 退出loginprocess");
            return false;
        }

        if (!login.getBaseRsp().isSuccess()) {
            Engine.getInstance().isLogIn = false;
            Res.toastErrCode(context, login.getBaseRsp().getMsg());
            return false;
        }

        // 登录成功的后续处理
        Engine.getInstance().isLogIn = true;
       
        TransferClientIdReq.transferclientidReq(context,
    			PushManager.getInstance().getClientid(context.getApplicationContext())+"",userName);
        
        // 保存国家码
        SpUtil.setCC(Engine.getInstance().getCC());
        
        // 启动同步通讯录
        Engine.getInstance().syncContactThread();
        
        // 获取用户信息
        Engine.getInstance().getUserInfoRequest(
                Engine.getInstance().getUserName(), context);
        
        // 获取公共状态
        GetCommStatusRsp rsp = Engine.getInstance().getCommStatusRequest(context,
                host);
        Engine.getInstance().syncProfilesThread(rsp);

        LogUtil.i("loginProcess in VerifyByBoxAct", "login success");

        // 根据homeLogin判断是否获取归属地配置信息还是漫游地配置信息
        if (Engine.getInstance().homeLogin == true) {
            // get home profile
            Engine.getInstance().getHomeProfileFromDB(
                    Engine.getInstance().getSimMcc(),
                    Engine.getInstance().getSimMnc());
        } else {
            // get roam profile
            Engine.getInstance().getRoamProfileFromDB(
                    Engine.getInstance().getSimMcc(),
                    Engine.getInstance().getSimMnc());

            String strRoamGlmsIp = Engine.getInstance().getRoamGlmsLoc();
            if (null == strRoamGlmsIp || strRoamGlmsIp.equals(""))
            {
                LogUtil.i("loginProcess in VerifyByBoxAct", "没有获取到漫游地配置信息 退出loginprocess");
                return false;
            }

            Engine.getInstance().loginRequest(UrlUtil.makeUrl(strRoamGlmsIp),
                    Engine.getInstance().userName, pswd, null);
        }
        
        if (TextUtils.isEmpty(login.getData().getLastLoginInfo())) {
            return true;
        }
        
        context.runOnUiThread(new Runnable() {
            public void run()
            {
                Engine.getInstance().showSysAlert(context.getApplication(),
                        "提示", login.getData().getLastLoginInfo(), "关闭");
            }
        });
        
        return true;
    }
	
	/**
	 * 用户退出登陆
	 * @param context
	 * @param listener
	 */
	public void logout(final Context context, final RequestResultListener listener) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				LogoutRsp rsp=LogoutReq.logout(context);
				if (rsp.getBaseRsp().isSuccess()) {
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
	                GroupDBHelper.getInstance().delAllData();
	                listener.success(rsp.getBaseRsp());
				} else {
					listener.fail(rsp.getBaseRsp());
				}
			}
		}).start();
	}
	
	/**
	 * 打电话
	 * @param context
	 * @param number 电话号码
	 */
	public void call(Context context, String number) {
		if (!Engine.getInstance().isNRRegisted) {
			Log.e(TAG, "no registed");
		} else {
			Engine.getInstance().startCall(context, number);
		}
	}
	
	/**
	 * 挂断电话
	 * @param c
	 */
	public void hungUp(Context c) {
		Engine.getInstance().hangUp(c);;
	}

}
