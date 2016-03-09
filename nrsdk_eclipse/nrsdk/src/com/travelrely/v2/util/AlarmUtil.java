package com.travelrely.v2.util;

/**
 * 闹钟相关
 * @author Travelrely
 *
 */
public class AlarmUtil {
    
    /**
     * 
     * @param msg
     * @param alarmMsg[0]date
     * @param alarmMsg[1]time
     * @param alarmMsg[2]name
     * @return
     */
    public static String[] analysisAlarmMsg(String msg){
        
        String[] alarmMsg = null;
        
        alarmMsg = AESUtils.getDecryptString(msg).split("\t");
        
        return alarmMsg;
        
    }

}
