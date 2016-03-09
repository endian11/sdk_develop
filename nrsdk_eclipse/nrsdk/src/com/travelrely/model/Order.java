package com.travelrely.model;

public class Order
{
    public class CrbtType
    {
        public static final int NULL = 0;
        public static final int TRA_MSG = 1;
        public static final int SECRETARY = 2;
        public static final int PASS_THROUGH = 3;
        public static final int INVALID = 255;
    }
    
    public class SimType
    {
        public final static int NULL = 0;
        public final static int HARD_CARD = 1;
        public final static int NEW_VSIM = 2;
        public final static int HAS_VSIM = 3;
    }
    
    public class SimSize
    {
        public final static int NULL = 0;
        public final static int STD = 1;
        public final static int MICRO = 2;
        public final static int NANO = 3;
    }
    
    public class OrderStatus
    {
        public static final int UNPAID = 0;
        public static final int PAID = 1;
        public static final int USING = 2;
        public static final int FINISHED = 3;
    }
    
    public class OrderType
    {
        public static final int NOMAL_TRIP = 0;
        public static final int TICKETS = 1;
        public static final int NO_ROAMING = 2;
        public static final int FRIENDLY_HK = 10;
        public static final int TUNIU = 11;
        public static final int PREPAID = 12;
    }
    
    public class PkgType
    {
        public static final int PREPAID = 0;
        public static final int POSTPAID = 1;
        public static final int NO_ROAMING = 2;
    }
    
    public class Append
    {
        public static final int NULL = 0;
        public static final int TRIP_DAYS = 1;
        public static final int DATA_FLOW = 2;
        public static final int IDD_CALLS = 3;
        public static final int LOC_CALLS = 4;
    }
    
    //是否购买旅信网络电话
    public class NetcallBuyflag{
        
        public static final int NULL = 0;
        public static final int BUY = 1;
    }
    
    //是否购买旅信蓝牙盒子
    public class BuyBleBox{
        
        public static final int NULL = 0;
        public static final int BUY = 1;
    }
    
    //是否购买旅信小号
    public class BuyLxnum{
        
        public static final int NULL = 0;
        public static final int BUY = 1;
    }
}
