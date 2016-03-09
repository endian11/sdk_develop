package com.travelrely.core.nr.voice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.R.string;

public class pcm2wav {
	static final private pcm2wav Instance=new pcm2wav();
	
	public boolean convertPcm2Wav(String pcmFilePath){
		
		try {
			File file=new File(pcmFilePath);
			if (!file.exists()) {
				return false;
			}
			
			FileInputStream ins=new FileInputStream(pcmFilePath);
	        if (ins==null) {
	        	return false;
			}
			
			String dirPathString=file.getParent();
			String pcmFileNameString=file.getName();
			String wavFilePathString=dirPathString+"/"+pcmFileNameString+".wav";
			RandomAccessFile randomAccessWriter = new RandomAccessFile(wavFilePathString, "rw");  
			randomAccessWriter.setLength(0); // Set file length to 0, to prevent unexpected behavior in case the file already existed  
			randomAccessWriter.writeBytes("RIFF");  
			randomAccessWriter.writeInt(0); // Final file size not known yet, write 0  
			randomAccessWriter.writeBytes("WAVE");  
			
			randomAccessWriter.writeBytes("fmt "); 
			randomAccessWriter.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM  
			randomAccessWriter.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM  
			randomAccessWriter.writeShort(Short.reverseBytes((short) 1));// Number of channels, 1 for mono, 2 for stereo  
			randomAccessWriter.writeInt(Integer.reverseBytes((int)8000)); // Sample rate  
			randomAccessWriter.writeInt(Integer.reverseBytes((int)16000)); // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8  
			randomAccessWriter.writeShort(Short.reverseBytes((short)2)); // Block align, NumberOfChannels*BitsPerSample/8  
			randomAccessWriter.writeShort(Short.reverseBytes((short)16)); // Bits per sample  
			randomAccessWriter.writeBytes("data");  
			randomAccessWriter.writeInt(0); // Data chunk size not known yet, write 0  
			
			int payloadSize =0;
			
			byte[] pcmContent=new byte[200];//100个short采样点
			int readCount=ins.read(pcmContent);
			while (readCount==pcmContent.length) {
				randomAccessWriter.write(pcmContent);
				payloadSize+=pcmContent.length;
				readCount=ins.read(pcmContent);
			}
			if (readCount>0 && readCount!=pcmContent.length) {
				randomAccessWriter.write(pcmContent,0,readCount);
				payloadSize+=readCount;
			}			
			
			ins.close();
			randomAccessWriter.seek(4); // Write size to RIFF header  
	        randomAccessWriter.writeInt(Integer.reverseBytes(36+payloadSize));  
	        
	        randomAccessWriter.seek(40); // Write size to Subchunk2Size field  
	        randomAccessWriter.writeInt(Integer.reverseBytes(payloadSize));  

	        randomAccessWriter.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		  
		return true;
	}
	
	public static pcm2wav getInstance(){
		return Instance;
	}
}
