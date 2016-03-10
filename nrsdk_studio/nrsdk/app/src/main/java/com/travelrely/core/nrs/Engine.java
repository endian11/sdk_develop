package com.travelrely.core.nrs;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysListAlert;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.app.view.SysListAlert.OnListAlertClickListener;
import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.ProgressOverlay;
import com.travelrely.core.glms.GetMsg.FetchMessage;
import com.travelrely.core.glms.ProgressOverlay.OnProgressEvent;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.core.nrs.ble.BleMsgId;
import com.travelrely.core.nrs.ble.BleService;
import com.travelrely.core.nrs.nr.NRService;
import com.travelrely.core.nrs.nr.msg.AppAgtDtmfInd;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.util.AESUtils;
import com.travelrely.core.util.ArithUtil;
import com.travelrely.core.util.DLoadAllHeadImgTask;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.FileUtil;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.PreferencesUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.TimeUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.core.util.ViewUtil;
import com.travelrely.core.util.FetchTokenOneTask.OnFetchTokenListener;
//import com.travelrely.app.activity.SmsChatListAct;
import com.travelrely.model.Adv;
import com.travelrely.model.CallRecord;
import com.travelrely.model.ContactModel;
import com.travelrely.model.CountryInfo;
import com.travelrely.model.CountryRes;
import com.travelrely.model.Order.PkgType;
import com.travelrely.model.PhoneSection;
import com.travelrely.model.Profile;
import com.travelrely.sdk.R;
import com.travelrely.sdk.SDKAction;
import com.travelrely.v2.Rent.RentService;
import com.travelrely.v2.Rent.msg.AgtRentCalledReq;
import com.travelrely.v2.Rent.msg.RentMsgId;
import com.travelrely.app.db.AdvDBHelper;
import com.travelrely.app.db.CallLogContentProvider;
import com.travelrely.app.db.CallRecordsDBHelper;
import com.travelrely.app.db.ContactDBHelper;
import com.travelrely.app.db.CountryInfoDBHelper;
import com.travelrely.app.db.ExpPriceDbHelper;
import com.travelrely.app.db.PackageDbHelper;
import com.travelrely.app.db.ProfileDBHelper;
import com.travelrely.app.db.ProfileIpDialDBHelper;
import com.travelrely.app.db.ServerIpDbHelper;
import com.travelrely.app.db.SmsEntityDBHelper;
import com.travelrely.app.db.TravelrelyMessageDBHelper;
import com.travelrely.app.db.TripInfoDBHelper;
import com.travelrely.app.db.UserRoamProfileDBHelper;
import com.travelrely.v2.model.Cart;
import com.travelrely.v2.model.Commodity;
import com.travelrely.v2.model.ExpressPrice;
import com.travelrely.v2.model.Package0;
import com.travelrely.v2.model.RoamProfile;
import com.travelrely.v2.model.ServerIp;
import com.travelrely.v2.model.SmsEntity;
import com.travelrely.v2.model.UserRoamProfile;
import com.travelrely.v2.net_interface.GetAdvReq;
import com.travelrely.v2.net_interface.GetAdvRsp;
import com.travelrely.v2.net_interface.GetComAdvReq;
import com.travelrely.v2.net_interface.GetComAdvRsp;
import com.travelrely.v2.net_interface.GetCommStatusReq;
import com.travelrely.v2.net_interface.GetCommStatusRsp;
import com.travelrely.v2.net_interface.GetCountryInfoReq;
import com.travelrely.v2.net_interface.GetCountryInfoRsp;
import com.travelrely.v2.net_interface.GetExpPriceReq;
import com.travelrely.v2.net_interface.GetExpPriceRsp;
import com.travelrely.v2.net_interface.GetHomeProfileReq;
import com.travelrely.v2.net_interface.GetHomeProfileRsp;
import com.travelrely.v2.net_interface.GetMultiTripReq;
import com.travelrely.v2.net_interface.GetMultiTripRsp;
import com.travelrely.v2.net_interface.GetMultiTripRsp.Data.Trip;
import com.travelrely.v2.net_interface.GetNRInfoReq;
import com.travelrely.v2.net_interface.GetNRInfoRsp;
import com.travelrely.v2.net_interface.GetNRPriceReq;
import com.travelrely.v2.net_interface.GetNRPriceRsp;
import com.travelrely.v2.net_interface.GetNRPriceRsp.Data.PackagePrice;
import com.travelrely.v2.net_interface.GetPackagesReq;
import com.travelrely.v2.net_interface.GetPackagesRsp;
import com.travelrely.v2.net_interface.GetPkgList.GetPkgListReq;
import com.travelrely.v2.net_interface.GetPkgList.GetPkgListRsp;
import com.travelrely.v2.net_interface.GetRoamProfileReq;
import com.travelrely.v2.net_interface.GetRoamProfileRsp;
import com.travelrely.v2.net_interface.GetUsrInfoReq;
import com.travelrely.v2.net_interface.GetUsrInfoRsp;
import com.travelrely.v2.net_interface.GetUsrRoamProfileReq;
import com.travelrely.v2.net_interface.GetUsrRoamProfileRsp;
import com.travelrely.v2.net_interface.LoginReq;
import com.travelrely.v2.net_interface.LoginRsp;
import com.travelrely.v2.net_interface.OrderQuery;
import com.travelrely.v2.net_interface.OrderQuery.OrderList;
import com.travelrely.v2.net_interface.OrderQueryReq;
import com.travelrely.v2.response.Balance;
import com.travelrely.v2.response.GetAppVersion;
import com.travelrely.v2.response.GetGroupList;
import com.travelrely.v2.response.GetGroupMsg;
import com.travelrely.v2.response.Response;
import com.travelrely.v2.response.SendMessageResult;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.response.TripInfo;
import com.travelrely.v2.response.TripInfoList;
import com.travelrely.v2.service.LoginService;

public class Engine implements OnFetchTokenListener
{
    private LogHandler mLogHandler;
    public CallRecord currentCallRecord;
    private Context context;

    public boolean isInitiativeLogin;

    /*================新增tripId字段=====================*/
    private int tripIdPos;//用来获取KI 需要上传tripId给服务器（orderType为3）
    private List<String> tripIds = new ArrayList<String>();
    public List<String> getTripIds() {
		return tripIds;
	}

	public void setTripIds(List<String> tripIds) {
		this.tripIds = tripIds;
	}

	public int getTripIdPos(){
    	return this.tripIdPos;
    }
    
    public void setTripIdPos(int tripid){
    	this.tripIdPos = tripid;
    }
    /*=======================END======================*/
    
    public void setContext(Context context)
    {
        this.context = context;
    }

    public Context getContext()
    {
        return context;
    }

    public String mLocation;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public boolean hasUsrName()
    {
        if (userName != null && userName != "")
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public String getLongPswd()
    {
        return longPassword;
    }

    public void setLongPswd(String longPassword)
    {
        this.longPassword = longPassword;
    }

    public boolean hasLongPswd()
    {
        if (longPassword != null && longPassword != "")
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public double mLongitude;

    public double mLatitude;

    public String mImagePath;

    public int mImageType = -1;

    public boolean isLogIn;

    public boolean isHeadImg;

    public static final String CACHE = "/sdcard/RenRenForAndroid/Cache/";

    public static Engine instance;

    private List<Activity> activities = new LinkedList<Activity>();

    public boolean homeLogin = true;

    public String userName;

    public String longPassword;
    
    public String cc = "+86";//根据国家码来设置IP,默认中国

    public String getCC()
    {
        return cc;
    }

    public void setCC(String countryCode)
    {
        this.cc = countryCode;
    }

    //public List<PhoneSection> newAllList;

    public List<OrderList> orderList = new ArrayList<OrderList>();
    public List<OrderList> prepaidOrderList = new ArrayList<OrderList>();
    public List<OrderList> unpaidOrderList = new ArrayList<OrderList>();
    public List<OrderList> finishedOrderList = new ArrayList<OrderList>();

    public String mobile_phone;

    public String fromType;

    String userHead;// 用户头像

    public List<TripInfoList> tripInfoList = new ArrayList<TripInfoList>();

    public List<TripInfoList.Daylist> daylists = new ArrayList<TripInfoList.Daylist>();

    public List<TripInfoList.ActivityList> activityLists = new ArrayList<TripInfoList.ActivityList>();

    public List<PackagePrice> packagePrices = new ArrayList<GetNRPriceRsp.Data.PackagePrice>();//NR价格
    
    public double btBleBoxPrice;//蓝牙盒子价格
    
    public List<PackagePrice> getPackagePrices() {
        return packagePrices;
    }

    public void setPackagePrices(List<PackagePrice> packagePrices) {
        this.packagePrices = packagePrices;
    }

    public double getBtBleBoxPrice() {
        return btBleBoxPrice;
    }

    public void setBtBleBoxPrice(double btBleBoxPrice) {
        this.btBleBoxPrice = btBleBoxPrice;
    }

    public List<TripInfoList> getTripInfoList()
    {
        return tripInfoList;
    }

    public void setTripInfoList(List<TripInfoList> tripInfoList)
    {
        this.tripInfoList = tripInfoList;
    }
//
//    public AMapLocation getaLocation()
//    {
//        return aLocation;
//    }
//
//    public void setaLocation(AMapLocation aLocation)
//    {
//        this.aLocation = aLocation;
//    }

    public String getUserHead()
    {
        return userHead;
    }

    public void setUserHead(String userHead)
    {
        this.userHead = userHead;
    }

    public String getFromType()
    {
        return fromType;
    }

    public void setFromType(String fromType)
    {
        this.fromType = fromType;
    }

    // SIM卡的MCC和MNC
    public String sim_mcc;
    public String sim_mnc;

    public String getSimMcc()
    {
        return sim_mcc;
    }

    public void setSimMcc(String simMcc)
    {
        sim_mcc = simMcc;
    }

    public String getSimMnc()
    {
        return sim_mnc;
    }

    public void setSimMnc(String simMnc)
    {
        sim_mcc = simMnc;
    }

    // 蜂窝网络的MCC和MNC
    public String net_mcc;
    public String net_mnc;
    public int isRoaming;

    // 1 means roaming, 0 means homing
    public int getIsRoaming()
    {
        return isRoaming;
    }

    public void setIsRoaming(int isRoaming)
    {
        this.isRoaming = isRoaming;
    }

    public String getHomeSmcLoc()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("home_smc_loc", "");
    }

    public void setHomeSmcLoc(String homeSmcLoc)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("home_smc_loc", homeSmcLoc);
        e.commit();
    }
    
    public String getHomeGlmsLoc()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("home_glms_loc", "");
    }

    public void setHomeGlmsLoc(String homeGpnsLoc)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("home_glms_loc", homeGpnsLoc);
        e.commit();
    }

    public List<String> getHomeIpDialMethodList()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        ArrayList<String> ip_dial_method_list = new ArrayList<String>();
        int number = sharedPreferences.getInt(
                "home_ip_dial_method_list_number", 0);
        for (int i = 0; i < number; i++)
        {
            ip_dial_method_list.add(sharedPreferences.getString(
                    "home_ip_dial_method_list_" + i, null));
        }
        return ip_dial_method_list;
    }

    public void setHomeIpDialMethodList(List<String> homeIpDialMethodList)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        int number = homeIpDialMethodList.size(); /*
                                                   * homeIpDialMethodList is an
                                                   * array
                                                   */
        e.putInt("home_ip_dial_method_list_number", number);
        for (int i = 0; i < number; i++)
        {
            e.remove("home_ip_dial_method_list_" + i);
            e.putString("home_ip_dial_method_list_" + i,
                    homeIpDialMethodList.get(i));
        }
        e.commit();
    }

    public String getRoamMCC()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("roam_mcc", "");
    }

    public void setRoamMCC(String roamMCC)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("roam_mcc", roamMCC);
        e.commit();
    }

    public String getRoamMNC()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("roam_mnc", "");
    }

    public void setRoamMNC(String roamMNC)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("roam_mnc", roamMNC);
        e.commit();
    }

    public String getRoamGlmsLoc()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("roam_glms_loc", "");
    }

    public void setRoamGlmsLoc(String roamGlmsLoc)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("roam_glms_loc", roamGlmsLoc);
        e.commit();
    }

    public String getRoamSmcLoc()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("roam_smc_loc", "");
    }

    public void setRoamSmcLoc(String roamSmcLoc)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("roam_smc_loc", roamSmcLoc);
        e.commit();
    }

    public List<String> getRoamIpDialMethodList()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        ArrayList<String> ip_dial_method_list = new ArrayList<String>();
        int number = sharedPreferences.getInt(
                "roam_ip_dial_method_list_number", 0);
        for (int i = 0; i < number; i++)
        {
            ip_dial_method_list.add(sharedPreferences.getString(
                    "roam_ip_dial_method_list_" + i, null));
        }
        return ip_dial_method_list;
    }

    public void setRoamIpDialMethodList(List<String> roamIpDialMethodList)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        int number = roamIpDialMethodList.size(); /*
                                                   * roamIpDialMethodList is an
                                                   * array
                                                   */
        e.putInt("roam_ip_dial_method_list_number", number);
        for (int i = 0; i < number; i++)
        {
            e.remove("roam_ip_dial_method_list_" + i);
            e.putString("roam_ip_dial_method_list_" + i,
                    roamIpDialMethodList.get(i));
        }
        e.commit();
    }

    // 购物车
    public String getCart()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        return sharedPreferences.getString("cart", "");
    }

    public void setCart(String strObject)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                userName, Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("cart", strObject);
        e.commit();
    }

	

    public void addToCart(Commodity commodity)
    {
        double totalPrice = 0.0;
        double selTtlPrice = 0.0;

        // 商品列表
        List<Commodity> commodityItems = new ArrayList<Commodity>();
        commodityItems.add(commodity);

        // 获取已存入的购物车商品
        try
        {
            String strObject = Engine.getInstance().getCart();
            Cart cart2 = Cart.deSerialization(strObject);

            commodityItems.addAll(cart2.getCommodityItems());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        for (Commodity c : commodityItems)
        {
            totalPrice = ArithUtil.add(totalPrice, c.getTotalPrice());

            if (c.getSelected())
            {
                selTtlPrice = ArithUtil.add(selTtlPrice, c.getTotalPrice());
            }
        }
        // 购物车
        	
        Cart cart = new Cart();
        cart.setTtlOrderPrice(totalPrice);
        cart.setSelectedOrderPrice(selTtlPrice);
        cart.setCommodityItems(commodityItems);
        cart.setTtlNumber(commodityItems.size()); // 订单中商品数量
        System.out.println("add cart=" + cart);

        try
        {
            String strObject = Cart.serialize(cart);
            Engine.getInstance().setCart(strObject);
            System.out.println("cart strobject" + strObject);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
   /* ==================0301添加===========================*/
    public void addToCartAsList(List<Commodity> comms)
    {
    	double totalPrice = 0.0;
    	double selTtlPrice = 0.0;
    	
    	// 商品列表
    	List<Commodity> commodityItems = new ArrayList<Commodity>();
    	if (comms == null){
    		return ;
    	}else{
    		commodityItems = comms;
    	}
    	// 获取已存入的购物车商品
    	try
    	{
    		String strObject = Engine.getInstance().getCart();
    		Cart cart2 = Cart.deSerialization(strObject);
    		
    		commodityItems.addAll(cart2.getCommodityItems());
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    	catch (ClassNotFoundException e)
    	{
    		e.printStackTrace();
    	}
    	
    	for (Commodity c : commodityItems)
    	{
    		totalPrice = ArithUtil.add(totalPrice, c.getTotalPrice());
    		
    		if (c.getSelected())
    		{
    			selTtlPrice = ArithUtil.add(selTtlPrice, c.getTotalPrice());
    		}
    	}
    	// 购物车
    	
    	Cart cart = new Cart();
    	cart.setTtlOrderPrice(totalPrice);
    	cart.setSelectedOrderPrice(selTtlPrice);
    	cart.setCommodityItems(commodityItems);
    	cart.setTtlNumber(commodityItems.size()); // 订单中商品数量
    	System.out.println("add cart=" + cart);
    	
    	try
    	{
    		String strObject = Cart.serialize(cart);
    		Engine.getInstance().setCart(strObject);
    		System.out.println("cart strobject" + strObject);
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
    	}
    }

    public void selNoneInCart()
    {
        double totalPrice = 0.0;

        // 商品列表
        List<Commodity> commodityItems = new ArrayList<Commodity>();

        // 获取已存入的购物车商品
        try
        {
            String strObject = Engine.getInstance().getCart();
            if (strObject == null || strObject.equals(""))
            {
                return;
            }
            Cart cart2 = Cart.deSerialization(strObject);

            commodityItems.addAll(cart2.getCommodityItems());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        for (Commodity c : commodityItems)
        {
            totalPrice = ArithUtil.add(totalPrice, c.getTotalPrice());

            c.setSelected(false);
        }

        // 购物车
        Cart cart = new Cart();
        cart.setTtlOrderPrice(totalPrice);
        cart.setSelectedOrderPrice(0.0);
        cart.setCommodityItems(commodityItems);
        cart.setTtlNumber(commodityItems.size()); // 订单中商品数量
//        LOGManager.d("add cart=" + cart);

        try
        {
            String strObject = Cart.serialize(cart);
            Engine.getInstance().setCart(strObject);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 需要传递给map的message
     */
    public TraMessage message;

    public String getMobile_phone()
    {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone)
    {
        this.mobile_phone = mobile_phone;
    }

    public List<OrderList> getOrderList()
    {
        return orderList;
    }

    /**所有订单集合（包括已支付的订单、待支付订单和已完成订单）
     * @param orderList
     */
    public void setOrderList(List<OrderList> orderList)
    {
        this.orderList = orderList;

        // 分拣订单之前需要先把各订单情况,避免重复
        prepaidOrderList.clear();
        unpaidOrderList.clear();
        finishedOrderList.clear();

        // 按订单状态分拣订单
        for (int i = 0; i < orderList.size(); i++)
        {
            OrderList order = orderList.get(i);

            if (order.getOrder_status() == 0)//待支付
            {
                unpaidOrderList.add(order);
            }
            else if (order.getOrder_status() == 3)//已完成
            {
                finishedOrderList.add(order);
            }
            else//已支付订单
            {
                prepaidOrderList.add(order);
            }
        }
    }

    public List<OrderList> getPrepaidOrderList()
    {
        return prepaidOrderList;
    }

    public List<OrderList> getUnpaidOrderList()
    {
        return unpaidOrderList;
    }

    public List<OrderList> getFinishedOrderList()
    {
        return finishedOrderList;
    }

    private Engine(Context context)
    {
        this.context = context;
        init();
    }

    public static Engine getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new Engine(context);
        }

        HttpConnector hConnector = new HttpConnector();
        hConnector.initConnectors();
        
        creatAlertConfig(context);
        
        return instance;
    }

    public static Engine getInstance()
    {
        return instance;
    }

    private void init()
    {
        cc = SpUtil.getCC(context);
        userName = SpUtil.getUserName(context);
        longPassword = SpUtil.getLongPswd(context);
        
        HandlerThread mLogThread = new HandlerThread("mLogThread");
        mLogThread.start();
        mLogHandler = new LogHandler(mLogThread.getLooper(), context);
    }

    public void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    // 遍历所有Activity并finish
    public void exit()
    {
        for (Activity activity : activities)
        {
            activity.finish();
        }
        System.exit(0);
    }

    public int getDisplayScreenResolution(Activity activity)
    {
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = activity.getWindowManager()
                .getDefaultDisplay();
        // display.getMetrics(dm);
        // int screenWidth = dm.widthPixels;
        // int screenHeight = dm.heightPixels;
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        LOGManager.d("get resolution:" + screenWidth + " * " + screenHeight);

        SpUtil.setScreenWidth(screenWidth);
        SpUtil.setScreenHeight(screenHeight);

        return 0;
    }

    // 在默认地址上登录一次
    public LoginRsp loginRequest(String url,
            String username, String pswd, final Context c)
    {
        LoginRsp rsp = LoginReq.login(c, url, username, pswd);
        if (rsp == null)
        {
            return null;
        }
        if (!rsp.getBaseRsp().isSuccess())
        {
        	Res.toastErrCode(c, rsp.getBaseRsp().getMsg());
            return rsp;
        }

        SpUtil.setCC(cc);
        SpUtil.setUserName(username);
        
        if (rsp.getData().getServerIpFlag() == 1)
        {
            List<ServerIp> pkgs = rsp.getData().getServerIpList();
            if (pkgs == null || pkgs.size() == 0)
            {
                return rsp;
            }
            ServerIpDbHelper helper = ServerIpDbHelper.getInstance();
            helper.insertAll(pkgs);
        }

        return rsp;
    }

    public GetCommStatusRsp getCommStatusRequest(Context c, String url)
    {
        GetCommStatusRsp rsp = GetCommStatusReq.getCommStatus(c, url);
        if (rsp == null)
        {
            return null;
        }
        if (!rsp.getBaseRsp().isSuccess())
        {
            Res.toastErrCode(c, rsp.getBaseRsp().getMsg());
            return null;
        }

        int isVisiting = rsp.getData().getVisitingFlag();
        SpUtil.setVisiting(isVisiting);

        return rsp;
    }

    public GetHomeProfileRsp getHomeProfileRequest(final Context activity)
    {
        LOGManager.d("******下载配置文件");
        GetHomeProfileRsp rsp = GetHomeProfileReq.getHomeProfile(activity);
        if (rsp == null)
        {
            return null;
        }
        
        if (!rsp.getBaseRsp().isSuccess())
        {
        
            return null;
        }
        
        List<Profile> profiles = rsp.getData().getCarrierList();
        if (profiles == null)
        {
            return null;
        }

        SpUtil.setHomeProfileVer(rsp.getData().getConfigVersion());
        
        List<RoamProfile> profileIp = rsp.getData().getIpDialMethodList();

        saveHomeProfileToDB(profiles, profileIp);

        return rsp;
    }

    public GetRoamProfileRsp getRoamProfileRequest(final Context activity)
    {
        GetRoamProfileRsp rsp = GetRoamProfileReq.getRoamProfile(activity);
        if (rsp == null)
        {
            return null;
        }
        
        if (!rsp.getBaseRsp().isSuccess())
        {
            return null;
        }
        
        List<Profile> profiles = rsp.getData().getCountryList();
        List<RoamProfile> roamprofile = rsp.getData().getIpDialMethodList();
        if (roamprofile == null || roamprofile.size() == 0)
        {
            return null;
        }
        
        SpUtil.setRoamProfileVer(rsp.getData().getConfigVersion());
        
        //ProfileIpDialDBHelper helper = ProfileIpDialDBHelper.getInstance();
        //helper.insertAll(roamprofile);
        
        saveRoamProfileToDB(profiles, roamprofile);

        return rsp;
    }

    public List<CountryInfo> countryInfoList;

    // 从数据库中读取国家运营商信息
    public void getCountryInfoFromDB()
    {
        CountryInfoDBHelper countryInfoDBHelper;

        countryInfoDBHelper = CountryInfoDBHelper.getInstance();
        countryInfoList = countryInfoDBHelper.query();

        for (CountryInfo countryInfo : countryInfoList)
        {
            // 为国家信息准备本地资源
            CountryRes res = new CountryRes();
            if (countryInfo.getMcc().equals("310"))
            {
                res.setFlagId(R.drawable.flag_usa);
                res.setNameId(R.string.America);
                res.setMonetaryUnitId(R.string.dollar);
            }
            else if (countryInfo.getMcc().equals("454"))
            {
                res.setFlagId(R.drawable.flag_hk);
                res.setNameId(R.string.Hongkong);
                res.setMonetaryUnitId(R.string.tv_yuan);
            }
            else if (countryInfo.getMcc().equals("510"))
            {
                res.setFlagId(R.drawable.flag_indonesia);
                res.setNameId(R.string.Indonesia);
                res.setMonetaryUnitId(R.string.rupiah);
            }
            else if (countryInfo.getMcc().equals("520"))
            {
                res.setFlagId(R.drawable.flag_thailand);
                res.setNameId(R.string.Thailand);
                res.setMonetaryUnitId(R.string.baht);
            }
            else if (countryInfo.getMcc().equals("460"))
            {
                res.setFlagId(R.drawable.flag_hk);
                res.setNameId(R.string.china_1);
                res.setMonetaryUnitId(R.string.tv_yuan);
            }
            countryInfo.setRes(res);
        }
    }

    public List<CountryInfo> getCountryInfo()
    {
        return countryInfoList;
    }

    public void getCountryInfoRequest(final Context activity)
    {
        // 解析国家信息
        GetCountryInfoRsp rsp = GetCountryInfoReq.getCountryInfo(activity);
        if (rsp == null)
        {
            LOGManager.d("获取国家信息失败");
            return;
        }

        if (!rsp.getResponseInfo().isSuccess())
        {
            LOGManager.d(rsp.getResponseInfo().getMsg());
            return;
        }
        
        List<CountryInfo> cs = rsp.getData().getCountryInfoList();
        if (cs == null || cs.size() == 0)
        {
            return;
        }

        // 保存国家信息版本号
        SpUtil.setCountryVer(rsp.getData().getCountryInfoVersion());
        
        CountryInfoDBHelper helper = CountryInfoDBHelper.getInstance();
        helper.insertAll(cs);

        return;
    }
    
    public void getPkgRequest(Context mContext)
    {
        GetPkgListRsp rsp = GetPkgListReq.getPkgList(mContext);
        if (rsp == null)
        {
            return;
        }
        
        if (!rsp.getBaseRsp().isSuccess())
        {
            return;
        }
        
        List<Package0> pkgs = rsp.getData().getPackages();
        if (pkgs == null || pkgs.size() == 0)
        {
            return;
        }
        
        SpUtil.setPkgVer(rsp.getData().getVersion());
        
        PackageDbHelper helper = PackageDbHelper.getInstance();
        helper.insertAll(pkgs);
    }
    
    public void getNRInfoRequest(Context mContext)
    {
        GetNRInfoRsp rsp = GetNRInfoReq.getNRInfoProfile(mContext);
        if (rsp == null)
        {
            return;
        }
        
        if (!rsp.getBaseRsp().isSuccess())
        {
            return;
        }
        
        SpUtil.setNRInfoVer(rsp.getData().getVersion());
        SpUtil.setNRService(rsp.getData().getStatus());
        SpUtil.setXHService(rsp.getData().getLxnum_status());
        SpUtil.setNRStart(rsp.getData().getStart_date());
        SpUtil.setNREnd(rsp.getData().getEnd_date());
        String femtoIp = rsp.getData().getFemtoIp();
       
        if(!TextUtils.isEmpty(SpUtil.getNRFemtoIp())){
        	//不是空的
        	if (SpUtil.getNRFemtoIp().equals(femtoIp)){
        		//相等
        	}else{
        		//原来存的femtoip和现在获得的不一样，就保存现在的ip 并断掉原来的连接，开始新的连接
        		SpUtil.setNRFemtoIp(femtoIp);
        	}
        }else{
        	//空的
        	SpUtil.setNRFemtoIp(femtoIp);
        }
        
        SpUtil.setNRTimeZone(rsp.getData().getTimeZone());
        SpUtil.setNRMode(rsp.getData().getNo_disturb_mode());
        SpUtil.setNRNoDisturbBegin(rsp.getData().getNo_disturb_start());
        SpUtil.setNRNoDisturbEnd(rsp.getData().getNo_disturb_end());
    }
    
    public void getExpPriceRequest(Context mContext)
    {
        GetExpPriceRsp rsp = GetExpPriceReq.getExpressPrice(mContext);
        if (rsp == null)
        {
            return;
        }
        
        if (!rsp.getBaseRsp().isSuccess())
        {
            return;
        }
        
        List<ExpressPrice> pkgs = rsp.getData().getExpressPrices();
        if (pkgs == null || pkgs.size() == 0)
        {
            return;
        }
        
        SpUtil.setExpPriceVer(rsp.getData().getExpressPriceVersion());
        
        ExpPriceDbHelper helper = ExpPriceDbHelper.getInstance();
        helper.insertAll(pkgs);
    }

    public boolean getAdvRequest(Context mContext)
    {
        int lateral_resolution = SpUtil.getScreenWidth();
        int axial_resolution = SpUtil.getScreenHeight();

        GetAdvRsp getAdv = GetAdvReq.getAdvInfo(mContext, lateral_resolution,
                axial_resolution);
        if (getAdv == null)
        {
            LOGManager.d("获取广告信息失败");
            return false;
        }

        if (!getAdv.getResponseInfo().isSuccess())
        {
            LOGManager.d(getAdv.getResponseInfo().getMsg());
            return false;
        }

        List<Adv> list = getAdv.getData().getAdv_list();
        if (list == null || list.size() == 0)
        {
            LOGManager.d("获取到的广告信息为空");
            return false;
        }
        
        SpUtil.setAdvVer(getAdv.getData().getAdv_version());

        AdvDBHelper helper = AdvDBHelper.getInstance();
        helper.insertAll(list);

        return true;
    }

    public boolean getComAdvRequest(Context mContext)
    {
        GetComAdvRsp getAdv = GetComAdvReq.getAdvInfo(mContext);
        if (getAdv == null)
        {
            LOGManager.d("获取商业广告信息失败");
            return false;
        }

        if (!getAdv.getResponseInfo().isSuccess())
        {
            LOGManager.d(getAdv.getResponseInfo().getMsg());
            return false;
        }

        List<Adv> list = getAdv.getData().getAdv_list();
        if (list == null || list.size() == 0)
        {
            LOGManager.d("获取到的商业广告信息为空");
            return false;
        }

        return true;
    }

    public GetAppVersion getAppVersionRequest(String url)
    {
        String postData = Request.getAppVersion(userName,
                Utils.getVersion(context));
        String urls = url + "api/config/get_app_version";
        String httpResult = null;
        HttpConnector httpConnector = new HttpConnector();
        httpResult = httpConnector.requestByHttpPut(urls, postData, context,
                false);
        if (httpResult == null || httpResult.equals(""))
        {
            return null;
        }
        GetAppVersion getAppVersion = Response.GetAppVersion(httpResult);

        return getAppVersion;
    }

    // 用户信息
    public GetUsrInfoRsp getUserInfo()
    {
        try
        {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    userName, Context.MODE_PRIVATE);
            String strObject = sharedPreferences.getString("user_info", "");
            if (strObject == "")
            {
                return null;
            }
            GetUsrInfoRsp getUserInfo = GetUsrInfoRsp
                    .deSerialization(strObject);
            return getUserInfo;

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setUserInfo(GetUsrInfoRsp getUserInfo)
    {
        try
        {
            String strObject = GetUsrInfoRsp.serialize(getUserInfo);
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    userName, Context.MODE_PRIVATE);
            Editor e = sharedPreferences.edit();
            e.putString("user_info", strObject);
            e.commit();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // 获得用户信息
    @SuppressLint("NewApi")
    public void getUserInfoRequest(String strUsrName,
            final Context activity)
    {
        GetUsrInfoRsp rsp = GetUsrInfoReq.getUsrInfo(activity, strUsrName);
        if (rsp.getResponseInfo().isSuccess())
        {
            // 保存用户信息
            this.setUserInfo(rsp);

            userHead = rsp.getData().getPersonal_info().getHeadPortrait();
            
            if (!userHead.isEmpty())
            {
                isHeadImg = true;
                Log.d("", "有头像");
                if (FileUtil.fileIsExists(userHead))
                {
                    Log.d("", "无新头像");
                }
                else
                {
                    getUserHeadImg(userHead, "_s");
                    Log.d("", "有新头像");
                }
            }
            else
            {
                isHeadImg = false;
                Log.d("", "无头像");
            }

            getBalanceRequest(activity);
            mobile_phone = rsp.getData().getPersonal_info().getMobilePhone();
            Log.d("", "获得用户信息成功");
        }
        else
        {
            String msg = rsp.getResponseInfo().getMsg();
            Log.d("", "获得用户信息失败 = " + msg);
        }
    }

    public String getString(int res)
    {
        return context.getResources().getString(res);
    }

    @Override
    public void onSucess()
    {
        Intent intent = new Intent();
        intent.setAction(IAction.CONTACT_CHANGED);
        intent.putExtra(IAction.BOOLEAN_EXTRA_DOWNLOAD_HEAD,
                true);
        context.sendBroadcast(intent);
    }

    @Override
    public void onFail()
    {
        // TODO Auto-generated method stub
    }

 
    /**查询订单
     * @param mContext
     * @param state 分类查询订单 1-待付费 0-已付费和正在执行 2-已完成 3-全部订单
     */
    public void queryOrder(Context mContext, String state)
    {
        OrderQuery rsp = OrderQueryReq.queryOrder(mContext, state);
        if (rsp == null)
        {
            Utils.showToast((Activity) mContext, mContext.getResources()
                    .getString(R.string.errorNetwork2));
            return;
        }

        if (!rsp.getResponseInfo().isSuccess())
        {
            String msg = rsp.getResponseInfo().getMsg();
            LOGManager.d(msg);
            return;
        }
        
        setOrderList(rsp.getData().getOrderLists());
        
        /**把 rsp.getData().getOrderLists()保存在文件中*/
        
        
        
        getBalanceRequest(mContext);
    }

    /**
     * 获取账户余额
     */
    public void getBalanceRequest(Context mContext)
    {
        String url = ReleaseConfig.getUrl(cc) + "api/money/balance";
        String username = Engine.getInstance().getUserName();
        String postdata = Request.getBalance(username);

        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPut(url, postdata,
                mContext, true);
        if (httpResult == null || httpResult.equals(""))
        {
            return;
        }

        Balance balance = Response.getBalance(httpResult);
        if (balance == null)
        {
            return;
        }

        if (balance.getResponseInfo().isSuccess())
        {
            SpUtil.setBalance(balance.getData().getBalance());
        }
        else
        {
            String msg = balance.getResponseInfo().getMsg();
            LOGManager.d(msg);
        }
    }

    /**
     * 账户余额支付
     */
//    public void gotoPayment(String id, int payment_mode, int currency_unit,
//            String total_fee, final Activity activity)
//    {
//
//        String url = ReleaseConfig.getUrl(cc) + "api/order/payment";
//        String username = Engine.getInstance().getUserName();
//        String postdata = Request.getPayment(username, id, payment_mode,
//                currency_unit, total_fee);
//
//        HttpConnector httpConnector = new HttpConnector();
//        String httpResult = httpConnector.requestByHttpPut(url, postdata,
//                activity, true);
//        if (httpResult == null || httpResult.equals(""))
//        {
////            activity.showShortToast(activity.getResources().getString(
////                    R.string.errorNetwork2));
//            return;
//        }
//
//        Payment payment = Response.getPayment(httpResult);
//        if (payment.getResponseInfo().isRet())
//        {
//            SpUtil.setBalance(
//                    Double.valueOf(payment.getData().getOrder().getBalance()));
////            Intent intent = new Intent(activity, PayOkActivity.class);
////            Bundle b = new Bundle();
////            b.putInt("ACTION", 1);
////            intent.putExtras(b);
////            activity.startActivity(intent);
//            // TODO cwj
//        }
//        else
//        {
//            String strMsg = payment.getResponseInfo().getMsg();
//            LOGManager.d(strMsg);
//
//            int retCode = payment.getResponseInfo().getRet();
//            int errCode = payment.getResponseInfo().getErrCode();
//            if (retCode < Res.networkErrCode.length
//                    && errCode < Res.networkErrCode[retCode].length)
//            {
//                strMsg = getString(Res.networkErrCode[retCode][errCode]);
//            }
//            else
//            {
//                strMsg = getString(R.string.unknownNetErr);
//            }
//            activity.showShortToast(strMsg);
//        }
//    }
    
    public List<PhoneSection> allSection;
    public List<PhoneSection> traSection;
    public List<PhoneSection> speSection;
    public List<ContactModel> allContacts;
    public List<ContactModel> traContacts;
    public List<ContactModel> speContacts;
    public boolean isContactReady = false;
    // add by wangqijun 优化通讯录同步
    public synchronized void syncContactThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
            	System.out.println("SpUtils.getContactStatus :  " + SpUtil.getContactStatus());
                switch (SpUtil.getContactStatus())
                {
                    case Res.CONTACT_NULL:
                        boolean rslt = Utils.importContacts();
                        if (rslt)
                        {
                            SpUtil.setContactStatus(Res.CONTACT_SYNC);

                            // 下载头像
                            new DLoadAllHeadImgTask().execute();
                        }
                        break;
                        
                    case Res.CONTACT_SYNC:
                    case Res.CONTACT_DLOAD_HEAD:
                        break;

                    default:
                        break;
                }
                
                Intent intent = new Intent();
                intent.setAction(IAction.CONTACT_CHANGED);
                context.sendBroadcast(intent);
                
                ContactDBHelper helper = ContactDBHelper.getInstance();
               
                // 取出旅信用户
                speContacts = helper.getTravelrelyContacts();
                
                speSection = Utils
                        .changeContactList2PhoneSections(traContacts, null);

                // 转换成显示形式
                ContactModel groupItem = new ContactModel();
                groupItem.setContactType(ContactModel.TRAVELRELY_GROUP);

                ContactModel traService = new ContactModel();
                traService.setContactType(ContactModel.TRAVELRELY_SERVICE_NUM);

                ContactModel comService = new ContactModel();
                comService.setContactType(ContactModel.PUBLIC_SERVICE_NUM);
                
                traContacts = new ArrayList<ContactModel>();
                traContacts.add(groupItem);
                traContacts.add(traService);
                traContacts.add(comService);
                traContacts.addAll(speContacts);
                
                // 取更新后的数据
                allContacts = helper.getAllContacts();
                
                allSection = Utils
                        .changeContactList2PhoneSections(allContacts, null);
                traSection = Utils
                        .changeContactList2PhoneSections(traContacts, null);
                
                isContactReady = true;
            }
        }).start();
    }
    
    public void syncCommStatusThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String url = "";
                GetCommStatusRsp rsp = getCommStatusRequest(context, url);
                syncProfilesThread(rsp);
            }
        }).start();
    }

    public void syncProfilesThread(final GetCommStatusRsp rsp)
    {
        if (rsp == null)
        {
            return;
        }
        
        if (rsp.getData().getHPChangeFlag() == 1)
        {
            //syncHPThread();
            getHomeProfileRequest(context);
        }
        
        if (rsp.getData().getRPChangeFlag() == 1)
        {
            //syncRPThread();
            getRoamProfileRequest(context);
        }
        
        if (rsp.getData().getNRInfoChangeFlag() == 1)
        {
            getNRInfoRequest(context);
        }
        //套餐列表有更新的时候 更新套餐列表
        if (rsp.getData().getPkgChangeFlag() == 1){
        	getPkgRequest(context);
        }
        
        // 暂时放开NR业务
        //SpUtil.setNRService(1);
        
        // 暂时放开xiaohao业务
        //SpUtil.setXHService(1);
        
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if (rsp.getData().getAdvChangeFlag() == 1)
                {
                    //syncAdvThread();
                }
                
                if (rsp.getData().getCountryChangeFlag() == 1)
                {
                    //syncCountryInfoThread();
                    getCountryInfoRequest(context);
                }
                
                if (rsp.getData().getPkgChangeFlag() == 1)
                {
                    //syncPkgThread();
                    getPkgRequest(context);
                }

                if (rsp.getData().getEpChangeFlag() == 1)
                {
                    //syncExpressPrice();
                    getExpPriceRequest(context);
                }
                
                if(rsp.getData().getUserroaminfo_update_flag() == 1)
                {
                    getUserRoamProfileRequest(context);
                }
            }
        }).start();
    }

    public void syncHPThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getHomeProfileRequest(context);
            }
        }).start();
    }
    
    public void syncRPThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getRoamProfileRequest(context);
            }
        }).start();
    }
    
    public void syncAdvThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getAdvRequest(context);
            }
        }).start();
    }
    
    public void syncCountryInfoThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getCountryInfoRequest(context);
            }
        }).start();
    }
    
    public void syncPkgThread()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getPkgRequest(context);
            }
        }).start();
    }

    /**
     * 加载快递价格
     */
    public void syncExpressPrice()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                getExpPriceRequest(context);
            }
        }).start();
    }
    
    public void syncLog(String log)
    {
        mLogHandler.procMsg(1, log);
    }
    
    public void syncLog()
    {
        mLogHandler.procMsg(2, null);
    }

    public Bitmap getDefaultBitmap()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_icon);
        return bitmap;
    }

    public Bitmap getHeadDefaultBitmap()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_icon);
        return bitmap;

    }

    public Bitmap getAdvDefaultBitmap()
    {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.adv1);
        return bitmap;

    }

    /**
     * 看是否打开聊天界面，且正在和消息发送者聊天
     * 
     * @return
     */

//    public ChatMsgListAct sendMessage;

//    public boolean isTalking(TraMessage message, SmsEntity tSms, String activity)
//    {
//        ActivityManager manager = (ActivityManager) context
//                .getSystemService(Context.ACTIVITY_SERVICE);
//        List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
//        RunningTaskInfo cinfo = runningTasks.get(0);
//        ComponentName component = cinfo.topActivity;
//        LOGManager.d(component.getShortClassName());
//        if (component.getShortClassName().contains(activity))
//        {
//            if (sendMessage != null)
//            {
//                if (message.getFrom().equals(sendMessage.message.getFrom()))
//                {
//                    return true;
//                }
//            }
//            if(tSms != null){
//                if(SmsChatListAct.from != null){
//                    if(tSms.getAddress().equals(SmsChatListAct.from)){
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    public boolean m_bKeyRight = true;

    // 从数据库中读取归属地的配置信息
    public void getHomeProfileFromDB(String mcc, String mnc)
    {
        
        String dfGlmsIP = ReleaseConfig.getUrl(cc).replace("http://", "").replace("/", "").trim();
        String dfSmcIP = ReleaseConfig.getUrl(cc).replace("http://", "").replace("/", "").trim();
        
        ProfileDBHelper profileDBHelper = ProfileDBHelper.getInstance();
        Profile profile = profileDBHelper.findByMccAndMnc(mcc, mnc, "1");
        if (profile == null)
        {
            // 未找到mcc/mnc对应的home profile，则用默认的SMC_LOCdwqedd
            this.setHomeSmcLoc(dfSmcIP);
            System.out.println("dfSmcIp:------------------ -  " + dfSmcIP);
            this.setRoamGlmsLoc(dfGlmsIP);
            return;
        }
        this.setHomeSmcLoc(profile.getSmc_loc());
        System.out.println("**********************" + profile.getSmc_loc()+"****************************");
        if(TextUtils.isEmpty(profile.getGlms_loc())){
            this.setHomeGlmsLoc(dfGlmsIP);
        }else{
            this.setHomeGlmsLoc(profile.getGlms_loc());
        }
        
        ProfileIpDialDBHelper profileIpDialDBHelper = ProfileIpDialDBHelper
                .getInstance();
        List<RoamProfile> profileIpDialList = profileIpDialDBHelper.query(
                mcc, mnc, "1");
        ArrayList<String> home_ip_dial_method_list = new ArrayList<String>();
        for (int i = 0; i < profileIpDialList.size(); i++)
        {
            home_ip_dial_method_list.add(profileIpDialList.get(i)
                    .getIp_dial_number());
        }
        this.setHomeIpDialMethodList(home_ip_dial_method_list);
    }

    // 向数据库中插入或更新归属地的配置信息
    public void saveHomeProfileToDB(List<Profile> profiles, List<RoamProfile> ip)
    {
        ProfileDBHelper helper1 = ProfileDBHelper.getInstance();
        if (profiles != null)
        {
            for (int i = 0; i < profiles.size(); i++)
            {
                Profile profile = profiles.get(i);
                profile.setIs_homing("1");
                helper1.insertOrUpdate(profile);
            }
        }
        
        // ip dial method list
        ProfileIpDialDBHelper helper2 = ProfileIpDialDBHelper.getInstance();
        if (ip != null)
        {
            for (int j = 0; j < ip.size(); j++)
            {
                RoamProfile profileIpDial = ip.get(j);
                profileIpDial.setIs_homing("1");
                helper2.insertOrUpdate(profileIpDial);
            }
        }

        getHomeProfileFromDB(this.sim_mcc, this.sim_mnc);
    }

    // 从数据库中读取漫游地的配置信息
    public void getRoamProfileFromDB(String mcc, String mnc)
    {
        ProfileDBHelper profileDBHelper = ProfileDBHelper.getInstance();
        Profile profile = profileDBHelper.findByMccAndMnc(mcc, mnc, "0");
        if (profile == null)
        {
            return;
        }
        this.setRoamMCC(profile.getMcc());
        this.setRoamMNC(profile.getMnc());
        this.setRoamGlmsLoc(profile.getGlms_loc());
        this.setRoamSmcLoc(profile.getSmc_loc());
        
        ProfileIpDialDBHelper profileIpDialDBHelper = ProfileIpDialDBHelper
                .getInstance();
        List<RoamProfile> profileIpDialList = profileIpDialDBHelper.query(
                mcc, mnc, "0");
        ArrayList<String> roam_ip_dial_method_list = new ArrayList<String>();
        for (int i = 0; i < profileIpDialList.size(); i++)
        {
            roam_ip_dial_method_list.add(profileIpDialList.get(i)
                    .getIp_dial_number());
        }
        this.setRoamIpDialMethodList(roam_ip_dial_method_list);
    }

    // 向数据库中插入或更新漫游地的配置信息
    public void saveRoamProfileToDB(List<Profile> profiles, List<RoamProfile> ip)
    {
        ProfileDBHelper helper1 = ProfileDBHelper.getInstance();
        if (profiles != null)
        {
            for (int i = 0; i < profiles.size(); i++)
            {
                Profile profile = profiles.get(i);
                profile.setIs_homing("0");
                helper1.insertOrUpdate(profile);
            }
        }
        
        // ip dial method list
        ProfileIpDialDBHelper helper2 = ProfileIpDialDBHelper.getInstance();
        if (ip != null)
        {
            for (int j = 0; j < ip.size(); j++)
            {
                RoamProfile profileIpDial = ip.get(j);
                profileIpDial.setIs_homing("0");
                helper2.insertOrUpdate(profileIpDial);
            }
        }

        getRoamProfileFromDB(this.sim_mcc, this.sim_mnc);
    }

    //public List<Adv> adv_list = new ArrayList<Adv>();

    // 从数据库中读取广告
    public List<Adv> getAdvFromDB()
    {
        AdvDBHelper advDBHelper = AdvDBHelper.getInstance();
        List<Adv> adv_list = advDBHelper.query();
        if (adv_list == null)
        {
        }
        return adv_list;
    }

    /**
     * 判断是归属地还是漫游地
     */
    public void judgeHomeOrRoam()
    {
        DeviceInfo deviceInfo = DeviceInfo.getInstance((Application) context);
        this.sim_mcc = deviceInfo.sim_mcc;
        this.sim_mnc = deviceInfo.sim_mnc;
        LOGManager.d("deviceInfo sim_mcc,mnc=" + sim_mcc + "," + sim_mnc);

        this.net_mcc = deviceInfo.net_mcc;
        this.net_mnc = deviceInfo.net_mnc;
        LOGManager.d("deviceInfo net_mcc,mnc=" + net_mcc + "," + net_mnc);

        if (sim_mcc != null && sim_mcc.equals("") == false && net_mcc != null
                && net_mcc.equals("") == false
                && sim_mcc.equals(net_mcc) == false)
        {
            isRoaming = 1; // 运营商视角的漫游
        }
        else
        {
            isRoaming = 0; // 运营商视角的非漫游，即在归属地
        }

        int isVisiting = SpUtil.getVisiting();
        if (isVisiting == 1)
        {
            homeLogin = false; // 漫游地登录
        }
        else
        {
            homeLogin = true; // 归属地登录
        }
        LOGManager.d("isVisiting, isRoaming, homeLogin = " + isVisiting + ","
                + isRoaming + "," + homeLogin);
    }

    /**
     * 获取账号头像
     */
    public void getUserHeadImg(String headImgName, String type)
    {

        String urlStr = ReleaseConfig.getUrl(cc) + "api/group/download";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(headImgName + type + ".jpg");
            ds.flush();
            InputStream is = con.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            FileUtil fileUtil = new FileUtil(context);
            fileUtil.saveUserHeadImg(headImgName, type, bitmap);

            bitmap.recycle();
            ds.close();
        }
        catch (Exception e)
        {
        }
    }

    public Bitmap headBitmap;

    public Bitmap readUserHeadBitmap(Context context)
    {
        if (headBitmap == null || headBitmap.isRecycled())
        {
            String s = getUserInfo().getData().getPersonal_info()
                    .getHeadPortrait();

            Bitmap bitmap = FileUtil.readUserHeadImg(getUserInfo().getData()
                    .getPersonal_info().getHeadPortrait()
                    + "_s");
            Bitmap small = Utils.headBitmap(bitmap);

            if (small == bitmap)
            {
            }
            else
            {
                bitmap.recycle();
            }
            headBitmap = small;
        }
        return headBitmap;
    }

    /**
     * download消息头像
     * 
     * @param mContext
     * @param message
     * @param type
     */
    public void downloadHeadImg(Context mContext, TraMessage message,
            String type)
    {
        String urlStr = ReleaseConfig.getUrl(cc) + "api/group/download";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(message.getHead_portrait() + type + ".jpg");
            ds.flush();
            InputStream is = con.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            FileUtil fileUtil = new FileUtil(mContext);
            fileUtil.saveHeadImg(message, bitmap);

            bitmap.recycle();
            ds.close();
        }
        catch (Exception e)
        {
        }
    }

    /**
     * download消息头像
     * 
     * @param mContext
     * @param imgPath
     * @param type
     */
    public String downloadHeadImg(Context mContext, String imgPath, String type)
    {
        String pathName = null;
        String urlStr = ReleaseConfig.getUrl(cc) + "api/group/download";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(imgPath + type + ".jpg");
            ds.flush();
            InputStream is = con.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (bitmap == null)
            {
                LOGManager.e("下载图片失败！");
            }
            else
            {
                FileUtil fileUtil = new FileUtil(mContext);
                pathName = fileUtil.saveHeadImg(imgPath, bitmap);
                bitmap.recycle();
            }

            ds.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return pathName;
    }

    public boolean downloadBigHeadImg(Context mContext, String path)
    {

        String urlStr = ReleaseConfig.getUrl(cc) + "api/group/download";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(path);
            ds.flush();
            InputStream is = con.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            if (bitmap != null)
            {
                FileUtil fileUtil = new FileUtil(mContext);
                fileUtil.saveBitmap(bitmap, "head_img", path + ".jpg");
                return true;
            }
            ds.close();
        }
        catch (Exception e)
        {
        }
        return false;
    }

    public String uploadImg(String strUrl, final Bitmap bitmap, File file)
    {
        String end = "\r\n";
        String Hyphens = "--";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";
        
        
        String PREFIX = "--", LINE_END = "\r\n";  
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成  
        final String CHARSET = "utf-8"; // 设置编码  
        
        try
        {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            
            if(bitmap != null){
                ds.writeBytes(Hyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; name=\"msgfile\"; filename=\"bi0503.jpg\";Context-Type:image/jpeg"
                        + end);
                ds.writeBytes(end);
                bitmap.compress(CompressFormat.JPEG, 100, ds);
                ds.writeBytes(end);
                ds.writeBytes(Hyphens + boundary + Hyphens + end);
                ds.flush();
                
                InputStream is = con.getInputStream();
                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1)
                {
                    b.append((char) ch);
                }
                System.out.println("b=" + b);
                ds.close();
                if(bitmap.isRecycled()){
                    bitmap.recycle();
                }
                return b.toString();
            }
            
            if(file != null){
  
                StringBuffer sb = new StringBuffer();  
                sb.append(PREFIX);  
                sb.append(BOUNDARY);  
                sb.append(LINE_END);  
                sb.append("Content-Disposition: form-data; name=\"msgfile\"; filename=\""  
                        + file.getName() + "\"" + LINE_END);  
                sb.append("Content-Type: application/octet-stream; charset="  
                        + CHARSET + LINE_END);  
                sb.append(LINE_END);  
                ds.write(sb.toString().getBytes()); 
                InputStream is = new FileInputStream(file);  
                byte[] bytes = new byte[1024];  
                int len = 0;  
                while ((len = is.read(bytes)) != -1) {  
                    ds.write(bytes, 0, len);  
                }  
                is.close();
                ds.write(LINE_END.getBytes());  
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)  
                        .getBytes();  
                ds.write(end_data);  
                ds.flush(); 
                int res = con.getResponseCode();  
                if (res == 200) {  
                    return String.valueOf(res);  
                } 
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean uploadLog(File file)
    {
        if (file == null)
        {
            return false;
        }

        String urlStr = ReleaseConfig.getUrl(cc)
                + "api/user/upload_app_log";
        
        final String PREFIX = "--", LINE_END = "\r\n";
        final String BOUNDARY = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";
        final String CHARSET = "utf-8"; // 设置编码  
        
        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + BOUNDARY);
            
            /* 设置DataOutputStream */
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());

            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            
//            sb.append("Content-Disposition: form-data; name=\"msgfile\"; filename=\""  
//                    + file.getName() + "\"" + LINE_END);
            sb.append("Content-Disposition: form-data; name=\"msgfile\"; filename=\""  
            		+ Engine.getInstance().getUserName() + "\"" + LINE_END);

            sb.append("Content-Type: application/octet-stream; charset="  
                    + CHARSET + LINE_END);
            sb.append(LINE_END);

            dos.write(sb.toString().getBytes());

            InputStream is = new FileInputStream(file);

            /* 设置每次写入1024bytes */
            byte[] buf = new byte[1024];

            /* 从文件读取数据至缓冲区 */ 
            int len = -1;  
            while ((len = is.read(buf)) != -1)
            {  
                dos.write(buf, 0, len);
            }
            is.close();
            
            dos.writeBytes(LINE_END);

            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + LINE_END);
            dos.flush();
            dos.close();

            return con.getResponseCode() == 200;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断是不是好友
     * 
     * @param userName
     * @return
     */
    public boolean isTraveRely(String userName)
    {
        ContactDBHelper contactDatabaseHelper = ContactDBHelper
                .getInstance();
        List<ContactModel> srcTravelrelyList = contactDatabaseHelper
                .getTravelrelyContacts();

        for (ContactModel model : srcTravelrelyList)
        {
            if (model.getTravelrelyNumber() != null)
            {
                if (model.getTravelrelyNumber().getValue().equals(userName))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取行程
     */
    public TripInfo getTrip(String url, List<TripInfoList> lInfoLists)
    {
        String postData = Request.getTripInfo(Engine.getInstance()
                .getUserName(), lInfoLists);
        String urls = url + "api/trip/get_trip_info";
        String httpResult = null;
        HttpConnector httpConnector = new HttpConnector();
        httpResult = httpConnector.requestByHttpPut(urls, postData, context,
                false);
        if (httpResult == null || httpResult.equals(""))
        {
            return null;
        }
        TripInfo getTripInfo = Response.getTripInfo(httpResult);

        return getTripInfo;
    }

    /**
     * 保存行程
     */
    public void saveTripInfo(TripInfo tripInfo)
    {
        List<TripInfoList> tList = tripInfo.getData().getlInfoLists();
        TripInfoDBHelper tripInfoDBHelper = TripInfoDBHelper.getInstance();

        if (tList != null)
        {
            for (int i = 0; i < tList.size(); i++)
            {
                Log.d("", "行程天数=" + tList.get(i).getDaylists().size());
                tripInfoDBHelper.addTripInfo(tList.get(i));
            }
        }
    }

    /**
     * 取行程
     */
    public List<TripInfoList> getTripInfo()
    {
        List<TripInfoList> tInfoLists = new ArrayList<TripInfoList>();

        TripInfoDBHelper tripInfoDBHelper = TripInfoDBHelper.getInstance();
        tInfoLists = tripInfoDBHelper.getTripInfoList();

        return tInfoLists;
    }

    public List<TripInfoList.Daylist> getDaylists()
    {
        TripInfoDBHelper tripInfoDBHelper = TripInfoDBHelper.getInstance();
        if (getTripInfoList().size() > 0)
        {
            for (int i = 0; i < getTripInfoList().size(); i++)
            {
                tripInfoDBHelper.getDayList(getTripInfoList().get(i)
                        .getTripid());

                activities.clear();
                for (int j = 0; j < getTripInfoList().get(i).getDaylists()
                        .size(); j++)
                {
                    tripInfoDBHelper.getActivityList(getTripInfoList().get(i)
                            .getDaylists().get(j).getDate());
                }
            }
        }
        return daylists;
    }

    /**
     * 保存收藏行程
     */
    public void saveCollection(String fileName, Object object)
    {
        FileUtil fileUtil = new FileUtil(context);
        try
        {
            fileUtil.save(fileName, object);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void readCollection(String fileName)
    {
        FileUtil fileUtil = new FileUtil(context);
        try
        {
            fileUtil.readObject(fileName);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 用来发送消息及回应 比如：发送签到及签到回应
     * 
     * @param message
     */
    public void sendMessageInBackground(final Context mContext,
            final TraMessage message)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String smc_loc = null;
                int linkSource;
                if (Engine.getInstance().homeLogin == true)
                {
                    smc_loc = Engine.getInstance().getHomeSmcLoc();
                    linkSource = 0;
                    System.out.println("homeLogin == true)smc的地址：================" + smc_loc+ "        ========");
                }
                else
                {
                    smc_loc = Engine.getInstance().getRoamSmcLoc();
                    linkSource = 1;
                    
                    System.out.println("homeLogin == false)smc的地址：================" + smc_loc+ "        ========");
                }
                HttpConnector httpConnector = new HttpConnector();
                String url = "http://" + smc_loc + "/" + "api/message/send";
                System.out.println("smc的地址：================" + url+ "        ========");

                String postdata = Request.sendMessage(Engine.getInstance()
                        .getUserName(), message, linkSource);
                String httpResult = httpConnector.requestByHttpPut(url,
                        postdata, mContext, true);

                if (httpResult != null)
                {
                    SendMessageResult sResult = new SendMessageResult();
                    sResult.setValue(httpResult);

                    if (sResult != null)
                    {
                        if (sResult.getResponseInfo().isRet())
                        {
                            updateMessageCode(message,
                                    TraMessage.SEND_MSG_CODE_OK);// 发送成功
                            sendBroad(mContext);
                        }
                        else
                        {
                            updateMessageCode(message,
                                    TraMessage.SEND_MSG_CODE_NO);// 发送失败
                            sendBroad(mContext);
                        }
                    }
                }
                else
                {
                    updateMessageCode(message, TraMessage.SEND_MSG_CODE_ERROR);// 发送失败，网络异常
                    sendBroad(mContext);
                }
            }
        }).start();
    }

    /**
     * 用来更新消息发送状态
     */
    public void updateMessageCode(TraMessage message, String strCode)
    {
        TravelrelyMessageDBHelper tHelper = TravelrelyMessageDBHelper
                .getInstance();
        tHelper.updateContext(message.getTo(), message.getContent(), "content",
                "code", strCode);
    }

    /**
     * 发广播刷新消息列表
     */
    public void sendBroad(Context mContext)
    {
        Intent intent = new Intent();
        intent.setAction(IAction.TRA_MSG_ITEM);
        mContext.sendBroadcast(intent);
    }

    /**
     * 获取群信息
     */
    public GetGroupMsg getGroupMsg(Context mContext, String from, int version)
    {
        HttpConnector httpConnector = new HttpConnector();
        String urls = ReleaseConfig.getUrl(cc) + "api/group/get";
        String postData = Request.getGroupMsg(version, from);
        String httpResult = httpConnector.requestByHttpPut(urls, postData,
                mContext, true);

        if (httpResult != null)
        {
            GetGroupMsg getGarriers = Response.getGroupMsg(httpResult);
            return getGarriers;
        }
        else
        {
            return null;
        }
    }

    /**
     * 用来保存升级dialog提醒状态
     */
    public void saveUpdateType(String str)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "update_type", Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putString("on_off", str);
        e.commit();
    }

    public String getUpdateType()
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "update_type", Context.MODE_PRIVATE);
        return sharedPreferences.getString("on_off", "");
    }

    public List<Trip> tripList;

    public void getMultiTripRequest(Context mContext)
    {
        if (tripList != null)
        {
            tripList.clear();
        }

        GetMultiTripRsp rsp = GetMultiTripReq.getMultiTrip(mContext);
        if (rsp == null)
        {
            Utils.showToast((Activity) mContext, mContext.getResources()
                    .getString(R.string.errorNetwork2));
            return;
        }

        if (!rsp.getResponseInfo().isSuccess())
        {
            String msg = rsp.getResponseInfo().getMsg();
            LOGManager.d(msg);
            return;
        }

        if (rsp.getData().getTrips() == null
                || rsp.getData().getTrips().size() == 0)
        {
            return;
        }

        tripList = rsp.getData().getTrips();
    }

    public int getDpValue(int value)
    {
        return ViewUtil.dip2px(context, value);
    }

    /**
     * 用来保存消息置顶状态
     */
    public void saveIsMsgTopType(String str_key, int type)
    {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getUserName() + "msg_top_type", Context.MODE_PRIVATE);
        Editor e = sharedPreferences.edit();
        e.putInt(str_key, type);
        e.commit();
    }

    public int getIsMsgTopType(String str_key)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getUserName() + "msg_top_type", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(str_key, 0);
    }

    /**
     * 用来解决换手机或者删除app重新安装后，不知道群主是谁的问题
     */
    public GetGroupList getGroupList(Context mContext)
    {

        HttpConnector httpConnector = new HttpConnector();
        String urls = ReleaseConfig.getUrl(cc) + "api/group/getgrouplist";
        String postData = Request.getGroupList();
        String httpResult = httpConnector.requestByHttpPut(urls, postData,
                mContext, true);

        if (httpResult == null || httpResult.equals(""))
        {
            return null;
        }

        GetGroupList getGarriers = Response.getGroupList(httpResult);
        return getGarriers;
    }
    
    public void broadcast(String action)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }
    
    public void broadcast(String action, String s)
    {
        Intent intent = new Intent(action);
        intent.putExtra(IntentMsg.INTENT_STR_MSG, s);
        context.sendBroadcast(intent);
    }
    
    public void broadcast(String action, long l)
    {
        Intent intent = new Intent(action);
        intent.putExtra(IntentMsg.INTENT_LONG, l);
        context.sendBroadcast(intent);
    }
    
    public void broadcast(String action, int i)
    {
        Intent intent = new Intent(action);
        intent.putExtra(IntentMsg.INTENT_ID, i);
        context.sendBroadcast(intent);
    }
    
    public void broadcast(String action, int id, String msg)
    {
        Intent intent = new Intent(action);
        intent.putExtra(IntentMsg.INTENT_ID, id);
        intent.putExtra(IntentMsg.INTENT_STR_MSG, msg);
        context.sendBroadcast(intent);
    }
    
    public void broadcast(String action, BluetoothDevice dev)
    {
        Intent intent = new Intent(action);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, dev);
        context.sendBroadcast(intent);
    }
    
    public void broadcastSmsEntity(String action, SmsEntity smsEntity) {
    	Intent intent = new Intent(action);
        intent.putExtra(Constant.SMS_ENTITY_INTENT_KEY, smsEntity);
        context.sendBroadcast(intent);
    }
    
    public void broadcastNotifiy(String action, TraMessage traMessage) {
    	Intent intent = new Intent(action);
        intent.putExtra(Constant.TRA_ENTITY_INTENT_KEY, traMessage);
        context.sendBroadcast(intent);
    }
    
    // NoRoaming开关默认打开
    public boolean isNRSwitchOn = true;
    
    // TCP是否连接成功的标志
    public boolean isTcpOK = false;

    // NoRoaming是否注册成功的标志
    public boolean isNRRegisted = false;

    // NoRoaming是否处于验证状态
    public boolean isOnVerify = false;
    
    // NoRoaming是否开启加密
    public boolean isEncEnable = false;
    public boolean isEncNeed = true;

    // APP主叫
    public void startCall(Context mContext, String num)
    {
        startNRService(mContext, MsgId.APP_CALLING_REQ, num);
    }

    /**
     * OPT 发送信息
     * 
     * @param
     */
//    public void sendSMS(Context mContext, String receiver, String sms)
//    {
//        if (receiver == null || receiver.equals("") || sms == null
//                || sms.equals(""))
//        {
//            LOGManager.e("短信内容为空");
//            return;
//        }
//        else
//        {
//            LOGManager.e("To:" + receiver + "[" + sms + "]");
//        }
//        CharString_s smsCharString_s = null;
//        receiver = Utils.FullNumber(receiver);
//        try
//        {
//            smsCharString_s = new CharString_s(
//                    TextUtil.EncodeUCS2BE(sms).length,
//                    TextUtil.EncodeUCS2BE(sms));
//            AppAgtSmsSendReq sendReq = new AppAgtSmsSendReq(userName(),
//                    receiver.replace("+", ""), smsCharString_s);
//
//            startNRService(mContext, MsgId.APP_AGENT_SMS_SEND_REQ,
//                    sendReq.toMsg());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
    
    public void sendSMS(Context mContext, long id)
    {
        startNRService(mContext, MsgId.UI_NR_SEND_SMS_REQ, id);
    }

    public void answerPhone(Context c)
    {
        startNRService(c, MsgId.APP_ANSWER);
    }

    public void hangUp(Context c)
    {
        startNRService(c, MsgId.APP_HANG_UP);
    }

    public void sendKeyVal(Context c, char ch)
    {
        AppAgtDtmfInd ind = new AppAgtDtmfInd(userName(), ch);
        startNRService(c, MsgId.APP_AGENT_DTMF_IND, ind.toMsg());
    }
    
    public void stopNR(Context c)
    {
        stopNRService(c);
        stopBleService(c);
    }
    
    public void startNR(Context c)
    {
        if (!TextUtils.isEmpty(SpUtil.getBtAddr()) && !isNRRegisted)
        {
            startNRService(c);
        }
    }
    
    public void tryToStartNR(Context c)
    {
        if (SpUtil.getNRService() == 1
                || SpUtil.getXHService() == 1)
        {
            // 开启状态机服务以及TCP服务
            startNRService(c);
        }
        else
        {
            return;
        }

        if (SpUtil.getNRService() == 0)
        {
            return;
        }

        if (TextUtils.isEmpty(SpUtil.getBtAddr()))
        {
            Engine.getInstance().isNRSwitchOn = false;
            Engine.getInstance().broadcast(IAction.NR_OPEN_ALERT);
            return;
        }
/*
        if (!Engine.getInstance().isNRSwitchOn)
        {
            return;
        }

        if (Engine.getInstance().isNRRegisted)
        {
            return;
        }
        
        Engine.getInstance().startNR(c);
        Engine.getInstance().broadcast(IAction.NR_START_REG);*/
    }
    
    public void switchOnNR(Context c)
    {
    	SpUtil.setfirstConnect(0);
        isNRSwitchOn = true;
        startNRService(c, MsgId.APP_OPEN_NR);
        startBleService(c, BleMsgId.NR_BLE_READ_SIM_IND);
    }
    
    public void switchOffNR(Context c)
    {
        isNRSwitchOn = false;
        startNRService(c, MsgId.APP_CLOSE_NR);
        stopBleService(c);
    }
    
    public void switchOnXH(Context c)
    {
        startNRService(c, MsgId.APP_OPEN_XH);
    }
    
    public void switchOffXH(Context c)
    {
        startNRService(c, MsgId.APP_CLOSE_XH);
    }

    public String userName()
    {
        // return Engine.getInstance().getUserName().replace("+", "");
        return userName;
    }
    
    public void startNRService(Context c, int iMsgId, long l)
    {
        Intent intent = new Intent(c, NRService.class);
        Bundle b = new Bundle();
        b.putInt("MSG_ID", iMsgId);
        b.putLong("MSG_LONG", l);
        intent.putExtras(b);
        c.startService(intent);
    }

    public void startNRService(Context c, int iMsgId, String s)
    {
        Intent intent = new Intent(c, NRService.class);
        Bundle b = new Bundle();
        b.putInt("MSG_ID", iMsgId);
        b.putString("MSG_STR", s);
        intent.putExtras(b);
        c.startService(intent);
    }

    public void startNRService(Context c, int iMsgId, byte[] msgContent)
    {
        Intent intent = new Intent(c, NRService.class);
        Bundle b = new Bundle();
        b.putInt("MSG_ID", iMsgId);
        b.putByteArray("MSG", msgContent);
        intent.putExtras(b);
        c.startService(intent);
    }

    public void startNRService(Context c, int iMsgId)
    {
        Intent intent = new Intent(c, NRService.class);
        Bundle b = new Bundle();
        b.putInt("MSG_ID", iMsgId);
        intent.putExtras(b);
        c.startService(intent);
    }

    public void startNRService(Context c)
    {
        Intent intent = new Intent(c, NRService.class);
        c.startService(intent);
    }
    
    public void stopNRService(Context c)
    {
        Intent intent = new Intent(c, NRService.class);
        c.stopService(intent);
    }
    
    public void startBleService(Context c, int iMsgId, byte[] msg)
    {
        Intent intent = new Intent(c, BleService.class);
        Bundle b = new Bundle();
        b.putInt("BLE_MSG_ID", iMsgId);
        b.putByteArray("BLE_MSG", msg);
        intent.putExtras(b);
        c.startService(intent);
    }

    public void startBleService(Context c, int iMsgId)
    {
        Intent intent = new Intent(c, BleService.class);
        Bundle b = new Bundle();
        b.putInt("BLE_MSG_ID", iMsgId);
        intent.putExtras(b);
        c.startService(intent);
    }
    
    public void startBleService(Context c)
    {
        Intent intent = new Intent(c, BleService.class);
        c.startService(intent);
    }
    
    public void stopBleService(Context c)
    {
        Intent intent = new Intent(c, BleService.class);
        c.stopService(intent);
    }

    public void showSysDialogAct(Context c, String title, String des, String l,
            String r, int show_type, String actName)
    {
        Intent intent = new Intent(SDKAction.NR_SDK_SYSTEM_DIALOG_INTENT);
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", title);
        bundle.putString("DESCRIPTION", des);
        bundle.putString("LEFT", l);
        bundle.putString("RIGHT", r);
        bundle.putInt("TYPE", show_type);
        bundle.putString("ACT_NAME", actName);
        intent.putExtras(bundle);
        
        c.sendBroadcast(intent);
    }
    
    private SysAlertDialog mAppDialog;

    public void showSysAlert(Context c, String title, String des, String ok)
    {
        if (mAppDialog == null)
        {
            mAppDialog = new SysAlertDialog(c);
        }
        mAppDialog.setCancelable(false);
        mAppDialog.setCanceledOnTouchOutside(false);
        mAppDialog.setTitle(title);
        mAppDialog.setMessage(des);
        mAppDialog.setOk(ok);
        mAppDialog.setOnClickListener(null);
        mAppDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mAppDialog.show();
    }
    
    public void showAppAlertDialog(Context c, String title, String des, String le, String ri, OnSysAlertClickListener l)
    {
        if (mAppDialog == null)
        {
            mAppDialog = new SysAlertDialog(c);
        }
        mAppDialog.setCancelable(false);
        mAppDialog.setCanceledOnTouchOutside(false);
        mAppDialog.setTitle(title);
        mAppDialog.setMessage(des);
        mAppDialog.setLeft(le);
        mAppDialog.setRight(ri);
        mAppDialog.setOnClickListener(l);
        mAppDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mAppDialog.show();
    }

    public void recvSMS(String body, String from)
    {
        SmsEntityDBHelper mHelper = SmsEntityDBHelper.getInstance();
        SmsEntity tMessageSms = new SmsEntity();
        
        if (TextUtils.isEmpty(from))
        {
            from = "未知号码";
        }
        else
        {
            ContactModel contact = ContactDBHelper.getInstance()
                    .getContactByNumberTry(from);
            if (contact != null)
            {
                tMessageSms.setNickName(contact.getName());
            }
            else
            {
                tMessageSms.setNickName(from);
            }
        }

        tMessageSms.setAddress(from);
        tMessageSms.setPerson(Engine.getInstance().getUserName());
        tMessageSms.setBody(body);
        tMessageSms.setDir(SmsEntity.DIR_INCOMING);
        tMessageSms.setBodyType(SmsEntity.TYPE_TEXT);
        
        tMessageSms.setDate(TimeUtil.getDateString(System.currentTimeMillis(),
                TimeUtil.dateFormat2));
        tMessageSms.setRead(SmsEntity.STATUS_UNREAD);
        
        long id = mHelper.addMessageSms(tMessageSms);

//        if (getTopAct(context).endsWith("SmsChatListAct")
//                && from.equals(SmsChatListAct.from))
//        {
            broadcast(IAction.REFRESH_UI);
//            return;
//        }

        broadcast(IAction.SMS_NOTIFY, id);
    }
    
    public void recvLxMsg(String body, String from)
    {
        TraMessage tMessage = new TraMessage();
        tMessage.setFrom(from);
        tMessage.setContent(AESUtils.getEntryString(body));
        tMessage.setMsg_type(2);
        tMessage.setNick_name(from);
        tMessage.setTime(TimeUtil.getDateString(System.currentTimeMillis(),
                TimeUtil.dateFormat2));
//        System.out.println("Engine : tMessage 旅信助手时间" + tMessage.getTime());
        TravelrelyMessageDBHelper sMessageDBHelper = TravelrelyMessageDBHelper.getInstance();

        sMessageDBHelper.addMessage(tMessage);

//        TravelService.sendNotifiyIfNeed(tMessage, null);
        //NotificationUtil.sendNotifiyIfNeed(tMessage, null);
        Engine.getInstance().broadcastNotifiy(SDKAction.NR_SDK_NOTIFIY_REC_SMS, tMessage);
        Intent intent = new Intent();
        intent.setAction(IAction.MSM_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        intent.putExtras(bundle);
        Engine.getInstance().getContext().sendBroadcast(intent);
//        TravelService.sendMainTabMsgCount(1);
        FetchMessage.sendMainTabMsgCount(1);
    }
    
    public void RentEntry(int iMsgType, String from, String content)
    {
        Log.w("RentEntry", "小号业务[" + iMsgType + "] " + from + "," + content);
        switch (iMsgType)
        {
            case 16:
                String[] tmp = content.split(":");
                AgtRentCalledReq req = new AgtRentCalledReq(RentMsgId.APP_CALLED_REQ,
                        from, userName, tmp[0], Integer.parseInt(tmp[1]));
                startRentService(context, RentMsgId.APP_CALLED_REQ, req.toByte());
                break;
                
            case 17:
                RentBeHungUp(context);
                break;

            default:
                break;
        }
    }
    
    public void startRentService(Context c)
    {
        Intent intent = new Intent(c, RentService.class);
        c.startService(intent);
    }

    public void startRentService(Context c, int iMsgId)
    {
        Intent intent = new Intent(c, RentService.class);
        Bundle b = new Bundle();
        b.putInt("MSG_ID", iMsgId);
        intent.putExtras(b);
        c.startService(intent);
    }

    public void startRentService(Context c, int iMsgId, byte[] msgContent)
    {
        Intent intent = new Intent(c, RentService.class);
        Bundle b = new Bundle();
        b.putInt("MSG_ID", iMsgId);
        b.putByteArray("MSG", msgContent);
        intent.putExtras(b);
        c.startService(intent);
    }
    
    public void stopRentService(Context c)
    {
        Intent intent = new Intent(c, RentService.class);
        c.stopService(intent);
    }
    
    private void RentBeHungUp(Context c)
    {
        startRentService(c, RentMsgId.APP_BE_HUNG_UP);
    }
    
    private static void creatAlertConfig(Context context){
        PreferencesUtil.setSharedPreferences(context, PreferencesUtil.ALERT_CONFIG, ConstantValue.callVoice, true);
        PreferencesUtil.setSharedPreferences(context, PreferencesUtil.ALERT_CONFIG, ConstantValue.callVabration, true);
    }
    
    public void showCallDialog(Context context, OnListAlertClickListener listener, int type){
        
        new SysListAlert(context, type, new String[] {
            context.getResources().getString(R.string.call_ip),
            context.getResources().getString(R.string.callNoRoaming)},
            new String[] {context.getResources().getString(R.string.call_ip_Explain),
            context.getResources().getString(R.string.callNoRoamingExplain)},"", 
                    listener).show();
    }
    
    public void getOnListAlertClick(Context context, int id, int which, CallRecord callRecord)
    {
        if (which == 0)
        {
            Utils.callVoip(context, callRecord);
        }
        else if (which == 1)
        {                
            if (!Engine.getInstance().isNRRegisted)
            {
                Engine.getInstance().showSysDialogAct(context, "提示",
                        "请先在\"我\"中发起旅信电话注册", "", "", 0, "");
                return;
            }
            // 号码匹配通讯录
            startCall(context, callRecord.getNumber());
        }
    }
    
    // 获得定制套餐价格
    public void getCustomizedPackages(final Context context, final String mcc, final int iPkgType, final int iCountryIdx)
    {
        ProgressOverlay progressOverlay = new ProgressOverlay(context);
        progressOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                String username = Engine.getInstance().userName;

                GetPackagesRsp rsp = GetPackagesReq.getPackages(
                        context, username, mcc);
                if (rsp == null)
                {
                    Utils.showToast((Activity)context,
                            context.getString(R.string.errorNetwork2));
                    return;
                }
                if (rsp.getBaseRsp().isSuccess())
                {
                    Log.d("", "" + "成功获得套餐数据价格表");
                    Intent intent;
                    
                    if (iPkgType == PkgType.PREPAID)
                    {
//                        intent = new Intent(context,
//                                MealTwoActivity.class);
                    	// TODO cwj
                    }
                    else
                    {
//                        intent = new Intent(context,
//                                MealOneActivity.class);
                    	// TODO cwj
                    }

//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(SelDestActivity.carrier, rsp);
//                    bundle.putInt("COUNTRY_IDX", iCountryIdx);
//                    intent.putExtras(bundle);
//
//                    context.startActivity(intent);
                    // TODO cwj
                }
                else
                {
                    Res.toastErrCode(context, rsp.getBaseRsp());
                }
            }
        });
    }
    
    public void gotoNoRoamOrderAct(final Context context, final Class<?> act, final int type)
    {
        //openActivity(MealNoRoamAct.class);
        ProgressOverlay progressOverlay = new ProgressOverlay(context);
        progressOverlay.show("", new OnProgressEvent()
        {
            /* (non-Javadoc)
             * @see com.travelrely.v2.net.ProgressOverlay.OnProgressEvent#onProgress()
             */
            @Override
            public void onProgress()
            {
                final GetNRPriceRsp rsp = GetNRPriceReq.getNoRoamPrice(
                        context);
                ResponseInfo baseRsp = rsp.getBaseRspInfo();
                if (!baseRsp.isSuccess())
                {
                    Res.toastErrCode(context, baseRsp);
                    return;
                }

                double btBoxPrice = rsp.getData().getBtBoxPrice();
                double usaBt = rsp.getData().getUsa_btbox_price();
                String NRDes = rsp.getData().getNoroaming_pricetxt1();
                String BoxDes = rsp.getData().getNoroaming_pricetxt2();
                List<PackagePrice> priceList = rsp.getData().getPkgPrices();
                setPackagePrices(priceList);
//                setBtBleBoxPrice(btBoxPrice);
                /*美国订单蓝牙盒子价格
                 * */
                setBtBleBoxPrice(usaBt);
                
                Intent intent = new Intent(context,
                        act);

                Bundle bundle = new Bundle();
                bundle.putDouble("BT_BOX_PRICE", btBoxPrice);
                bundle.putDouble("USABT_BOX_PRICE", usaBt);
                bundle.putString("NRDes", NRDes);
                bundle.putString("BoxDes", BoxDes);
                bundle.putSerializable("PKG_PRICE", (Serializable) priceList);
                bundle.putInt("COUNTRY_IDX", type);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }
    
    // 判断是否在前台运行
    public boolean isForeground;
    
    private boolean isRunningForeground(Context c)
    {
        String packageName = getPackageName(c);
        String topPackageName = getForegroundPackageName(c);
        LOGManager.d("packageName=" + packageName
                + ",topPackageName=" + topPackageName);
        if (packageName.equals(topPackageName))
        {
            LOGManager.d("—> isRunningForeground");
            return true;
        }
        else
        {
            LOGManager.d("—> isRunningBackground");
            String t = Utils.GetDate(0, "yyyy-MM-dd HH:mm:ss.SSS");
            SpUtil.setLastFreground(t);
            return false;
        }
    }

    private String getPackageName(Context context)
    {
        String packageName = context.getPackageName();
        return packageName;
    }

    private String getForegroundPackageName(Context context)
    {
        String topPackageName = "";
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(android.content.Context.ACTIVITY_SERVICE));

        // android.app.ActivityManager.getRunningTasks(int maxNum)
        // int maxNum—>The maximum number of entries to return in the list
        // 即最多取得的运行中的任务信息(RunningTaskInfo)数量
        List<RunningTaskInfo> runningTaskInfos = activityManager
                .getRunningTasks(1);
        if (runningTaskInfos != null)
        {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topPackageName = f.getPackageName();
        }

        // 按下Home键盘后 topPackageName=com.android.launcher2.Launcher
        return topPackageName;
    }
    
    public String getTopAct(Context context)
    {
        String topPackageName = "";
        ActivityManager activityManager = (ActivityManager) (context
                .getSystemService(android.content.Context.ACTIVITY_SERVICE));

        // android.app.ActivityManager.getRunningTasks(int maxNum)
        // int maxNum—>The maximum number of entries to return in the list
        // 即最多取得的运行中的任务信息(RunningTaskInfo)数量
        List<RunningTaskInfo> runningTaskInfos = activityManager
                .getRunningTasks(1);
        if (runningTaskInfos != null)
        {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topPackageName = f.getShortClassName();
        }

        // 按下Home键盘后 topPackageName=com.android.launcher2.Launcher
        return topPackageName;
    }
    
    public void syncForegroundThread()
    {
        try
        {
            Thread.sleep(500);
            new Thread()
            {
                public void run()
                {
                    isForeground = isRunningForeground(context);
                }
            }.start();
        }
        catch (Exception e)
        {
    
        }
    }
    
    public void startLoginService(final Context context)
    {
        if (Engine.getInstance().hasUsrName()
                && Engine.getInstance().hasLongPswd())
        {
            Engine.getInstance().isLogIn = true;

            if (Utils.isNetworkAvailable(context))
            {
                LOGManager.d(getClass().getName()+":启动LoginService登录");
                Intent sIntent = new Intent(context, LoginService.class);
                context.startService(sIntent);
            }
            else
            {
                // bugfix1005
                //Engine.getInstance().isLogIn = false;
                Utils.showToast((Activity)context,
                        context.getResources().getString(R.string.errorNetwork1));
            }
        }
        else
        {
            Engine.getInstance().isLogIn = false;
        }
    }
    
    /**
     * 发送签到
     * @param type
     * @param messages
     * @param conStrings
     */
    public void sendReceptionMsg(Context context, int type,TraMessage messages, String conStrings){
        
//      String msg = "接收" + "\t" + conStrings[1] + "\t" +
//              conStrings[2] + "\t" + conStrings[2] + "\t" + 
//              latitude + "\t" + longitude;
      String[] msgContext = AESUtils.getDecryptString(conStrings).split("\t");
      String sendMsg = msgContext[0];
      LOGManager.d("发送我要签到的内容=" + msgContext);
      refreshReceptionMsg(messages, msgContext[0]);
      
      TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), sendMsg,
              messages.getFrom(), type, "", 3, 1, 1, messages.getNick_name(), messages.getGroup_name(), 
              Engine.getInstance().getUserInfo().getData().getPersonal_info().getHeadPortrait(), 
              messages.getFrom_head_portrait());
      
      Engine.getInstance().sendMessageInBackground(context,message2);
  }
    
    private void refreshReceptionMsg(TraMessage msg, String newContext) {

//      setReceptionBt(1);
      TravelrelyMessageDBHelper.getInstance().updateContext(msg.getFrom(), String.valueOf(msg.getId()), "id",
              "content", newContext);
  }
    /**
     * 发送雷达
     * @param messages
     */
    public void sendRadar(Context cot, TraMessage messages, double latitude, double longitude){
        
        String[] context = AESUtils.getDecryptString(messages.getContent()).split("\t");
        String msg = context[0] + "\t" + latitude + "\t" + longitude;
        String msgContext = AESUtils.getEntryString(msg);
        
        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), msgContext,
                messages.getFrom(), TraMessage.TYPE_RADAR_RESPONSE, "", 2, 0, 1, messages.getNick_name(), messages.getGroup_name(), 
                Engine.getInstance().getUserInfo().getData().getPersonal_info().getHeadPortrait(), 
                messages.getFrom_head_portrait());
        
        Engine.getInstance().sendMessageInBackground(cot,message2);
    }
    
    /**
     * 
     * @param num 号码
     * @param numberLable 拨号方式 1=VOIP  2=旅信网络电话
     * @param durations 通话时间
     * @param type 拨号类型（来电:1  拨出:2   未接:3）
     * @return
     */
    public CallRecord setCallRecord(String num, int numberLable, long durations, int type){
        
        CallRecord cRecord = new CallRecord();
        cRecord.setNumber(num);
        cRecord.setNumberLable(numberLable);
        cRecord.setDuration(durations);
        cRecord.setType(type);
        
        return cRecord;
    }
    
    /**
     * 下载蓝牙升级文件
     * @throws Exception
     */
    public void downloadBluetooth() throws Exception {
        
        String path = "/travelrely/MT100.hex";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + path);
        
        InputStream is = null;
        FileOutputStream fos = null;
        HttpURLConnection con = null;
        String urlStr = ReleaseConfig.getUrl(cc) + "api/user/download/bluetooth";
        
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";
        try {
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            
            if (con.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            
            is = con.getInputStream();
            fos = new FileOutputStream(file, false);
            byte buffer[] = new byte[4096];
            int readsize = 0;
            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                }
        } finally {
            if (con != null) {
                con.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }

    }

    
    /**
     * 获取用户漫游信息参数
     * 
     * @param mContext
     * @return
     */
    public void getUserRoamProfileRequest(final Context mContext)
    {
        GetUsrRoamProfileRsp rsp = GetUsrRoamProfileReq.getUsrRoamProfile(
                mContext);
        if (rsp == null)
        {
            return;
        }
        
        if (!rsp.getBaseRsp().isSuccess())
        {
            return;
        }
        
        ArrayList<UserRoamProfile> list = rsp.getData().getTriplist();
        if (list == null || list.size() == 0)
        {
            return;
        }
        
        SpUtil.setUsrRoamProfileVer(rsp.getData().getUser_roam_version());
        
        UserRoamProfileDBHelper helper = UserRoamProfileDBHelper.getInstance();
        helper.insertAll(list);
        
        Engine.getInstance().setUrl(rsp);
    }

    /**
     * 通过漫游地配置信息来设置url
     */
    private void setUrl(GetUsrRoamProfileRsp uProfile) {
//        Engine.getInstance().getSimMcc()
        UserRoamProfile tripList = UserRoamProfileDBHelper.getInstance().selsctData(
                Engine.getInstance().getSimMcc(), "mcc");
        if (tripList != null) {
            try {
                if (!Utils.nowTimeCompare(tripList.getStart(), Utils.y_m_d)
                        && Utils.nowTimeCompare(tripList.getEnd(), Utils.y_m_d)) {
                    LOGManager.d("在行程中");
                    Profile profile = ProfileDBHelper.getInstance().selsctMcc(
                            Engine.getInstance().getSimMcc(), "mcc");
                    if (profile != null) {
                        
                        setRoamSmcLoc(profile.getSmc_loc());
                        setRoamGlmsLoc(profile.getGlms_loc());
                        
                        this.homeLogin = false;
                        SpUtil.setVisiting(1);
                    }
                } else {
                    this.homeLogin = true;
                    SpUtil.setVisiting(0);
                    LOGManager.d("不在行程中");
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }
    
  //insert到通话记录
    public void insertCallLog(Context context, CallRecord callRecord, int numberLable){
        
        callRecord.setNumberLable(numberLable);
        CallRecordsDBHelper.getInstance().insert(callRecord);
        //插入到ContentProvider做共享数据使用
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(CallLogContentProvider.CONTENT_URI, callRecord.generateValue());
    }


}
