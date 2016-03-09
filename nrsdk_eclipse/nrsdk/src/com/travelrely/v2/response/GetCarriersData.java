
package com.travelrely.v2.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCarriersData extends BaseData implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String username;

    private String mcc;

    private List<Carrier> carriers;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public List<Carrier> getCarriers() {
        return carriers;
    }

    public void setCarriers(List<Carrier> carriers) {
        this.carriers = carriers;
    }

    @Override
    public void setValue(JSONObject jsonObject) {
        super.setValue(jsonObject);

        username = jsonObject.optString("username");
        mcc = jsonObject.optString("mcc");

        JSONArray jsonArray = jsonObject.optJSONArray("carriers");
        this.carriers = new ArrayList<Carrier>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = (JSONObject)jsonArray.getJSONObject(i);
                Carrier carrier = new Carrier();
                carrier.setValue(jsonObject2);
                carriers.add(carrier);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "GetCarriersData [username=" + username + ", mcc=" + mcc + ", carriers=" + carriers
                + ", toString()=" + super.toString() + "]";
    }

}
