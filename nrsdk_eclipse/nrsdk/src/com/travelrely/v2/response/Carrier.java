
package com.travelrely.v2.response;

import java.io.Serializable;

import org.json.JSONObject;

import com.travelrely.core.glms.response.BaseResponse;

public class Carrier extends BaseResponse implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String countryname;

    private String carriername;

    private String mnc;

    private String carrierdesc;
    
	public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCarriername() {
        return carriername;
    }

    public void setCarriername(String carriername) {
        this.carriername = carriername;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getCarrierdesc() {
        return carrierdesc;
    }

    public void setCarrierdesc(String carrierdesc) {
        this.carrierdesc = carrierdesc;
    }

    @Override
    public void setValue(JSONObject jsonObject) {

        this.countryname = jsonObject.optString("countryname");
        this.carriername = jsonObject.optString("carriername");
        this.mnc = jsonObject.optString("mnc");
        this.carrierdesc = jsonObject.optString("carrierdesc");
    }

    @Override
    public String toString() {
        return "Carrier [countryname=" + countryname + ", carriername=" + carriername + ", mnc="
                + mnc + ", carrierdesc=" + carrierdesc + ", toString()=" + super.toString() + "]";
    }
}
