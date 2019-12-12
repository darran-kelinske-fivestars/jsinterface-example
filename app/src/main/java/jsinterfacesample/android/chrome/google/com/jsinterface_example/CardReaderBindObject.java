package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.magtek.mobile.android.mtlib.MTSCRA;
import com.magtek.mobile.android.mtlib.MTSCRAEvent;


public class CardReaderBindObject {

    private static final String TAG = "CardReaderBindObject";

    private MTSCRA mtscra;

    private Handler scraHandler = new Handler(new SCRAHandlerCallback());

    private class SCRAHandlerCallback implements Callback  {
        public boolean handleMessage(Message msg)
        {
            try
            {
                Log.i(TAG, "*** Callback " + msg.what);
                switch (msg.what) {
                    case MTSCRAEvent.OnDeviceResponse:
                        OnDeviceResponse((String) msg.obj);
                        break;
                    default:
                        Log.i(TAG,  String.valueOf(msg.what));
                }
            }
            catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }

            return true;
        }
    }

    public CardReaderBindObject(Context context) {
        mtscra = new MTSCRA(context, scraHandler);
        long batteryLevel = mtscra.getBatteryLevel();
        Log.i(TAG, "Battery level is: " +batteryLevel);
    }

    protected void OnDeviceResponse(String data) {
        Log.i(TAG, data);
    }

    @JavascriptInterface
    public void getBatteryLevel() {
        long batteryLevel = mtscra.getBatteryLevel();
        Log.i(TAG, "Battery level is: " +batteryLevel);
    }
}
