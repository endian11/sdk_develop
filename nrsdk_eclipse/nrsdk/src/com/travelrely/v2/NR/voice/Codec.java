package com.travelrely.v2.NR.voice;
import android.R.integer;
import android.util.Log;

public class Codec {
    static final private String TAG = "amrnb-codec";
    
    static final private Codec INSTANCE = new Codec();
    
    public native void amr_test(int delaytime, int mode);
//    public native void amr_lib_init();
//    public native void amr_lib_finit();
//    public native int  amr_enc(byte[] samples, byte[] data);
//    public native int amr_dec(byte[] data, byte[] samples); 
//    public native int amr_cng(byte[] samples);
//    public native void amr_aec_init(int mode);
//    public native void amr_aec_finit();
//    public native int  amr_far_denoise(byte[] far, byte[] defar);
//    public native void ec(byte[] far, byte[] near, byte[] aec); // ec : cancel far sound' echo
    
    public native int encode(byte[] samples, int samplesOffset,
            int samplesLength, byte[] data, int dataOffset, int amrMode);
    
    public native int decode(byte[] data, int dataOffset, int dataLength,
            byte[] samples, int samplesOffset); 
    
    public native void Encoder_Interface_init(int dtx);
    public native void Encoder_Interface_exit();
    public native int  Decoder_Interface_init();
    public native void Decoder_Interface_exit();

    
    private Codec() {
        try {
        	System.loadLibrary("amrnb-codec");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    static public Codec instance() {
        return INSTANCE;
    }
 }
