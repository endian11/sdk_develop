
package com.travelrely.v2.model;

import java.util.ArrayList;
import java.util.List;

public class PhoneSection implements Comparable<PhoneSection> {

    /**
     * 大写为主
     */
    char begin;

    private List<PhoneItem> list;

    public char getBegin() {
        return begin;
    }

    public void setBegin(char begin) {
        this.begin = begin;
    }

    public List<PhoneItem> getList() {
        return list;
    }

    public void setList(List<PhoneItem> list) {
        this.list = list;
    }

    public void generate() {

        list = new ArrayList<PhoneItem>();
        begin = (char)('A' + (int)(Math.random() * 26));
        int size = (int)(Math.random() * 20);
        for (int i = 0; i < size; i++) {
            PhoneItem phoneItem = new PhoneItem();
            phoneItem.name = begin + "" + i;
            list.add(phoneItem);
        }
    }

    @Override
    public int compareTo(PhoneSection another) {
        if (this.begin < another.begin) {
            return -1;
        } else if (this.begin == another.begin) {
            return 0;
        }
        return 1;
    }
}
