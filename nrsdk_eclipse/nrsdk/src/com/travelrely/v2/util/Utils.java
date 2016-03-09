package com.travelrely.v2.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.http.util.EncodingUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.travelrely.core.Engine;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.core.Res;
import com.travelrely.app.activity.HomePageActivity;
import com.travelrely.app.view.SysAlertDialog;
import com.travelrely.app.view.SysAlertDialog.OnSysAlertClickListener;
import com.travelrely.model.CallRecord;
import com.travelrely.model.ContactModel;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.net.HttpConnector;
import com.travelrely.model.OneDayCallRecords;
import com.travelrely.model.PhoneSection;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.db.GroupDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.model.ContactTemp;
import com.travelrely.v2.response.FetchToken;
import com.travelrely.v2.response.FetchToken.Data.FetchTokenContact;
import com.travelrely.v2.response.Response;
import com.travelrely.v2.response.TraMessage;

public class Utils
{

    public static final String ymdd = "yyyy年MM月dd日";
    public static final String mdd = "M月dd日";
    public static final String y_m_d = "yyyy-MM-dd";
    public static final String y_m_d_h_m = "yyyy-MM-dd HH:mm";
    public static final String y_m_d_h_m_s = "yyyy-MM-dd HH:mm:ss";
    public static final String hm = "HH:mm";
    public static final String hms = "HH:mm:ss";
    public static final String ymd_ = "yyyy.MM.dd";

    public static void sms(Activity activity, String to)
    {
        Uri uri = Uri.parse("smsto:" + to);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        // it.putExtra("sms_body", "102");
        activity.startActivity(it);

    }

    public static boolean isTopActivity(Context context)
    {
        String packageName = "com.travelrely.v2";
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            System.out.println("packagename" + tasksInfo.get(0).topActivity.getPackageName());
            // 应用程序位于堆栈的顶层
            if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    
    public static void mail(Activity activity, String url)
    {
        String[] reciver = new String[] { url };
        String[] mySbuject = new String[] { "" };
        String myCc = "";
        String mybody = "";
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
        myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
        activity.startActivity(Intent.createChooser(myIntent, "客服"));
    }

    public static void web(Activity activity, String url)
    {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        activity.startActivity(intent);
    }
    
    private static void makeCall(Context activity, String num)
    {
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + num));
        // 启动
        activity.startActivity(phoneIntent);
        LOGManager.v("make call=" + num);
    }
    
    public static void call(Context activity, String num)
    {
        CallRecord callRecord = new CallRecord();
        callRecord.setNumber(num);
        Engine.getInstance().currentCallRecord = callRecord;
        makeCall(activity, num);
    }

    public static void callVoip(final Context activity, final CallRecord callRecord)
    {
        final String newStr = formVoipNum(callRecord.getNumber());
        
        if(newStr != null){
            SysAlertDialog dialog = new SysAlertDialog(activity);
            dialog.setTitle("SIM卡1拨号");
            dialog.setMessage(newStr);
            dialog.setLeft(activity.getString(R.string.ok));            
            dialog.setRight(activity.getString(R.string.cancel));
            
            
            
            Cursor c = activity.getContentResolver().query(Uri.withAppendedPath(
                    PhoneLookup.CONTENT_FILTER_URI, newStr), new String[] {
                    PhoneLookup._ID,
                    PhoneLookup.NUMBER,
                    PhoneLookup.DISPLAY_NAME,
                    PhoneLookup.TYPE, PhoneLookup.LABEL }, null, null,   null );

    if(c.getCount() == 0)
            {
               //没找到电话号码
            } else if (c.getCount() > 0) {

                c.moveToFirst();
                String phonename = c.getString(2); //获
                if (!TextUtils.isEmpty(phonename)){
                	callRecord.name = phonename;
                }
            }
            
            
            
            dialog.setOnClickListener(new OnSysAlertClickListener()
            {
                @SuppressLint("NewApi")
                @Override
                public void onLeftClick(SysAlertDialog dialog)
                {
                    Engine.getInstance().currentCallRecord = callRecord;
                    String[] strs = newStr.split(",");
                    callRecord.vipNum = strs[0];
                    
                    if(!callRecord.getNumber().isEmpty()){
                        
//                        if(!ContactDetailActivity.IS_TOKEN_NUM){
//                            Engine.getInstance().insertCallLog(activity, callRecord, 1);
//                        }
//                        ContactDetailActivity.IS_TOKEN_NUM = false;
                    }
                  
                    makeCall(activity, newStr);
                }

                @Override
                public void onRightClick(SysAlertDialog dialog)
                {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onOkClick(SysAlertDialog dialog)
                {
                }
            });
            dialog.show();
        }
    }

    public static String formVoipNum(String str)
    {
        String newStr = getIpNum();
        if (newStr == null || newStr.equals("") || !newStr.contains("XXXXX"))
        {
            // 如果没有VOIP号码资源就返回原号码
            return str;
        }
        
        if (newStr.contains("XXXXX"))
        {
            // 补全号码
            str = FullNumber(str);

            // 去除号码前的+
            if (str.startsWith("+") == true)
            {
                str = str.substring(1);
            }
            
            newStr = newStr.replaceAll("XXXXX", str);
            
            return newStr;
        }
        
        return str;
    }
    
    public static String getIpNum()
    {
        List<String> numbers = null;
        if (Engine.getInstance().isLogIn)
        {
            if (Engine.getInstance().homeLogin)
            {
                numbers = Engine.getInstance().getHomeIpDialMethodList();
            }
            else
            {
                numbers = Engine.getInstance().getRoamIpDialMethodList();
            }
        }

        if (numbers != null && numbers.size() > 0)
        {
            int i = (int) (Math.random() * numbers.size());

            return numbers.get(i);
        }
        
        return null;
    }

//    /**
//     * 获取通话记录
//     * 
//     * @param context
//     */
//    public static List<CallRecord> getCallRecords(Context context)
//    {
//        ContentResolver resolver = context.getContentResolver();
//        Cursor c = resolver.query(CallLog.Calls.CONTENT_URI, null, null,
//                new String[] {
//
//                }, null);
//        List<CallRecord> list = new ArrayList<CallRecord>();
//
//        while (c.moveToNext())
//        {
//
//            CallRecord callRecord = new CallRecord();
//            callRecord.setValues(c);
//
//            list.add(callRecord);
//        }
//        LOGManager.d(c.getCount() + "");
//        c.close();
//        return list;
//    }

    /**
     * 获取通话记录
     * 
     * @param context
     */
//    public static List<CallRecord> getCallRecords(Context context, String num)
//    {
//        ContentResolver resolver = context.getContentResolver();
//        Cursor c = resolver.query(CallLog.Calls.CONTENT_URI, null,
//                CallLog.Calls.NUMBER + "=?", new String[] { num }, null);
//        List<CallRecord> list = new ArrayList<CallRecord>();
//
//        while (c.moveToNext())
//        {
//
//            CallRecord callRecord = new CallRecord();
//            callRecord.setValues(c);
//
//            list.add(callRecord);
//        }
//        LOGManager.d(c.getCount() + "");
//        c.close();
//        return list;
//    }

    public static List<OneDayCallRecords> generateOneDayCallRecords(
            List<CallRecord> list)
    {
        List<OneDayCallRecords> oneDayCallRecordss = new ArrayList<OneDayCallRecords>();
        HashMap<String, OneDayCallRecords> maps = new HashMap<String, OneDayCallRecords>();
        for (int i = 0; i < list.size(); i++)
        {
            CallRecord callRecord = list.get(i);
            if (callRecord.isOutGoing())
            {
                String day = callRecord.getDay();
                String num = callRecord.getNumber();
                String name = callRecord.getName();
                String key = day + "-" + num;
                OneDayCallRecords oneDayCallRecords;
                if (maps.containsKey(key))
                {
                    oneDayCallRecords = maps.get(key);
                }
                else
                {
                    oneDayCallRecords = new OneDayCallRecords();
                    oneDayCallRecords.setKeyNumber(num);
                    oneDayCallRecords.setName(name);

                    maps.put(key, oneDayCallRecords);
                }
                oneDayCallRecords.setTime(callRecord.getCurrentTime());
                oneDayCallRecords.getCallRecords().add(callRecord);
            }
        }
        Iterator iter = maps.entrySet().iterator();
        while (iter.hasNext())
        {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            OneDayCallRecords val = (OneDayCallRecords) entry.getValue();
            oneDayCallRecordss.add(val);
        }
        Collections.sort(oneDayCallRecordss);
        return oneDayCallRecordss;
    }

    public static void registCallLogListener(Activity activity)
    {

    }

    public static void unRegistCallLogListener(Activity activity)
    {

    }

    /**
     * 判断是否为旅信用户
     * 
     * @return
     */
    public static boolean isTravelrelyUser(String num)
    {

        // TODO 这个地方需要完善，等下次旅信列表出来了就可以做了
        return false;
    }

    public static Bitmap getBitmap(Context context, int id)
    {
        Drawable drawable = context.getResources().getDrawable(id);
        BitmapDrawable bitmap = (BitmapDrawable) drawable;
        return bitmap.getBitmap();
    }

    public static String isStrNull(String str)
    {
        if (str == null || str.equals(""))
        {
            return "";
        }
        else
        {
            return str;
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static String generateMoney(double money)
    {
        // 1,000.00
        // 1.000,00
        String value = String.format(Locale.CHINA, "%.2f", money);
        NumberFormat nf2 = NumberFormat.getInstance(Locale.CHINA);
        double dd = Double.parseDouble(value);
        String str = nf2.format(dd);

        return str;
    }

    /**
     * 将money转成2位小数
     * 
     * @param money
     * @return
     */
    public static double changeMoney(double money)
    {
        // 1,000.00
        // 1.000,00
        String value = String.format(Locale.US, "%.2f", money);
        double dd = Double.parseDouble(value);
        return dd;
    }

    public static String generateFormatMoney(String currency, double money)
    {
        String sign;
        if (money >= 0)
        {
            sign = "+";
        }
        else
        {
            sign = "-";
        }

        String moneyValue = generateMoney(money);
        if (moneyValue.indexOf("-") != -1)
        {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1)
        {
            return "";
        }

        moneyValue = sign + currency + moneyValue;
        return moneyValue;
    }

    public static String notPlusGenerateFormatMoney(String currency,
            double money)
    {
        String sign;
        if (money >= 0)
        {
            sign = "";
        }
        else
        {
            sign = "-";
        }

        String moneyValue = generateMoney(money);
        if (moneyValue.indexOf("-") != -1)
        {
            moneyValue = moneyValue.substring(1, moneyValue.length());
        }

        if (moneyValue == null || moneyValue.length() < 1)
        {
            return "";
        }

        moneyValue = sign + currency + moneyValue;
        return moneyValue;
    }

    public static String notPlusGenerateFormatMoney(String currency,
            String money)
    {
        if (money.length() == 1 && money.charAt(0) == 46)
        {
            money = "0.0";
        }

        double m = Double.parseDouble(money);
        String moneyValue = notPlusGenerateFormatMoney(currency, m);
        return moneyValue;
    }

    public static String generateFormatMoney(String currency, String money)
    {
        if (money.length() == 1 && money.charAt(0) == 46)
        {
            money = "0.0";
        }

        double m = Double.parseDouble(money);
        String moneyValue = generateFormatMoney(currency, m);
        return moneyValue;
    }

    /**
     * 金额格式化
     * 
     * @param s
     *            金额
     * @param len
     *            小数位数
     * @return 格式后的金额 String str = "100000.2345"; str =
     *         Utils.insertComma("100000.23",str.length());
     */
    public static String insertComma(String s, int len)
    {

        if (s == null || s.length() < 1)
        {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0)
        {
            formater = new DecimalFormat("###.###");
        }
        else
        {
            StringBuffer buff = new StringBuffer();
            buff.append("###.###,");
            for (int i = 0; i < len; i++)
            {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    public static void isHasFile_Del(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            file.delete();
        }
    }

    public static boolean isHasFile(String path)
    {
        File file = new File(path);
        if (file.exists())
            return true;
        else
            return false;
    }

    /**
     * �ж��Ƿ���SD��
     * 
     * @return
     */
    public static boolean isSDCardExists()
    {
        return (Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED));
    }

    /*
     * public static String getSDCARD(Context context) { if
     * (Environment.getExternalStorageState().equals(
     * Environment.MEDIA_MOUNTED)) { String path =
     * Environment.getExternalStorageDirectory().getPath() + "/." +
     * ADDataManager.GetDataManager(context).Get_AppID() + "_" +
     * DeviceInfo.getIMEI(context); File f = new File(path); if (!f.exists()) {
     * f.mkdirs(); Log.i("crate image cache dir", "" + f.getAbsolutePath()); }
     * return path; } else { return null; } }
     */

    public static InputStream getRes(Context context, String fileName)
    {
        InputStream is;
        if (fileName == null)
            return null;
        else
            return is = Utils.class.getClassLoader().getResourceAsStream(
                    "res/" + fileName);// עRESӦΪSRC���½����ļ���
    }

    
    public static float  PixelsToDip(Context context, int Pixels) {  
	    final float SCALE = context.getResources().getDisplayMetrics().density;  
	  
	    float dips = Pixels / SCALE;  
	  
	    return dips;  
	  
	}  
    
    public static void writeFileData(String fileName, String message,
            Context context)
    {
        try
        {
            FileOutputStream fout = context.openFileOutput(fileName,
                    context.MODE_PRIVATE);
            byte[] bytes = message.getBytes();
            fout.write(bytes);
            fout.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ���ļ���./data/data/com.tt/files/����

    public String readFileData(String fileName, Context context)
    {

        String res = "";
        try
        {
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return res;

    }

    public static String readAssets(Context context, String fileName)
    {
        String str = null;
        try
        {
            str = Utils.convertStreamToString(context.getAssets()
                    .open(fileName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * @param is
     * @return
     * @throws IOException
     */
    public static String convertStreamToString(InputStream is)
            throws IOException
    {
        StringBuilder sb = new StringBuilder();
        if (is != null)
        {
            String line;
            try
            {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line).append("\n");
                }
            }
            finally
            {
                is.close();
            }
        }
        return sb.toString();
    }

    /**
     * StringתInputStream
     * 
     * @param str
     * @return
     */
    public static InputStream StringToInputStream(String str)
    {
        InputStream in = new ByteArrayInputStream(str.getBytes());
        return in;
    }

    public static float measureX(Paint paint, String text, double width)
    {
        float textLenght;
        float x;
        textLenght = paint.measureText(text);
        x = (float) ((width - textLenght) / 2f);
        return x;
    }

    public static String MD5(String str)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
        {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toUpperCase();
    }

    public static String getFirstName(String fullName)
    {
        if (fullName == null)
        {
            return null;
        }
        else
        {
            return fullName.charAt(0) + "";
        }
    }

    public static String getLastName(String fullName)
    {

        if (fullName == null)
        {
            return null;
        }
        else if (fullName.length() <= 1)
        {

            return "";
        }
        else
        {
            return fullName.substring(1, fullName.length());
        }
    }

    public static float getTrafficStateReceiveBytes()
    {

        long byteresult = TrafficStats.getMobileRxBytes();

        if (byteresult == TrafficStats.UNSUPPORTED)
        {
            return 0f;
        }
        else
        {
            return byteresult / (1024f * 1024);
        }
    }

    public static float getTrafficStateUpBytes()
    {
        long byteresult = TrafficStats.getMobileTxBytes();

        if (byteresult == TrafficStats.UNSUPPORTED)
        {
            return 0f;
        }
        else
        {
            return byteresult / (1024f * 1024);
        }
    }

    /**
     * 把号码剪成没符号的
     * 
     * @param num
     * @return
     */
    public static String generateNum(String num)
    {
        if (num == null || num.equals(""))
        {
            return "";
        }
        if (num.charAt(0) == '+')
        {
            if (num.charAt(1) == '8' && num.charAt(2) == '6')
            {

                return num.substring(3, num.length());
            }
            else
            {
                return num.substring(1, num.length());
            }

        }

        return num;
    }

    public static String getAndroidTag(int type)
    {
        String result = Engine.getInstance().getString(R.string.phone);
        int id = 0;
        switch (type)
        {
            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                id = R.string.phone_type_home;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                id = R.string.phone_type_mobile;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME:
                id = R.string.phone_type_home_fax;
                break;
            case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK:
                id = R.string.phone_type_work_fax;
                break;

            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
            case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                id = R.string.phone_type_work;
                break;
            default:
                id = R.string.phone_type_other;
                break;
        }
        if (id != 0)
        {
            result = Engine.getInstance().getString(id);
        }
        return result;
    }

    public static String getChinaMNC(String num)
    {

        if (true)
        {
            return "电话";
        }
        num = generateNum(num);
        // 移动：134，135,136,137,138,139,152,182,188
        List<String> mobileList = new ArrayList<String>();
        mobileList.add("134");
        mobileList.add("135");
        mobileList.add("136");
        mobileList.add("137");
        mobileList.add("138");
        mobileList.add("139");
        mobileList.add("152");
        mobileList.add("182");
        mobileList.add("188");
        // 联通：130,131,132,150,151,156，186
        List<String> liantong = new ArrayList<String>();
        liantong.add("130");
        liantong.add("131");
        liantong.add("132");
        liantong.add("150");
        liantong.add("151");
        liantong.add("156");
        liantong.add("186");
        String result = "";
        // 电信：133,153,189
        List<String> dianxin = new ArrayList<String>();
        dianxin.add("133");
        dianxin.add("153");
        dianxin.add("189");
        if (num.length() == 11 && num.charAt(0) == '1')
        {

            String begin = num.substring(0, 3);

            for (int i = 0; i < mobileList.size(); i++)
            {
                if (begin.equals(mobileList.get(i)))
                {

                    return "移动";

                }
            }
            for (int i = 0; i < liantong.size(); i++)
            {
                if (begin.equals(liantong.get(i)))
                {

                    return "联通";

                }
            }
            for (int i = 0; i < dianxin.size(); i++)
            {
                if (begin.equals(dianxin.get(i)))
                {

                    return "电信";

                }
            }
        }
        return TagNumber.TAG_DEFAULT;
    }

    /**
     * 自动国家码
     * 
     * @return
     */
    public static String FullNumber(String src)
    {
        String begin = Engine.getInstance().getCC();

        if (src == null || src.equals(""))
        {
            return "";
        }

        src = src.replace(" ", "");
        if (src == null || src.equals(""))
        {
            return "";
        }

        char firstChar = src.charAt(0);
        if (firstChar == '+')
        {
            return src;
        }
        else if (firstChar == '0')
        {
            if (src.length() == 1)
            {
                return src;
            }

            char second = src.charAt(1);
            if (second == '0')
            {
                // 如果第二个字符也是0，则已经带国家码前缀，则把00改为+号
                return "+" + src.substring(2, src.length());
            }
            else if (second == '1' && src.charAt(2) == '1')
            {
                // 如果第二/三个字符是1，则已经带国家码前缀，则把011改为+号
                return "+" + src.substring(3, src.length());
            }
            else
            {
                // 如果第二个字符不为0，则去掉第一个字符0后，再加前缀
                return begin + src.substring(1, src.length());
            }
        }
        else
        {
            if (firstChar >= '1' && firstChar <= '9')
            {
                return begin + src;
            }
            else
            {
                return src;
            }
        }
    }

    public static int small_bitmap_width = 300;

    public static int small_bitmap_height = 300;

    public static int big_bitmap_width = 480;

    public static int big_bitmap_height = 640;

    public static Bitmap generateSmallBitmap(Bitmap src, String path)
    {

        if (src == null)
        {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        float xx = 0;
        if (width > small_bitmap_width)
        {

            xx = width * 1f / small_bitmap_width;
            width = small_bitmap_width;
            height = (int) (height / xx);

        }
        else if (height > small_bitmap_height)
        {
            xx = height * 1f / small_bitmap_height;
            height = small_bitmap_height;
            width = (int) (width / xx);
        }
        return zoomImage(src, width, height, path);
    }

    // 生成圆角图片
    public static Bitmap headBitmap(Bitmap bitmap)
    {
        try
        {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint(1);
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 50;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        }
        catch (Exception e)
        {
            return bitmap;
        }
    }

    public static Bitmap generateBigBitmap(Bitmap src, String path)
    {

        int width = src.getWidth();
        int height = src.getHeight();
        float xx = 0;
        if (width > big_bitmap_width)
        {

            xx = width * 1f / big_bitmap_width;
            width = big_bitmap_width;
            height = (int) (height / xx);

        }
        else if (height > big_bitmap_height)
        {
            xx = height * 1f / big_bitmap_height;
            height = big_bitmap_height;
            width = (int) (width / xx);
        }
        return zoomImage(src, width, height, path);

    }
    
    /***
     * 图片的缩放方法
     * 
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                    double newHeight, String path) {
            // 获取这个图片的宽和高
            float width = bgimage.getWidth();
            float height = bgimage.getHeight();
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight);
            matrix.postRotate(PictureUtil.readPictureDegree(path));
            Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                            (int) height, matrix, true);
            return bitmap;
    }

    /**
     * bitmap转为base64
     * 
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap)
    {

        String result = null;
        ByteArrayOutputStream baos = null;
        try
        {
            if (bitmap != null)
            {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (baos != null)
                {
                    baos.flush();
                    baos.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * 
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data)
    {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap rotate(Bitmap b, int degrees)
    {
        if (degrees != 0 && b != null)
        {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) b.getWidth() / 2,
                    (float) b.getHeight() / 2);
            try
            {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                        b.getHeight(), m, true);
                if (b != b2)
                {
                    b.recycle(); // 提示Bitmap操作完应该显示的释放
                    b = b2;
                }
            }
            catch (OutOfMemoryError ex)
            {
                //出现了内存不足异常，最好return 原始的bitmap对象。.
            }
        }
        return b;
    }

    public static String getCountryNameByMcc(String mnc)
    {
        if (mnc.equals("310"))
        {
            return "美国";
        }
        else if (mnc.equals("460"))
        {

            return "中国";
        }
        else if (mnc.equals("454"))
        {
            return "香港";
        }
        return mnc;
    }

    public static void showToast(final Activity activity, final String content)
    {
        activity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast toast = Toast.makeText(activity, content,
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    /**判断字符串是否全为数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){ 
		if(str.contains("+86")){
    		str = str.replace("+86", "");
        }
    	   Pattern pattern = Pattern.compile("[0-9]*"); 
    	   Matcher isNum = pattern.matcher(str);
    	   if( !isNum.matches() ){
    	       return false; 
    	   } 
    	   return true; 
    	}
    
    /**
     * 验证手机号码
     */
    public static boolean isMobileNO(String mobiles) {
        if(mobiles.contains("+86")){
            mobiles = mobiles.replace("+86", "");
        }
        Pattern p = Pattern.compile("^(1[0-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isTel(String num)
    {
        String phoneRegexp = "^[+]{1}([0-9]{7,})*";// 固话的匹配模式
        Pattern p = Pattern.compile(phoneRegexp);
        Matcher m = p.matcher(num);
        return m.matches();
    }

    /**
     * 判断手机网络是否可用
     * 
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null)
        {
            for (int i = 0; i < info.length; i++)
            {
                if (info[i].getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得日期
     */
    public static String GetDate(int type, String t)
    {
        String temp_str = "";
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, type);
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getInstance();
        df.applyPattern(t);
        temp_str = df.format(ca.getTime());
        return temp_str;
    }

    // 获得今天日期
    public static Date getTodayData()
    {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        return today;
    }

    /**
     * 获得日期
     */
    public static String GetSomeDate(String strDate, int day)
    {
        String temp_str = "";
        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getInstance();
        df.applyPattern("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        Date date;
        try
        {
            date = df.parse(strDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            date = ca.getTime(); // 如果报异常，则取今天日期
        }
        ca.setTime(date);
        ca.add(Calendar.DATE, day);
        date = ca.getTime();
        temp_str = df.format(date);
        return temp_str;
    }

    /**
     * 计算行程天数
     * 
     * @throws ParseException
     */
    public static int OrderTime(String begin, String end) throws ParseException
    {

        SimpleDateFormat sDate, eDate;

        sDate = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start_date = sDate.parse(begin);
        long beginTime = start_date.getTime();

        eDate = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date end_date = eDate.parse(end);
        long endTime = end_date.getTime();

        long oneDay = 24 * 60 * 60 * 1000;
        int day = (int) ((endTime - beginTime) / oneDay);
        return day + 1;
    }
    
    public static String formatDate(String date, String format)
    {
        SimpleDateFormat sDate;

        sDate = new SimpleDateFormat(format);
        java.util.Date dat;
        try
        {
            dat = sDate.parse(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }

        SimpleDateFormat df = (SimpleDateFormat) DateFormat.getInstance();
        df.applyPattern(format);
        return df.format(dat);
    }

    /**
     * 计算日期差
     * 
     * @throws ParseException
     */
    public static int dateDiff(String begin, String end) throws ParseException
    {

        SimpleDateFormat sDate, eDate;

        sDate = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date start_date = sDate.parse(begin);
        long beginTime = start_date.getTime();

        eDate = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date end_date = eDate.parse(end);
        long endTime = end_date.getTime();

        long oneDay = 24 * 60 * 60 * 1000;
        int day = (int) ((endTime - beginTime) / oneDay);
        return day;
    }

    public static long isShowTime(long startTime, long endTime)
    {

        long duration = (endTime - startTime) / (1000 * 60);

        return duration;
    }

    /**
     * 将本地通讯录存到旅信应用里
     */
    public static void doSyncLocal()
    {
        doSyncLocalIfHasSim();
        // if (ContactDatabaseHelper.getInstance().isSimExist()) {
        //
        //
        // } else {
        //
        // }

    }

    private static void doSyncLocalIfHasSim()
    {
        ContactDBHelper contactDatabaseHelper = ContactDBHelper
                .getInstance();

        // 读取手机本地通讯录
        List<ContactModel> list = contactDatabaseHelper.getPhoneContacts();

        boolean success = contactDatabaseHelper.insertAll(list);
        if (success)
        {
            ;
        }
    }
    
    public static boolean importContacts()
    {
        long t0 = System.currentTimeMillis();

        HashMap<String, List<ContactTemp>> map = new HashMap<String, List<ContactTemp>>();
        //List<String> phoneList = new ArrayList<String>();
        HashMap<Long, ContactModel> hashMap = new HashMap<Long, ContactModel>();
        
        ContentResolver resolver = Engine.getInstance().getContext()
                .getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{Phone.DISPLAY_NAME,
                        Phone.NUMBER,
                        Phone.CONTACT_ID,
                        Phone.TYPE, 
                        "sort_key"}, null, null, null);

        if (phoneCursor != null)
        {
            while (phoneCursor.moveToNext())
            {
                // 得到手机号码
                String phoneNumber = phoneCursor.getString(1);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                {
                    LOGManager.w("没有号码" + phoneCursor
                        .getString(0));
                    
                    continue;
                }

                // 去除号码中的分隔符
                phoneNumber = Utils.removeNonnumericChar(phoneNumber);

                // 得到联系人名称
                String contactName = phoneCursor.getString(0);
                
                String sortKey = phoneCursor.getString(4);

                // 得到联系人ID
                long contactid = phoneCursor.getLong(2);
                int type = phoneCursor.getInt(3);
                
                List<ContactTemp> contacts = map.get(phoneNumber);
                if (contacts == null)
                {
                    contacts = new ArrayList<ContactTemp>();
                    map.put(phoneNumber, contacts);
                }

                ContactTemp c = new ContactTemp();
                c.setName(contactName);
                c.setNum(phoneNumber);
                c.setTag(Utils.getAndroidTag(type));
                c.setRawContactId(contactid);
                c.setSortKey(sortKey);
                contacts.add(c);
            }

            phoneCursor.close();
        }
        
        long t1 = System.currentTimeMillis();
        LOGManager.w("导入：" + (t1 - t0));
        
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String urls = host + "api/user/fetch_token";

        String postData = Request.fetchToken(map); // 只查询是否旅信用户
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPut(urls, postData,
                null, false);
        if (httpResult == null)
        {
            LOGManager.e("fetch token all: httpResult == null");
            return false;
        }

        FetchToken rsp = Response.FetchToken(httpResult);
        if (rsp.getResponseInfo().isSuccess() == false)
        {
            LOGManager.e("fetch token all: " + rsp.getResponseInfo().getMsg());
            return false;
        }

        List<FetchTokenContact> list = rsp.getData().getPhone_list();
        if (list == null || list.size() == 0)
        {
            LOGManager.e("fetch token all: fetchToken.getData().getPhone_list()==null");
            return true;
        }
        long t3 = System.currentTimeMillis();
        LOGManager.w("网络：" + (t3 - t1));
        for (int i = 0; i < list.size(); i++)
        {
            FetchTokenContact fetchTokenContact = list.get(i);

            String phone_number = fetchTokenContact.getPhone_number();
            List<ContactTemp> contactModels2 = map.get(phone_number);
            if (contactModels2 == null || contactModels2.size() == 0)
            {
                continue;
            }

            for (int j = 0; j < contactModels2.size(); j++)
            {
                ContactTemp c = contactModels2.get(j);
                
                ContactModel contact = hashMap.get(c.getRawContactId());
                List<TagNumber> numbers = null;
                if (contact != null)
                {
                    numbers = contact.getPhoneNumList();
                }
                else
                {
                    contact = new ContactModel();
                    contact.setFirstName(Utils.getFirstName(c.getName()));
                    contact.setLastName(Utils.getLastName(c.getName()));
                    contact.setSortKey(c.getSortKey());
                    contact.setRawContactId(c.getRawContactId());
                    numbers = new ArrayList<ContactModel.TagNumber>();
                    contact.setPhoneNumList(numbers);
                    hashMap.put(c.getRawContactId(), contact);
                }
                TagNumber tagNumber = new TagNumber();
                tagNumber.setTag(c.getTag());
                tagNumber.setValue(c.getNum());
                tagNumber.setValue(fetchTokenContact);
                if (tagNumber.isRegisted())
                {
                    contact.setContactType(ContactModel.TRAVELRELY_USER);
                    contact.setTravelPhoneNumber(phone_number);
                    contact.setNickName(fetchTokenContact.getNick_name());
                    contact.setHeadPortrait(fetchTokenContact
                            .getHeadportrait());
                }

                numbers.add(tagNumber);
            }
        }
        long t4 = System.currentTimeMillis();
        LOGManager.w("处理：" + (t4 - t3));
        ContactDBHelper helper = ContactDBHelper.getInstance();
        helper.insertAll(hashMap);
        long endTime = System.currentTimeMillis();
        LOGManager.w("插入：" + (endTime - t4));
        
        return true;
    }

    public static void heBingContact(ContactModel newContact,
            ContactModel localContact)
    {
        List<TagNumber> newList = newContact.getPhoneNumList();
        List<TagNumber> localList = localContact.getPhoneNumList();
        TreeMap<String, TagNumber> treeMap = new TreeMap<String, TagNumber>();
        for (int j = 0; j < localList.size(); j++)
        {
            TagNumber localTagNumber = localList.get(j);
            treeMap.put(localTagNumber.toCode(), localTagNumber);
        }
        for (int i = 0; i < newList.size(); i++)
        {
            TagNumber newTagNumber = newList.get(i);
            treeMap.put(newTagNumber.toCode(), newTagNumber);
        }

        Iterator<String> iterator = treeMap.keySet().iterator();
        List<TagNumber> hebingTagNumbers = new ArrayList<ContactModel.TagNumber>();
        while (iterator.hasNext())
        {
            String entry = iterator.next();
            TagNumber tagNumber = (TagNumber) treeMap.get(entry);
            hebingTagNumbers.add(tagNumber);
        }
        localContact.setPhoneNumList(hebingTagNumbers);
    }

    public static void hebingContacts()
    {
        long time = System.currentTimeMillis();
        // 先拿到系统的
        ContactDBHelper helper = ContactDBHelper.getInstance();
        TreeMap<Long, ContactModel> phone = helper.getPhoneContactHash();
        long systemContactTime = System.currentTimeMillis();
        TreeMap<Long, ContactModel> local = helper.getAllContactsHash();
        long contactLocalTime = System.currentTimeMillis();

        long beginTime = System.currentTimeMillis();
        Iterator<Entry<Long, ContactModel>> titer = phone.entrySet().iterator();
        while (titer.hasNext())
        {
            Entry<Long, ContactModel> ent = (Entry<Long, ContactModel>) titer.next();
            long key = (Long) ent.getKey();
            ContactModel newContactModel = (ContactModel) ent.getValue();

            // 用相同的key查看APP中是否有该联系人
            ContactModel localContactModel = local.get(key);
            if (localContactModel == null)
            {
                // app里没有的新联系人
                local.put(key, newContactModel);
            }
            else
            {
                heBingContact(newContactModel, localContactModel);
            }
        }

        long endTime = System.currentTimeMillis();
        long biduiTime = endTime - beginTime;

        long addInDataBaseBegin = System.currentTimeMillis();
        helper.insertAll(local);
        long addIndataBaseTime = System.currentTimeMillis()
                - addInDataBaseBegin;
        long wastTime = System.currentTimeMillis() - time;
        long systemWastTime = systemContactTime - time;
        long localWastTime = contactLocalTime - systemContactTime;
        LOGManager.d("同步耗时读系统电话时间：" + systemWastTime);
        LOGManager.d("同步耗时：读应用电话时间" + localWastTime);
        LOGManager.d("同步耗时：比对" + biduiTime);
        LOGManager.d("同步耗时：插入数据库" + addIndataBaseTime);
        LOGManager.d("同步耗时：同步时间" + wastTime);
    }

    public static void addContactLocal(ContactModel contactModel)
    {
        ContactDBHelper contactDatabaseHelper = ContactDBHelper
                .getInstance();
        boolean success = contactDatabaseHelper.insert(contactModel);

        if (success)
        {
            LOGManager.d("添加通讯录成功.");
        }
        else
        {
            LOGManager.d("添加通讯录失败.");

        }

    }

    /**
      * deleteContactLocal(删除app通讯录数据库里的数据)
      * 会删除系统通讯录相关数据
      *
      * @Title: deleteContactLocal
      * @Description: TODO
      * @param @param contactModel
      * @param @return    设定文件
      * @return boolean    true-删除成功 false-删除失败
      * @throws
      */
    public static boolean deleteContactLocal(ContactModel contactModel)
    {
        ContactDBHelper contactDatabaseHelper = ContactDBHelper
                .getInstance();
        if (contactDatabaseHelper.delete(contactModel.getId()) > 0)
        {
            return true;
        }

        return false;
    }

    public static boolean fetch_token(List<ContactModel> contacts)
    {
        if (contacts == null || contacts.size() == 0)
        {
            return false;
        }

        HashMap<String, List<ContactModel>> hashMap = new HashMap<String, List<ContactModel>>();
        List<String> phoneList = new ArrayList<String>();

        for (int i = 0; i < contacts.size(); i++)
        {
            ContactModel contactModel = contacts.get(i);

            // 某个联系人的号码薄为空则继续处理下一个联系人
            if (contactModel.getPhoneNumList() == null)
            {
                continue;
            }

            // 遍历处理每个号码
            for (int j = 0; j < contactModel.getPhoneNumList().size(); j++)
            {
                TagNumber tagNumber = contactModel.getPhoneNumList().get(j);
                if (hashMap.containsKey(tagNumber.getValue()))
                {
                    // 该号码已经作为key存在于hashMap(一号对应多人)
                    List<ContactModel> contactModels2 = hashMap.get(tagNumber
                            .getValue());
                    contactModels2.add(contactModel);
                }
                else
                {
                    List<ContactModel> contactModels2 = new ArrayList<ContactModel>();
                    contactModels2.add(contactModel);
                    hashMap.put(tagNumber.getValue(), contactModels2);
                    phoneList.add(tagNumber.getValue());
                }
            }
        }

        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String urls = host + "api/user/fetch_token";

        String postData = Request.fetchToken(phoneList); // 只查询是否旅信用户
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPut(urls, postData,
                null, false);
        if (httpResult == null)
        {
            LOGManager.e("fetch token all: httpResult == null");
            return false;
        }

        FetchToken rsp = Response.FetchToken(httpResult);
        if (rsp.getResponseInfo().isSuccess() == false)
        {
            LOGManager.e("fetch token all: " + rsp.getResponseInfo().getMsg());
            return false;
        }

        List<FetchTokenContact> list = rsp.getData().getPhone_list();
        if (list == null || list.size() == 0)
        {
            LOGManager.e("fetch token all: fetchToken.getData().getPhone_list()==null");
            return false;
        }
        for (int i = 0; i < list.size(); i++)
        {
            FetchTokenContact fetchTokenContact = list.get(i);

            String phone_number = fetchTokenContact.getPhone_number();
            List<ContactModel> contactModels2 = hashMap.get(phone_number);
            if (contactModels2 == null || contactModels2.size() == 0)
            {
                continue;
            }

            for (int j = 0; j < contactModels2.size(); j++)
            {
                ContactModel contactModel = contactModels2.get(j);

                // 旅信用户的处理(regist == 1)
                int regist = fetchTokenContact.getRegister();
                if (regist == 1)
                {
                    contactModel.setContactType(ContactModel.TRAVELRELY_USER);
                    contactModel.setTravelPhoneNumber(phone_number);
                    contactModel.setNickName(fetchTokenContact.getNick_name());
                    contactModel.setHeadPortrait(fetchTokenContact
                            .getHeadportrait());
                }
                
                // 更新tagNum
                List<TagNumber> tagNumbers = contactModel.getPhoneNumList();
                for (int k = 0; k < tagNumbers.size(); k++)
                {
                    TagNumber tagNumber = tagNumbers.get(k);
                    if (tagNumber != null && tagNumber.getValue() != null)
                    {
                        if (tagNumber.getValue().equals(phone_number))
                        {
                            tagNumber.setValue(fetchTokenContact);
                            contactModel.updateTagNumberInDB(tagNumber);
                        }
                    }
                }

                long row = ContactDBHelper.getInstance().update(contactModel);
            }
        }

        LOGManager.d("fetchtokens 完毕");
        return true;
    }

    
    
    /**
     * 判断是否为旅信用户
     */
    public static void getFetchToken(ContactModel contact)
    {
        String cc = Engine.getInstance().getCC();
        String host = ReleaseConfig.getUrl(cc);
        String urls = host + "api/user/fetch_token";

        String postData = Request.fetchToken(contact); // 查询是否旅信用户和获取Token
        HttpConnector httpConnector = new HttpConnector();
        String httpResult = httpConnector.requestByHttpPut(urls, postData,
                null, false);
        if (httpResult == null)
        {
            return;
        }

        FetchToken fetchToken = Response.FetchToken(httpResult);
        if (fetchToken.getResponseInfo().isSuccess() == false)
        {
            // fetchToken失败
            return;
        }

        List<FetchTokenContact> list = fetchToken.getData().getPhone_list();
        boolean isRegist = false;
        for (int i = 0; i < list.size(); i++)
        {
            FetchTokenContact fetchTokenContact = list.get(i);
            if (fetchTokenContact.getRegister() == 1)
            {
                isRegist = true;
                
                contact.is_travel_user = 1;
                contact.travel_user_phone = fetchTokenContact
                        .getPhone_number();

                contact.setContactType(ContactModel.TRAVELRELY_USER);
                contact.setNickName(fetchTokenContact.getNick_name());
                contact.setHeadPortrait(fetchTokenContact
                        .getHeadportrait());
                long row = ContactDBHelper.getInstance().update(
                        contact);
                if (row > 0)
                {
                    LOGManager.d("找到一个旅信用户！并存入数据库");
                }
            }
            
            for (int j = 0; j < contact.getPhoneNumList().size(); j++)
            {
                TagNumber tagNumber = contact.getPhoneNumList().get(j);
                if (tagNumber.getValue() != null
                        && tagNumber.getValue().equals(
                                fetchTokenContact.getPhone_number()))
                {
                    tagNumber.setValue(fetchTokenContact);
                    contact.updateTagNumberInDB(tagNumber);
                }
            }
        }

        if (!isRegist)
        {
            // 不是 旅信用户
            contact.is_travel_user = 0;
            contact.travel_user_phone = "";
            contact.setContactType(ContactModel.NORMAL_USER);
            contact.setHeadPortrait(null);

            long row = ContactDBHelper.getInstance().update(contact);
            if (row > 0)
            {
                LOGManager.d("不是旅信用户");
            }
        }
    }

    public static List<PhoneSection> changeContactList2PhoneSections(
            List<ContactModel> list, String key)
    {
        if (list == null || list.size() == 0)
        {
            return null;
        }
        List<PhoneSection> phoneSections = new ArrayList<PhoneSection>();
        HashMap<Character, PhoneSection> hashmap = new HashMap<Character, PhoneSection>();
        // //群组
        PhoneSection groupSection = new PhoneSection();
        groupSection.setBegin('#');
        PhoneSection comSection = new PhoneSection();
        comSection.setBegin('#');

        for (int i = 0; i < list.size(); i++)
        {
            ContactModel contactModel = list.get(i);

            String contactName = contactModel.getName();
            if (contactModel.isGroup())
            {
                groupSection.getList().add(contactModel);
                continue;
            }

            if (contactModel.isTravelrelyService()
                    || contactModel.isPublicService())
            {
                comSection.getList().add(contactModel);
                continue;
            }

            if (key != null && !contactName.contains(key))
            {
                continue;
            }
            char firstChar = ' ';
            if (contactName != null && !contactName.equals(""))
            {
                firstChar = contactName.toUpperCase().charAt(0);
            }
            String[] strrs = PinyinHelper.toHanyuPinyinStringArray(firstChar);
            char strr = ' ';
            if (strrs == null || strrs.length == 0)
            {
                strr = firstChar;
            }
            else
            {
                strr = strrs[0].toUpperCase().charAt(0);
            }
            // 如果不是A-Z就是#
            if (strr < 'A' || strr > 'Z')
            {
                strr = '#';
            }
            PhoneSection phoneSection = null;
            if (hashmap.containsKey(strr))
            {
                // 拿到
                phoneSection = hashmap.get(strr);

            }
            else
            {
                phoneSection = new PhoneSection();
                phoneSection.setBegin(strr);
                hashmap.put(strr, phoneSection);
            }
            phoneSection.getList().add(contactModel);
        }
        Collection<PhoneSection> phoneSections2 = hashmap.values();

        for (PhoneSection phoneSection : phoneSections2)
        {
            phoneSections.add(phoneSection);
        }
        Collections.sort(phoneSections);
        if (groupSection.getList().size() > 0)
        {
            phoneSections.add(0, groupSection);
        }

        if (comSection.getList().size() > 0)
        {
            phoneSections.add(1, comSection);
        }
        return phoneSections;
    }

    public static List<PhoneSection> changeContactList2PhoneSections(
            List<ContactModel> list)
    {

        if (list == null)
        {
            return null;
        }
        List<PhoneSection> phoneSections = new ArrayList<PhoneSection>();
        HashMap<Character, PhoneSection> hashmap = new HashMap<Character, PhoneSection>();
        for (int i = 0; i < list.size(); i++)
        {

            ContactModel contactModel = list.get(i);

            String contactName = contactModel.getName();
            char firstChar = ' ';
            if (contactName != null)
            {

                firstChar = contactName.charAt(0);
            }
            String[] strrs = PinyinHelper.toHanyuPinyinStringArray(firstChar);
            char strr = ' ';
            if (strrs == null || strrs.length == 0)
            {
                strr = firstChar;
            }
            else
            {

                strr = strrs[0].toUpperCase().charAt(0);
            }
            // 如果不是A-Z就是#
            if (strr < 'A' || strr > 'Z')
            {
                strr = '#';
            }
            PhoneSection phoneSection = null;
            if (hashmap.containsKey(strr))
            {
                // 拿到
                phoneSection = hashmap.get(strr);

            }
            else
            {
                phoneSection = new PhoneSection();
                phoneSection.setBegin(strr);
                hashmap.put(strr, phoneSection);
            }
            phoneSection.getList().add(contactModel);

        }
        Collection<PhoneSection> phoneSections2 = hashmap.values();

        for (PhoneSection phoneSection : phoneSections2)
        {

            phoneSections.add(phoneSection);
        }
        Collections.sort(phoneSections);
        return phoneSections;
    }

    /**
     * 如果没有就把手机的往通讯录里填
     */
    public static List<ContactModel> getAppContactsOrCopyFromSystem(
            AsyncTask<List<ContactModel>, String, String> task)
    {
        ContactDBHelper contactDatabaseHelper = ContactDBHelper
                .getInstance();
        List<ContactModel> list = contactDatabaseHelper.getAllContacts();

        if (list == null || list.size() == 0)
        {
            LOGManager.i("旅信数据库没有通讯录，需要从手机导入");
            doSyncLocal();

            list = contactDatabaseHelper.getAllContacts();
            if (list == null || list.size() == 0)
            {
                LOGManager.e("旅信数据库通讯录list==null");
            }
            if (task != null)
            {
                LOGManager.d("判断是否为旅信用户");
                task.execute(list);
            }
        }
        return list;
    }

    /**
     * 隐藏键盘
     */
    public static void hideInputMethod(Activity mContext)
    {

        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                mContext.getCurrentFocus().getWindowToken(), 0);
    }

    public static SpannableString bitmapSmile(Context context, int arg2)
    {

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeResource(context.getResources(),
                Res.expressionImgs[arg2 % Res.expressionImgs.length]);
        ImageSpan imageSpan = new ImageSpan(context, bitmap);
        SpannableString spannableString = new SpannableString(
                Res.expressionImgNames[arg2]);
        spannableString.setSpan(imageSpan, 0,
                Res.expressionImgNames[arg2].length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    public static String getTopActivity(Activity activity)
    {
        if (activity == null)
        {
            return "";
        }

        ActivityManager am = (ActivityManager) activity
                .getSystemService(Activity.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        LOGManager.d("topActivity " + cn.getClassName());
        return cn.getClassName();
    }

    /**
     * 检查是否存在SDCard
     * 
     * @return
     */
    public static boolean hasSdcard()
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 获取版本号
     * 
     * @return
     */
    public static String getVersion(Context context)
    {
        try
        {
            PackageManager pm = context.getPackageManager();

            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            // int versionCode = pinfo.versionCode;
            String versionName = pinfo.versionName;
            // return context.getString(R.string.travelrely) + " " +
            // versionName;
            return versionName;
        }
        catch (NameNotFoundException e)
        {
            return "TravelRely";
        }
    }

    /**
     * @param mContext
     * @param value 时间字符串 2012-09-23 hh:mm:ss
     * @param act
     *            1:聊天界面 2：消息列表界面
     * @return
     * @throws Exception
     */
//    public static String msgTime(Context mContext, String value)
//            throws Exception
//    {
//
//        SimpleDateFormat format = new SimpleDateFormat(y_m_d);
//        final int DAY = 1440 * 1000 * 60;
//        long msgTime = ConverToDate(value).getTime();
//        long nowTime = Calendar.getInstance().getTime().getTime();
//        long duration = nowTime - msgTime;
//
//        long hours = Calendar.getInstance().getTime().getHours()
//                * (60 * 60 * 1000);
//        long minutes = Calendar.getInstance().getTime().getMinutes()
//                * (60 * 1000);
//        int dayTime = (int) (hours + minutes);
//
//        if (duration > 0)
//        {
//            if (duration <= dayTime)
//            {
//                return timeFormat(value, hm);
//            }
//            else if (duration >= dayTime && duration < (DAY + dayTime))
//            {
////                return value = mContext.getResources().getString(
////                        R.string.yesterday);
//                return value = mContext.getResources().getString(
//                		R.string.yesterday) + timeFormat(value, hm);
//            }
//            else if (duration >= dayTime && duration < (DAY * 6 + dayTime))
//            {
//                Calendar c = Calendar.getInstance();
//                c.setTime(format.parse(value));
//                String Week = "";
//                if (c.get(Calendar.DAY_OF_WEEK) == 1)
//                {
//                    Week += mContext.getResources()
//                            .getString(R.string.saturday);
//                }
//                if (c.get(Calendar.DAY_OF_WEEK) == 2)
//                {
//                    Week += mContext.getResources().getString(R.string.monday);
//                }
//                if (c.get(Calendar.DAY_OF_WEEK) == 3)
//                {
//                    Week += mContext.getResources().getString(R.string.tuesday);
//                }
//                if (c.get(Calendar.DAY_OF_WEEK) == 4)
//                {
//                    Week += mContext.getResources().getString(
//                            R.string.wendesday);
//                }
//                if (c.get(Calendar.DAY_OF_WEEK) == 5)
//                {
//                    Week += mContext.getResources()
//                            .getString(R.string.thursday);
//                }
//                if (c.get(Calendar.DAY_OF_WEEK) == 6)
//                {
//                    Week += mContext.getResources().getString(R.string.friday);
//                }
//                if (c.get(Calendar.DAY_OF_WEEK) == 7)
//                {
//                    Week += mContext.getResources()
//                            .getString(R.string.saturday);
//                }
//                return Week;
//            }
//            else
//            {
//                SimpleDateFormat sdf = new SimpleDateFormat(y_m_d);
//                sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//                return value = sdf.format(new Date(msgTime));
//            }
//        }
//        else
//        {
//            return value;
//        }
//    }

    /**
     * 与当前时间比较
     * 
     * @param x
     * @return
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean nowTimeCompare(String date, String type) throws ParseException
    {

        SimpleDateFormat sdf = new SimpleDateFormat(type);
        Date d1 = sdf.parse(date);

        long timeLong = d1.getTime() - Calendar.getInstance().getTimeInMillis();
        if (timeLong > 0)
        {
            return true;
        }
        return false;
    }

    /*
     * 当获取到的时间是7:1的时候,经过这个方法时间显示为 07:01
     */
    public static String format(int x)
    {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }

    // 把日期转为字符串
    public static String ConverToString(Date date, String type)
    {
        DateFormat df = new SimpleDateFormat(type);
        return df.format(date);
    }

    // 把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.parse(strDate);
    }

    public void getGroups()
    {
        String user = Engine.getInstance().getUserName();
        final List<TraMessage> personMessages = TravelrelyMessageDBHelper
                .getInstance().getMessages(user, 3);
        for (int i = 0; i < personMessages.size(); i++)
        {
            TraMessage groupMessage = personMessages.get(i);
            // map.put(groupMessage.getGroup_name(), groupMessage);

        }
    }

    public static Drawable bitmapToDrawable(Bitmap bitmap)
    {

        Drawable drawable = new BitmapDrawable(bitmap);

        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static boolean isSmilies(String s)
    {

        Pattern pattern = Pattern.compile("\\[(.*?)\\]");
        Matcher m = pattern.matcher(s);
        while (m.find())
        {
            return true;
        }
        return false;
    }

    /***
     * 动态设置listview的高度
     * 
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    List<ContactModel> srcTravelrelyList;

    /**
     * 计算时间差
     * 
     * @param sTime
     * @param eTime
     * @return
     * @throws Exception
     */
    public static long durationTime(String sTime, String eTime)
            throws Exception
    {

        long msgTime = ConverToDate(sTime).getTime();
        long nowTime = ConverToDate(eTime).getTime();
        long duration = (nowTime - msgTime) / (1000 * 60);

        return duration;
    }

    public static void msgTime(List<TraMessage> list) throws Exception
    {

        for (int i = 1; i < list.size(); i++)
        {
            long l = durationTime(list.get(i - 1).getTime(), list.get(i)
                    .getTime());

            if (l < 5)
            {
                list.get(i - 1).setTime(null);
            }
        }
        if (durationTime(ConverToString(getTodayData(), y_m_d_h_m),
                list.get(list.size() - 1).getTime()) < 5)
        {
            list.get(list.size() - 1).setTime(null);
        }
    }

    /**返回二十四小时的字符串形式（时:分）09:02
     * @param time
     * @param type
     * @return
     * @throws Exception
     */
    public static String timeFormat(String time, String type) throws Exception
    {

        SimpleDateFormat sdf = new SimpleDateFormat(type);
        return sdf.format(new Date(ConverToDate(time).getTime()));
    }

    /**
     * 生成随机数
     */

    public static long getRandom()
    {
        // TODO Auto-generated method stub
        Random random = new Random();
        return random.nextLong();
    }

    // 去除号码中非数字字符
    public static String removeNonnumericChar(String strSrc)
    {
        String tmpStr = "";

        if (strSrc == null || strSrc.length() <= 0)
        {
            return "";
        }

        // 去除字符串中非数字字符
        for (int i = 0; i < strSrc.length(); i++)
        {
            if (i == 0 && strSrc.charAt(0) == '+')
            {
                tmpStr += "+";
                continue;
            }

            String tmp = "" + strSrc.charAt(i);
//            if ((tmp).matches("[0-9.]")) // . 可以匹配任意字符，这里只选择要数字
            if ((tmp).matches("[0-9]"))
            {
                tmpStr += tmp;
            }
        }

        return tmpStr;
    }

    /**
     * 用来判断是否是群主
     */
    public static boolean isGroupLeader(String groupId)
    {

        GroupDBHelper gHelper = GroupDBHelper.getInstance();
        return gHelper.isGroupLeader(groupId, "1");
    }

    /**
     * 判断手机是否支持googlemap
     */
    public static boolean isGoogleMapAddOn()
    {
        try
        {
            Class.forName("com.google.android.maps.MapActivity");
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 判断手机是否支持高德地图
     */
    public static boolean isGaoDeMapAddOn()
    {
        try
        {
            Class.forName("com.amap.api.maps.AMap");
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    
    /**
     * 获取现在时间
     * 
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(String type) {
    	  Date currentTime = new Date();
    	  SimpleDateFormat formatter = new SimpleDateFormat(type);
    	  String dateString = formatter.format(currentTime);
    	  return dateString;
    	}
    
	public static List<String> stringsToList(final String[] src) {
		if (src == null || src.length == 0) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < src.length; i++) {
			result.add(src[i]);
		}
		return result;
	}
	
	public static byte[] HexStringToBytes(String paramString)
    {
        int len,k;
        k = paramString.length();
        len = (k % 2 == 1) ? (k/2 + 1) : k/2;
        byte[] arrayOfByte = new byte[len];
        int i;
        int hexChar;
        
        for(i=0;i<len;i++)
        {
            hexChar = 0xFF & Integer.parseInt(paramString.substring(2*i, 2*i+2), 16);
            arrayOfByte[i] = (byte)hexChar;
        }
        return arrayOfByte;
    }
	
	public static String bytesToHexString(byte[] data)
    {
        if (data == null)
        {
            return null;
        }
        
        String content = "";
        for (int i=0; i<data.length; i++)
        {
            String hex = Integer.toHexString(data[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = "0" + hex;
            }
            
            content += hex;
        }
        return content;
    }
	
	
/**** ===============================把一个对象保存在文件中或从文件中读取对象====================*****/
	  public static void writeObjectToFile(Object obj,Context context)
	    {
//	        File file =new File("test.dat");
	        FileOutputStream out;
	        try {
//	            out = new FileOutputStream(file);
	        	out = context.openFileOutput("test.dat", Context.MODE_PRIVATE);
	            ObjectOutputStream objOut=new ObjectOutputStream(out);
	            objOut.writeObject(obj);
	            objOut.flush();
	            objOut.close();
	            System.out.println("write object success!");
	        } catch (IOException e) {
	            System.out.println("write object failed");
	            e.printStackTrace();
	        }
	    }
	  /**
	   * 从文件中读取对象
	   */
	    public static Object readObjectFromFile(Context context)
	    {
	        Object temp=null;
//	        File file =new File("test.dat");
	        FileInputStream in;
	        try {
//	            in = new FileInputStream(file);
	        	in = context.openFileInput("test.dat");
	            ObjectInputStream objIn=new ObjectInputStream(in);
	            temp=objIn.readObject();
	            objIn.close();
	            System.out.println("read object success!");
	        } catch (IOException e) {
	            System.out.println("read object failed");
	            e.printStackTrace();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        }
	        return temp;
	    }
	    /******=============================END=================================******/
	    
	    
	    /**
	     * 获取simmcc 
	     */
	    public static String getMcc(Context context){
	        // TelephonyManager mTelephonyMgr =
	        // (TelephonyManager)application.getBaseContext().
	        // .getSystemService(Context.TELEPHONY_SERVICE);
	        TelephonyManager mTelephonyMgr = (TelephonyManager) context
	                .getSystemService(Context.TELEPHONY_SERVICE);
	        
	        
	        // 返回MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)(IMSI)
	        String simOperator = mTelephonyMgr.getSimOperator();

	        if (simOperator == null || simOperator.equals(""))
	        {
	            LOGManager.d("未装入SIM卡");
	        }
	        String sim_mcc;
			// imsi = "460021184135849";
	        // imsi = "310381184135849";
	        // imsi = "454001184135849";
	        if (simOperator != null && simOperator.length() >= 5)
	        {
	            sim_mcc = simOperator.substring(0, 3);
	        }
	        else
	        {
	            sim_mcc = "";
	        }
	        return sim_mcc;
	    }
	    
	    
	    
	   public static String getPkgName(Context context){
		   return context.getPackageName();
	   } 
	    
	    
	   /**参数 -2当前日期钱两天是几号   2后两天
	 * @return 当前日期的前几天日期是多少号
	 * @throws ParseException
	 */
	public static String getStatetime(int day) throws ParseException{
			  
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			       
			        Calendar c = Calendar.getInstance();  
			        c.add(Calendar.DATE, day);  
			        Date monday = c.getTime();
			        String preMonday = sdf.format(monday);
			        return preMonday;
			   } 
}
