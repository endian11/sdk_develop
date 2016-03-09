package sdk.travelrely.lib.device.manager;

import android.os.Message;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.TRAction;
import sdk.travelrely.lib.device.BLE;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.util.BLEUtil;
import sdk.travelrely.lib.obersver.DeviceManagerObersver;
import sdk.travelrely.lib.util.ByteUtil;
import sdk.travelrely.lib.util.LogUtil;
import sdk.travelrely.lib.util.SharedUtil;
import sdk.travelrely.lib.util.TextUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.manager
 * ＊ 16:02
 */
public class BoxManager extends BaseManager {

    private static BoxManager manager = new BoxManager();

    @Override
    public void receiveMessage(Message message) {
        super.receiveMessage(message);
        switch (message.what) {
            case BLE.ACTION_ISHAVE_KEY:
                //TODO key检查结果，检查是否需要进行配对
                boolean flag = (boolean) message.obj;
                if (flag) {
                    LogUtil.d("有key");//证明该盒子 已经跟别的手机配对过。
                    DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SimManager.getDefault().readSimInfo();
                        }
                    }, 100);

                } else {
                    LogUtil.d("无key");
                    DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            readMacAddress();
                        }
                    }, 100);
                }
                break;
            case BLE.ACTION_READ_MAC:
                //TODO 读取MAC地址
                byte[] mac = (byte[]) message.obj;
                SharedUtil.set(TRAction.SHARED_BT_ADDR, ByteUtil.toMacAddr(mac));
                LogUtil.d("mac addr: " + ByteUtil.toMacAddr(mac));
                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        generateKey();
                    }
                }, 100);
                break;
            case BLE.ACTION_GEMERAT_KEY:
                //TODO 保存key成功

                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readCosVersion();
                    }
                }, 100);
                break;
            case BLE.ACTION_READ_COS_VERSION:

                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readMtSN();
                    }
                }, 100);
                break;
            case BLE.ACTION_READ_MT_SN:

                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readPowerLevel();
                    }
                }, 100);
                break;
            case BLE.ACTION_READ_POWER_LEVEL:
                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SimManager.getDefault().readSimInfo();
                    }
                }, 100);

                break;
        }
    }

    private BoxManager() {
        super();
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
        BLE.CURRENT_ACTION = BLE.ACTION_READ_MAC;
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
        BLE.CURRENT_ACTION = BLE.ACTION_GEMERAT_KEY;
        LogUtil.d("send generateKey cmd");
        String key = TextUtil.getRandomString(6);
        SharedUtil.set(TRAction.SHARED_BT_KEY, key);//保存key
        byte[] sendReq = BLEUtil.getKeySaveReq(Constant.keySaveReq, key, 5, 6);
        try {
            BLEManager.getDefault().send(sendReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读COS版本号
     */
    private void readCosVersion() {
        BLE.CURRENT_ACTION = BLE.ACTION_READ_COS_VERSION;
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
        BLE.CURRENT_ACTION = BLE.ACTION_READ_MT_SN;
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
        BLE.CURRENT_ACTION = BLE.ACTION_READ_POWER_LEVEL;
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
     */
    private void CheckPair() {
        BLE.CURRENT_ACTION = BLE.ACTION_ISHAVE_KEY;
        try {
            BLEManager.getDefault().send(Constant.keyStateReq);
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验key
     */
    public void sendChkKey() {
        LogUtil.d("send sendChkKey cmd");
        BLE.CURRENT_ACTION = BLE.ACTION_CHECK_KEY;
        try {
            String key = SharedUtil.get(TRAction.SHARED_BT_KEY, "");
            byte[] sendReq = BLEUtil.getKeySaveReq(Constant.keyChkReq, key, 5, 6);
            if (sendReq != null) {
                BLEManager.getDefault().send(sendReq);
            }
        } catch (BLEException e) {
            e.printStackTrace();
        }
    }


}
