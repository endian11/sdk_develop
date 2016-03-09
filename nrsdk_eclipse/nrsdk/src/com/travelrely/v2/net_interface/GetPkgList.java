package com.travelrely.v2.net_interface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.travelrely.core.glms.HttpConnector;
import com.travelrely.core.glms.json.request.Request;
import com.travelrely.core.glms.response.BaseResponse;
import com.travelrely.core.glms.response.ResponseInfo;
import com.travelrely.core.nrs.Engine;
import com.travelrely.core.nrs.ReleaseConfig;
import com.travelrely.core.util.DeviceInfo;
import com.travelrely.core.util.LOGManager;
import com.travelrely.core.util.SpUtil;
import com.travelrely.v2.model.Package0;
import com.travelrely.v2.response.BaseData;

public class GetPkgList
{
    public static class GetPkgListReq
    {
        public static final String formJsonData()
        {
            String result = "";
            try
            {
                JSONObject json = Request.generateBaseJson();
                JSONObject data = new JSONObject();
                json.put("data", data);
                data.put("link_source", Integer.toString(DeviceInfo.linkSource));
                data.put("version", Integer.toString(SpUtil.getPkgVer()));
    
                result = json.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }
    
        public static GetPkgListRsp getPkgList(final Context mContext)
        {
            String cc = Engine.getInstance().getCC();
            String host = ReleaseConfig.getUrl(cc);
            String url = host + "api/operator/get_package_list";
            
            String postdata = formJsonData();
            HttpConnector httpConnector = new HttpConnector();
            String httpRslt = httpConnector.requestByHttpPut(url, postdata,
                    mContext, false);
            if (httpRslt == null || httpRslt.equals(""))
            {
                LOGManager.d("未接收到服务器端的数据");
                return null;
            }
    
            GetPkgListRsp rsp = new GetPkgListRsp();
            rsp.setValue(httpRslt);
    
            return rsp;
        }
    }
    
    public static class GetPkgListRsp extends BaseResponse implements Serializable
    {
        private static final long serialVersionUID = 1L;

        ResponseInfo responseInfo;
        GetPkgListRsp.Data data;

        public ResponseInfo getBaseRsp()
        {
            return responseInfo;
        }

        public void setBaseRsp(ResponseInfo responseInfo)
        {
            this.responseInfo = responseInfo;
        }

        public GetPkgListRsp.Data getData()
        {
            return data;
        }

        public void setData(GetPkgListRsp.Data data)
        {
            this.data = data;
        }

        @Override
        public void setValue(JSONObject jsonObject)
        {
            super.setValue(jsonObject);
            responseInfo = new ResponseInfo();
            responseInfo.setValue(jsonObject);
            data = new GetPkgListRsp.Data();
            JSONObject dataobj = jsonObject.optJSONObject("data");
            data.setValue(dataobj);
        }

        public static class Data extends BaseData implements Serializable
        {
            private static final long serialVersionUID = 1L;

            int ver;
            List<Package0> packages;
            
            public int getVersion()
            {
                return ver;
            }
            
            public List<Package0> getPackages()
            {
                return packages;
            }

            @Override
            public void setValue(JSONObject jsonObject)
            {
                super.setValue(jsonObject);

                JSONArray jsonArray = jsonObject.optJSONArray("packagelist");
                if (jsonArray == null)
                {
                    return;
                }
                
                packages = new ArrayList<Package0>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);

                    Package0 pkg = new Package0();
                    pkg.setMcc(jsonObject2.optString("mcc"));
                    pkg.setMnc(jsonObject2.optString("mnc"));
                    pkg.setDays(jsonObject2.optInt("days"));
                    pkg.setPrice(jsonObject2.optInt("price"));
                    pkg.setData(jsonObject2.optString("data"));
                    pkg.setLocalVoice(jsonObject2.optString("localvoice"));
                    pkg.setIddVoice(jsonObject2.optString("iddvoice"));

                    packages.add(pkg);
                }
                
                ver = jsonObject.optInt("version");
            }
        }
    }
}
