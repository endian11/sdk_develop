package sdk.travelrely.lib.device.message;

import android.os.Message;

import sdk.travelrely.lib.device.BLE;
import sdk.travelrely.lib.obersver.DeviceManagerObersver;
import sdk.travelrely.lib.util.ByteUtil;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.message
 * ＊ 17:50
 */
public class ProcessMessage {

    private ProcessMessage() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 派发交互事件
     *
     * @param Action
     * @param message
     */
    private static void dispatchAction(int Action, Message message) {
        message.what = Action;
        message.sendToTarget();
    }

    public static void process(byte[] message) {
        //TODO 解析信息
        Message parseMessage = DeviceManagerObersver.get().getHandler().obtainMessage();
        switch (BLE.CURRENT_ACTION) {
            case BLE.ACTION_ISHAVE_KEY://检查key是否存在
                // true 存在  false不存在
                if ((message[5] & 0xFF) == 0x01) {
                    //有key  key存在
                    parseMessage.obj = true;
                } else {
                    parseMessage.obj = false;
                }
                dispatchAction(BLE.ACTION_ISHAVE_KEY, parseMessage);
                break;
            case BLE.ACTION_READ_MAC://读mac地址
                int id = 0;
                if (message != null && message.length > 4) {
                    id = ByteUtil.getInt(message);
                }
                if (id == 0x81570F0F) {
                    byte[] mac = ByteUtil.subArray(message, 5, message[4] & 0xff);
                    //mac地址
                    parseMessage.obj = mac;
                }
                dispatchAction(BLE.ACTION_READ_MAC, parseMessage);
                break;
            case BLE.ACTION_GEMERAT_KEY:
                if ((message[5] & 0xff) == 0x00) {
                    parseMessage.obj = true;//保存key成功
                } else {
                    parseMessage.obj = false;
                }
                dispatchAction(BLE.ACTION_GEMERAT_KEY, parseMessage);
                break;
            case BLE.ACTION_READ_COS_VERSION:
                if ((message[0] & 0xFF) == 0x01
                        && (message[message.length - 2] & 0xFF) == 0x90
                        && (message[message.length - 1] & 0xFF) == 0x00) {
                    byte[] cosVer = ByteUtil.subArray(message, 2, message[1] & 0xff);
                    parseMessage.obj = cosVer;
                }
                dispatchAction(BLE.ACTION_READ_COS_VERSION, parseMessage);
                break;
            case BLE.ACTION_READ_MT_SN://读MT100 SN号
                if ((message[0] & 0xFF) == 0x01
                        && (message[message.length - 2] & 0xFF) == 0x90
                        && (message[message.length - 1] & 0xFF) == 0x00) {

                    byte[] MT100SN = ByteUtil.subArray(message, 2, message[1] & 0xff);
                    parseMessage.obj = MT100SN;
                }
                dispatchAction(BLE.ACTION_READ_MT_SN, parseMessage);
                break;
            case BLE.ACTION_READ_POWER_LEVEL://读电量
                if (ByteUtil.getInt(message) == 0x99570F0F) {
                    parseMessage.obj = message[5];
                }

                dispatchAction(BLE.ACTION_READ_POWER_LEVEL, parseMessage);
                break;
            case BLE.ACTION_CHECK_KEY:
                parseMessage.obj = (message != null && message.length > 4 && ByteUtil.getInt(message) == 0x83570F0F && message[message.length-1] == 0x00);
                dispatchAction(BLE.ACTION_CHECK_KEY, parseMessage);
                break;
            default:
                break;
        }
    }
}
