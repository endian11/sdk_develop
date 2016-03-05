// IBleServiceAidl.aidl
package sdk.travelrely.lib;

// Declare any non-default types here with import statements
import  sdk.travelrely.lib.IBleCallback;


interface IBleServiceAidl {
    void registBleCallback(IBleCallback callback);
    void startScan();
    void stopScan();
}
