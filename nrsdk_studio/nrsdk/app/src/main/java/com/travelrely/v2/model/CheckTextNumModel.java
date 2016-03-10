package com.travelrely.v2.model;

/** 
 * 
 * @author zhangyao
 * @version 2014年9月25日上午10:25:13
 * 用来遍历系统消息中的电话号码及特殊字段
 */

public class CheckTextNumModel {
    
    String num;
    
    int startPosttion;
    
    int endPosttion;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getStartPosttion() {
        return startPosttion;
    }

    public void setStartPosttion(int startPosttion) {
        this.startPosttion = startPosttion;
    }

    public int getEndPosttion() {
        return endPosttion;
    }

    public void setEndPosttion(int endPosttion) {
        this.endPosttion = endPosttion;
    }

}
