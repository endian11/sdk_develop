/* This file is based on 
 * http://www.anyexample.com/programming/java/java_play_wav_sound_file.xml
 * Please see the site for license information.
 */
package com.travelrely.core.nrs.nr.voice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import java.io.RandomAccessFile;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.travelrely.core.nrs.nr.jlibrtp.DataFrame;
import com.travelrely.core.nrs.nr.jlibrtp.Participant;
import com.travelrely.core.nrs.nr.jlibrtp.RTPAppIntf;
import com.travelrely.core.nrs.nr.jlibrtp.RTPSession;
import com.travelrely.core.nrs.nr.util.ByteUtil;
import com.travelrely.core.nrs.nr.util.RingBuffer4EcPlay;
import com.travelrely.core.nrs.nr.util.RingBuffer4EcRec;
import com.travelrely.core.nrs.nr.util.RingBuffer4Play;
import com.travelrely.core.nrs.nr.util.TextUtil;
import com.travelrely.core.nrs.nr.util.memUnit;
import com.travelrely.core.nrs.nr.util.readMemunit;
import com.travelrely.core.util.LOGManager;

import android.R.string;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.nfc.tech.IsoDep;
import android.os.Environment;
import android.util.Log;
import android.os.Process;

import com.travelrely.v2.NR.voice.Codec;

/**
 * @author Arne Kepp
 */
public class rtpTerminal implements RTPAppIntf {
	// test
	RTPSession rtpSession = null;
	private Position curPosition;
	byte[] abData = null;
	int nBytesRead = 0;
	int pktCount = 0;
	int dataCount = 0;
	int offsetCount = 0;
	private ShowToastListener showToastListener = null;
	private int TotalRecv = 0;

	Thread recordThread = null;
	Thread decodeThread = null;
	Thread ecThread = null;
	boolean recording = false;
	volatile boolean bMute=false;
	volatile boolean bSpeaker=false;
	int amrMode = amrnb.MR122;
	volatile long startTimeStamp = 32000;

	volatile long lrtpSerial = 0;
	volatile long lrtpTotalCountPlayStart = 0;
	volatile long lrtpTotalCountRecStart = 0;
	volatile long lrtpTotalCountEcStart = 0;
	volatile long lrtpTotalCountSendStart = 0;
	volatile long lrtpTotalCountPlay = 0;
	volatile long lrtpTotalCountRec = 0;
	volatile long lrtpTotalCountEc = 0;
	volatile long lrtpTotalCountSend = 0;
	volatile long lrtpPlaytimeStart = 0;
	volatile long lrtpRectimeStart = 0;
	volatile long lrtpEctimeStart = 0;
	volatile long lrtpSendtimeStart = 0;
	
	volatile boolean bDataArrival = false;
	volatile boolean bRecPlayOptStart = false;
	volatile long lrtpFirstDataArrivalTime = 0;

	final protected Lock dataArriavlLock = new ReentrantLock();
	final protected Condition dataArrivalCondition = dataArriavlLock
			.newCondition();
	volatile boolean bRecReady = false;
	volatile boolean bPlayReady = false;
	
	volatile FileOutputStream osNear = null; // rec
	volatile FileOutputStream osFar = null; // play
	volatile FileOutputStream osEc = null; // ec
	volatile FileOutputStream osAmr = null;
	volatile int  nLinePlayBufSizeInFrame=5;
	volatile int  nLineRecBufSizeInFrame=4;
	volatile int  nDelayFrameNum = 10;
	//int           nDelayFrameNum = 10;
	
	public InetSocketAddress rtpAddress = null;
		
	static enum RXFrameType {
		RX_SPEECH_GOOD, RX_SPEECH_DEGRADED, RX_ONSET, RX_SPEECH_BAD, RX_SID_FIRST, RX_SID_UPDATE, RX_SID_BAD, RX_NO_DATA, RX_N_FRAMETYPES
	};

	private static final String SDCARD_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	public void setShowToastListener(ShowToastListener listener) {
		showToastListener = listener;
	}

	enum Position {
		LEFT, RIGHT, NORMAL
	};

	public void setRecording(boolean recordInput) {
		recording = recordInput;
	}

	public  static  String printHexString( byte[] b) {
		String a = "";
		  for (int i = 0; i < b.length; i++) { 
		    String hex = Integer.toHexString(b[i] & 0xFF); 
		    if (hex.length() == 1) { 
		      hex = '0' + hex; 
		    }
		    a = a+hex;
		  } 
		 return a;
	}
	public void receiveData(DataFrame frame, Participant p) {
		Log.e("amrlib", "enter receiveData");
		byte[] byteInput = frame.getConcatenatedData();

		long curSeq = frame.sequenceNumbers()[0];
		if (lrtpSerial != 0 && curSeq - lrtpSerial != 1) { // 掉包时不补帧
			Log.e("","rtp seq number is not conitinued, seq last is "
					+ lrtpSerial + " seq now is " + curSeq);
		}
		lrtpSerial = curSeq;

		if (byteInput.length == 33 || byteInput.length==7) {

			RingBuffer4Play.getInstance().writeMemunit(new memUnit(byteInput)); //write ring buffer
		} 
		else {
			Log.e("", "byteInput len is " + byteInput.length);
		}
	}

	public void userEvent(int type, Participant[] participant) {
		// Do nothing
	}

	public int frameSize(int payloadType) {
		return 1;
	}

	public void startRtpSession(String RemoteIp, int remoteRtpPort,
			int remoteRtcpPort, DatagramSocket rtpSocket) {
		try {
			rtpSocket.setReceiveBufferSize(2048);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			osNear = new FileOutputStream("/sdcard/near.pcm");
			osFar = new FileOutputStream("/sdcard/far.pcm");
			osEc=new FileOutputStream("/sdcard/ec.pcm");
			osAmr = new FileOutputStream("/sdcard/amr.dat");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		//Codec.instance().amr_lib_init();
		//Codec.instance().amr_aec_init(1);
		
		Codec.instance().Encoder_Interface_init(0);
		Codec.instance().Decoder_Interface_init();
		
		RingBuffer4EcPlay.getInstance();
		RingBuffer4EcRec.getInstance();
		RingBuffer4Play.getInstance();
		
		startRecord();
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		startDecode();
//		while (!bRecReady || !bPlayReady) {
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

		//Log.e("", "sd card root is " + SDCARD_ROOT);

		LOGManager.e("IP=" + RemoteIp + ":" + remoteRtpPort);
		DatagramSocket rtcpSocket = null;

		try {
			rtcpSocket = new DatagramSocket();
		} catch (Exception e) {
			System.out.println("RTPSession failed to obtain port");
		}
		Log.e("",
				"rtp sock is " + rtpSocket + "port is "
						+ rtpSocket.getLocalPort());
		rtpSession = new RTPSession(rtpSocket, rtcpSocket);
		rtpSession.naivePktReception(true);
		rtpSession.RTPSessionRegister(this, null, null);

		Participant p = new Participant(RemoteIp, remoteRtpPort, remoteRtcpPort);
		rtpSession.addParticipant(p);


		recording = true;
		rtpAddress = new InetSocketAddress(RemoteIp, remoteRtpPort);
	}

	public void endRtpSession() {

//		dataArriavlLock.lock();
//		try {
//			dataArrivalCondition.signalAll();
//		} finally {
//			dataArriavlLock.unlock();
//			Log.e("", "dataArriavlLock.unlock()");
//		}
		Log.e("", "end rtp session!");
		recording = false;
		
		if (recordThread != null) {
			while (recordThread.isAlive()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			recordThread = null;
		}

		if (decodeThread != null) {
			while (decodeThread.isAlive()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			decodeThread = null;
		}
		// if (ecThread != null) {
		// while (ecThread.isAlive()) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// ecThread = null;
		// }
		//Codec.instance().amr_aec_finit();
		//Codec.instance().amr_lib_finit();
		Codec.instance().Decoder_Interface_exit();
		Codec.instance().Encoder_Interface_exit();
		
		if (null != rtpSession) {
			rtpSession.endSession();
			rtpSession = null;
		}

		lrtpSerial = 0;
		lrtpTotalCountPlay = 0;
		lrtpTotalCountRec = 0;
		lrtpTotalCountEc = 0;
		lrtpTotalCountSend = 0;
		lrtpTotalCountPlayStart = 0;
		lrtpTotalCountRecStart = 0;
		lrtpTotalCountEcStart=0;
		lrtpTotalCountPlayStart=0;
		lrtpPlaytimeStart = 0;
		lrtpRectimeStart = 0;
		lrtpEctimeStart=0;
		lrtpPlaytimeStart=0;
		bDataArrival = false;
		bRecPlayOptStart = false;
		bRecReady = false;
		bPlayReady = false;
		bMute =false;
		bSpeaker=false;
		RingBuffer4EcPlay.getInstance().clean();
		RingBuffer4EcRec.getInstance().clean();
		RingBuffer4Play.getInstance().clean();
		
		if (osNear != null) {
			try {
				osNear.close();
				osNear = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (osFar != null) {
			try {
				osFar.close();
				osFar = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (osEc != null) {
			try {
				osEc.close();
				osEc = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (osAmr != null) {
			try {
				osAmr.close();
				osAmr=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
//		Log.e("", "start convert pcm to wav!");
		pcm2wav.getInstance().convertPcm2Wav("/sdcard/far.pcm");
		pcm2wav.getInstance().convertPcm2Wav("/sdcard/near.pcm");
		pcm2wav.getInstance().convertPcm2Wav("/sdcard/ec.pcm");
		pcm2wav.getInstance().convertPcm2Wav("/sdcard/mic.pcm");
//		Log.e("", "end convert pcm to wav!");

	}

	public void setMuteState( boolean bMuteInput) { //true:静音 false:恢复录制
		bMute=bMuteInput;
	}
	public void setSpeakerState( boolean bSpeakerInput) { //true:静音 false:恢复录制
		bSpeaker=bSpeakerInput;
	}
	public void sendData(byte[] byte2Send) {
		rtpSession.sendData(byte2Send, startTimeStamp); //直接发送
		startTimeStamp += 320;
		//Log.e("amrlib", "send data!");
	}
	public void sendDataAdd70(byte[] byte2Send) {
		byte[] bytenew = new byte[byte2Send.length + 1];
		bytenew[0] = 0x70;
		System.arraycopy(byte2Send, 0, bytenew, 1, byte2Send.length);
		rtpSession.sendData(bytenew, startTimeStamp);
		startTimeStamp += 320;
	}
	
	public void sendDataWithoutRtpHead(byte[] byte2Send) {
		rtpSession.sendDataWithoutRtpHead(byte2Send,rtpAddress);
	}
	
	public void startRecord() {
		recordThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				int bufferSize;
				bufferSize = AudioRecord.getMinBufferSize(8000,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT);

				if (bufferSize == AudioRecord.ERROR_BAD_VALUE) {
					return;
				}
				Log.e("amrlib", "bufferSize need is "+bufferSize);
				if (bufferSize <= nLineRecBufSizeInFrame*320) 
				{ // 4帧
					bufferSize = nLineRecBufSizeInFrame*320;
				}else {
					nLineRecBufSizeInFrame = bufferSize/320+1;
					bufferSize = nLineRecBufSizeInFrame*320;
				}
				Log.e("amrlib", "bufferSize set is "+bufferSize);
				
				nDelayFrameNum = nLineRecBufSizeInFrame+nLinePlayBufSizeInFrame;
				if (nDelayFrameNum>50) {
					nDelayFrameNum=50;
				}
				AudioRecord record = new AudioRecord(AudioSource.MIC , 8000,
						AudioFormat.CHANNEL_IN_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bufferSize);
				Log.e("", "AudioRecord buff is =" + bufferSize);

				bRecReady = true;
				
				record.startRecording();
				Log.e("", "rec thread start rec over!");
				lrtpRectimeStart=System.currentTimeMillis();
				
				byte[] ecBuffer=new byte[320];
				byte[] zeroBuffer=new byte[320];
				byte[] data=new byte[32]; //最长33

				byte[] recBuffer = new byte[320];// 一帧数据
				
				while (recording) {
					
					int bufferRead = record.read(recBuffer, 0, recBuffer.length);
					if (bufferRead != 320) {
						Log.e("","Rec Len is not 320");
					}
					
					int code_length = Codec.instance().encode(recBuffer, 0,
							recBuffer.length, data, 0, amrMode);
					//Log.e("","code_length is "+code_length);
					if (!bMute) {
						sendDataAdd70(data);
					}
					
					lrtpTotalCountRec += 1;
					long lTimePass = System.currentTimeMillis()
							- lrtpRectimeStart;
					if (lTimePass > 30 * 1000) {
						Log.e("", "Rec 30sec len:" + lTimePass
								+ " lrtpTotalCountRec:"
								+ (lrtpTotalCountRec - lrtpTotalCountRecStart)
								+ " total rec count " + lrtpTotalCountRec);
						
						Log.e("", "play 30sec: total play count:"+lrtpTotalCountPlay);
						lrtpRectimeStart = System.currentTimeMillis();
						lrtpTotalCountRecStart = lrtpTotalCountRec;
					}
				
				}// while (recording) {
				
				record.stop();
				record.release();
			}
		});
		recordThread.setPriority(Thread.MAX_PRIORITY);
		recordThread.start();
	}
	public void startDecode() {
		decodeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				// Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
				int bufferSize = AudioTrack.getMinBufferSize(8000,
						AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT);

				if (bufferSize != 320*nLinePlayBufSizeInFrame) { // 5帧
					bufferSize = 320*nLinePlayBufSizeInFrame;
				}
				AudioTrack mTrack = new AudioTrack(
						AudioManager.STREAM_VOICE_CALL, 8000,
						AudioFormat.CHANNEL_OUT_MONO,
						AudioFormat.ENCODING_PCM_16BIT, bufferSize,
						AudioTrack.MODE_STREAM);
				float maxVol = AudioTrack.getMaxVolume();  
				if (mTrack.setStereoVolume(maxVol, maxVol)==AudioTrack.SUCCESS) {
					Log.e("", "set max volume success!");
				}
				Log.e("", "AudioTrack buff is " + bufferSize + ", max vol is "+maxVol);
				
				bPlayReady = true;

				Log.e("", "play thread start play!!!!! ");
				mTrack.play();
				lrtpPlaytimeStart = System.currentTimeMillis();
				int nDecLen=0;
				
				byte[] samples = new byte[320];// 1帧数据
				while (recording) 
				{
					//long lstart=System.currentTimeMillis();
					readMemunit rtreadMemunit=RingBuffer4Play.getInstance().readMemunit();
//					if (rtreadMemunit.rtcode==true) {
//						Log.e("amrlib", "got amr packet len is "+rtreadMemunit.rtMemUnit.data.length);
//					}
					
					if (rtreadMemunit.rtcode==true) 
					{ //ringbuffer有数据可读
//						try {
////							byte cLen=(byte)rtreadMemunit.rtMemUnit.data.length;
////							byte[] bWrite=new byte[1];
////							bWrite[0]=cLen;
////							osAmr.write(bWrite);
//							osAmr.write(rtreadMemunit.rtMemUnit.data);
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
						
						byte[] data = ByteUtil.subArray(rtreadMemunit.rtMemUnit.data, 1, rtreadMemunit.rtMemUnit.data.length - 1);// 去掉70
						if (data.length==32) {
							int frametype = Codec.instance().decode(data, 0, data.length,
									samples, 0);
						}
						else if(data.length==6){
							data[0]=0x44;
							int frametype = Codec.instance().decode(data, 0, data.length,
									samples, 0);
						}
						else{
							Log.e("amrlib", "amr enc len is without 0x70"+data.length);
						}
						//Log.e("amrlib", "amr enc len is without 0x70"+data.length);
						mTrack.write(samples, 0, samples.length);
						lrtpTotalCountPlay+=1;
					}
//					else 
//					{//无buffer无数据可读
//						nDecLen = Codec.instance().amr_cng(farArray);
//					}
					
				}// while (recording) {

				mTrack.stop();
				mTrack.release();
			}
		});
		decodeThread.setPriority(Thread.MAX_PRIORITY);
		decodeThread.start();
	}
		
}
