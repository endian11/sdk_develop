package sdk.travelrely.lib.device.message;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.message
 * ＊ 17:50
 */
public class ProcessMessage {

    private Queue<byte[]> queue = new SynchronousQueue<>();

    private ProcessMessage(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String process(byte[] message){
        //TODO 解析信息
        
        return "";
    }
}
