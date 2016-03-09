package com.travelrely.core.nr.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

public class ActivityCollector {
	private static List<Activity> activities = new ArrayList<Activity>();
	
	
	public static void addActivity(Activity activity){
		activities.add(activity);
	}
	
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	public static Activity getEndActivity(){
        return activities.get(activities.size()-1);
    }
	
	public static void finishAll(){
		
		Iterator<Activity> iterator = activities.iterator();
		while(iterator.hasNext()){
			Activity next = iterator.next();
			iterator.remove();
		}
	}
}
