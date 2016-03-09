
package com.travelrely.app.activity;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.travelrely.app.adapter.ContentGridViewAdapter;
import com.travelrely.app.adapter.MessageListViewAdapter;
import com.travelrely.app.view.PullDownListView;
import com.travelrely.app.view.DialogManager.OnMedialClickListener;
import com.travelrely.app.view.PullDownListView.OnRefreshListener;
import com.travelrely.core.Engine;
import com.travelrely.core.IAction;
import com.travelrely.core.RecordManager;
import com.travelrely.core.ReleaseConfig;
import com.travelrely.core.Res;
import com.travelrely.core.RecordManager.OnRecordCreateListener;
import com.travelrely.model.ContactModel;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.net.ProgressOverlay;
import com.travelrely.net.ProgressOverlay.OnProgressEvent;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.ContactDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.json.request.Request;
import com.travelrely.v2.response.GetNewGroup;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.response.TripInfoList;
import com.travelrely.v2.util.AESUtils;
import com.travelrely.v2.util.FetchTokenOneTask;
import com.travelrely.v2.util.FileUtil;
import com.travelrely.v2.util.PictureUtil;
import com.travelrely.v2.util.Utils;

public class ChatMsgListAct extends NavigationActivity implements OnClickListener,
        OnMedialClickListener, OnFocusChangeListener, OnRecordCreateListener, 
        OnCheckedChangeListener{

    MessageItemReceiver mReceiver;
    
    Handler handler;

    LinearLayout layout_msg;

    TextView str_title;

    RelativeLayout send_bar;

    Context mContext;

    ImageView send_contont;
    
    ToggleButton send_voic;

    TextView send_bt;
    
    Button voic_message;

    EditText ed_message;

    GridView gViewContent, gViewSmile;

    ContentGridViewAdapter cAdapter;

    MessageListViewAdapter sAdapter;

    private PullDownListView msgList;

    public static List<TraMessage> mDataArrays;

    public TraMessage message;

    private BroadcastReceiver myReceiver;

    public String fromHeadPath;

    public int messageId;

    int fromType = 2;

    int toType = 0;

    int type;

    String nick_name = null;

    String group_name = null;

    String from = null;

    TagNumber number;

    ArrayList<ContactModel> contactModels;

    ContactModel cModel;// 通讯录群

    GetNewGroup groupInfo;

    boolean isNewGroup;

    boolean isGroup;

    String newGroupId;

    int message_type;
    RecordManager recordManager;
    

    int msgCount = 10;// 查询数据条数

    List<TripInfoList> tInfoLists;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        handler = new Handler();
        //Engine.getInstance().sendMessage = this;
        setContentView(R.layout.layout_message_list);
        mContext = this;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactModels = (ArrayList<ContactModel>) extras.getSerializable("contacts");
            cModel = (ContactModel) extras.getSerializable("contact");
            groupInfo = (GetNewGroup) extras.getSerializable("new_group");
            group_name = extras.getString("group_name");
            message_type = extras.getInt("message_type");
            if (message_type == 1) {
                if(MessageActivity.personMessages != null){
                    if (MessageActivity.personMessages.size() > 0) {
                        message = MessageActivity.personMessages.get(extras.getInt("position"));
                        if (message != null) {
                            if (message.isGroup()) {
                                type = 3;
                                fromType = 3;
                                toType = 1;
                                isGroup = true;
                                from = message.getGroup_id();
                            }else{
                                fromHeadPath = message.getFrom_head_portrait();
                            }
                        }
                        from = Engine.getInstance().getUserName();
                      
                    }
                }
            }
            if (contactModels != null) {
                number = contactModels.get(0).getTravelrelyNumber();
                if (contactModels.get(0).getNickName() == null) {
                    nick_name = number.getNewNum();
                } else {
                    nick_name = contactModels.get(0).getNickName();
                }
                from = contactModels.get(0).getPhoneNumList().get(0).getNewNum();
                isNewGroup = false;
                isGroup = false;
                fromHeadPath = contactModels.get(0).getHeadPortrait();
            }
            if (groupInfo != null) {
                nick_name = group_name;
                newGroupId = groupInfo.getData().getId();
                from = newGroupId;
                isNewGroup = true;
                type = 3;
                fromType = 3;
                toType = 1;
                isGroup = true;
            }
            if (cModel != null) {
                from = cModel.getGroupId();
                nick_name = cModel.getGroupName();
                type = 3;
                fromType = 3;
                toType = 1;
                isGroup = true;
                fromHeadPath = cModel.getHeadPortrait();
            }
        }
        
        init();

        if (contactModels != null || cModel != null) {
            str_title.setText(nick_name);
            initReceiver();
            refresh();
        }
        if (groupInfo != null) {
            str_title.setText(nick_name);
        }
        if (message_type == 1) {
            from = message.getFrom();
            nick_name = message.getNick_name();
            group_name = message.getGroup_name();
            setSendMessageStyle();
            send_bar = (RelativeLayout) findViewById(R.id.rl_layout);
            if (message == null) {
                return;
            }
            if (message.getFrom_type() == 0) {
                send_bar.setVisibility(View.GONE);
                getNavigationBar().hideRight();
            } else if (message.getFrom_type() == 2) {
            }
            if (message.getFrom_type() == 1) {
                send_bar.setVisibility(View.GONE);
            }

        }
        if(mReceiver == null){
            mReceiver = new ChatMsgListAct.MessageItemReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IAction.TRA_MSG_ITEM);
            mContext.registerReceiver(mReceiver, intentFilter);
        }
    }

    @Override
    protected void initNavigationBar() {
        // TODO Auto-generated method stub
        getNavigationBar().hideLeftText();
        setLeftText(R.string.travelrely);
        
    }

    public void init() {

        if (this.message != null) {
            getNavigationBar().showRight();
            if (isGroup == true) {
                getNavigationBar().getRightImg().setBackgroundResource(R.drawable.group_message_bg);
            } else {
                getNavigationBar().getRightImg().setBackgroundResource(R.drawable.person_msg_right_bt_bg);
            }
        } else {
            getNavigationBar().showRight();
            if (isGroup == false) {
                getNavigationBar().getRightImg().setBackgroundResource(R.drawable.person_msg_right_bt_bg);
            } else {
                getNavigationBar().getRightImg().setBackgroundResource(R.drawable.group_message_bg);

            }
        }

        str_title = (TextView) findViewById(R.id.title);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) str_title
                .getLayoutParams();
        if (this.message != null) {
            if (isGroup == true) {
                layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.right);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.left_img);
            } else {
            }
        } else {
            if (isGroup == false) {
            } else {
                layoutParams.addRule(RelativeLayout.LEFT_OF, R.id.right);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.left_img);
            }
        }
        str_title.setLayoutParams(layoutParams);

        send_voic = (ToggleButton) findViewById(R.id.img_send_voic);
        send_voic.setOnCheckedChangeListener(this);
        send_contont = (ImageView) findViewById(R.id.img_content_bt);
        send_contont.setOnClickListener(this);

        ed_message = (EditText) findViewById(R.id.ed_message);
        ed_message.setOnClickListener(this);
        ed_message.setOnFocusChangeListener(this);
        send_bt = (TextView) findViewById(R.id.bt_send);
        send_bt.setOnClickListener(this);
        voic_message = (Button)findViewById(R.id.voic_message);
        recordManager = new RecordManager(this, voic_message);
        recordManager.setOnRecordCreateListener(this);
        msgList = (PullDownListView) findViewById(R.id.travel_msg_list);

        mDataArrays = new ArrayList<TraMessage>();
        sAdapter = new MessageListViewAdapter(this, mDataArrays);
        msgList.setAdapter(sAdapter);
        msgList.setOnTouchListener(onListener);
        msgList.setonRefreshListener(onRefreshListener);

        setContentGrid(type);
        setSmileGrid();
    }

    @Override
    public void onRightClick() {
        // TODO Auto-generated method stub
        if (Utils.isNetworkAvailable(this)) {
            if(isGroup){
            }else{
                ContactDBHelper dHelper = ContactDBHelper.getInstance();
                ContactModel contactModel = dHelper.getContactByNumber(from, "new_num");
                if(contactModel != null){
                    goContactDetailActivity(contactModel);
                }else{
                    addContact(from, message);
                }
            }
            
        } else {
            showShortToast(R.string.no_net);
        }
    }

    private void setSendMessageStyle() {
        initReceiver();
        refresh();
        if (message.isGroup()) {
            str_title.setText(message.getGroup_name());
        }
        if (!message.isGroup()) {
            str_title.setText(message.getNick_name());
        }

    }

    private void initReceiver() {
        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    fromHeadPath = bundle.getString("message_from_head");
                    messageId = bundle.getInt("messageId");
                }
                refresh();
            }
        };
    }

    private void refresh() {

        TravelrelyMessageDBHelper travelrelyMessageDBHelper = TravelrelyMessageDBHelper
                .getInstance();

        // 刷新未读消息条数
        if (messageId != 0) {
            travelrelyMessageDBHelper.updateContext(from, String.valueOf(messageId), "msg_id", "unReadCount", String.valueOf(0));
        }
        if (message != null) {
            travelrelyMessageDBHelper.updateContext(message.getFrom(), String.valueOf(message.getId()), "id",
                    "unReadCount", String.valueOf(0));
        }

        final List<TraMessage> list1 = travelrelyMessageDBHelper.getMessages(from, null, msgCount);
        Collections.reverse(list1);

        if (fromHeadPath == null) {
            if (list1.size() > 0) {
                TraMessage msg = travelrelyMessageDBHelper.getContext(from, "head_portrait");
                fromHeadPath = msg.getHead_portrait();
            }
        }

//        if (list1.size() > 1) {
//            try {
//                Utils.msgTime(list1);
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                copyMessages(sAdapter.list, list1);
                sAdapter.list = list1;
                sAdapter.notifyDataSetChanged();
                msgList.setSelection(sAdapter.list.size() - 1);

            }
        });
    }

    private void copyMessages(List<TraMessage> src, List<TraMessage> target) {

        HashMap<Integer, TraMessage> hashmap = new HashMap<Integer, TraMessage>();
        for (int i = 0; i < src.size(); i++) {

            TraMessage message = src.get(i);
            message.getId();

            hashmap.put(message.getId(), message);

        }
        for (int i = 0; i < target.size(); i++) {
            TraMessage targetMessage = target.get(i);
            TraMessage srcMessage = hashmap.get(targetMessage.getId());
            if (srcMessage != null) {
                copyMessage(srcMessage, targetMessage);
            }
        }
        hashmap.clear();
        src.clear();
    }

    private void copyMessage(TraMessage src, TraMessage target) {

        target.locationBitmap = src.locationBitmap;
        target.smallBitmap = src.smallBitmap;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
		if (id == R.id.img_content_bt) {
			hideKeyBoard();
			if (gViewContent.isShown()) {
			    gViewContent.setVisibility(View.GONE);
			} else if (!gViewContent.isShown()) {
			    gViewSmile.setVisibility(View.GONE);
			    gViewContent.setVisibility(View.VISIBLE);
			    Utils.hideInputMethod(ChatMsgListAct.this);
			}
		} else if (id == R.id.bt_send) {
			if (Utils.isNetworkAvailable(mContext)) {
			    String str = ed_message.getText().toString();
			    if (!TextUtils.isEmpty(str.trim())) {
			        sendTextMessage();
			        ed_message.setText("");
			    } else {
			        showShortToast(getResources().getString(R.string.enterContent));
			    }
			} else {
			    showShortToast(R.string.no_net);
			}
		} else if (id == R.id.ed_message) {
			gViewContent.setVisibility(View.GONE);
			gViewSmile.setVisibility(View.GONE);
		}
    }

    private void sendTextMessage() {

        String message = AESUtils.getEntryString(ed_message.getText().toString());

        String from = null;
        String to = null;

        if (message_type == 1) {
            if (this.message.isGroup()) {
                // toType = 1;
                to = this.message.getFrom();
                from = Engine.getInstance().getUserName();
                group_name = str_title.getText().toString();
            } else {
                // toType = 0;
                to = this.message.getFrom();
                from = this.message.getTo();
                nick_name = this.message.getNick_name();
            }
        } else {
            if (isNewGroup == true) {
                // toType = 1;
                to = newGroupId;
                from = Engine.getInstance().getUserName();
            }
            if (cModel != null) {
                to = cModel.getGroupId();
                from = Engine.getInstance().getUserName();
                group_name = cModel.getGroupName();
            }
            if (contactModels != null) {
                to = contactModels.get(0).getPhoneNumList().get(0).getNewNum();
                from = Engine.getInstance().getUserName();
                nick_name = contactModels.get(0).getNickName();
            }
        }

        final TraMessage message2 = Request.generateSendMessage(from, message, to, 0, "", fromType,
                toType, 1, nick_name, group_name, Engine.getInstance().getUserInfo().getData()
                        .getPersonal_info().getHeadPortrait(), fromHeadPath);
        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
        refresh();
        Engine.getInstance().sendMessageInBackground(mContext,message2);
    }

    private void performBackToMain() {
//        Intent intent = new Intent(MessageListsActivity.this, MainActivity.class);
//        intent.putExtra(MainActivity.SHOW_TAB_INDEX, 1);
//        startActivity(intent);
        hideKeyBoard();
        onBackPressed();
    }

    @Override
    public void onLeftClick() {
        performBackToMain();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (gViewSmile.isShown()) {
                gViewSmile.setVisibility(View.GONE);
            }
            if (gViewContent.isShown()) {
                gViewContent.setVisibility(View.GONE);
            } else {
                performBackToMain();
            }
        } else if (keyCode == KeyEvent.ACTION_DOWN) {

        }

        return false;
    }

    // 设置发送选项
    private void setContentGrid(int type) {

        String strFrom = Engine.getInstance().getUserName();
        if(cModel != null){
            strFrom = cModel.getGroupId();
        }
        if(type == 3){
            if(groupInfo != null){
                strFrom = newGroupId;
            }
            if(message != null){
                strFrom = message.getGroup_id();
            }
        }
        gViewContent = (GridView) findViewById(R.id.grid_content);
        cAdapter = new ContentGridViewAdapter(this, type, strFrom);
        gViewContent.setAdapter(cAdapter);
        gViewContent.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gViewContent.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = null;
                switch (position) {
                    case 0:
                        hideKeyBoard();
                        if (gViewContent.isShown()) {
                            gViewContent.setVisibility(View.GONE);
                        }
                        if (send_voic.isChecked()){
                            ed_message.setVisibility(View.VISIBLE);
                            voic_message.setVisibility(View.GONE);
                            send_bt.setVisibility(View.VISIBLE);
                            send_voic.setChecked(false);
                        }
                        gViewSmile.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        startActivityForResult(intent, 1);
                        break;
                    case 2:
                        onSelectCarma();
                        break;
                    case 3:
                        onSelectLocation();
                        break;
                    case 4:
                        goAlarm();
                        break;
                    case 5:
                        showShortToast(R.string.tv_googlemap_no);
                       
                        break;
                    case 6:
                        if(Utils.isGaoDeMapAddOn()){
                            sendReception();
                        }
                        else if(Utils.isGoogleMapAddOn()){
                            sendReception();
                        }else{
                            showShortToast(R.string.tv_googlemap_no);
                        }
                        break;
                    case 7:
                        break;
                    case 8:
//                        Intent it = new Intent(ChatMsgListAct.this, ConveneAct.class);
//                        startActivityForResult(it, ConveneAct.type);
                    	// TODO cwj
                        break;
                    default:
                        break;
                }
            }
        });
    }

    // 设置表情
    private void setSmileGrid() {
        gViewSmile = (GridView) findViewById(R.id.grid_smile);
        gViewSmile.setSelector(new ColorDrawable(Color.TRANSPARENT));
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        // 生成27个表情
        for (int i = 0; i < 33; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("image", Res.expressionImgs[i]);
            listItems.add(listItem);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(mContext, listItems,
                R.layout.grid_items_smile, new String[] {
                        "image"
                }, new int[] {
                        R.id.image
                });

        gViewSmile.setAdapter(simpleAdapter);
        gViewSmile.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 编辑框设置数据
                switch (arg2) {
                    case 32:
                        if (ed_message.getText().length() > 0) {
                            int edLength = ed_message.getSelectionStart();
                            if (edLength >= 11) {
                                ed_message.getText().delete(edLength - 11, edLength);
                            } else {
                                ed_message.setText("");
                            }
                        }
                        break;
                    default:
                        ed_message.append(Utils.bitmapSmile(mContext, arg2));
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            String sdState = Environment.getExternalStorageState();
            if (!sdState.equals(Environment.MEDIA_MOUNTED)) {
                showShortToast(getResources().getString(R.string.noValidStorage));
                return;
            }
            String path = fileDir + "/" + fileName + ".jpg";
            Bitmap bitmap = PictureUtil.loadImageFromUrl(path);
            sendBitmap(bitmap, path);
            return;
        }
        if (requestCode == 0) {
            Bundle b = data.getExtras();
            message = (TraMessage) b.getSerializable("message");
            setSendMessageStyle();
        } else if (requestCode == 1) {
            if (data != null) {
                Uri uri = data.getData();
                Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String imgPath = cursor.getString(cursor
                        .getColumnIndex(android.provider.MediaStore.Images.Media.DATA));
                Bitmap bitmap = PictureUtil.loadImageFromUrl(imgPath);
                sendBitmap(bitmap, imgPath);
            }
        }
        if (resultCode == 3) {
            Bundle b = data.getExtras();
            String time = b.getString("date_time");

            sendAlarm(time);
        }
       

//        if (resultCode == LocationGaoDeActivity.LOCTION_TYPE) {
//
//            sendLocationMessage();
//        }
        
//        if (requestCode == SelectTripCalendarActivity.TRIP_TYPE){
//            if(data != null){
//                Bundle bundle = data.getExtras();
//                if(bundle != null){
//                    String msg = bundle.getString("MSG");
//                    sendTripMessage(msg);
//                }
//            }
//        }
        // TODO cwj
        
//        if (requestCode == ConveneAct.type) {
//            if(data != null){
//                Bundle bundle = data.getExtras();
//                if(bundle != null){
//                    String msg = bundle.getString("CONTENT");
//                    sendConvene(msg);
//                }
//            }
//        }
        // TODO cwj
    }

    /**
     * 处理从gallery 和 camera里拿到的图片并发送出去
     * 
     * @param bitmap
     */
    private void sendBitmap(Bitmap bitmap, String path) {

        if (bitmap != null) {
            final Bitmap scaled = Utils.generateBigBitmap(bitmap, path);
            if (scaled != bitmap) {
                bitmap.recycle();
            }

            ProgressOverlay progressOverlay = new ProgressOverlay(this);
            progressOverlay.show("正在发送...", new OnProgressEvent() {

                @Override
                public void onProgress() {
                    String cc = Engine.getInstance().getCC();
                    String host = ReleaseConfig.getUrl(cc);
                    String content = Engine.getInstance().uploadImg(host + "api/message/upload", scaled, null);
                    // 如果上传失败，就把这张图片用时间点存下来
                    if (content == null) {

                        content = String.valueOf(System.currentTimeMillis());
                    }
                    sendImgMessage(scaled, content);
                }
            });

        }

    }

    private String uploadVoice(String path) {
        String smc_loc = null;
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }
        if (Engine.getInstance().homeLogin == true) {
            smc_loc = Engine.getInstance().getHomeSmcLoc();
        } else {
            smc_loc = Engine.getInstance().getRoamSmcLoc();
        }
        String urlstr = "http://" + smc_loc + "/" + "api/message/upload";
        String end = "\r\n";
        String Hyphens = "--";
        String boundary = "------WebKitFormBoundaryTowhxUoSqFqPQ2El";

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            URL url = new URL(urlstr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(Hyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; name=\"msgfile\"; filename=\"bi0503.jpg\";Context-Type:image/jpeg"
                    + end);
            ds.writeBytes(end);
            // bitmap.compress(CompressFormat.JPEG, 100, ds);
            byte[] buffer = new byte[256];
            while (fileInputStream.read(buffer) > 0) {
                ds.write(buffer);
            }
            ds.writeBytes(end);
            ds.writeBytes(Hyphens + boundary + Hyphens + end);
            ds.flush();
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            System.out.println("b=" + b);
            ds.close();
            return b.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private void sendImgMessage(Bitmap bitmap, String content) {
        // String to = this.message.getWidth_user();

        if (this.message != null) {
            if (this.message.isGroup()) {
                toType = 1;
            }
        }
        if (isNewGroup == true) {
            toType = 1;
        }

        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), content,
                from, 2, TraMessage.EXT_TYPE_JPG, fromType, toType, 1, nick_name, group_name,
                Engine.getInstance().getUserInfo().getData().getPersonal_info().getHeadPortrait(),
                fromHeadPath);
        FileUtil file = new FileUtil(this);
        boolean isSave = file.saveImg(message2, bitmap);
        bitmap.recycle();
        if (isSave) {
            TravelrelyMessageDBHelper.getInstance().addMessage(message2);
            refresh();
            Engine.getInstance().sendMessageInBackground(mContext,message2);
        }
    }

    private void sendVoiceMessage(String systemPath, String content) {
        FileUtil fileUtil = new FileUtil(this);
        TraMessage message = Request.generateSendMessage(Engine.getInstance().getUserName(), content,
                from, 4, "amr", fromType, toType, 1, nick_name, group_name,
                Engine.getInstance().getUserInfo().getData().getPersonal_info().getHeadPortrait(),
                fromHeadPath);
        boolean suc = fileUtil.saveVoice(message, systemPath);
        if (suc) {
            TravelrelyMessageDBHelper.getInstance().addMessage(message);
            refresh();
            Engine.getInstance().sendMessageInBackground(mContext,message);
        }
    }

    public static String fileDir = "";

    public static long fileName = 0;

    public void onSelectCarma() {
        fileDir = Environment.getExternalStorageDirectory().toString() + "/myImage";
        File path1 = new File(fileDir);
        if (!path1.exists()) {
            path1.mkdirs();
        }

        fileName = System.currentTimeMillis();
        File file = new File(path1, fileName + ".jpg");
        Uri mOutPutFileUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 调用android自带的照相机
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onSelectGallery() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    private void recycleListImg() {
        for (TraMessage message : sAdapter.list) {
            message.destory();
        }
        sAdapter.list.clear();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
        unregisterReceiver(mReceiver);
        
        
        recycleListImg();
        // Engine.getInstance().sendMessage = null;
        sAdapter.destory();
    }

    public void goAlarm() {
//        Intent intent = new Intent(this, AlarmActivity.class);
//        startActivityForResult(intent, 3);
    	// TODO cwj
    }

    public void sendAlarm(String msg) {

        if (this.message != null) {
            if (this.message.isGroup()) {
                toType = 1;
            }
        }
        if (isNewGroup == true) {
            toType = 1;
            // from = Engine.getIonstance().getUserName();
        }

        String message = AESUtils.getEntryString(msg);
        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), message,
                from, 3, "", 3, 1, 1, nick_name, group_name, Engine.getInstance().getUserInfo()
                        .getData().getPersonal_info().getHeadPortrait(), fromHeadPath);

        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
        refresh();
        Engine.getInstance().sendMessageInBackground(mContext,message2);
    }

    @Override
    public void onSelectLocation() {
        // TODO Auto-generated method stub
//        if(Utils.isGaoDeMapAddOn()){
//            Intent intent = new Intent(this, LocationGaoDeActivity.class);
//            startActivityForResult(intent, LocationGaoDeActivity.LOCTION_TYPE); 
//        }
//        else if(Utils.isGoogleMapAddOn()){
//            if(Engine.getInstance().isRoaming == 1){
//                Intent intent = new Intent(this, LocationGoogleActivity.class);
//                startActivityForResult(intent, LocationGaoDeActivity.LOCTION_TYPE);
//            }
//        }
    	// TODO cwj
        
    }

    public void sendLocationMessage() {

//        if (this.message != null) {
//            if (this.message.isGroup()) {
//                toType = 1;
//            }
//        }
//        if (isNewGroup == true) {
//            toType = 1;
//        }
//
//        double la = Engine.getInstance().getaLocation().getLatitude();
//        double lo = Engine.getInstance().getaLocation().getLongitude();
//        String msg = la + "\t" + lo + "\t"
//                + Engine.getInstance().getaLocation().getExtras().getString("desc");
//        String message = AESUtils.getEntryString(msg);
//        // String to = this.message.getWidth_user();
//        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), message,
//                from, 1, "", fromType, toType, 1, nick_name, group_name,
//                Engine.getInstance().getUserInfo().getData().getPersonal_info().getHeadPortrait(),
//                fromHeadPath);
//
//        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
//        refresh();
//        Engine.getInstance().sendMessageInBackground(mContext,message2);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (v.getId() == R.id.ed_message && hasFocus) {
            gViewContent.setVisibility(View.GONE);
            gViewSmile.setVisibility(View.GONE);
        }
    }

    View.OnTouchListener onListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                hideKeyBoard();
                if (gViewSmile.isShown()) {
                    gViewSmile.setVisibility(View.GONE);
                }
                if (gViewContent.isShown()) {
                    gViewContent.setVisibility(View.GONE);
                }
            }
            return false;
        }
    };

    PullDownListView.OnRefreshListener onRefreshListener = new OnRefreshListener() {

        @Override
        public void onRefresh() {
            // TODO Auto-generated method stub
            new AsyncTask<Void, Void, Void>() {
                protected Void doInBackground(Void... params) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    msgCount += 5;
                    refresh();
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    sAdapter.notifyDataSetChanged();
                    msgList.onRefreshComplete();
                }

            }.execute();
        }
    };

    @Override
    public void onRecordCreate(final String path) {
        ProgressOverlay progressOverlay = new ProgressOverlay(this);
        progressOverlay.show("正在发送...", new OnProgressEvent() {

            @Override
            public void onProgress() {
                String content = uploadVoice(path);
                // 如果上传失败，就把这张图片用时间点存下来
                if (content == null) {

                    content = String.valueOf(System.currentTimeMillis());
                }
                sendVoiceMessage(path, content);
            }
        });

    }
    
    private void sendTripMessage(String msg){
        
        if (this.message != null) {
            if (this.message.isGroup()) {
                toType = 1;
            }
        }
        if (isNewGroup == true) {
            toType = 1;
            // from = Engine.getIonstance().getUserName();
        }

        String message = AESUtils.getEntryString(msg);
        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), message,
                from, 5, "", 3, 1, 1, nick_name, group_name, Engine.getInstance().getUserInfo()
                        .getData().getPersonal_info().getHeadPortrait(), fromHeadPath);

        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
        refresh();
        Engine.getInstance().sendMessageInBackground(mContext,message2);
    }
    
    private void goLocationSet(Class<?> pClass){
        
        Bundle bundle = new Bundle();
        bundle.putString("FROM", from);
        bundle.putString("NICK_NAME", nick_name);
        bundle.putString("GROUP_NAME", group_name);
        bundle.putString("FROM_HEADPATH", fromHeadPath);
        openActivity(pClass,bundle);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // TODO Auto-generated method stub
        send_voic.setChecked(isChecked);
        if(isChecked){
            hideKeyBoard();
            ed_message.setVisibility(View.GONE);
            send_bt.setVisibility(View.GONE);
            voic_message.setVisibility(View.VISIBLE);
            if(gViewSmile.isShown()){
                gViewSmile.setVisibility(View.GONE);
            }
            
        }else{
            ed_message.setVisibility(View.VISIBLE);
            send_bt.setVisibility(View.VISIBLE);
            voic_message.setVisibility(View.GONE);
        }
    }
    
    private void sendReception(){
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, Calendar.getInstance().getTime().getMinutes() + 10);
        calendar.getTime().getMinutes();
        
        SimpleDateFormat sdf = new SimpleDateFormat(Utils.y_m_d_h_m_s);
        String dateStr = sdf.format(calendar.getTime());
        
        String msg = Engine.getInstance().getUserName() + String.valueOf(Utils.getRandom()) + "\t" + 
                Utils.GetDate(1, Utils.y_m_d_h_m_s) + "\t" + dateStr;
        String message = AESUtils.getEntryString(msg);
        
        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), message,
                from, 7, "", 3, 1, 1, nick_name, group_name, Engine.getInstance().getUserInfo()
                        .getData().getPersonal_info().getHeadPortrait(), fromHeadPath);

        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
        refresh();
        Engine.getInstance().sendMessageInBackground(mContext,message2);
    }
    
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        
        IntentFilter filter = new IntentFilter();
        filter.addAction(IAction.MSM_ACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);
    }
    
    public class MessageItemReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, final Intent intent)
        {
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                String path = bundle.getString("VOIC_PATH"); 
                int msgId = bundle.getInt("MSG_ID"); 
                TravelrelyMessageDBHelper.getInstance().deleteMessage(msgId);
                onRecordCreate(path);
            }
            refresh();
        }
    }
    
    private void sendConvene(String msg){
        
        if (this.message != null) {
            if (this.message.isGroup()) {
                toType = 1;
            }
        }
        if (isNewGroup == true) {
            toType = 1;
            // from = Engine.getIonstance().getUserName();
        }

        String message = AESUtils.getEntryString(msg);
        TraMessage message2 = Request.generateSendMessage(Engine.getInstance().getUserName(), message,
                from, TraMessage.TYPE_CONVENE, "", 3, 1, 1, nick_name, group_name, Engine.getInstance().getUserInfo()
                        .getData().getPersonal_info().getHeadPortrait(), fromHeadPath);

        TravelrelyMessageDBHelper.getInstance().addMessage(message2);
        refresh();
        Engine.getInstance().sendMessageInBackground(mContext,message2);
    }
    
    /**
     * 添加联系人
     */
    private void addContact(String number, TraMessage message){
        
//        ArrayList<ContactModel> contactModels = new ArrayList<ContactModel>();
        ArrayList<TagNumber> tagNumbers = new ArrayList<TagNumber>();
        final ContactModel contact = new ContactModel();
        TagNumber tagNumber = new TagNumber();
        tagNumber.setValue(number);
        tagNumber.setNewNum(number);
        tagNumber.setRegist(1);
        tagNumbers.add(tagNumber);
        contact.setPhoneNumList(tagNumbers);
        contact.setTravelrelyNumber(tagNumber.getNewNum());
        contact.setTravelPhoneNumber(tagNumber.getNewNum());
        contact.setNickName(message.getNick_name());
        contact.setLastName(message.getNick_name());
        contact.setFirstName("");
        contact.setContactType(1);
        contact.setTravelUserPhone(message.getTo());
        contact.setHeadPortrait(message.getHead_portrait());
        contact.addTagNumberInDB(tagNumber);
        
//        contactModels.add(contact);
        
//        ContactDBHelper.getInstance().insert(contact);
        doFinish(contact);
        
        goContactDetailActivity(contact);
    }
    
    private void doFinish(ContactModel contactModel) {
        FetchTokenOneTask fetchTokenOneTask = new FetchTokenOneTask();
        fetchTokenOneTask.setFetchTokenListener(Engine.getInstance());
        fetchTokenOneTask.execute(contactModel);
    }

    /*
     *联系人详情
     */
    private void goContactDetailActivity(ContactModel contactModel){
        
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("contact", contactModel);
//        openActivity(MsgPersonageSetActivity.class, bundle);
    	// TODO cwj
    }
}
