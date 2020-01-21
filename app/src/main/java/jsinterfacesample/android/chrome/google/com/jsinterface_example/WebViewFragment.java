package jsinterfacesample.android.chrome.google.com.jsinterface_example;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebMessage;
import android.webkit.WebMessagePort;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.IOException;
import java.io.StringReader;


public class WebViewFragment extends Fragment {

    private WebView webView;
    private WebMessagePort port;

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

        initPort();

        return rootView;
    }

    private void initPort() {
        final WebMessagePort[] channel = webView.createWebMessageChannel();

        port=channel[0];
        port.setWebMessageCallback(new WebMessagePort.WebMessageCallback() {
            @Override
            public void onMessage(WebMessagePort port, WebMessage message) {
                Log.e("darran",  "woo woo");
            }
        });

        webView.postWebMessage(new WebMessage("asdf", new WebMessagePort[]{channel[1]}),
                Uri.EMPTY);
    }

    public boolean goBack() {
        if(!webView.canGoBack()) {
            return false;
        }

        webView.goBack();
        return true;
    }
}
