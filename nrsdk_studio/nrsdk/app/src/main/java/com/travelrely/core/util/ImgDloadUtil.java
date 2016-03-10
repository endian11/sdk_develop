package com.travelrely.core.util;

import java.io.File;
import java.util.List;

import com.travelrely.core.nrs.Engine;
import com.travelrely.model.ContactModel.TagNumber;
import com.travelrely.app.db.ContactDBHelper;

public class ImgDloadUtil
{
    public static void dloadAllHead(List<TagNumber> tags)
    {
        if (tags == null || tags.size() == 0)
        {
            return;
        }

        for (TagNumber tag : tags)
        {
            String headImgName = tag.getHeadPortrait();
            String headImgPath = tag.getLocalHeadPath();
            if (headImgName == null || headImgName.equals(""))
            {
                LOGManager.d(tag.getValue() + "没有头像");
                continue;
            }

            if (headImgPath != null && headImgPath.contains(headImgName))
            {
                // 说明已经有图片不用下载
                File file = new File(headImgPath);
                LOGManager.d(tag.getValue() + "已经有头像！");
                if (!file.exists())
                {
                    LOGManager.d(tag.getValue() + "本地头像无法找到,重新下载！");
                    download(tag, headImgName);
                }
            }
            else
            {
                download(tag, headImgName);
            }
        }
    }
    
    private static void download(TagNumber tag, String head)
    {
        String localUrl = Engine.getInstance().downloadHeadImg(
                Engine.getInstance().getContext(), head, "_s");
        if (localUrl != null)
        {
            tag.setLocalHeadPath(localUrl);
            ContactDBHelper.getInstance().updateHeadImg(tag);
            LOGManager.d("下载一个头像成功!" + tag.getValue());
        }
        else
        {
            tag.setLocalHeadPath("");
            ContactDBHelper.getInstance().updateHeadImg(tag);
            LOGManager.e("下载一个头像失败!" + tag.getValue());
        }
    }
}