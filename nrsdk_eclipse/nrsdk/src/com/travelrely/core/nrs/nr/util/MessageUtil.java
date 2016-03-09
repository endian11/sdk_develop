package com.travelrely.core.nrs.nr.util;

import com.travelrely.core.util.Utils;
import com.travelrely.v2.response.TraMessage;

import android.os.Bundle;

public class MessageUtil {
	
	public static String getFromType(TraMessage m) {

        String nickName = m.getNick_name();
        if (m.isGroup()) {
            return m.getGroup_name();
        } else {
            if (m.getMsg_type() == 1) {
                return nickName;
            }
        }
        return nickName;
    }
	
	public static String generateContent(TraMessage m) {

        if (m.getContent().length() > 0)
        {
            String content = m.getDecryptContent();
            if (content != null)
            {
                if (m.isText())
                {
                    if (Utils.isSmilies(content))
                    {
                        return "[表情]";
                    }
                    String shortMessage = content;
                    if (content != null && content.length() > 20)
                    {
                        shortMessage = content.substring(0, 20) + "...";
                        return shortMessage;
                    }
                    else
                    {
                        return shortMessage;
                    }
                }
                else if (m.isJPG())
                {
                    return "[图片]";
                }
                else if (m.isMp3())
                {
                    return "[语音]";
                }
                else if (m.isLocation())
                {
                    return "[位置]";
                }
                else if (m.isAlarm())
                {
                    return "[闹钟]";
                }
                else if (m.isTrip())
                {
                    return "[行程]";
                }
                else if (m.isColltion())
                {
                    return "[定点集合]";
                }
                else if (m.isRegistration())
                {
                    return "[签到]";
                }
                else if (m.isRegistrationResponse())
                {
                    return "[签到回应]";
                }
                else if (m.isRadar())
                {
                    return "[位置雷达]";
                }
                else if (m.isRadarResponse())
                {
                    return "[位置雷达回应]";
                }
                else if (m.isVoic())
                {
                    return "[语音]";
                }
                else if (m.isConvene())
                {
                    return "[召集]";
                }
            }
        }
        return "";
    }

	public static Bundle generateBunlde(TraMessage message) {
        Bundle extras = new Bundle();

        String content = MessageUtil.generateContent(message);
        message.setContent(content);

        extras.putSerializable("message", message);

        // extras.putInt("message_id", );
        return extras;
    }
}
