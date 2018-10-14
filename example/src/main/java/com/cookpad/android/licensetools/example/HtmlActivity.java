package com.cookpad.android.licensetools.example;

import com.cookpad.android.licensetools.example.databinding.ActivityHtmlBinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class HtmlActivity extends AppCompatActivity {

    private static final String LICENSES_HTML_PATH = "file:///android_asset/licenses.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHtmlBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_html);

        WebView webView = binding.webView;
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl(LICENSES_HTML_PATH);
    }
}
