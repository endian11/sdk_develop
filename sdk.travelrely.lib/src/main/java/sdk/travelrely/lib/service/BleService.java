package sdk.travelrely.lib.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import sdk.travelrely.lib.IBleCallback;
import sdk.travelrely.lib.IBleServiceAidl;
import sdk.travelrely.lib.util.LogUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/3.
 * ＊ sdk.travelrely.lib.service
 * ＊ 16:15
 */
public class BleService extends Service {

    public static final String ACTION = BleService.class.getName();

    private IBleCallback mCallback;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("receive receiver -> " + intent.getAction());
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                //the device is found
                LogUtil.d("found new bluetooth device...");
                if (mCallback != null) {
                    try {
                        mCallback.found();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("bleservice is bind");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.d("bleservice is start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registReceiver();
        LogUtil.d("bleservice is startCommand");
        //return super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LogUtil.d("bleservice is destroy");
        super.onDestroy();
        unregistReceiver();
    }

    private void registReceiver() {
        LogUtil.d("bleservice is registreceiver");
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    private void unregistReceiver() {
        LogUtil.d("bleservice is unregistreceiver");
        unregisterReceiver(receiver);
    }
}
