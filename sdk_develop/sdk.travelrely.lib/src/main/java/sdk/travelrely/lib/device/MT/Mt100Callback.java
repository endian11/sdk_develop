package sdk.travelrely.lib.device.MT;

/**
 * Created by john on 2016/3/3.
 */
public interface Mt100Callback {
    public void callRslt(byte[] rslt);
    public void sendBtMsg(byte[] msg);
}
