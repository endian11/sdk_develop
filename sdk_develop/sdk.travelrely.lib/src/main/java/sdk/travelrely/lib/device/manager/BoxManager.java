package sdk.travelrely.lib.device.manager;

import sdk.travelrely.lib.Constant;
import sdk.travelrely.lib.device.exception.BLEException;
import sdk.travelrely.lib.device.util.BLEUtil;
import sdk.travelrely.lib.util.TextUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.manager
 * ＊ 16:02
 */
public class BoxManager {

    private static BoxManager manager = new BoxManager();

    private BoxManager() {

    }

    public static BoxManager getDefaultBoxManager() {
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

    }

    /**
     * 读取电量
     */
    public void readPowerLevel() {
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
        if (!CheckPair()) {
            //TODO 未配对成功
            doPair();
        } else {
            //TODO 配对成功

        }
    }

    /**
     * 检测是否已经配对过
     *
     * @return
     */
    private Boolean CheckPair() {

        return false;
    }
}
