package com.travelrely.core.nr.util;

import com.travelrely.core.nr.LockService;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class CallUtil {

	public static int enableSpeaker(AudioManager mAudioManager, Boolean isSpeakerOn) {
        if(shouldUseRoutingApi()) {
           mAudioManager.setRouting(AudioManager.MODE_IN_CALL, isSpeakerOn?AudioManager.ROUTE_SPEAKER:AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
          }else {	  		         
        	  if (android.os.Build.BRAND.equals("Samsung")) {
        		  if (isSpeakerOn) {
        			  // route audio to back speaker
        			  //mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		  } else {
        			  // route audio to earpiece
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		  }
        	  }else
        	  {
        		  if(android.os.Build.FINGERPRINT.contains("Huawei/D2-2010"))
        		  {
        			  if (isSpeakerOn) {
        				  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        				  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  } else {
        			  // route audio to earpiece
        				  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        				  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  }
        		  }else if(android.os.Build.FINGERPRINT.contains("Huawei/P7-L09"))
        		  {
            		  if (isSpeakerOn) {
            			  // route audio to back speaker
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            		  } else {
            			  // route audio to earpiece
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            		  }
        		  }else if(android.os.Build.FINGERPRINT.contains("Huawei/P6-C00"))
        		  {
            		  if (isSpeakerOn) {
            			  // route audio to back speaker
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            		  } else {
            			  // route audio to earpiece
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            		  }
        		  }else if(android.os.Build.FINGERPRINT.contains("Xiaomi/mione_plus"))
        		  {
            		  if (isSpeakerOn) {
            			  // route audio to back speaker
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            		  } else {
            			  // route audio to earpiece
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            		  }
        		  }else if(android.os.Build.FINGERPRINT.contains("Huawei/C8815"))
        		  {
            		  if (isSpeakerOn) {
            			  // route audio to back speaker
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            		  } else {
            			  // route audio to earpiece
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            		  }
        		  }else if(android.os.Build.FINGERPRINT.contains("Huawei/C8813"))
        		  {
            		  if (isSpeakerOn) {
            			  // route audio to back speaker
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            		  } else {
            			  // route audio to earpiece
            			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
            		  }
        		  }else if(android.os.Build.FINGERPRINT.contains("htc_europe/shooteru"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("lge/b1w_open_tw"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("MOTO/XT882_CT"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("motorola/XT928_ct_cn"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("Xiaomi/aries"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("Xiaomi/cancro"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("Xiaomi/cancro_wc_lte"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  //mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("htccn_chs/htc_shooteru"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  //mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("vivo/msm8974"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  //mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("samsung/e7ltezc"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else if(android.os.Build.FINGERPRINT.contains("vivo/bbk6752_lwt_kk"))
    		      {
        		      if (isSpeakerOn) {
        			  // route audio to back speaker
        			  mAudioManager.setMode(AudioManager.MODE_NORMAL);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } else {
        			  // route audio to earpiece
        		      mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        			  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        		      } 
        		  }else{
        			  if (isSpeakerOn) {
        				  mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        				  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        			  } else {
        			  // route audio to earpiece
        				  mAudioManager.setSpeakerphoneOn(isSpeakerOn);
        				  mAudioManager.setMode(AudioManager.MODE_IN_CALL);
        			  }  
        		  }
        	  }
	         
	  }
         return 0;
    }
	
	private static boolean shouldUseRoutingApi() {
        // HTC evo 4G
        if (android.os.Build.PRODUCT.equalsIgnoreCase("htc_supersonic")) {
            return true;
        }

            // ZTE joe
            if (android.os.Build.DEVICE.equalsIgnoreCase("joe")) {
                return true;
            }
    
    	    if(android.os.Build.DEVICE.equalsIgnoreCase("hwC8813Q")){
    	         return true;
    	    }
    	
            // Samsung GT-S5830
            if (android.os.Build.DEVICE.toUpperCase().startsWith("GT-S")) {
                return true;
            }
    
            if (!isCompatible(4)) {
                // If android 1.5, force routing api use
                return true;
            } else {
                return false;
            }
   }
	
	private static boolean isCompatible(int apiLevel) {
        return android.os.Build.VERSION.SDK_INT >= apiLevel;
     }
	
	public static void unlockScreen(Context context, boolean on)
    {
        Intent intent = new Intent(context, LockService.class);

        if (on)
        {
            context.startService(intent);
        }
        else
        {
            context.stopService(intent);
        }
    }


}
