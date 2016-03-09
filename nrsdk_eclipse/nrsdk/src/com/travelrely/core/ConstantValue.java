package com.travelrely.core;

public interface ConstantValue {
	/**
	 * 烧号相关URL
	 */
	String ShaoHaoUrl = "http://210.51.190.112";
	
	String GetKiPath = "/tps/getkiinfo";
	String NotifyKiSuccess = "/tps/notifykisuccess";
	
	boolean connectGattFlag = false;//connectGatt(, . ) 连接蓝牙标志
	
	CharSequence BLE_brand = "";//厂商的盒子关键字

	String INComingCall = "0";//服务器告知来电
	String FetchMsg = "1";//服务器通知取消息

	String PartnerID = "0000010100030500";// 合作伙伴公司ID标识 ,目前只有前面8个字符有用00000101 
	
	public static final String callVoice = "call_voice_onoff";
    public static final String callVabration = "call_vabration_onoff";
    public static final String startTimeKey = "no_roaming_start_time_type";
    public static final String endTimeKey = "no_roaming_end_time_type";
    public static final String layoutTypeKey = "no_roaming_type"; // 1==开启 0==关闭
    																// 2==时差模式
    
}
