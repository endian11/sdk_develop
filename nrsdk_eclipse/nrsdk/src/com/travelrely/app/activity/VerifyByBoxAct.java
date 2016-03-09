package com.travelrely.app.activity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travelrely.app.view.FormsFinishButton;
import com.travelrely.app.view.ListViewForScrollView;
import com.travelrely.core.ConstantValue;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.IntentMsg;
import com.travelrely.core.Res;
import com.travelrely.core.ble.BleMsgId;
import com.travelrely.core.nr.BoxManager;
import com.travelrely.core.nr.msg.MsgId;
import com.travelrely.core.nr.util.ByteUtil;
import com.travelrely.net.ProgressOverlay;
import com.travelrely.net.ProgressOverlay.OnProgressEvent;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.sdk.Api;
import com.travelrely.sdk.RequestResultListener;
import com.travelrely.sdk.R;
import com.travelrely.v2.net_interface.VerifySuccReq;
import com.travelrely.v2.net_interface.VerifySuccRsp;
import com.travelrely.v2.util.LogUtil;
import com.travelrely.v2.util.SpUtil;
import com.travelrely.v2.util.Utils;

/**
 * 选择蓝牙盒子
 * 
 * @author Travelrely
 */
public class VerifyByBoxAct extends NavigationActivity implements
		OnItemClickListener, OnCheckedChangeListener, OnClickListener
{
    private final static String TAG = VerifyByBoxAct.class.getSimpleName();
    
	private LayoutInflater inflater;
	
	private TextView tv_num;
	
	private CheckBox chkPhone, chkBtBox;
	private EditText etAuthCode;
	private Button btnGetAuth;
	
	private TextView tvScanning;
	private ImageView ivScan;
	
	RelativeLayout verifyState;
	private TextView verifyDesc;
	private Button verifyTimer;
	
	private FormsFinishButton btnSure;
	
	private TimeCount timer;
	
	private BtBoxAdapter lvAdapter;

	// 存放可用于显示的蓝牙盒子
    private List<BluetoothDevice> devList = new ArrayList<BluetoothDevice>();

	private static final int REQUEST_ENABLE_BT = 2;
	private BluetoothAdapter mBtAdapter;

	private boolean isClickScan = false;
	private boolean isScanEnable = false;
	boolean mScanning = false;
	
	String username;
	
	private Handler mHandler = new Handler();
	    
	private Runnable mStopScanTask= new Runnable()
    {
        @Override
        public void run()
        {
            stopScan();
            LogUtil.i(TAG, "扫描时间到，停止扫描");
        }
    };
    
    class TimeCount extends CountDownTimer
    {
        public TimeCount(long millisInFuture, long countDownInterval)
        {
            // 参数依次为总时长,和计时的时间间隔
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish()
        {
            // 计时完毕时触发
            if (chkPhone.isChecked())
            {
                btnGetAuth.setText(R.string.repeat_auth_code);
                btnGetAuth.setEnabled(true);
            }
            
            if (chkBtBox.isChecked())
            {
                verifyTimer.setText("验证计时(0)");
                verifyDesc.setText("验证超时，重新配对");
                alertWait(false, "");
            }
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            // 计时过程显示
            if (chkPhone.isChecked())
            {
                btnGetAuth.setEnabled(false);
                btnGetAuth.setText(getString(R.string.repeat_auth_code_1) + "("
                        + millisUntilFinished / 1000 + ")");
            }
            
            if (chkBtBox.isChecked())
            {
                verifyTimer.setText("验证计时" + "("
                        + millisUntilFinished / 1000 + ")");
            }
        }
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Engine.getInstance().isOnVerify = true;
		
		username = getIntent().getStringExtra("USER_NAME");

		setContentView(R.layout.act_verify_by_box);

		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		lvAdapter = new BtBoxAdapter();

		ListViewForScrollView lvBle = (ListViewForScrollView) findViewById(R.id.btBoxList);
		lvBle.setAdapter(lvAdapter);

		// 添加列表项被单击的监听器
		lvBle.setOnItemClickListener(this);
		
		initView();
		
		// 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE))
        {
            showShortToast("该设备不支持低功耗蓝牙功能");
            finish();
            return;
        }

        /**
         * 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器
         * (API必须在以上android4.3或以上和版本)
         */
        final BluetoothManager btManager = (BluetoothManager) getSystemService(
                Context.BLUETOOTH_SERVICE);
        mBtAdapter = btManager.getAdapter();

		// 检查设备上是否支持蓝牙
		if (mBtAdapter == null)
		{
			showShortToast("该设备不支持蓝牙功能");
			finish();
			return;
		}
	}
	
	private void initView()
	{	    
	    tv_num = (TextView) findViewById(R.id.tv_num);
	    tv_num.setText(username);
	    
	    etAuthCode = (EditText) findViewById(R.id.etAuthCode);
	    
	    btnGetAuth = (Button) findViewById(R.id.btnGetAuth);
	    btnGetAuth.setText("点击获取");
	    btnGetAuth.setOnClickListener(this);
	    
	    chkPhone = (CheckBox) findViewById(R.id.chkPhone);
	    chkPhone.setOnCheckedChangeListener(this);
	    chkBtBox = (CheckBox) findViewById(R.id.chkBtBox);
	    chkBtBox.setOnCheckedChangeListener(this);
	    chkPhone.setChecked(true);
	    
	    tvScanning = (TextView) findViewById(R.id.tvScanning);
	    ivScan = (ImageView) findViewById(R.id.ivScan);
	    ivScan.setOnClickListener(this);
	    
	    verifyState = (RelativeLayout) findViewById(R.id.verifyState);
	    verifyState.setVisibility(View.GONE);
	    verifyDesc = (TextView) findViewById(R.id.verifyDesc);
	    verifyTimer = (Button) findViewById(R.id.verifyTimer);
	    verifyTimer.setText("验证计时(60)");
	    
	    timer = new TimeCount(60000, 1000);
	    
	    btnSure = (FormsFinishButton) findViewById(R.id.btnSure);
	    btnSure.setText(R.string.ok);
	    btnSure.setOnClickListener(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();		
		
        // 注册蓝牙状态变化广播
        //registerReceiver(mReceiver, makeFilter());

		/**
		 * 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,
		 * 弹出对话框向用户要求授予权限来启用
		 */
		if (!mBtAdapter.isEnabled())
		{
		    ///*
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			//*/
		    //mBtAdapter.enable();
			
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED)
		{
			finish();
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
    protected void onPause()
    {
        super.onPause();
        
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null)
        {
            scanLeDevice(false);
        }
        //unregisterReceiver(mReceiver);
    }

    @Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

    @Override
	protected void initNavigationBar()
	{
		setTitle(getResources().getString(R.string.verfiy1));
		setRightText(getResources().getString(R.string.verfiycomplete));
	}
    @Override
    public void onRightClick() {
    	// TODO Auto-generated method stub
    	super.onRightClick();
    	gotoVerify();
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (buttonView == chkPhone)
        {
            if (isChecked)
            {
                chkBtBox.setChecked(false);
            }
            else
            {
                chkBtBox.setChecked(true);
            }
            showRight();//如果是手机内sim卡验证，就需要显示标题栏右侧的完成按钮
        }

        if (buttonView == chkBtBox)
        {
            if (isChecked)
            {
                chkPhone.setChecked(false);
            }
            else
            {
                chkPhone.setChecked(true);
                
            }
            hideRight();//如果是蓝牙盒子验证，就不需要显示标题栏右侧的 完成 按钮
        }
    }
    
    @Override
    public void onClick(View v)
    {
        int id = v.getId();
		if (id == R.id.btnGetAuth) {
			getAuthCode();
		} else if (id == R.id.ivScan) {
			gotoScanBox();
		} else if (id == R.id.btnSure) {
			gotoVerify();
		} else {
		}
        
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
	    if (position == lvAdapter.getSelectItem())
	    {
	        return;
	    }
	    final BluetoothDevice device = devList.get(position);
//	    if (device.getAddress().equals(SpUtil.getBtAddr()))
//	    {
//	        return;// 上次连接的设备
//	    }
	    
	    // 先停止扫描再发起连接注册
	    mHandler.removeCallbacks(mStopScanTask);
	    stopScan();
	    
	    Engine.getInstance().startNRService(VerifyByBoxAct.this);
	    Engine.getInstance().stopBleService(VerifyByBoxAct.this);
	    
	    LogUtil.i(TAG, "2秒后发起配对");

        lvAdapter.setSelectItem(position);
        
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Engine.getInstance().startBleService(VerifyByBoxAct.this,
                        BleMsgId.NR_BLE_MATCH_BOX,
                        device.getAddress().getBytes());
                //为防止用户误操作，如 按下返回键 之类 等等  一个是配对失败，必须让弹框消失 还有就是盒子验证
                //登陆成功也必须让弹框消失
                alertWait(true, "");
            }
        }, 2000);
	}
	
    @Override
    protected void onBroadcastReceived(Context context, Intent intent)
    {        
        String action = intent.getAction();
        
        LogUtil.i(TAG, "action = " + action);
        
        if (IAction.BOX_FOUND.equals(action))
        {
            BluetoothDevice device = intent.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE);
            showDevice(device);

            scanLeDevice(true);
        }
        
        if (IAction.BOX_NOT_FOUND.equals(action))
        {
            scanLeDevice(true);
        }
        
        if (IAction.BOX_MATCH_SUCC.equals(action))
        {
            int pos = lvAdapter.getSelectItem();
            BluetoothDevice device = devList.get(pos);
            lvAdapter.setSelectItem(-1);
            devList.clear();
            devList.add(device);
            lvAdapter.notifyDataSetChanged();
            //配对成功之后，需要提醒用户是否开通国内漫游，以免造成对移动的投诉
            String imsiStr = BoxManager.getDefault().getImsiStr();
//            LogUtil.e("box imsi: ", imsiStr);
            System.out.println("box imsi: "+ imsiStr);
            Engine.getInstance().startNRService(getApplicationContext(),
                    MsgId.APP_INNER_RESET);
        }
        
        if (IAction.BOX_MATCH_FAIL.equals(action))
        {
            lvAdapter.setSelectItem(-1);
            alertWait(false, "");
        }
        
        if (IAction.NR_START_REG.equals(action))
        {
            verifyState.setVisibility(View.VISIBLE);
            verifyDesc.setText("正在验证...");
            timer.start();
        }
        
        if (IAction.NR_REG_FAIL.equals(action))
        {
        	alertWait(false, "");
            showAppAlert("提示",
                    "验证失败（注册失败）", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
            
        }
        
        if (IAction.NR_REG_EXPIRE.equals(action))
        {
        	 alertWait(false, "");
            showAppAlert("提示",
                    "验证失败（注册超时）", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
           
        }
        
        if (IAction.BOX_MISS_MATCH.equals(action))
        {
        	 alertWait(false, "");
            showAppAlert("提示",
                    "验证失败（盒子未配对）", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
           
        }
        
        if (IAction.BOX_NO_SIM.equals(action))
        {
        	alertWait(false, "");
            showAppAlert("提示",
                    "蓝牙设备未插入sim卡，请插入手机sim卡后使用", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
            
        }
        
        if (IAction.BOX_SIM_INIT_FAIL.equals(action))
        {
        	alertWait(false, "");
            showAppAlert("提示",
                    "请确认蓝牙设备内sim卡是否正确安装或电池是否可用", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
            
        }
        
        if (IAction.BOX_SIM_CHANGED.equals(action))
        {
        	alertWait(false, "");
            showAppAlert("提示",
                    "验证失败（sim卡已更换）", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
            
        }
        
        if (IAction.BOX_CIPHER_KEY_SAVED.equals(action))
        {
            Engine.getInstance().startNRService(VerifyByBoxAct.this,
                    MsgId.APP_AGENT_USER_VERIFY_REQ);
        }
        
        if (IAction.USER_VERIFY_SUCC.equals(action))
        {
            Engine.getInstance().stopNR(VerifyByBoxAct.this);
            SpUtil.setfirstConnect(0);
            Log.e("oldcwj", "user_verify_suss");
            // Toast.makeText(this, "aa", Toast.LENGTH_LONG);
            gotoVerifySucc(intent.getIntExtra(IntentMsg.INTENT_ID, 0));
        }
        
        if (IAction.USER_VERIFY_FAIL.equals(action))
        {
        	alertWait(false, "");
            showAppAlert("提示", "手机号码验证失败", "关闭");
            verifyDesc.setText("验证失败");
            timer.cancel();
        }
    }

    private void scanLeDevice(final boolean enable)
    {
        if (!isScanEnable)
        {
            return;
        }
        
        if (enable)
        {
            mScanning = true;
            mBtAdapter.startLeScan(mLeScanCallback);
            LogUtil.i(TAG, "启动扫描");
        }
        else
        {
            mScanning = false;
            mBtAdapter.stopLeScan(mLeScanCallback);
            LogUtil.i(TAG, "停止扫描");
        }
    }
    
    private List<UUID> parseUuids(byte[] advertisedData)
    {
        List<UUID> uuids = new ArrayList<UUID>();

        ByteBuffer buffer = ByteBuffer.wrap(advertisedData).order(ByteOrder.LITTLE_ENDIAN);
        while (buffer.remaining() > 2)
        {
            byte length = buffer.get();
            if (length == 0)
            {
                break;
            }

            byte type = buffer.get();
            switch (type)
            {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (length >= 2)
                    {
                        uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getShort())));
                        length -= 2;
                    }
                    break;
                    
                case 0x04: // Partial list of 32-bit UUIDs
                case 0x05: // Complete list of 32-bit UUIDs
                    while (length >= 4)
                    {
                        uuids.add(UUID.fromString(String.format(
                                "%08x-0000-1000-8000-00805f9b34fb", buffer.getInt())));
                        length -= 4;
                    }
                    break;

                case 0x06: // Partial list of 128-bit UUIDs
                case 0x07: // Complete list of 128-bit UUIDs
                    while (length >= 16)
                    {
                        long lsb = buffer.getLong();
                        long msb = buffer.getLong();
                        uuids.add(new UUID(msb, lsb));
                        length -= 16;
                    }
                    break;

                default:
                    buffer.position(buffer.position() + length - 1);
                    break;
            }
        }

        return uuids;
    }
    
    private boolean isBox(byte[] code)
    {
        List<UUID> uuids = parseUuids(code);
        for (UUID uuid : uuids)
        {
            LogUtil.i(TAG, uuid.toString());
            if (uuid.toString().equals("0000fee9-0000-1000-8000-00805f9b34fb"))
            {
                return true;
            }
        }
        
        return false;
    }
    
    // Device scan callback.
    private LeScanCallback mLeScanCallback = new LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                byte[] scanRecord)
        {            
            if (!isClickScan)
            {
                return;
            }
            
            LogUtil.i(TAG, device.getName() + "[" + device.getAddress() + "]\n"
                    + ByteUtil.toHexString(scanRecord));
            if (isBox(scanRecord))
            {                
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showDevice(device);
                    }
                });
            }
            
            /*
            if (device.getAddress().startsWith("00:41:57"))
            {
                scanLeDevice(false);
                Engine.getInstance().startBleService(VerifyByBoxAct.this,
                        BleMsgId.NR_BLE_FIND_BOX,
                        device.getAddress().getBytes());
            }*/
        }
    };

	class BtBoxAdapter extends BaseAdapter
	{
		private int selectedPosition = -1;// 选中的位置

		@Override
		public int getCount()
		{
			// 指定一共包含n个选项
			return devList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		// 重写该方法，该方法的返回值将作为列表项的ID
		@Override
		public long getItemId(int position)
		{
			return position;
		}

		public void setSelectItem(int position)
		{
			selectedPosition = position;
			notifyDataSetChanged();
		}
		
	    public int getSelectItem()
	    {
	        return selectedPosition;
	    }

		// 重写该方法，该方法返回的View将作为列表框
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
		    ViewHolder holder = null;
		    if (convertView == null)
		    {
		        convertView = inflater.inflate(R.layout.bt_box_item, parent, false);
		        holder = new ViewHolder();
		        holder.devName = (TextView) convertView.findViewById(R.id.tvDevName);
		        holder.devAddr = (TextView) convertView.findViewById(R.id.tvMacAddr);
		        holder.devState = (TextView) convertView.findViewById(R.id.tvState);
		        holder.progBar = (ProgressBar) convertView.findViewById(R.id.progress);
		        convertView.setTag(holder);
		    }
		    else
		    {
		        holder = (ViewHolder) convertView.getTag();
		    }

			BluetoothDevice dev = devList.get(position);
			
			final String deviceName = dev.getName();
            if (deviceName != null && deviceName.length() > 0)
            {
                holder.devName.setText(deviceName);
            }
            else
            {
                holder.devName.setText(R.string.unknown);
            }
            holder.devAddr.setText(dev.getAddress());

            if (dev.getAddress().equals(SpUtil.getBtAddr()))
            {
                holder.devState.setText("已配对");
                holder.progBar.setVisibility(View.GONE);
            }
            else
            {
                holder.devState.setText("未配对");
                holder.progBar.setVisibility(View.GONE);
            }
            
            if (selectedPosition == position)
            {
                holder.devState.setVisibility(View.INVISIBLE);
                holder.progBar.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.devState.setVisibility(View.VISIBLE);
                holder.progBar.setVisibility(View.INVISIBLE);
            }

			return convertView;
		}
	}
	
    static class ViewHolder
    {
        TextView devName;
        TextView devAddr;
        TextView devState;
        ProgressBar progBar;
    }

    private void showDevice(BluetoothDevice dev)
    {
        if (!devList.contains(dev))
        {
        	String name = dev.getName();
        	if (name.contains(ConstantValue.BLE_brand)){
        		devList.add(dev);
        		lvAdapter.notifyDataSetChanged();
        	}
        }
    }
    
    private void clearDevList()
    {
        devList.clear();
        lvAdapter.notifyDataSetChanged();
    }
    
    private void gotoScanBox()
    {
        if (!chkBtBox.isChecked())
        {
            return;
        }
        if (!isScanEnable)
        {
            SpUtil.setBtAddr("");
            SpUtil.setfirstConnect(0);
            isClickScan = true;
            clearDevList();
            startScan();
        }
    }
    
    void startScan()
    {
        tvScanning.setVisibility(View.VISIBLE);
        isScanEnable = true;
        scanLeDevice(true);
        
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(mStopScanTask, 20000);
    }
    
    void stopScan()
    {
        tvScanning.setVisibility(View.GONE);
        
        scanLeDevice(false);
        
        isScanEnable = false;
        
        isClickScan = false;
    }
    
    private void getAuthCode() {
        if (!chkPhone.isChecked()) {
            return;
        }
        
        ProgressOverlay overlay = new ProgressOverlay(this);
        overlay.show("", new OnProgressEvent() {
            @Override
            public void onProgress() {
            	Api.getInstance().generateVerifyCode(VerifyByBoxAct.this, username,
            			new RequestResultListener() {

							@Override
							public void success(ResponseInfo responseInfo) {
								
							}

							@Override
							public void fail(ResponseInfo responseInfo) {
								Res.toastErrCode(VerifyByBoxAct.this, responseInfo);
							}
            		
            	});
                
                timer.start();
            }
        });
    }

    private void gotoVerifySucc(final int verifyflag) {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                VerifySuccRsp rsp = VerifySuccReq.verifySucc(VerifyByBoxAct.this,
                        Engine.getInstance().getUserName(), verifyflag);
                if (!rsp.getBaseRsp().isSuccess())
                {
                    Res.toastErrCode(VerifyByBoxAct.this, rsp.getBaseRsp().getMsg());
                    return;
                }
                
                Engine.getInstance().isOnVerify = false;
                
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        verifyDesc.setText("验证完成");
                        timer.cancel();
                    }
                });
                
                Engine.getInstance().setUserName(rsp.getData().getUserName());
                Engine.getInstance().setLongPswd(rsp.getData().getPassword());

                SpUtil.setLongPswd(rsp.getData().getPassword());
                SpUtil.setUserName(rsp.getData().getUserName());
                // Toast.makeText(VerifyByBoxAct.this, "gotoVerifySucc", Toast.LENGTH_SHORT);
                // Log.e("oldcwj", "eeeeeee");
                if (Api.getInstance().loginProcess(VerifyByBoxAct.this, Engine.getInstance().getUserName())) {
                    openActivity(HomePageActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Engine.getInstance().tryToStartNR(VerifyByBoxAct.this);
                    //盒子验证登陆成功，取消弹框
                    runOnUiThread(new Runnable() {
						public void run() {
							alertWait(false, "");
		                    finish();
						}
					});
                    
                }else{
                	 runOnUiThread(new Runnable() {
 						public void run() {
 							 alertWait(false, "");
 		                	 showAppAlert("", "登陆失败", "ok");
 						}
 					});
                	
                }
            }
        }).start();
    }

    private void gotoVerify()
    {
        if (!chkPhone.isChecked())
        {
            return;
        }
        
        final String auth_num = etAuthCode.getText().toString();
        if (TextUtils.isEmpty(auth_num))
        {
            Utils.showToast(VerifyByBoxAct.this, getResources()
                    .getString(R.string.enterCorrectAuthCode));
            return;
        }
        
        ProgressOverlay overlay = new ProgressOverlay(this);
        overlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
            	Api.getInstance().verify(VerifyByBoxAct.this, username, auth_num, new RequestResultListener() {

					@Override
					public void success(ResponseInfo responseInfo) {
						openActivity(HomePageActivity.class,
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
					}

					@Override
					public void fail(ResponseInfo responseInfo) {
						Res.toastErrCode(VerifyByBoxAct.this, responseInfo.getMsg());
					}
            		
            	});
                
            }
        });
    }
    
}
