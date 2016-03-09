
package com.travelrely.app.adapter;

import java.text.ParseException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.travelrely.core.Engine;
import com.travelrely.core.nr.util.MessageUtil;
import com.travelrely.sdk.R;
import com.travelrely.v2.db.GroupDBHelper;
import com.travelrely.v2.db.TravelrelyMessageDBHelper;
import com.travelrely.v2.response.GetNewGroup;
import com.travelrely.v2.response.TraMessage;
import com.travelrely.v2.util.FileUtil;
import com.travelrely.v2.util.TimeUtil;
import com.travelrely.v2.util.Utils;

public class SystemMessageAdapter extends NavigationBaseAdapter {

    public List<TraMessage> list;
    Context context;
    Handler otherThreadHandler;
    private LayoutInflater mInflater;
    private int mRightWidth = 0;

    public SystemMessageAdapter(Context context, List<TraMessage> list, int rightWidth) {

        this.context = context;
        this.list = list;
        this.mRightWidth = rightWidth;
        HandlerThread handlerThread = new HandlerThread("downloadImg");
        handlerThread.start();
        otherThreadHandler = new Handler(handlerThread.getLooper());

        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListHoder listHoder = new ListHoder();

        final TraMessage m = list.get(position);
//        System.out.println("SystemMessageAdapter : mmmmmmmmmmmmmmmmmmmmmmm " + m);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.per_msg_item, null);

            listHoder.name = (TextView) convertView.findViewById(R.id.name);
            listHoder.msg = (TextView) convertView.findViewById(R.id.msg);
            listHoder.time = (TextView) convertView.findViewById(R.id.time);
            listHoder.pic = (ImageView) convertView.findViewById(R.id.pic);
            listHoder.msg_count = (TextView) convertView.findViewById(R.id.tv_msg_count);
            listHoder.frame_msg_count = (FrameLayout) convertView.findViewById(R.id.frame_msg_count);
            listHoder.item_left = convertView.findViewById(R.id.left_layout);
            listHoder.item_right = convertView.findViewById(R.id.right_layout);

            convertView.setTag(listHoder);
        } else {
            listHoder = (ListHoder) convertView.getTag();
        }

        checkGroupValidity(m);

        if (m.getUnReadCount() > 0) {
            listHoder.frame_msg_count.setVisibility(View.VISIBLE);
            String s = String.valueOf(m.getUnReadCount());
            listHoder.msg_count.setText(s);
        } else {
            listHoder.frame_msg_count.setVisibility(View.GONE);
        }

        String from = MessageUtil.getFromType(m);
        listHoder.name.setText(from);
        String content = MessageUtil.generateContent(m);
        listHoder.msg.setText(content);

        String time1 = null;
        try {
            time1 = TimeUtil.getChatTime(m.getTime(), TimeUtil.dateFormat2);
//            System.out.println("SystemMessageAdapter : --------------------------time1" + time1);
            if (time1 != null){
            		listHoder.time.setText(time1);
            		System.out.println("SystemMessageAdapter : listHoder.time.settext()=======");
            	}
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (m.isGroup()) {
            listHoder.pic.setImageBitmap(Utils.drawableToBitmap(context.getResources().getDrawable(
                    R.drawable.group_icon)));
        }
        if (m.getMsg_type() == 2) {
            if (m.getFrom_type() == 0) {
                listHoder.pic.setImageBitmap(Utils.drawableToBitmap(context.getResources()
                        .getDrawable(R.drawable.tra_msg_icon)));
            }
            if (m.getFrom_type() == 1) {
                listHoder.pic.setImageBitmap(Utils.drawableToBitmap(context.getResources()
                        .getDrawable(R.drawable.ic_launcher)));
            }
            if (m.getFrom_type() == 2) {
                listHoder.pic.setImageBitmap(m.getHeadBitmap(context));
            }
            if (m.getFrom_type() == 3) {
                listHoder.pic.setImageBitmap(Utils.drawableToBitmap(context.getResources()
                        .getDrawable(R.drawable.group_icon)));
            }
        }
        if (m.getMsg_type() == 1) {
            if (m.getTo_type() == 0) {
                Bitmap headBitmap = null;
                Bitmap bitmap = FileUtil.readUserHeadImg(m.getFrom_head_portrait() + "_s");
                Bitmap small = Utils.headBitmap(bitmap);

                if (small == bitmap) {
                } else {
                    bitmap.recycle();
                }
                headBitmap = small;
                listHoder.pic.setImageBitmap(headBitmap);
            }
        }

        if (Engine.getInstance().getIsMsgTopType(m.getGroup_id()) == 1) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.green1));
        } 
        if(Engine.getInstance().getIsMsgTopType(m.getFrom()) == 1){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.green1));
        }
        else {
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }
        
        LinearLayout.LayoutParams lp1 = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        listHoder.item_left.setLayoutParams(lp1);
        LinearLayout.LayoutParams lp2 = new LayoutParams(mRightWidth,
                LayoutParams.MATCH_PARENT);
        listHoder.item_right.setLayoutParams(lp2);
        
        listHoder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        
        convertView.invalidate();
        return convertView;
    }

    /**
     * 检查群的有效期 若过期就删除掉本地消息
     */
    private void checkGroupValidity(TraMessage message) {

        if (message.isGroup()) {
            try {
                if (Utils.isGroupLeader(message.getFrom())) {
                    GroupDBHelper gHelper = GroupDBHelper.getInstance();
                    GetNewGroup.Data data = gHelper.getNewGroup(message.getFrom(), 1);
                    if (!Utils.nowTimeCompare(data.getExpireddate(), Utils.y_m_d_h_m)) {
                        TravelrelyMessageDBHelper.getInstance().deleteMessageList(
                                message.getFrom());
                        if (message.getUrl() != null) {
                            FileUtil fileUtil = new FileUtil(context);
                            fileUtil.deleteFile(message.getUrl());
                        }
                    }
                }
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public class ListHoder {

        public TextView name = null;
        public TextView msg = null;
        public TextView time = null;
        public ImageView pic = null;
        public TextView msg_count = null;
        public FrameLayout frame_msg_count = null;
        View item_left;
        View item_right;
        
    }

}
