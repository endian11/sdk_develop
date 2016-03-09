package sdk.travelrely.lib.device.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.TRSdk;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.util.BLEUtil;
import sdk.travelrely.lib.util.LogUtil;
import sdk.travelrely.lib.util.TextUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.manager
 * ＊ 16:02
 */
public class BoxManager {

    private static BoxManager manager = new BoxManager();

    /**
     * ProcessMessage 中处理该处蓝牙设备返回结果数据
     * 判断执行下一步所要执行的事件
     */

    public static final int ACTION_CHECK_KEY = 5;
    public static final int ACTION_READ_MAC = 0;
    public static final int ACTION_GEMERAT_KEY = 1;
    public static final int ACTION_READ_COS_VERSION = 2;
    public static final int ACTION_READ_MT_SN = 3;
    public static final int ACTION_READ_POWER_LEVEL = 4;

    public static int CURRENT_ACTION = -1;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ACTION_CHECK_KEY:
                    //TODO key检查结果，检查是否需要进行配对

                    break;
                case ACTION_READ_MAC:
                    //TODO 读取MAC地址

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            generateKey();
                        }
                    }, 100);
                    break;
                case ACTION_GEMERAT_KEY:
                    //TODO 保存key成功

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readCosVersion();
                        }
                    }, 100);
                    break;
                case ACTION_READ_COS_VERSION:

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readMtSN();
                        }
                    }, 100);
                    break;
                case ACTION_READ_MT_SN:

                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readPowerLevel();
                        }
                    }, 100);
                    break;
                case ACTION_READ_POWER_LEVEL:

                    break;
            }
        }
    };

    public Handler getHandler() {
        return mHandler;
    }

    private BoxManager() {

    }

    public static BoxManager getDefault() {
        return manager;
    }

    /**
     * 执行配对流程
     */
    public void doPair() {
        readMacAddress();
    }

    /**
     * 读取MAC地址
     */
    private void readMacAddress() {
        CURRENT_ACTION = ACTION_READ_MAC;
        LogUtil.d("send readMacAddress cmd");
        try {
            BLEManager.getDefault().send(Constant.macAddress);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成key 发送保存key命令
     */
    private void generateKey() {
        CURRENT_ACTION = ACTION_GEMERAT_KEY;
        LogUtil.d("send generateKey cmd");
        String key = TextUtil.getRandomString(6);
        byte[] sendReq = BLEUtil.getKeySaveReq(key);
        try {
            BLEManager.getDefault().send(sendReq);
        } catch (BLEException e) {
            e.printStackTrace();
        } finally {
            sendReq = null;
        }

    }

    /**
     * 读COS版本号
     */
    private void readCosVersion() {
        CURRENT_ACTION = ACTION_READ_COS_VERSION;
        LogUtil.d("send readCosVersion cmd");
        try {
            BLEManager.getDefault().send(Constant.CosVerReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取MT100序列号
     */
    private void readMtSN() {
        CURRENT_ACTION = ACTION_READ_MT_SN;
        LogUtil.d("send readMtSN cmd");

        try {
            BLEManager.getDefault().send(Constant.boxSnReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取电量
     */
    public void readPowerLevel() {
        CURRENT_ACTION = ACTION_READ_POWER_LEVEL;
        LogUtil.d("send readPowerLevel cmd");
        try {
            BLEManager.getDefault().send(Constant.BatteryQueryReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始执行后续操作
     */
    public void CheckTask() {
        //TODO 检测是否已经配对成功
        CheckPair();
    }

    /**
     * 检测是否已经配对过
     *
     * @return
     */
    private void CheckPair() {
        CURRENT_ACTION = ACTION_CHECK_KEY;
        try {
            BLEManager.getDefault().send(Constant.keyStateReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }
}
