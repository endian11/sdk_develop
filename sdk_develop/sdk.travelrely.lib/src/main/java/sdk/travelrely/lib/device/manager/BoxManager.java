package sdk.travelrely.lib.device.manager;

import android.os.Message;
import android.text.TextUtils;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.TRAction;
import sdk.travelrely.lib.TRSdk;
import sdk.travelrely.lib.device.BLE;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.util.BLEUtil;
import sdk.travelrely.lib.minterface.TRAlertCallback;
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
                boolean boxHasKay = (boolean) message.obj;
                String localKey = SharedUtil.get(TRAction.SHARED_BT_KEY, "");
                if (boxHasKay) {
                    LogUtil.d("有key");//证明该盒子 已经跟别的手机配对过。
                    if (TextUtils.isEmpty(localKey)) {
                        //TODO 提示用户是否重新配对
                        TRSdk.getInstance().alert("请重新配对盒子");
                    } else {
                        //TODO 执行检验key 验证盒子里的key是否和本地key一致
                        DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendChkKey();
                            }
                        }, 100);
                    }

                } else {
                    LogUtil.d("无key");
                    DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doPair();
                        }
                    }, 100);
                }
                break;
            case BLE.ACTION_CHECK_KEY:
                LogUtil.d("receive BoxManager.ACTION_CHECK_KEY :" + message.obj);
                if ((Boolean) message.obj) {
                    DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CheckKeyComplete();
                        }
                    }, 100);
                } else {
                    TRSdk.getInstance().alert("请重新配对盒子");
                }
                break;
            case BLE.ACTION_READ_MAC:
                //TODO 读取MAC地址结果
                byte[] mac = (byte[]) message.obj;
                SharedUtil.set(TRAction.SHARED_BT_ADDR, ByteUtil.toMacAddr(mac));
                LogUtil.d("mac addr: " + ByteUtil.toMacAddr(mac));
                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readCosVersion();
                    }
                }, 100);
                break;
            case BLE.ACTION_GEMERAT_KEY:
                //TODO 保存key成功结果

                DeviceManagerObersver.get().getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        readMacAddress();
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
                        SimManager.getDefault().readSimInfo(false);
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
        generateKey();
    }

    /**
     * 读取MAC地址
     */
    private void readMacAddress() {
        //TODO 读取盒子MAC地址
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
        //TODO 生成六位随机值 发送保存key
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
        //TODO 读取COS版本号信息
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
        //TODO 读取MT100 序列号
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
        //TODO 读取剩余电量
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
        LogUtil.d("当前BLE状态："+BLE.CURRENT_BLE_MODE);
        switch (BLE.CURRENT_BLE_MODE) {
            case BLE.BLE_MODE_NORMAL:
                CheckPair();
                break;
            case BLE.BLE_MODE_CALL:
            case BLE.BLE_MODE_SMS:

                break;
            default:
                break;
        }
    }

    /**
     * 校验key完成 选择执行
     */
    private void CheckKeyComplete() {
        switch (BLE.CURRENT_BLE_MODE) {
            case BLE.BLE_MODE_NORMAL:
                SimManager.getDefault().ReadSim();
                break;
            case BLE.BLE_MODE_CALL:
            case BLE.BLE_MODE_SMS:

                break;
            default:
                break;
        }
    }

    /**
     * 检测是否已经配对过
     */
    private void CheckPair() {
        //TODO 检查设备是否已配对过
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
        //TODO 发送校验KEY指令
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
