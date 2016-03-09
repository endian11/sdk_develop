package com.travelrely.core.nrs;

import android.app.Activity;
import android.content.Context;

import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.Utils;
import com.travelrely.sdk.R;

public final class Res
{
	public static final int ACT_REQ_CODE_START_TIME = 0;
	public static final int ACT_REQ_CODE_END_TIME = 1;
	public static final int ACT_REQ_CODE_CALL_FWD_SET = 2;
	public static final int ACT_REQ_CODE_DATA_FLOW = 3;
	public static final int ACT_REQ_CODE_VOICE = 4;
	public static final int ACT_REQ_CODE_CARD_TYPE = 5;
	public static final int ACT_REQ_CODE_CARD_SCALE = 6;
	public static final int ACT_REQ_CODE_CARD_NUM = 7;
	public static final int ACT_REQ_CODE_SELECT_PACKAGE = 17;
	
	public static final int ACT_REQ_CODE_COUPONS = 8;
    public static final int ACT_REQ_CODE_ADD_SHIPADDR = 9;
    public static final int ACT_REQ_CODE_EDIT_SHIPADDR = 10;
    
    public static final int ACT_REQ_CODE_LOCAL_VOICE = 11;
    public static final int ACT_REQ_CODE_INTER_VOICE = 12;
    
    public static final int ACT_REQ_CODE_HK_START_TIME = 13;
    public static final int ACT_REQ_CODE_HK_END_TIME = 14;
    public static final int ACT_REQ_CODE_ID_START_TIME = 15;
    public static final int ACT_REQ_CODE_ID_END_TIME = 16;
	
	
	public static final int CONTACT_NULL = 0;
    public static final int CONTACT_IMPORT = 1;
    public static final int CONTACT_SYNC = 2;
    public static final int CONTACT_DLOAD_HEAD = 3;
    public static final int CONTACT_RADY = 4;

    public final static int[][] networkErrCode = new int[][]
    {
        {
            R.string.Err0_0
        },
        {
            R.string.resv,
            R.string.Err1_1,
            R.string.Err1_2,
            R.string.Err1_3,
            R.string.Err1_4,
            R.string.Err1_5,
            R.string.Err1_6,
            R.string.Err1_7
        },
        {
            R.string.resv
        },
        {
            R.string.resv,
            R.string.Err3_1,
            R.string.Err3_2,
            R.string.Err3_3,
            R.string.Err3_4,
            R.string.Err3_5,
            R.string.Err3_6,
            R.string.Err3_7,
            R.string.Err3_8,
            R.string.Err3_9,
            R.string.Err3_10,
            R.string.Err3_11,
            R.string.Err3_12
        },
        {
            R.string.resv,
            R.string.Err4_1,
            R.string.Err4_2,
            R.string.Err4_3,
            R.string.Err4_4,
            R.string.Err4_5,
            R.string.Err4_6,
            R.string.Err4_7,
            R.string.Err4_8,
            R.string.Err4_9,
            R.string.Err4_10,
            R.string.Err4_11,
            R.string.Err4_12,
            R.string.Err4_13,
            R.string.Err4_14,
            R.string.Err4_15,
            R.string.Err4_16,
            R.string.Err4_17,
            R.string.Err4_18,
            R.string.Err4_19,
            R.string.Err4_20,
            R.string.Err4_21,
            R.string.Err4_22,
            R.string.Err4_23,
            R.string.Err4_24,
            R.string.Err4_25,
            R.string.Err4_26,
            R.string.Err4_27,
            R.string.Err4_28,
            R.string.Err4_29,
            R.string.Err4_30,
            R.string.Err4_31,
            R.string.Err4_32,
            R.string.Err4_33,
            R.string.Err4_34,
            R.string.Err4_35,
            R.string.Err4_36,
            R.string.Err4_37,
            R.string.Err4_38,
            R.string.Err4_39,
            R.string.Err4_40,
            R.string.Err4_41,
            R.string.Err4_42,
            R.string.Err4_43,
            R.string.Err4_44,
            R.string.Err4_45,
            R.string.Err4_46,
            R.string.Err4_47,
            R.string.Err4_48,
            R.string.Err4_49,
            R.string.Err4_50,
            R.string.Err4_51,
            R.string.Err4_52,
            R.string.Err4_53
        },
        {
            R.string.resv,
            R.string.Err5_1,
            R.string.Err5_2,
            R.string.Err5_3,
            R.string.Err5_4,
            R.string.Err5_5,
            R.string.Err5_6,
            R.string.Err5_7,
            R.string.Err5_8,
            R.string.Err5_9
        },
        {
            R.string.resv,
            R.string.Err6_1,
            R.string.Err6_2
        }
    };
    
    public static void toastErrCode(final Context c, ResponseInfo baseRspInfo)
    {
        String strMsg = baseRspInfo.getMsg();
        LOGManager.d(strMsg);

        int retCode = baseRspInfo.getRet();
        int errCode = baseRspInfo.getErrCode();
        if (retCode < networkErrCode.length
                && errCode < networkErrCode[retCode].length)
        {
            strMsg = c.getString(networkErrCode[retCode][errCode]);
        }
        else
        {
            strMsg = c.getString(R.string.unknownNetErr);
        }

        try
        {
            Utils.showToast((Activity)c, strMsg);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
    }

    public static String getErrString(final Context c, ResponseInfo baseRspInfo)
    {
        String strMsg = baseRspInfo.getMsg();
        LOGManager.d(strMsg);

        int retCode = baseRspInfo.getRet();
        int errCode = baseRspInfo.getErrCode();
        if (retCode < networkErrCode.length
                && errCode < networkErrCode[retCode].length)
        {
            strMsg = c.getString(networkErrCode[retCode][errCode]);
        }
        else
        {
            strMsg = c.getString(R.string.unknownNetErr);
        }

        return strMsg;
    }

	
	 public static void toastErrCode(final Context c, String msg)
    {

        try
        {
            Utils.showToast((Activity)c, msg);
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
    }
    
	
    public final static int[][] simCard = new int[][]
    {
        {
            R.string.unknown,
        },
        {
            R.string.unknown,
            R.string.simCard00,
            R.string.simCard01,
            R.string.simCard02,
        },
        {
            R.string.unknown,
            R.string.simCard10,
            R.string.simCard11,
            R.string.simCard12,
        },
        {
            R.string.unknown,
            R.string.simCard20,
            R.string.simCard21,
            R.string.simCard22,
        }
    };
    
    public static int getSim(int simType, int simSize)
    {
        if (simType < simCard.length
                && simSize < simCard[simType].length)
        {
            return simCard[simType][simSize];
        }
        else
        {
            return R.string.unknown;
        }
    }
    
    public final static int[] simType = new int[]
    {
        R.string.unknown,
        R.string.cardTypeOne,
        R.string.cardTypeTwo,
        R.string.cardTypeThree,
    };
    
    public static int getSimType(int type)
    {
        if (type >= simType.length || type < 0)
        {
            return R.string.unknown;
        }
        
        return simType[type];
    }
    
    public final static int[] simSize = new int[]
    {
        R.string.unknown,
        R.string.stdCard,
        R.string.microCard,
        R.string.nanoCard,
    };
    
    public static int getSimSize(int size)
    {
        if (size >= simSize.length || size < 0)
        {
            return R.string.unknown;
        }
        
        return simSize[size];
    }
    
    public final static int[] crbtType = new int[]
    {
        R.string.call_set_null,
        R.string.call_set_com,
        R.string.callFwdSetSecretary,
        R.string.callFwdSetVip,
    };
    
    public static int getCrbtType(int type)
    {
        if (type >= crbtType.length || type < 0)
        {
            return R.string.call_set;
        }
        
        return crbtType[type];
    }
    

    
    public static int[] expressionImgs = new int[] { R.drawable.smiley_10,R.drawable.smiley_11,R.drawable.smiley_12,
        R.drawable.smiley_13, R.drawable.smiley_14,R.drawable.smiley_15,R.drawable.smiley_16,
        R.drawable.smiley_17,R.drawable.smiley_18,R.drawable.smiley_19,R.drawable.smiley_20,
        R.drawable.smiley_21, R.drawable.smiley_22,R.drawable.smiley_23,R.drawable.smiley_24,
        R.drawable.smiley_25, R.drawable.smiley_26,R.drawable.smiley_27,R.drawable.smiley_28,
        R.drawable.smiley_29,R.drawable.smiley_30,R.drawable.smiley_31, R.drawable.smiley_32,
        R.drawable.smiley_33,R.drawable.smiley_34,R.drawable.smiley_35,R.drawable.smiley_36,
        R.drawable.smiley_37,R.drawable.smiley_38, R.drawable.smiley_39,
        R.drawable.smiley_40,R.drawable.smiley_41,R.drawable.del_smile,
    };

    public static String[] expressionImgNames = new String[] { "[smiley_10]",
        "[smiley_11]", "[smiley_12]", "[smiley_13]", "[smiley_14]", "[smiley_15]", "[smiley_16]",
        "[smiley_17]", "[smiley_18]", "[smiley_19]", "[smiley_20]", "[smiley_21]", "[smiley_22]",
        "[smiley_23]", "[smiley_24]", "[smiley_25]", "[smiley_26]", "[smiley_27]", "[smiley_28]",
        "[smiley_29]", "[smiley_30]", "[smiley_31]", "[smiley_32]", "[smiley_33]", "[smiley_34]",
        "[smiley_35]", "[smiley_36]", "[smiley_37]", "[smiley_38]", "[smiley_39]", "[smiley_40]",
        "[smiley_41]", "[smiley_42]"};
    
    public static int[] expressionImgs2 = new int[] {
        R.drawable.send_simle_bg,R.drawable.send_img_bg,R.drawable.send_camera_bg,R.drawable.send_location_bg,R.drawable.send_clock_bg,
        R.drawable.send_colltion_bg,R.drawable.send_registration_bg,R.drawable.send_trip_bg,R.drawable.send_set_bg
    };
    
    public static int[] expressionImgs2Names = new int[] {
        R.string.smile,R.string.photo,R.string.shoot,R.string.location,R.string.clock,R.string.tab_gather,R.string.tab_reception,R.string.tab_calendar,R.string.tab_set
    };
    
    public static int[] expressionImgs3 = new int[] {
        R.drawable.send_simle_bg,R.drawable.send_img_bg,R.drawable.send_camera_bg,R.drawable.send_location_bg
    };
        
    public static int[] expressionImgs3Names = new int[] {
        R.string.smile,R.string.photo,R.string.shoot,R.string.location
    };
    
    public static int[] countryName = new int[] {R.string.tv_china,
        R.string.tv_hk, R.string.tv_tw, R.string.tv_usa, R.string.tv_macao
        };
    
    public static int[] countryNum = new int[] {R.string.tv_china_num,
        R.string.tv_hk_num, R.string.tv_tw_num, R.string.tv_usa_num, R.string.tv_macao_num
        };
    
    public static int[] countryIcon = new int[] {
        R.drawable.country_china, R.drawable.country_hk, R.drawable.country_tw, R.drawable.country_usa, 
        R.drawable.country_macao
    };
    
    public static int[] countryNameReg = new int[] {R.string.tv_china,
        R.string.tv_hk
        };

}
