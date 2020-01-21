/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends Activity {

    private WebViewFragment mWebViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mWebViewFragment = new WebViewFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_main_container, mWebViewFragment)
                    .commit();
        }
        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        usbManager.requestPermission(getUsbDeviceSelections().get(0), PendingIntent.getBroadcast(this, 0, new Intent("7"), 0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * If the help menu option is selected, show a new
     * screen in the WebView
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                mWebViewFragment.loadJavascript("showSecretMessage();");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Go back through the WebView back stack before
     * exiting the app
     */
    @Override
    public void onBackPressed() {
        if(!mWebViewFragment.goBack()) {
            super.onBackPressed();
        }
    }

    private ArrayList<UsbDevice> getUsbDeviceSelections() {
        ArrayList<UsbDevice> selectionList = new ArrayList<>();

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        if (usbManager != null)
        {
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

            while (deviceIterator.hasNext())
            {
                UsbDevice device = deviceIterator.next();

                if (device != null)
                {
                    if (device.getVendorId() == 0x0801)
                    {
                        String name = device.getDeviceName();

                        if (android.os.Build.VERSION.SDK_INT >= 21)
                        {
                            String dsn = device.getSerialNumber();

                            if ((dsn != null) && !dsn.isEmpty())
                            {
                                name = dsn;
                            }
                        }

                        selectionList.add(device);
                    }
                }
            }
        }

        return selectionList;
    }
}
