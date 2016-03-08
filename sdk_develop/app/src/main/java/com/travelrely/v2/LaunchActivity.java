package com.travelrely.v2;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sdk.travelrely.lib.TRAction;
import sdk.travelrely.lib.TRDevice;
import sdk.travelrely.lib.TRSdk;
import sdk.travelrely.lib.device.manager.BLEManager;
import sdk.travelrely.lib.minterface.ITRCallback;
import sdk.travelrely.lib.util.AndroidUtil;
import sdk.travelrely.lib.util.LogUtil;
import sdk.travelrely.lib.util.ToastUtil;

public class LaunchActivity extends Activity implements ITRCallback, View.OnClickListener {

    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luanch);

        TRSdk.init(this).setDebug(true);

        PackageManager manager = getPackageManager();
        int type = manager.checkPermission(Manifest.permission.ACCESS_WIFI_STATE, getPackageName());
        if(type == PackageManager.PERMISSION_GRANTED) {
            LogUtil.d("check permission access_wifi ->" + type);
        }else{
            LogUtil.d("check permission not allowed access_wifi ->" + type);
        }

        findViewById(R.id.stopBle).setOnClickListener(this);
        findViewById(R.id.startBle).setOnClickListener(this);
        findViewById(R.id.emailto).setOnClickListener(this);
        findViewById(R.id.clearlog).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);


        final TextView logview = (TextView) findViewById(R.id.logtext);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollview);
        ListView listView = (ListView) findViewById(R.id.blelist);
        adapter = new ListAdapter(getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = (BluetoothDevice) adapter.getItem(position);
                TRSdk.getInstance().getDevice().pairByDevice(device);
            }
        });



        LogUtil.setLogCallback(new LogUtil.LogCallback() {
            @Override
            public void log(final String info) {
                LaunchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (logview != null)
                            logview.append(info + "\n");
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    private static class ListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> devices = new ArrayList<>();
        private LayoutInflater inflater;

        static class Holder {
            public TextView title;
            public TextView mac;
        }


        public ListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void addData(BluetoothDevice device) {
            devices.add(device);
            notifyDataSetChanged();
        }

        public void addAll(Collection<BluetoothDevice> collection) {
            devices.addAll(collection);
            notifyDataSetChanged();
        }

        public void clear() {
            devices.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (devices == null) return 0;
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            if (position > devices.size()) position = devices.size() - 1;
            if (position < 0) position = 0;
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.simple_layout, null);
                holder = new Holder();
                holder.title = (TextView) convertView.findViewById(R.id.name);
                holder.mac = (TextView) convertView.findViewById(R.id.mac);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            BluetoothDevice device = (BluetoothDevice) getItem(position);
            if (device != null && holder != null) {
                holder.title.setText(device.getName());
                holder.mac.setText(device.getAddress());
            }

            return convertView;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void faild(Object message) {
        ToastUtil.showLong(getApplicationContext(), message.toString());
    }

    @Override
    public void findDevice(BluetoothDevice device) {
        if (adapter != null) {
            adapter.addData(device);
        }
        System.out.println("发现设备->" + device.getName());
    }

    @Override
    public void findPairedDevice(BluetoothDevice device) {
        System.out.println("发现已配对设备->" + device.getName());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stopBle:
                TRSdk.getInstance().getDevice().stopScan();
                TRSdk.getInstance().getDevice().release();
                break;
            case R.id.startBle:
                if (adapter != null) {
                    adapter.clear();
                }
                TRSdk.getInstance().getDevice().startScan(LaunchActivity.this);
                break;
            case R.id.emailto:
                //AndroidUtil.openSystemSetting(AndroidUtil.ACTION_SOUND_SETTINGS, this);
                try {
//                    Intent data = new Intent(Intent.ACTION_SENDTO);
//                    data.setData(Uri.parse("mailto:135235621@qq.com"));
//                    data.putExtra(Intent.EXTRA_SUBJECT, "log");
//                    String content = ((TextView) findViewById(R.id.logtext)).getText().toString();
//                    data.putExtra(Intent.EXTRA_TEXT, content);
//                    startActivity(data);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String content = ((TextView) findViewById(R.id.logtext)).getText().toString();
                            MailUtil util = new MailUtil("smtp.qq.com", 25, "135235621@qq.com", "19830919");
                            util.sendEmail("135235621@qq.com", "log", content, new MailUtil.SendResult() {
                                @Override
                                public void success() {
                                    LaunchActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LaunchActivity.this, "发送成功", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void faild() {
                                    LaunchActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LaunchActivity.this, "发送失败", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }

                                @Override
                                public void start() {
                                    LaunchActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LaunchActivity.this, "正在发送邮件...", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }
                            });
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.disconnect:
                TRSdk.getInstance().disconnectBlueTooth();
                break;
            case R.id.clearlog:
                ((TextView) findViewById(R.id.logtext)).setText("");
                break;
        }
    }
}
