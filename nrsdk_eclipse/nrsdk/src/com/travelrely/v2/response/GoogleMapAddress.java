package com.travelrely.v2.response;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class GoogleMapAddress{
    
    /**
     * 根据地址查询结果解析json
     */
    
    String results;
    
    String status;
    
    List<Results> lResults;

    public List<Results> getlResults() {
        return lResults;
    }

    public void setlResults(List<Results> lResults) {
        this.lResults = lResults;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setValue(JSONObject jsonObject){
        
        lResults = new ArrayList<GoogleMapAddress.Results>();
        
        status = jsonObject.optString("status");
        if(status.equals("OK")){
            JSONArray jsonArray = jsonObject.optJSONArray("results");
            if(jsonArray != null){
                for(int i = 0; i < jsonArray.length(); i ++){
                    jsonObject = jsonArray.optJSONObject(i);
                    if(jsonObject != null){
                        Results results = new Results();
                        results.setValue(jsonObject);
                        lResults.add(results);
                    }
                    
                }
            }
        }
        
    }
    
    public static class Results{
        
        public List<AddressComponents> lAddressComponents;
        
        public String formatted_address;
        
        public Geometry geometry;
        
        public String types;

        public List<AddressComponents> getlAddressComponents() {
            return lAddressComponents;
        }

        public void setlAddressComponents(List<AddressComponents> lAddressComponents) {
            this.lAddressComponents = lAddressComponents;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getTypes() {
            return types;
        }

        public void setTypes(String types) {
            this.types = types;
        }
        
        public void setValue(JSONObject jsonObject){
            
            lAddressComponents = new ArrayList<GoogleMapAddress.AddressComponents>();
            JSONArray jArray = jsonObject.optJSONArray("address_components");
            if(jArray != null){
                for(int i = 0; i < jArray.length(); i ++){
                    JSONObject jAddress = jArray.optJSONObject(i);
                    AddressComponents aComponents = new AddressComponents();
                    aComponents.setValue(jAddress);
                    Log.d("", "解析的结果=" + aComponents.getLong_name());
                    lAddressComponents.add(aComponents);
                }
            }
            formatted_address = jsonObject.optString("formatted_address");
            Log.d("", "formatted_address=" + formatted_address);
            
            JSONObject jGeometry = jsonObject.optJSONObject("geometry");
            Geometry geome = new Geometry();
            geome.setValue(jGeometry);
            geometry = geome;
            
            types = jsonObject.optString("types");
        }
        
    }
    
    public static class AddressComponents{
        
        String long_name;
        
        String short_name;
        
        JSONArray types;

        public JSONArray getTypes() {
            return types;
        }

        public void setTypes(JSONArray types) {
            this.types = types;
        }

        public String getLong_name() {
            return long_name;
        }

        public void setLong_name(String long_name) {
            this.long_name = long_name;
        }

        public String getShort_name() {
            return short_name;
        }

        public void setShort_name(String short_name) {
            this.short_name = short_name;
        }
        
        public void setValue(JSONObject jsonObject){
            long_name = jsonObject.optString("long_name");
            short_name = jsonObject.optString("short_name");
            types = jsonObject.optJSONArray("types");
        }
    }
    
    public static class Geometry{
        
        public String lat;
        
        public String lng;

        public String location_type;
        
        public JSONObject viewport;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
        
        public String getLocation_type() {
            return location_type;
        }

        public void setLocation_type(String location_type) {
            this.location_type = location_type;
        }

        public JSONObject getViewport() {
            return viewport;
        }

        public void setViewport(JSONObject viewport) {
            this.viewport = viewport;
        }
        
        public void setValue(JSONObject jsonObject){
            
            JSONObject jLocation = jsonObject.optJSONObject("location");
            lat = jLocation.optString("lat");
            lng = jLocation.optString("lng");
            location_type = jsonObject.optString("location_type");
            viewport = jsonObject.optJSONObject("viewport");
            
        }
    }

}
