package com.travelrely.v2.util;

import java.io.IOException;

import android.media.MediaRecorder;

public class MyRecorder {

	private final MediaRecorder recorder= new MediaRecorder();
	private static int SAMPLE_RATE_IN_HZ = 8000; 
	/**
	 * 最长时间
	 */
	private static final int MAX_RECORD_TIME=60*1000;
	
	public MyRecorder() {
	}
	/**开始录音*/
	public void start(String path){
		

		
		try {
			//设置声音的来源
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//设置声音的输出格式
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			//设置录音最大时间,超过最大时间,再录就录不上了,录下来的是前60秒的
			recorder.setMaxDuration(MAX_RECORD_TIME);
			//设置声音的编码格式
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			//设置音频采样率
			recorder.setAudioSamplingRate(SAMPLE_RATE_IN_HZ);
			//设置音轨数 1 单声道,2.立体声  
			recorder.setAudioChannels(1);  
//			recorder.setAudioEncodingBitRate(7950);
			//设置输出文件
			recorder.setOutputFile(path);
			//准备录音
			recorder.prepare();
			
			//开始录音
			recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**停止录音*/
	public void stop(){
		try{
		//停止录音
		recorder.stop();
		//释放资源
		recorder.release();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public double getAmplitude() {		
		if (recorder != null){			
			return  (recorder.getMaxAmplitude());		
			}		
		else			
			return 0;	
		}
}
