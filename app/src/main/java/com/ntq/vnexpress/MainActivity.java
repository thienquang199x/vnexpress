package com.ntq.vnexpress;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ntq.vnexpress.adapter.RcNewAdapter;
import com.ntq.vnexpress.model.ExpressNew;
import com.ntq.vnexpress.provider.VNExpressProvider;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Boolean isDark = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check support dark mode in android Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            // Display button switch dark mode ui
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Change drawable button switch dark mode
            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_dark_mode, null));
            // Get default dark mode in setting
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
                isDark = false;
            } else {
                isDark = true;
            }
        }

        // Create dialog
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        // Create webview
        WebView wv = new WebView(this);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        alert.setView(wv);
        // Set event close for negative button
        alert.setNegativeButton("Close", (dialog, id) -> dialog.dismiss());


        // Create recyclerview item
        ArrayList<ExpressNew> expressNews = new ArrayList<>();
        RecyclerView rcNews = findViewById(R.id.rcNews);
        RcNewAdapter rcNewAdapter = new RcNewAdapter(expressNews, this, url -> {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                if (isDark){
                    WebSettingsCompat.setForceDark(wv.getSettings(), WebSettingsCompat.FORCE_DARK_ON);
                } else {
                    WebSettingsCompat.setForceDark(wv.getSettings(), WebSettingsCompat.FORCE_DARK_OFF);
                }
            }
            wv.loadUrl(url);
            alert.show();
        });
        rcNews.setAdapter(rcNewAdapter);
        rcNews.setLayoutManager(new LinearLayoutManager(this));
        // Padding item in list
        rcNews.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.bottom = 10;
            }
        });

        // Create retrofit to get news
        VNExpressProvider vnExpressProvider = new VNExpressProvider();
        vnExpressProvider.getVnExpressService().getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rss -> {
                    // Update recyclerview
                    expressNews.addAll(rss.getChannel().getItems());
                    rcNewAdapter.notifyDataSetChanged();
                }, throwable -> System.out.println(throwable.getMessage()));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Switch dark mode for button in action bar
        if (isDark){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isDark = false;
        } else  {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            isDark = true;
        }

        return true;
    }
}