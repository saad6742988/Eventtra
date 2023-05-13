package com.example.eventtra.Attendee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.eventtra.R;

public class LiveStreamView extends AppCompatActivity {

    String streamUrl;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream_view);
        streamUrl = getIntent().getStringExtra("streamLink");
        webView = (WebView) findViewById(R.id.webView);
        String frameVideo = "<html><body>" +
                "<iframe width=\"840\" height=\"350\" " +
                "src='" + streamUrl + "' frameborder=\"5\" allowfullscreen>" +
                "</iframe></body></html>";


        //setting web client
        webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {return false;
                }
            });
        //web settings for JavaScript Mode
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadData(frameVideo, "text/html", "utf-8");



    }
}