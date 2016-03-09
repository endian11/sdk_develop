package sdk.travelrely.lib.device.message;

import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import sdk.travelrely.lib.device.manager.BoxManager;

/**
 * ＊ LvXin_V2
 * Created by weihaichao on 16/3/2.
 * ＊ sdk.travelrely.lib.device.message
 * ＊ 17:50
 */
public class ProcessMessage {

    private ProcessMessage() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 派发交互事件
     *
     * @param Action
     * @param message
     */
    private static void dispatchAction(int Action, Message message) {
        message.what = Action;
        message.sendToTarget();
    }

    public static String process(byte[] message) {
        //TODO 解析信息
        Message parseMessage = BoxManager.getDefault().getHandler().obtainMessage();
        switch (BoxManager.CURRENT_ACTION) {
            case BoxManager.ACTION_CHECK_KEY:
                dispatchAction(BoxManager.ACTION_CHECK_KEY,parseMessage);
                break;
            case BoxManager.ACTION_READ_MAC:
                dispatchAction(BoxManager.ACTION_READ_MAC, parseMessage);
                break;
            case BoxManager.ACTION_GEMERAT_KEY:
                dispatchAction(BoxManager.ACTION_GEMERAT_KEY, parseMessage);
                break;
            case BoxManager.ACTION_READ_COS_VERSION:
                dispatchAction(BoxManager.ACTION_READ_COS_VERSION, parseMessage);
                break;
            case BoxManager.ACTION_READ_MT_SN:
                dispatchAction(BoxManager.ACTION_READ_MT_SN, parseMessage);
                break;
            case BoxManager.ACTION_READ_POWER_LEVEL:
                dispatchAction(BoxManager.ACTION_READ_POWER_LEVEL, parseMessage);
                break;
        }
        return "";
    }
}
