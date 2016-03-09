package com.travelrely.v2.net_interface;

import java.io.Serializable;
import org.json.JSONObject;

import com.travelrely.net.response.BaseResponse;
import com.travelrely.net.response.ResponseInfo;
import com.travelrely.v2.response.BaseData;

public class GetNRInfoRsp extends BaseResponse implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	ResponseInfo				responseInfo;
	GetNRInfoRsp.Data		data;

	public ResponseInfo getBaseRsp()
	{
		return responseInfo;
	}

	public void setBaseRsp(ResponseInfo responseInfo)
	{
		this.responseInfo = responseInfo;
	}

	public GetNRInfoRsp.Data getData()
	{
		return data;
	}

	public void setData(GetNRInfoRsp.Data data)
	{
		this.data = data;
	}

	@Override
	public void setValue(JSONObject jsonObject)
	{
		super.setValue(jsonObject);
		responseInfo = new ResponseInfo();
		responseInfo.setValue(jsonObject);

		data = new GetNRInfoRsp.Data();
		JSONObject dataobj = jsonObject.optJSONObject("data");
		data.setValue(dataobj);
	}

	public static class Data extends BaseData implements Serializable
	{
		private static final long	serialVersionUID	= 1L;

		int version;
		int nr_status;
		int lxnum_status;
		String start_date;
		String end_date;
		String femtoIp;
		String timeZone;
		int no_disturb_mode;
		String no_disturb_start;
		String no_disturb_end;

		public int getVersion()
        {
            return version;
        }

        public void setVersion(int version)
        {
            this.version = version;
        }

        public int getStatus()
        {
            return nr_status;
        }

        public void setStatus(int status)
        {
            this.nr_status = status;
        }

        public int getLxnum_status()
        {
            return lxnum_status;
        }

        public void setLxnum_status(int lxnum_status)
        {
            this.lxnum_status = lxnum_status;
        }

        public String getStart_date()
        {
            return start_date;
        }

        public void setStart_date(String start_date)
        {
            this.start_date = start_date;
        }

        public String getEnd_date()
        {
            return end_date;
        }

        public void setEnd_date(String end_date)
        {
            this.end_date = end_date;
        }

        public String getFemtoIp()
        {
            return femtoIp;
        }

        public void setFemtoIp(String femtoIp)
        {
            this.femtoIp = femtoIp;
        }

        public String getTimeZone()
        {
            return timeZone;
        }

        public void setTimeZone(String timeZone)
        {
            this.timeZone = timeZone;
        }

        public int getNo_disturb_mode()
        {
            return no_disturb_mode;
        }

        public void setNo_disturb_mode(int no_disturb_mode)
        {
            this.no_disturb_mode = no_disturb_mode;
        }

        public String getNo_disturb_start()
        {
            return no_disturb_start;
        }

        public void setNo_disturb_start(String no_disturb_start)
        {
            this.no_disturb_start = no_disturb_start;
        }

        public String getNo_disturb_end()
        {
            return no_disturb_end;
        }

        public void setNo_disturb_end(String no_disturb_end)
        {
            this.no_disturb_end = no_disturb_end;
        }

        @Override
		public void setValue(JSONObject jsonObject)
		{
			super.setValue(jsonObject);

			version = jsonObject.optInt("nrs_info_version");
			nr_status = jsonObject.optInt("nrs_status");
			lxnum_status = jsonObject.optInt("lxnum_status");
			
			start_date = jsonObject.optString("start_date");
			end_date = jsonObject.optString("end_date");
			
			femtoIp = jsonObject.optString("femto_ip");
			timeZone = jsonObject.optString("time_zone");
			no_disturb_mode = jsonObject.optInt("no_disturb_mode");

			JSONObject obj = jsonObject.optJSONObject("no_disturb_period");
			if (obj == null)
			{
			    no_disturb_start = "";
			    no_disturb_end = "";
				return;
			}

			no_disturb_start = obj.optString("start_time");
			no_disturb_end = obj.optString("end_time");
		}
	}
}
