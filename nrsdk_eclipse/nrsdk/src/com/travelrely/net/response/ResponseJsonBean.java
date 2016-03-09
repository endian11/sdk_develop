package com.travelrely.net.response;

import org.json.JSONObject;

public abstract class ResponseJsonBean
{
    public abstract void setValue(JSONObject jsonObject);

    public void setValue(String jsonStr)
    {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonStr);
            setValue(jsonObject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}