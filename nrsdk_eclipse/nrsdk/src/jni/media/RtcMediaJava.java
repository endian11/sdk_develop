package jni.media;

import org.webrtc.videoengine.ViERenderer;
import org.webrtc.videoengine.VideoCaptureAndroid;

import android.content.Context;
import android.view.SurfaceView;

// 使用webrtc框架体系的媒体库
public class RtcMediaJava extends HMedia {
	// 加载媒体库so
	static { System.loadLibrary("media-native"); };
	public RtcMediaJava(Context ctx) { SetWebrtcContext(ctx);}
	public void Release() { SetWebrtcContext(null); }
	@Override
	public void LoadFuncs(int pFuncs) { LoadWebrtcFuncs(pFuncs); };
	@Override
	protected int NewCall(String sArgs,Object videoWindow) { return NewWebrtcCall(sArgs,videoWindow); };
	@Override
	public int DelCall(int callobj) { return DelWebrtcCall(callobj); };
	// 重置摄像头
	public static void resetCamera() { VideoCaptureAndroid.resetCamera(); }
	// 创建本地视频窗口
	public static SurfaceView CreateLocalVideo(Context context) { return ViERenderer.CreateLocalRenderer(context); }
	// 创建远端视频窗口
	public static SurfaceView CreateRemoteVideo(Context context,boolean useOpenGLES2) { return ViERenderer.CreateRenderer(context,useOpenGLES2); }
}
