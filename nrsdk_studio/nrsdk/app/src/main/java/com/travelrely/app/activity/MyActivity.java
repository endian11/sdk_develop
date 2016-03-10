
package com.travelrely.app.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.travelrely.app.view.FormsArrawsRightButton;
import com.travelrely.app.view.FormsArrawsRightUpBt;
import com.travelrely.app.view.PullScrollView;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.core.glms.ProgressOverlay;
import com.travelrely.core.glms.GetMsg.FetchMessage;
import com.travelrely.core.glms.ProgressOverlay.OnProgressEvent;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.FileUtil;
import com.travelrely.core.util.PreferencesUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.SysUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.UserRoamProfileDBHelper;
import com.travelrely.v2.model.UserRoamProfile;
import com.travelrely.v2.net_interface.GetUsrInfoRsp.Data;
import com.travelrely.v2.net_interface.GetUsrInfoRsp.PersonalInfo;
import com.travelrely.v2.response.TripInfo;

public class MyActivity extends NavigationActivity implements OnClickListener, PullScrollView.OnTurnListener
{
    private ImageView ivHeadImg;
    private TextView tvName;// 名称
    private TextView tvValidOrder;
    private TextView tvPos;
    private TextView tvNR;
    private TextView tvContent;

    private ImageView ivVisitingFlag; // 旅信漫游状态
    private ImageView ivRoamingFlag; // 运营商漫游状态

    private FormsArrawsRightButton btnMyOrder; // 我的订单

    private FormsArrawsRightButton btnCalendar; // 行程日历
    
    private FormsArrawsRightButton btnNRRegister; // 旅信网络电话注册

    private FormsArrawsRightUpBt btnSetting; // 系统设置

    private MyTravelrelyReceiver myTravelrelyReceiver;

    private Handler handler;

    PullScrollView pullScrollView;
    ImageView imViewHead;;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_activity);

        init();

        handler = new Handler();

        myTravelrelyReceiver = new MyTravelrelyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IAction.MY_CHANGED);
        registerReceiver(myTravelrelyReceiver, intentFilter);
    }

    @Override
    protected void initNavigationBar()
    {
        setTitle(R.string.myTra);
        getNavigationBar().hideLeftText();
        getNavigationBar().getLeftImg().setImageResource(
                R.drawable.home_icon_bg);
        getNavigationBar().hideRight();
    }

    @Override
    public void onResume()
    {
    	super.onResume();
        setTexts();

    }

    private void init()
    {
    	pullScrollView = (PullScrollView) findViewById(R.id.scroll_view);
    	imViewHead = (ImageView) findViewById(R.id.background_img);
    	
        ivHeadImg = (ImageView) findViewById(R.id.ivHeadImg);
        ivHeadImg.setOnClickListener(this);

        tvName = (TextView) findViewById(R.id.tv_name);
        tvValidOrder = (TextView) findViewById(R.id.tvValidOrder);
        tvPos = (TextView) findViewById(R.id.tvPos);
        tvNR = (TextView) findViewById(R.id.tvNR);
        tvContent = (TextView) findViewById(R.id.tvContent);

        ivVisitingFlag = (ImageView) findViewById(R.id.iv_visit);
        ivRoamingFlag = (ImageView) findViewById(R.id.iv_roam);

        btnMyOrder = (FormsArrawsRightButton) findViewById(R.id.btnMyOrder);
        btnMyOrder.setOnClickListener(this);
        btnMyOrder.setLeftText(R.string.myOrders);
        btnMyOrder.setVisibility(View.GONE);

        btnCalendar = (FormsArrawsRightButton) findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(this);
        btnCalendar.setLeftText(R.string.travel_calendar);

        btnNRRegister = (FormsArrawsRightButton) findViewById(R.id.btnNRRegister);
        btnNRRegister.setOnClickListener(this);
        btnNRRegister.setLeftText(R.string.NoRoaming);
        
        btnSetting = (FormsArrawsRightUpBt) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
        btnSetting.setLeftText(R.string.sysSetting);

        if (SpUtil.getVisiting() == 1)
        {
            // 旅信漫游状态
            ivVisitingFlag.setImageResource(R.drawable.loc_visiting);
            ivVisitingFlag.setVisibility(View.VISIBLE);
            
        }
        else
        {
            if (Engine.getInstance().getIsRoaming() == 0)
            {
                // 旅信归属状态
                ivVisitingFlag.setImageResource(R.drawable.loc_homing);
                ivVisitingFlag.setVisibility(View.VISIBLE);
            }
        }
        if (Engine.getInstance().getIsRoaming() == 1)
        {
            // 运营商漫游状态
            ivRoamingFlag.setVisibility(View.VISIBLE);
//            TravelService.tempMaxId = -1;
            FetchMessage.tempMaxId = -1;
        }
        else
        {
            // 运营商归属状态
            ivRoamingFlag.setVisibility(View.GONE);
//            TravelService.tempMaxId = -1;
            FetchMessage.tempMaxId = -1;
        }
        
        pullScrollView.setHeader(imViewHead);
        pullScrollView.setOnTurnListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;

        int viewId = v.getId();
        if (viewId == R.id.btnMyOrder) {
        	if (Engine.getInstance().isLogIn)
            {
                getMyOrder();
            }
            else
            {
                Utils.showToast(this, "请登录后再查询订单");
            }
        } else if (viewId == R.id.btnSetting) {
        	openActivity(SetActivity.class);
        } else if (viewId == R.id.btnNRRegister) {
        	if (SpUtil.getNRService() == 0)
            {
                showAppAlert("提示", "您没有购买旅信网络电话，请到“商店”购买使用",
                        "确定", "立即购买", new OnSysAlertClickListener() {
                            @Override
                            public void onRightClick(SysAlertDialog dialog) {
                                //openActivity(MealNoRoamAct.class);
                                finish();
                            }
                            
                            @Override
                            public void onOkClick(SysAlertDialog dialog) {
                                
                            }
                            
                            @Override
                            public void onLeftClick(SysAlertDialog dialog) {
                                
                            }
                        });
                return;
            }
            
            if (SysUtil.getSysSdkCode() < 18) {
                showShortToast("系统版本过低，无法使用旅信网络电话功能");
            } else {
                openActivity(NRAct.class);
            }
        } else {
        }
    }

    private void setTexts()
    {
        handler.post(new Runnable()
        {
            @SuppressLint("ResourceAsColor")
            @Override
            public void run()
            {
                if (Engine.getInstance().getUserInfo() != null)
                {
                	Data data = Engine.getInstance().getUserInfo().getData();
                	PersonalInfo personalInfo = null;
                    if (data != null){
                    	personalInfo = data.getPersonal_info();
                    	
                    }
                    if (personalInfo != null)
                    {
                        if (personalInfo.getNickname().length() > 0)
                        {
                            tvName.setText(personalInfo.getNickname());
                        }
                        else
                        {
                            tvName.setText(Engine.getInstance().getUserName());
                        }

                        if (SpUtil.getVisiting() == 1)
                        {
                            // 旅信漫游状态
                            ivVisitingFlag
                                    .setImageResource(R.drawable.loc_visiting);
                            ivVisitingFlag.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            if (Engine.getInstance().getIsRoaming() == 0)
                            {
                                // 旅信归属状态
                                ivVisitingFlag
                                        .setImageResource(R.drawable.loc_homing);
                                ivVisitingFlag.setVisibility(View.VISIBLE);
                            }
                        }
                        if (Engine.getInstance().getIsRoaming() == 1)
                        {
                            // 运营商漫游状态
                            ivRoamingFlag.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            // 运营商归属状态
                            ivRoamingFlag.setVisibility(View.GONE);
                        }
                    }

                    Data data2 = Engine.getInstance().getUserInfo().getData();
                    if (data2 != null){
                    	String token = data2.getTokenNum();
                    	if (TextUtils.isEmpty(token))
                        {
                            tvContent.setText("我的账号：" + Engine.getInstance()
                                    .getUserName());
                        }
                        else
                        {
                            tvContent.setText("境外临时号：" + token);
                        }
                    }
                    
                }

                if (Engine.getInstance().isHeadImg)
                {
                    Bitmap bitmap = FileUtil.getBitmpHead(Engine.getInstance()
                            .getUserInfo().getData().getPersonal_info()
                            .getHeadPortrait()
                            + "_s");
                    ivHeadImg.setImageBitmap(bitmap);
                }

                if (SpUtil.getNRService() == 0)
                {
                    tvNR.setText("旅信电话：未开通");
                    btnNRRegister.hideRightText();
                }
                else
                {
                    tvNR.setText("旅信电话：" + SpUtil.getNRStart()
                            + "-" + SpUtil.getNREnd());
                    btnNRRegister.showRightText();
                    if(Engine.getInstance().isNRRegisted
                            && Engine.getInstance().isTcpOK)
                    {
                        String layoutTypeKey = PreferencesUtil
                                .getSharedPreferencesStr(MyActivity.this,
                                        PreferencesUtil.PUBLIC_PRERENCES,
                                        ConstantValue.layoutTypeKey);
                        if ("0".equals(layoutTypeKey))
                        {
                            btnNRRegister.setRightHint("在线");
                        }
                        if ("1".equals(layoutTypeKey))
                        {
                            btnNRRegister.setRightHint("勿扰模式");
                        }
                        if ("2".equals(layoutTypeKey))
                        {
                            btnNRRegister.setRightHint("时差模式");
                        }
                    }
                    else
                    {
                        btnNRRegister.setRightHint("离线");
                    }
                }
                
                UserRoamProfile validTrip = UserRoamProfileDBHelper.getInstance().getTripList();
                if (validTrip == null)
                {
                    tvValidOrder.setText("境外卡订单：未订购");
                    tvPos.setVisibility(View.GONE);
                }
                else
                {
                    tvValidOrder.setText("境外卡订单：已开始");
                    tvPos.setVisibility(View.VISIBLE);
                    if (validTrip.getMcc().equals("310"))
                    {
                        tvPos.setText(R.string.America);
                    }
                    else if (validTrip.getMcc().equals("454"))
                    {
                        tvPos.setText(R.string.Hongkong);
                    }
                    else if (validTrip.getMcc().equals("510"))
                    {
                        tvPos.setText(R.string.Indonesia);
                    }
                }
            }
        });
    }

    public class MyTravelrelyReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent)
        {
//            setTexts();
        	setIsLive();
        }
    }

    private void setIsLive(){
    	 if (SpUtil.getNRService() == 0)
         {
             tvNR.setText("旅信电话：未开通");
             btnNRRegister.hideRightText();
         }
         else
         {
             tvNR.setText("旅信电话：" + SpUtil.getNRStart()
                     + "-" + SpUtil.getNREnd());
             btnNRRegister.showRightText();
             if(Engine.getInstance().isNRRegisted
                     && Engine.getInstance().isTcpOK)
             {
                 String layoutTypeKey = PreferencesUtil
                         .getSharedPreferencesStr(MyActivity.this,
                                 PreferencesUtil.PUBLIC_PRERENCES,
                                 ConstantValue.layoutTypeKey);
                 if ("0".equals(layoutTypeKey))
                 {
                     btnNRRegister.setRightHint("在线");
                 }
                 if ("1".equals(layoutTypeKey))
                 {
                     btnNRRegister.setRightHint("勿扰模式");
                 }
                 if ("2".equals(layoutTypeKey))
                 {
                     btnNRRegister.setRightHint("时差模式");
                 }
             }
             else
             {
                 btnNRRegister.setRightHint("离线");
             }}
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.unregisterReceiver(myTravelrelyReceiver);
    }

    /**
     * 获取订单
     */
    private void getMyOrder()
    {
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                // 取全部订单
                Engine.getInstance().queryOrder(MyActivity.this, "3");
                if (Engine.getInstance().getOrderList() == null
                        || Engine.getInstance().getOrderList().size() == 0)
                {
                    Utils.showToast(MyActivity.this,
                            getResources().getString(R.string.tv_no_data));
                    return;
                }

//                Intent intent = new Intent(MyActivity.this,
//                        MyOrderActivity.class);
//                startActivity(intent);
            }
        });
    }
    


    /**
     * 行程信息
     */
    TripInfo tripInfo;

    private void getTripInfo()
    {
        ProgressOverlay pOverlay = new ProgressOverlay(MyActivity.this);
        pOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                String cc = Engine.getInstance().getCC();
                String host = ReleaseConfig.getUrl(cc);
                tripInfo = new TripInfo();
                tripInfo = Engine.getInstance().getTrip(
                        host,
                        Engine.getInstance().getTripInfo());

                if (tripInfo.getResponseInfo().isSuccess())
                {
                    Log.d("", "行程条数="
                            + tripInfo.getData().getlInfoLists().size());
                    Engine.getInstance().getDaylists().clear();
                    Engine.getInstance().saveTripInfo(tripInfo);
                    Engine.getInstance().setTripInfoList(
                            Engine.getInstance().getTripInfo());
                    Engine.getInstance().getDaylists();

                }
                else
                {
                    Utils.showToast(MyActivity.this, tripInfo.getResponseInfo()
                            .getMsg());
                }
            }
        });
    }

    @Override
    public void onTurn()
    {
        // TODO Auto-generated method stub
    }
    
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        if(Engine.getInstance().isNRRegisted){
            setGuideResId(this.getClass().getName(), R.id.layout_my, R.drawable.reg_help_480);
        }
        super.onStart();
    }
}
