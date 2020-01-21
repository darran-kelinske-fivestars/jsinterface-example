package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;


public class WebViewFragment extends Fragment {

    public static final String EXTRA_FROM_NOTIFICATION = "EXTRA_FROM_NOTIFICATION";

    private WebView webView;

    public WebViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get reference of WebView from layout/activity_main.xml
        webView = (WebView) rootView.findViewById(R.id.fragment_main_webview);

        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/www/index.html");
        preventBGColorFlicker();
        return rootView;
    }

    /**
     * This is a little bit of trickery to make the background color of the UI
     * the same as the anticipated UI background color of the web-app.
     *
     * @param bgColor
     */
    private void preventBGColorFlicker() {
        int bgColor = Color.parseColor("#1abc9c");
        ((ViewGroup) getActivity().findViewById(R.id.activity_main_container)).setBackgroundColor(bgColor);
        webView.setBackgroundColor(bgColor);
    }

    /**
     * This method is designed to hide how Javascript is injected into
     * the WebView.
     *
     * In KitKat the new evaluateJavascript method has the ability to
     * give you access to any return values via the ValueCallback object.
     *
     * The String passed into onReceiveValue() is a JSON string, so if you
     * execute a javascript method which return a javascript object, you can
     * parse it as valid JSON. If the method returns a primitive value, it
     * will be a valid JSON object, but you should use the setLenient method
     * to true and then you can use peek() to test what kind of object it is,
     *
     * @param javascript
     */
    public void loadJavascript(String javascript) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // In KitKat+ you should use the evaluateJavascript method
            webView.evaluateJavascript(javascript, new ValueCallback<String>() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onReceiveValue(String s) {
                    JsonReader reader = new JsonReader(new StringReader(s));

                    // Must set lenient to parse single values
                    reader.setLenient(true);

                    try {
                        if(reader.peek() != JsonToken.NULL) {
                            if(reader.peek() == JsonToken.STRING) {
                                String msg = reader.nextString();
                                if(msg != null) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    } catch (IOException e) {
                        Log.e("TAG", "MainActivity: IOException", e);
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            // NOOP
                        }
                    }
                }
            });
        } else {
            /**
             * For pre-KitKat+ you should use loadUrl("javascript:<JS Code Here>");
             * To then call back to Java you would need to use addJavascriptInterface()
             * and have your JS call the interface
             **/
            webView.loadUrl("javascript:"+javascript);
        }
    }

    public boolean goBack() {
        if(!webView.canGoBack()) {
            return false;
        }

        webView.goBack();
        return true;
    }
}
