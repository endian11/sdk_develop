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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.travelrely.app.view.FormsArrawsRightButton;
import com.travelrely.app.view.FormsRightOnOff;
import com.travelrely.app.view.ListViewForScrollView;
import com.travelrely.core.glms.ProgressOverlay;
import com.travelrely.core.glms.ProgressOverlay.OnProgressEvent;
import com.travelrely.core.nrs.ConstantValue;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.IAction;
import com.travelrely.core.nrs.IntentMsg;
import com.travelrely.core.nrs.ble.BleMsgId;
import com.travelrely.core.nrs.nr.BoxManager;
import com.travelrely.core.nrs.nr.msg.MsgId;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.util.LogUtil;
import com.travelrely.core.util.SpUtil;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;
import com.travelrely.v2.net_interface.CancelVsimReq;
import com.travelrely.v2.net_interface.CancelVsimRsp;
import com.travelrely.v2.net_interface.GetKiReq;
import com.travelrely.v2.net_interface.GetKiRsp;
import com.travelrely.v2.net_interface.NotifyKiSuccReq;
import com.travelrely.v2.net_interface.NotifyKiSuccRsp;

/**
 * 选择蓝牙盒子
 * 
 * @author Travelrely
 */
public class NRAct extends NavigationActivity implements
		OnItemClickListener, OnCheckedChangeListener
{
	
    private final static String TAG = NRAct.class.getSimpleName();
    
	private LayoutInflater inflater;

	private FormsRightOnOff nrSwitch;
	private FormsRightOnOff vSimSwitch;
	
	private FormsArrawsRightButton btnTestCall;
	
	private RelativeLayout vMatched;
	private TextView tvName;
	private TextView tvAddr;
	private ImageView ivState;
	
	private TextView tvScanning;
	
	private BtBoxAdapter lvAdapter;

	// 存放可用于显示的蓝牙盒子
    private List<BluetoothDevice> devList = new ArrayList<BluetoothDevice>();

	private static final int REQUEST_ENABLE_BT = 2;
	private BluetoothAdapter mBtAdapter;

	private boolean isClickScan = false;
	private boolean isScanEnable = false;
	boolean mScanning = false;
	
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
    
    private OnCheckedChangeListener vSimSwitchListener = new OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (isChecked)
            {
                if (!TextUtils.isEmpty(SpUtil.getBtAddr()))
                {
                    alertWait(true, "正在写号...");
                    Engine.getInstance().startBleService(getApplicationContext(), 
                            BleMsgId.UI_BLE_READ_UL01);
                }
                else
                {
                    showAppAlert("提示", "请与蓝牙设备的配对", "知道了");
                }
            }
            else
            {
                alertWait(true, "正在注销...");
                gotoCancelVsim();
            }
        }
    };
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.act_nr);

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
		
	
		
	    nrSwitch = (FormsRightOnOff) findViewById(R.id.nrSwitch);
	    nrSwitch.setTextLeft("旅信网络电话服务");
	    nrSwitch.setToggleBackground(R.drawable.nr_switch_bg);
	    
	    vSimSwitch = (FormsRightOnOff) findViewById(R.id.vSimSwitch);
	    vSimSwitch.setTextLeft("蓝牙烧号注册/注销");
	    vSimSwitch.setToggleBackground(R.drawable.nr_switch_bg);
	    vSimSwitch.tButton.setChecked(false);
	    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
	    
	    btnTestCall = (FormsArrawsRightButton) findViewById(R.id.btnTestCall);
	    btnTestCall.setLeftText("拨打旅信测试电话");
	    btnTestCall.setImageResource(R.drawable.nr_test);
	    
	    vMatched = (RelativeLayout) findViewById(R.id.vMatched);
	    tvName = (TextView) findViewById(R.id.tvName);
	    tvAddr = (TextView) findViewById(R.id.tvAddr);
	    ivState = (ImageView) findViewById(R.id.ivState);
	    
	    tvScanning = (TextView) findViewById(R.id.tvScanning);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		
		nrSwitch.tButton.setOnCheckedChangeListener(null);
		if (Engine.getInstance().isNRSwitchOn)
		{
		    nrSwitch.tButton.setChecked(true);
		   
		}
		else
		{
		    nrSwitch.tButton.setChecked(false);
		}
		nrSwitch.tButton.setOnCheckedChangeListener(this);

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
		
		String btAddr = SpUtil.getBtAddr();
        if (TextUtils.isEmpty(btAddr))
        {
            vMatched.setVisibility(View.GONE);
        }
        else
        {
            showMatched();
            ivState.setImageResource(R.drawable.chk_yes);
//            startScan();//已经配对过了，不需要扫描
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
    }

    @Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

    @Override
	protected void initNavigationBar()
	{
		setTitle(R.string.NoRoaming);
		getNavigationBar().setRightBackground(R.drawable.help);
	}

    @Override
    public void onRightClick()
    {
    	super.onRightClick();
//        Intent intent = new Intent(this, BrowserActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("TITLE", "Q&A");
//        bundle.putString("URL", "http://115.28.52.8/carriers/noroaminghelp.html");
//        bundle.putString("ACTION", "");
//        intent.putExtras(bundle);
//        startActivity(intent);
    	// TODO cwj
    }
    
    @Override
    protected void onBroadcastReceived(Context context, Intent intent)
    {
        super.onBroadcastReceived(context, intent);
        
        String action = intent.getAction();
        
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
            lvAdapter.setSelectItem(-1);
            
            showMatched();
           
            ivState.setImageResource(R.drawable.chk_yes);
            
            clearDevList();
//            LogUtil.e("box imsi: nr ", SpUtil.getImsiInBox());
            
            
            Engine.getInstance().startNRService(getApplicationContext(),
                    MsgId.APP_INNER_RESET);
        }
        
        if (IAction.BOX_MATCH_FAIL.equals(action))
        {
            lvAdapter.setSelectItem(-1);
            ivState.setImageResource(R.drawable.chk_yes);
        }
        
        if (IAction.BOX_MISS_MATCH.equals(action))
        {
            vMatched.setVisibility(View.GONE);
        }
        
        if (IAction.NR_CLOSED.equals(action))
        {
            showAppAlert("提示", "您已关闭旅信网络电话服务，请手动开启", "关闭");
        }
        
        if (IAction.NR_CLOSE_SUCC.equals(action))
        {
            if (SpUtil.getXHService() == 0)
            {
                //Engine.getInstance().stopNR(getApplicationContext());
            }
        }
        
        if (IAction.OP_VSIM.equals(action))
        {
            int id = intent.getIntExtra(IntentMsg.INTENT_ID, 0);
            switch (id)
            {
                case IntentMsg.WR_SIM_READ_UL01_SUCC:
                    gotoSendUl01AndGetKi();
                    break;

                case IntentMsg.WR_SIM_READ_UL01_EXPIRE:
                    alertWait(false, null);
                    showAppAlert("提示", "读取上行01超时", "关闭");
                    vSimSwitch.tButton.setOnCheckedChangeListener(null);
                    vSimSwitch.tButton.setChecked(false);
                    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
                    break;
                    
                case IntentMsg.WR_SIM_READ_UL01_FAIL:
                    alertWait(false, null);
                    String e = intent.getStringExtra(IntentMsg.INTENT_STR_MSG);
                    showAppAlert("提示", "读取上行01失败("+e+")", "关闭");
                    vSimSwitch.tButton.setOnCheckedChangeListener(null);
                    vSimSwitch.tButton.setChecked(false);
                    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
                    break;
                    
                case IntentMsg.WR_SIM_SAVE_DL01_SUCC:
                    alertWait(false, null);
                    showAppAlert("提示", "蓝牙写号成功", "关闭");
                    gotoNotifyKiSucc(BoxManager.getDefault().getUl02Ota());
                    break;
                    
                case IntentMsg.WR_SIM_SAVE_DL01_EXPIRE:
                    alertWait(false, null);
                    showAppAlert("提示", "保存下行01超时", "关闭");
                    vSimSwitch.tButton.setOnCheckedChangeListener(null);
                    vSimSwitch.tButton.setChecked(false);
                    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
                    break;
                    
                case IntentMsg.WR_SIM_SAVE_DL01_FAIL:
                    alertWait(false, null);
                    e = intent.getStringExtra(IntentMsg.INTENT_STR_MSG);
                    showAppAlert("提示", "保存下行01失败("+e+")", "关闭");
                    vSimSwitch.tButton.setOnCheckedChangeListener(null);
                    vSimSwitch.tButton.setChecked(false);
                    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
                    break;
                    
                case IntentMsg.WR_SIM_SAVE_DL04_SUCC:
                    alertWait(false, null);
                    showAppAlert("提示", "VSIM注销成功", "关闭");
                    gotoNotifyKiSucc(BoxManager.getDefault().getUl04Ota());
                    break;
                    
                case IntentMsg.WR_SIM_SAVE_DL04_EXPIRE:
                    alertWait(false, null);
                    showAppAlert("提示", "保存下行04超时", "关闭");
                    vSimSwitch.tButton.setOnCheckedChangeListener(null);
                    vSimSwitch.tButton.setChecked(true);
                    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
                    break;
                    
                case IntentMsg.WR_SIM_SAVE_DL04_FAIL:
                    alertWait(false, null);
                    e = intent.getStringExtra(IntentMsg.INTENT_STR_MSG);
                    showAppAlert("提示", "保存下行04失败("+e+")", "关闭");
                    vSimSwitch.tButton.setOnCheckedChangeListener(null);
                    vSimSwitch.tButton.setChecked(true);
                    vSimSwitch.tButton.setOnCheckedChangeListener(vSimSwitchListener);
                    break;

                default:
                    alertWait(false, null);
                    showAppAlert("提示", "VSIM访问过程超时", "关闭");
                    break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked)
        {
            if (!TextUtils.isEmpty(SpUtil.getBtAddr()))
            {
                Engine.getInstance().switchOnNR(this);
            }
            else
            {
                Engine.getInstance().isNRSwitchOn = true;
                showAppAlert("提示", "请先进行APP与蓝牙设备的配对", "知道了");
            }
        }
        else
        {
            Engine.getInstance().switchOffNR(this);
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
	    if (lvAdapter.getSelectItem() == position)
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
	    
	    Engine.getInstance().startNRService(NRAct.this);
	    Engine.getInstance().stopBleService(NRAct.this);
	    
	    LogUtil.i(TAG, "2秒后发起配对");

        lvAdapter.setSelectItem(position);
        
//        handler.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                Engine.getInstance().startBleService(NRAct.this,
//                        BleMsgId.NR_BLE_MATCH_BOX,
//                        device.getAddress().getBytes());
//            }
//        });
        handler.postDelayed(new Runnable()
        {
        	@Override
        	public void run()
        	{
        		Engine.getInstance().startBleService(NRAct.this,
        				BleMsgId.NR_BLE_MATCH_BOX,
        				device.getAddress().getBytes());
        	}
        }, 2000);
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
//            if (device.getAddress().equals(SpUtil.getBtAddr()))
//            {
//                LogUtil.i(TAG, "find matched box, update UI");
//
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        ivState.setImageResource(R.drawable.chk_yes);
//                    }
//                });
//                
//                // 非点击扫描则停止扫描，如果是点击扫描的话就继续扫描其他盒子
//                if (!isClickScan)
//                {
//                	tvScanning.setVisibility(View.GONE);
//                    mHandler.removeCallbacks(mStopScanTask);
//                    stopScan();
//                }
//                
//                return;
//            }
            
            // 非点击扫描,继续寻找已配对盒子,忽略其他盒子
            if (!isClickScan)
            {
                return;
            }
            
            // 不是已配对盒子则进行盒子过滤显示
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
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (device.getAddress().equals(SpUtil.getBtAddr()))
                    {
                        LogUtil.i(TAG, "扫描到已配对盒子");
                        ivState.setImageResource(R.drawable.chk_yes);
                        
                        if (!isClickScan)
                        {
                            mHandler.removeCallbacks(mStopScanTask);
                            stopScan();
                        }
                    }
                }
            });
            
            if (!isClickScan)
            {
                return;
            }
            
            //if (device.getAddress().startsWith("00:41:57"))
            {
                scanLeDevice(false);
                Engine.getInstance().startBleService(NRAct.this,
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
            if (deviceName != null && deviceName.length() > 0)//各个厂家的要过滤一下名字
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
                holder.devState.setText("重配对");
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
        	//有的盒子的名称读取出来会为空，所以要加个判断
        	if (TextUtils.isEmpty(name)){
        		//如果名字为空，暂时不提示用户
        		
        	}else{
        		
        		if (name.contains(ConstantValue.BLE_brand)){
        			devList.add(dev);
        			lvAdapter.notifyDataSetChanged();
        		}
        	}
        }
    }
    
    private void clearDevList()
    {
        devList.clear();
        lvAdapter.notifyDataSetChanged();
    }
    
    public void testCall(View src)
    {
        Engine.getInstance().startNRService(this,
                MsgId.APP_AGENT_TEST_CALLING_REQ);
    }
    
    public void onScan(View src)
    {        
        if (!isScanEnable)
        {
            isClickScan = true;
            clearDevList();
            startScan();
        }
    }
    
    void startScan()
    {
    	if(!isClickScan){
    		
    		tvScanning.setVisibility(View.GONE);
    	}else{
    		
    		tvScanning.setVisibility(View.VISIBLE);
    	}
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
    
    void showMatched()
    {
        vMatched.setVisibility(View.VISIBLE);
        tvName.setText(SpUtil.getBtName());
        tvAddr.setText(SpUtil.getBtAddr());
        int cosVer = SpUtil.getCosVer();
        StringBuilder s = new  StringBuilder();
        int boxBattery = SpUtil.getBoxBattery();
        byte[] cosBytes = ByteUtil.getBytes(cosVer);
    	for (int i=cosBytes.length-1; i>=0;i--){
    		byte b = cosBytes[i];
//    		int a = b;
//    		System.out.println("aaaaaaaaaaaa in nr " + a );
    		s.append(b);
    	}
    	Drawable drawable;
        if (boxBattery == -1 || boxBattery == 0){
        		
        	tvAddr.setText("版本号:"+ s.toString() + " 电量:未知" ); 
        }else{
        	if ( boxBattery>0 && boxBattery <=30){
        		drawable = getResources().getDrawable(R.drawable.low);
        		
        	}else if(boxBattery >30 && boxBattery <=50){
        		drawable = getResources().getDrawable(R.drawable.half);
        	}else if(boxBattery >50 && boxBattery<=80){
        		drawable = getResources().getDrawable(R.drawable.more);
        	}else{
        		drawable = getResources().getDrawable(R.drawable.full);
        	}
        	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        	tvAddr.setCompoundDrawables(null, null, drawable, null);
        	tvAddr.setText("版本号:"+ s.toString() + " 电量:" ); 
        	
        }
        
        tvAddr.setTextSize(16);
        // tvAddr.setTextColor(com.travelrely.v2.R.color.gray);
    }
    
    void gotoSendUl01AndGetKi()
    {
        ProgressOverlay mOverlay = new ProgressOverlay(this);
        mOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                byte[] ulota = BoxManager.getDefault().getUl01Ota();
                GetKiRsp rsp = GetKiReq.getKi(NRAct.this, 
                        Utils.bytesToHexString(ulota));
                if (rsp == null)
                {
                    return;
                }
                
                if (!rsp.getBaseRsp().isSuccess())
                {
                    return;
                }
                
                byte[] dlota = Utils.HexStringToBytes(rsp.getData().getDl01OTA());
                
                BoxManager.getDefault().setDl01Ota(dlota);
                Engine.getInstance().startBleService(NRAct.this,
                        BleMsgId.UI_BLE_SAVE_DL01);
            }
        });
    }
    
    void gotoNotifyKiSucc(final byte[] ota)
    {
        ProgressOverlay mOverlay = new ProgressOverlay(this);
        mOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                NotifyKiSuccRsp rsp = NotifyKiSuccReq.notifySucc(NRAct.this, 
                        Utils.bytesToHexString(ota));
                if (rsp == null)
                {
                    return;
                }
                
                if (!rsp.getBaseRsp().isSuccess())
                {
                    return;
                }
            }
        });
    }
    
    void gotoCancelVsim()
    {
        ProgressOverlay mOverlay = new ProgressOverlay(this);
        mOverlay.show("", new OnProgressEvent()
        {
            @Override
            public void onProgress()
            {
                CancelVsimRsp rsp = CancelVsimReq.getCancelOta(NRAct.this, "");
                if (rsp == null)
                {
                    return;
                }
                
                if (!rsp.getBaseRsp().isSuccess())
                {
                    return;
                }
                
                byte[] ota = Utils.HexStringToBytes(rsp.getData().getDl04OTA());
                
                BoxManager.getDefault().setDl04Ota(ota);
                Engine.getInstance().startBleService(NRAct.this,
                        BleMsgId.UI_BLE_CANCLE_VSIM);
            }
        });
    }
}
