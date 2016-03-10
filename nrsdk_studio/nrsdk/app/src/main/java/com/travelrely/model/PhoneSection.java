package com.travelrely.model;

import java.util.ArrayList;
import java.util.List;

public class PhoneSection implements Comparable<PhoneSection>
{
    /**
     * 大写为主
     */
    char begin;

    private List<ContactModel> list;

    public char getBegin()
    {
        return begin;
    }

    public void setBegin(char begin)
    {
        this.begin = begin;
    }

    public List<ContactModel> getList()
    {
        return list;
    }

    public void setList(List<ContactModel> list)
    {
        this.list = list;
    }

    public PhoneSection()
    {
        list = new ArrayList<ContactModel>();
    }

    public void generate()
    {
        list = new ArrayList<ContactModel>();
        begin = (char) ('A' + (int) (Math.random() * 26));
        int size = (int) (Math.random() * 20);
        for (int i = 0; i < size; i++)
        {
            ContactModel phoneItem = new ContactModel();
            phoneItem.first_name = begin + "" + i;
            list.add(phoneItem);
        }
    }

    @Override
    public int compareTo(PhoneSection another)
    {
        if (this.begin < another.begin)
        {
            return -1;
        }
        else if (this.begin == another.begin)
        {
            return 0;
        }
        return 1;
    }
}
