package jni.media;

import android.content.Context;

// 媒体库基础类
// 不同媒体库实现通用接口
public abstract class HMedia {
	// 获取库内媒体实现函数表
	protected native int LoadWebrtcFuncs(int pFuncs);
	// 设置Android执行上下文环境
	protected native int SetWebrtcContext(Context ctx);
	// 点对点呼叫
	// voicectrl\nlocalip\nlocalport\nremoteip\nremoteport\npayloadname\npayloadvalue\nsamples
	// \nvlocalip\nvlocalport\nvremoteip\nvremoteport\nvpayloadname\nvpayloadvalue\nvsamples
	protected native int NewWebrtcCall(String sArgs,Object videoWindow);
	// 挂断点对点呼叫
	protected native int DelWebrtcCall(int callobj);
	// 操作
	public native int OptWebrtcCall(int callobj,int optID,String sArgs);
	
	// 加载媒体函数 for "C"
	public abstract void LoadFuncs(int pFuncs);
	
	// 点对点呼叫入口 for "Java"
	public int NewDirectCall(int voiceCtrl,String localip,int localport,String remoteip,int remoteport,
		String payloadname,int payloadvalue,int samplerate) { 
		return NewCall(
			voiceCtrl+"\n"+localip+"\n"+localport+"\n"+remoteip+"\n"+remoteport+"\n"
			+payloadname+"\n"+payloadvalue+"\n"+samplerate,null); 
	};
	// 点对点视频呼叫
	public int NewDirectVideoCall(int voiceCtrl,String localip,int localport,String remoteip,int remoteport,
			String payloadname,int payloadvalue,int samplerate,
			String vlocalip,int vlocalport,String vremoteip,int vremoteport,
			String vpayloadname,int vpayloadvalue,int vsamplerate,Object videoWindow) {
		return NewCall(
				voiceCtrl+"\n"+localip+"\n"+localport+"\n"+remoteip+"\n"+remoteport+"\n"
				+payloadname+"\n"+payloadvalue+"\n"+samplerate
				+"\n"+vlocalip+"\n"+vlocalport+"\n"+vremoteip+"\n"+vremoteport+"\n"
				+vpayloadname+"\n"+vpayloadvalue+"\n"+vsamplerate,videoWindow); 		
	}
	protected abstract int NewCall(String sArgs,Object videoWindow);
	public abstract int DelCall(int callobj);
	
	// 使用其他媒体库
}
