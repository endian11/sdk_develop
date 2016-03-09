package sdk.travelrely.lib;

import java.util.UUID;

/**
 * Created by weihaichao on 16/3/1.
 *
 */
public final class Constant {
    public static final String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    public static final String DESCRIB_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    public final static String UUID_TX = "d44bc439-abfd-45a2-b575-925416129600";
    public final static String UUID_RX = "d44bc439-abfd-45a2-b575-925416129601";
    public final static UUID UUID_HEART_RATE_MEASUREMENT =
            UUID.fromString(SERVICE_UUID);

    /** 获取当前盒子是否保存配对key **/
    public static final byte[] keyStateReq = new byte[]{0x0F, 0x0F, 0x57, 0x00, 0x00};
   /** 获取盒子mac地址指令 **/
    public static final byte[] macAddress  = new byte[]{0x0F, 0x0F, 0x57, 0x01, 0x00};
    /** 保存key指令 **/
    public static final byte[] keySaveReq = new byte[]{0x0F, 0x0F, 0x57, 0x02, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    /** key检测指令 **/
    public static final byte[] keyChkReq = new byte[]{0x0F, 0x0F, 0x57, 0x03, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    /** 获取电池电量指令 **/
    public static final byte[] BatteryQueryReq = new byte[]{0x0F, 0x0F, 0x57, 0x19, 0x00};
    /** 获取COS版本号指令 **/
    public static final byte[] CosVerReq = new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, 0x51, 0x00, 0x02, 0x04};
    /**查询盒子SN码指令**/
    public static final byte[] boxSnReq = new byte[]{(byte) 0xF0, (byte) 0xF0, 0x75, 0x03, 0x00, 0x51, 0x00, 0x01, 0x0A};
}
