package com.travelrely.v2;

public class AesLib
{
    static
    {
        System.loadLibrary("Aes");
    }
    
    native public static int set_key(byte[]key);
    native public static int encrypt(byte[]src, byte[]dst);
    native public static int decrypt(byte[]src, byte[]dst);
}
